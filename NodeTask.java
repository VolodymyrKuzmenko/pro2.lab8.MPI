import mpi.MPI;
/**
 * -------------------------------------------------------------------------
 *                Parallel and Distributed Computing
 *                Laboratory work  №8. MPI API
 *
 * Task: MA = max(B)MO = alpha)MT*MR)
 *
 * Author: Kuzmenko Volodymyr
 * Group1: ІО-21
 * Date: 03.05.15
 * --------------------------------------------------------------------------
 */
public class NodeTask {
	private LeafComm leaf;
	private RootComm root;
	private Vector B;
	private Matrix MO, MT, MR, MA;
	private Integer maxB;
	private Integer alpha;

	private int rank;

	private int P = Executor.P;

	public NodeTask(RootComm comm, LeafComm leaf, int rank) {
		this.root = comm;
		this.leaf = leaf;
		this.rank = rank;
	}

	public void start() {
		if (isCurrentTaskFilter()) {
			MessageBox[] recvBox = new MessageBox[1];
			MessageBox[] sendBox = new MessageBox[P / 4 + 1];
			//1.	Прийняти α, B((N-H)/4), MO((N-H)/4), MT((N-H)/4), MR від кореня
			root.Scatter(sendBox, recvBox);

			Vector[] B_buf = recvBox[0].getVector(0).divide(4);
			Matrix[] MO_buf = recvBox[0].getMatrix(0).divide(4);
			Matrix[] MT_buf = recvBox[0].getMatrix(1).divide(4);
			this.MR = recvBox[0].getMatrix(2);
			this.alpha = recvBox[0].getValue(0);

			recvBox = new MessageBox[1];
			sendBox = new MessageBox[4];

			for (int i = 0; i < sendBox.length; i++) {
				sendBox[i] = new MessageBox();
				sendBox[i].AddVector(B_buf[i]);
				sendBox[i].addMatrix(MO_buf[i]);
				sendBox[i].addMatrix(MT_buf[i]);
				sendBox[i].addMatrix(MR);
				sendBox[i].AddValue(alpha);
			}
			//2.	Передати α, BH, MOH, MTH, MR листам
			leaf.Scatter(sendBox, recvBox);
			B = null;
			MO = null;
			MT = null;

			this.B = recvBox[0].getVector(0);
			this.MO = recvBox[0].getMatrix(0);
			this.MT = recvBox[0].getMatrix(1);

			int[] sendBuffer = new int[1];
			int[] recvBuffer = new int[1];
			//3.	Обчислити mj = max(BH)
			sendBuffer[0] = CalculateUtils.max(this.B);
			//4.	Прийняти mi від листа
			//5.	Обчислити mj = max(mj, mi)
			leaf.AllReduce(sendBuffer, recvBuffer);
			sendBuffer[0] = recvBuffer[0];
			//6.	Передати mj кореню
			//7.	Прийняти m від кореня
			root.AllReduce(sendBuffer, recvBuffer);
			//8.	Передати m листам
			leaf.Bcast(recvBuffer);
			maxB = recvBuffer[0];
			//9. Обчислити MAH = m∙MOH + α∙(MTH∙MR)
			MA = CalculateUtils.add(
					CalculateUtils.mult(maxB, this.MO),
					CalculateUtils.mult(alpha,
							CalculateUtils.mult(this.MT, this.MR)));

			sendBox = new MessageBox[1];
			recvBox = new MessageBox[4];

			sendBox[0] = new MessageBox();
			sendBox[0].addMatrix(MA);
			//10. Прийняти MAH від листів
			leaf.Gather(sendBox, recvBox);

			for (int i = 1; i < recvBox.length; i++) {
				MA.merge(recvBox[i].getMatrix(0));
			}

			sendBox = new MessageBox[1];
			recvBox = new MessageBox[P / 4 + 1];

			sendBox[0] = new MessageBox();
			sendBox[0].addMatrix(MA);
			//11. Передати MA(N-H)/4 кореню
			root.Gather(sendBox, recvBox);

		}

	}

	private boolean isCurrentTaskFilter() {
		return this.rank == MPI.COMM_WORLD.Rank();
	}
}

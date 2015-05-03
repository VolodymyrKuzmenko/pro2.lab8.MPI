
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

public class RootTask {
	private RootComm comm;
	private Vector B;
	private Matrix MO, MT, MR, MA;
	private Integer maxB;
	private Integer alpha;
	
	private int rank;
	
	private int P = Executor.P;

	private int H = Executor.H;
	
	
	public RootTask(RootComm comm, int rank) {
		this.comm = comm;
		this.rank = rank;
	}
	
	public void start(){
		if(isCurrentTaskFilter()){
			MessageBox [] recvBox = new MessageBox[1];
			MessageBox [] sendBox = new MessageBox[(P/4)+1];
			
			Vector [] B = new Vector [P/4+1];
			Matrix [] MO = new Matrix[P/4+1];
			Matrix [] MT = new Matrix [P/4+1];
			//1.	Введення α, B, MO, MT, MR
			Vector B_N = CalculateUtils.inputVector(1);
			Matrix MO_N = CalculateUtils.inputMatrix(1);
			Matrix MT_N = CalculateUtils.inputMatrix(1);
			this.MR = CalculateUtils.inputMatrix(1);
			alpha = 1;
			
			B[0] = B_N.copy(0, H);
			MO[0] = MO_N.copy(0, H);
			MT[0] = MT_N.copy(0, H);
			
			B_N = B_N.copy(H, B_N.size()-H);
			MO_N = MO_N.copy(H, MO_N.size()-H);
			MT_N = MT_N.copy(H, MT_N.size()-H);
			
			int index = 1;
			for (Vector vector : B_N.divide(P/4)) {
				B[index] = vector;
				index ++;
			}
			
			index = 1;
			for (Matrix matrix : MO_N.divide(P/4)) {
				MO[index] = matrix;
				index++;
			}
			
			index = 1;
			for (Matrix matrix : MT_N.divide(P/4)) {
				MT[index] = matrix;
				index++;
			}
			
	
			
			for (int i = 0; i < sendBox.length; i++) {
				sendBox[i] = new MessageBox();
				sendBox[i].AddVector(B[i]);
				sendBox[i].addMatrix(MO[i]);
				sendBox[i].addMatrix(MT[i]);
				sendBox[i].addMatrix(MR);
				sendBox[i].AddValue(alpha);
			}
			//2.	Передати α, B((N-H)/4), MO((N-H)/4), MT((N-H)/4), MR вузлам
			comm.Scatter(sendBox, recvBox);
			
			B = null;
			MO = null;
			MT = null;
			
			this.B = recvBox[0].getVector(0);
			this.MO = recvBox[0].getMatrix(0);
			this.MT = recvBox[0].getMatrix(1);
			
			int [] sendBuffer = new int[1];
			int [] recvBuffer = new int[1];
			//3.	Обчислити m = max(BH)
			sendBuffer[0] =  CalculateUtils.max(this.B);
			//4.	Прийняти від вузлів mj
			//5.	Обчислити m = max (m, mj)
			//6.	Передати вузлам m
			comm.AllReduce(sendBuffer, recvBuffer);
			maxB = recvBuffer[0];
			//7.	Обчислити MAH = m∙MOH + α∙(MTH∙MR)
			MA = CalculateUtils.add(CalculateUtils.mult(maxB, this.MO), CalculateUtils.mult(alpha, CalculateUtils.mult(this.MT, this.MR)));
			
			sendBox = new MessageBox[1];
			recvBox = new MessageBox[P/4+1];
			
			sendBox[0] = new MessageBox();
			sendBox[0].addMatrix(MA);
			//8.	Прийняти MA((N-H)/4) від вузлів
			comm.Gather(sendBox, recvBox);
			
			for (int i = 1; i < recvBox.length; i++) {
				MA.merge(recvBox[i].getMatrix(0));
			}
			
			//9.	Вивести МА
			System.out.println(MA);
		}
		
	}
	
	private boolean isCurrentTaskFilter(){
		return this.rank == MPI.COMM_WORLD.Rank();
	}
	
}

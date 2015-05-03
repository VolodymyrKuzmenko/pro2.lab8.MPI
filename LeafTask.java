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

public class LeafTask {
	private LeafComm comm;
	private Vector B;
	private Matrix MO, MT, MR, MA;
	private Integer maxB;
	private Integer alpha;
	
	private int rank;
	
	public LeafTask(LeafComm comm, int rank) {
		this.comm = comm;
		this.rank = rank;
	}
	public void start(){
		if(isCurrentTaskFilter()){
		
			MessageBox [] recvBox = new MessageBox[1];
			MessageBox [] sendBox = new MessageBox[4];
			//1.	Прийняти α, BH, MOH, MTH, MR від вузла
			comm.Scatter(sendBox, recvBox);
			B = recvBox[0].getVector(0);
			MO = recvBox[0].getMatrix(0);
			MT = recvBox[0].getMatrix(1);
			MR = recvBox[0].getMatrix(2);
			alpha = recvBox[0].getValue(0);
			
			int [] sendBuffer = new int[1];
			int [] recvBuffer = new int[1];
			//2. Обчислити mi = max(BH)
			sendBuffer[0] =  CalculateUtils.max(this.B);
			//3. Передати mi  вузлу
			comm.AllReduce(sendBuffer, recvBuffer);
			//4. Прийняти m від вузла
			comm.Bcast(recvBuffer);
			
			maxB = recvBuffer[0];
			//5. Обчислити MAH = m∙MOH + α∙(MTH∙MR)
			MA = CalculateUtils.add(CalculateUtils.mult(maxB, this.MO), CalculateUtils.mult(alpha, CalculateUtils.mult(this.MT, this.MR)));
			sendBox = new MessageBox[1];
			recvBox = new MessageBox[4];
			sendBox[0] = new MessageBox();
			sendBox[0].addMatrix(MA);
			//6.	Передати MAH вузлу.
			comm.Gather(sendBox, recvBox);	
		}
		
	}
	
	private boolean isCurrentTaskFilter(){
		return this.rank == MPI.COMM_WORLD.Rank();
	}
	
	
}

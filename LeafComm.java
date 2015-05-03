
import mpi.MPI;
/**
 * -------------------------------------------------------------------------
 *                Parallel and Distributed Computing
 *                Laboratory work  ¹8. MPI API
 *
 * Task: MA = max(B)MO = alpha)MT*MR)
 *
 * Author: Kuzmenko Volodymyr
 * Group1: ²Î-21
 * Date: 03.05.15
 * --------------------------------------------------------------------------
 */
public class LeafComm extends TreeCommunicator {

	
	public LeafComm(int rootRank, int[] leafRanks) {
		super(rootRank, leafRanks);
		
	}
	
	
	@Override
	public String toString() {
		if (taskInCommunicatorFilter()){
			String message = "Leaf Communicator."+"Current rank: "+comm.Rank()+"\n";
			message += "Root rank: "+ rootRank+"\n Leafs ranks: ";
			for (int i : leafRanks) {
				message+=i+", ";
			}
			return message;
		}
		return "";
	}
	
	@Override
	public void Scatter (Object [] sendbuf, Object [] recvbuf){
		if(taskInCommunicatorFilter()){
			
			
			comm.Scatter(sendbuf, 0, 1, MPI.OBJECT, recvbuf, 0, 1, MPI.OBJECT, 0);
		}
	}
	@Override
	public void Gather(Object [] sendbuf, Object [] recvbuf){
		if(taskInCommunicatorFilter()){
			comm.Gather(sendbuf, 0, 1, MPI.OBJECT, recvbuf, 0, 1, MPI.OBJECT, 0);
		}
	}
	
}


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

public class RootComm extends TreeCommunicator {

	public RootComm(int[] nodeRanks) {
		super(0, nodeRanks);
	}
	@Override
	public String toString() {
		if (taskInCommunicatorFilter()){
			  
			String message = "Root Communicator."+"Current rank: "+comm.Rank()+"\n";
			message += "Root of graph rank: "+ rootRank+"\n Node ranks: ";
			for (int i : leafRanks) {
				message+=i+", ";
			}
			return message;
		}
		return "";
	}
	
	public void Scatter (Object [] sendbuf, Object [] recvbuf){
		if(taskInCommunicatorFilter()){
			comm.Scatter(sendbuf, 0, 1, MPI.OBJECT, recvbuf, 0, 1, MPI.OBJECT, rootRank);
			
		}
	}
	
	public void Gather(Object [] sendbuf, Object [] recvbuf){
		if(taskInCommunicatorFilter()){
			comm.Gather(sendbuf, 0, 1, MPI.OBJECT, recvbuf, 0, 1, MPI.OBJECT, rootRank);
		}
	}
	
	
	
}


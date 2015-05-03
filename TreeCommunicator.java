import mpi.Group;
import mpi.Intracomm;
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

public abstract class TreeCommunicator {
	protected Intracomm comm;
	protected int rootRank;
	protected int [] leafRanks;
	
	protected TreeCommunicator (int rootRank, int[] leafRanks){
		this.leafRanks = leafRanks;
		if(taskInCommunicatorFilter()){
			this.rootRank = rootRank;
			Group worldGroup = MPI.COMM_WORLD.group;
			this.comm = MPI.COMM_WORLD.Create(worldGroup.Incl(leafRanks));			
		}
	}
	
	public abstract void Scatter (Object [] sendbuf, Object [] recvbuf);
	
	public abstract void Gather(Object [] sendbuf, Object [] recvbuf);
	
	
	public boolean taskInCommunicatorFilter(){
		int currentRank = MPI.COMM_WORLD.Rank();
		for (int thisGroupRank : leafRanks) {
			if (thisGroupRank == currentRank){
				return true;
			}
		}
		return false;
	}
	
	public boolean isTaskRoot(){
		return MPI.COMM_WORLD.Rank() == this.rootRank;
	}
	
	
	
	public int Rank(){
		return comm.Rank();
	}

	public int getRootRank() {
		return rootRank;
	}
	
	public int [] getLeafRanks(){
		int [] result = new int [leafRanks.length-1];
		for (int i = 1; i < leafRanks.length; i++) {
			result[i-1] = leafRanks[i];
		}
		return result;
	}
	public void AllReduce(int[] sendBuffer, int[] recvBuffer){
		if(taskInCommunicatorFilter()){
			comm.Allreduce(sendBuffer, 0, recvBuffer, 0, 1, MPI.INT, MPI.MAX);
		}
	}
	
	
	public void Bcast(int[] buffer){
		comm.Bcast(buffer, 0, 1, MPI.INT, 0);
	}
	
	
	
}

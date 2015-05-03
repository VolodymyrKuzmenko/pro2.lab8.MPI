import mpi.*;
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
public class Executor {
	public static int N;
	public static int P;
	public static int H;
	
	public static void main (String args []){
		P = Integer.parseInt(args[1]);
		N = Integer.parseInt(args[3]);
		H = N / P;

		CommunicatorsBuilder builder = new CommunicatorsBuilder(P);
		
		
		MPI.Init(args);
		
		builder.builCommunicators();
		
		RootComm root = builder.geRootComm();
		
		LeafComm [] leafComms = builder.getLeafComms();
		
		new RootTask(root, 0).start();
		
		for (int i = 0; i < root.getLeafRanks().length; i++) {
			new NodeTask(root, leafComms[i],root.getLeafRanks()[i]).start();	
		}
		
		for (LeafComm node : leafComms) {
			for (int rank : node.getLeafRanks()) {
				new LeafTask(node, rank).start();				
			}
		}
		
		
		MPI.Finalize();
	}
	
	
}

import java.util.ArrayList;
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
public class CommunicatorsBuilder {
	private int P;
	private ArrayList<Integer> edges = new ArrayList<Integer>();
	private RootComm rootOfGraphComm;
	private LeafComm [] leafOfGraphComms;
	
	public CommunicatorsBuilder(int p) {
		this.P = p;
		leafOfGraphComms = new LeafComm[P/4];
		
	}
	
	public LeafComm [] getLeafComms (){
		return this.leafOfGraphComms;
	}
	
	public RootComm geRootComm(){
		return rootOfGraphComm;
	}
		
	public void builCommunicators() {
		buildRootCommunicator();
		BuildLeafsCommunicators();
		
	}

	private void BuildLeafsCommunicators() {
		int indexLeafs = 0;
		Integer  [] transitNodes = new Integer [edges.size()];
		transitNodes = edges.toArray(transitNodes);
		edges.clear();
		for (Integer transitNode : transitNodes) {
			leafOfGraphComms[indexLeafs] = buildLeafCommunicator(transitNode);
			indexLeafs++;
		}
	}

	private void buildRootCommunicator() {
		edges.add(0);
		for (int i= 1;i < P;i+=4) {
			edges.add(i);
		}
		rootOfGraphComm = new RootComm(this.toIntArray(edges));
		edges.remove(0);
	}
	
	private LeafComm buildLeafCommunicator(Integer transitNode) {
		edges.add(transitNode);
		for (int i = 1; i <= 3; i++) {
			edges.add(i+transitNode);
		}
		
		LeafComm result = new LeafComm(transitNode, toIntArray(edges));
		edges.clear();
		return result;
	}
		
	private int [] toIntArray(ArrayList<Integer> list){
		int [] result = new int [list.size()];
		int index = 0;
		for (int value : list) {
			result[index] = value;
			index++;
		}
		return result;
	}
}

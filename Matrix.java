import java.io.Serializable;
import java.util.Arrays;
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

public class Matrix implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector[] array;

	public Matrix(int n) {
		array = new Vector[n];
		for (int i = 0; i < array.length; i++) {
			array[i] = new Vector(Executor.N);
		}
	}

	public void set(int n, int m, int val) {
		array[n].set(m, val);
	}

	public int get(int n, int m) {
		return array[n].get(m);
	}

	public Vector get(int index) {
		return array[index];
	}

	public int size() {
		return array.length;
	}

	public String toString() {
		String res = "";
		for (int i = 0; i < array.length; i++) {
			res += array[i].toString();
		}
		return res;
	}

	public void merge(Matrix second) {
		  Vector[] result = Arrays.copyOf(array, array.length + second.array.length);
		  System.arraycopy(second.array, 0, result, array.length, second.array.length);
		  array = result;
		} 

	public Matrix copy(int start,int size){
		Matrix result = new Matrix(size);
		for (int i = 0; i < result.size(); i++) {
			result.array[i] = array[i+start].clone();
		}
		return result;
	}

	public Matrix[] divide(int numberOfParts) {
		Matrix[] result = new Matrix[numberOfParts];
		for (int i = 0; i < result.length; i++) {
			result[i] = this.copy(i * (this.size() / numberOfParts),
					(this.size() / numberOfParts));
		}
		return result;
	}
}

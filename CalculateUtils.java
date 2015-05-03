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
public class CalculateUtils {

	public static Vector inputVector(int value) {
		Vector vector = new Vector(Executor.N);
		for (int i = 0; i < vector.size(); i++) {
			vector.set(i, value);
		}
		return vector;
	}

	public static Matrix inputMatrix(int value) {
		Matrix matrix = new Matrix(Executor.N);
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.size(); j++) {
				matrix.set(i, j, value);
			}
		}
		return matrix;
	}

	public static void outputVector(Vector vector) {
		if (vector.size() <= 40) {
			System.out.print(vector.toString());
		}
	}

	public static void outputMatrix(Matrix matrix) {
		if (matrix.size() <= 40) {
			System.out.print(matrix.toString());
		}
	}

	public static Matrix add (final Matrix left, final Matrix right){
		Matrix result = new Matrix(right.size());
		for (int i = 0; i < right.size(); i++) {
			for (int j = 0; j < Executor.N; j++) {
				result.set(i, j, left.get(i, j)+right.get(i, j));
			}
		}
		return result;
	}
	
	public static Vector add(final Vector left, final Vector right) {

		Vector result = new Vector(left.size());
		for (int i = 0; i < left.size(); i++) {
			result.set(i, left.get(i) + right.get(i));

		}
		return result;
	}

	public static Vector mult(final int left, final Vector right) {

		Vector result = new Vector(right.size());
		for (int i = 0; i < right.size(); i++) {
			result.set(i, left * right.get(i));

		}

		return result;
	}

	public static Matrix mult(final int left, final Matrix right) {

		Matrix result = new Matrix(Executor.N).copy(0, right.size());
		for (int i = 0; i < right.size(); i++) {
			for (int j = 0; j < Executor.N; j++) {
				result.set(i, j, right.get(i, j)*left);				
			}

		}

		return result;
	}

public static Matrix mult(final Matrix left, final Matrix right) {
 
 		
 		Matrix result = new Matrix(right.size());
 		result = result.copy(0, left.size());
 		
 		for (int i = 0; i < left.size(); i++) {
 			for (int j = 0; j < right.size(); j++) {
 				result.set(i, j, 0);
 				for (int y = 0; y < right.size(); y++) {
 					result.set(i, j, result.get(i, j) + left.get(i, y)
 							* right.get(y, j));
 				}
 			}
 		}
 		return result;
 	}
 

	public static Vector mult(final Vector left, final Matrix right) {

		Vector result = new Vector(right.size());
		for (int i = 0; i < right.size(); i++) {
			result.set(i, 0);
			for (int j = 0; j < left.size(); j++) {
				result.set(i, result.get(i) + left.get(j) * right.get(i, j));
			}
		}
		return result;
	}

	public static int min(final Vector vector) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < vector.size(); i++) {
			if (vector.get(i) < min) {
				min = vector.get(i);
			}
		}
		return min;
	}

	public static int max(final Vector vector) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < vector.size(); i++) {
			if (vector.get(i) > max) {
				max = vector.get(i);
			}
		}
		return max;
	}

}

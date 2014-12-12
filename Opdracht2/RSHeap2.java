import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * 
 * @author Hans Schollaardt
 */
public class RSHeap2 {
	private static int HEAP_SIZE;
	private static final int RANDOM_NUMBERS = 137;
	private static final int RANDOMIZE_AROUND = 5000;
	private int indexRandomArray, output;
	private int[] random, heap;
	private PrintWriter out;
	private int DEADSPACE_SIZE;

	public RSHeap2(int heapsize) {
		init(heapsize);
		// read HEAP_SIZE-elements into heap-array
		fillHeap();
		// Build the heap for the first time
		buildHeap();
		// while array still holds more values, continue
		while (indexRandomArray < random.length) {

			removeItem();
			insert(random[indexRandomArray]);
			indexRandomArray++;
		}
		// write remainder of heap to OUT
		out.println("<---- REST OF THE HEAP!! --->");
//		System.err.println("<---- REST OF THE HEAP!! --->");
		writeHeap();
		buildHeap();
		out.println("<---- THE REMAINDER!! --->");
//		System.out.println(toDotString());
		// write remainder to OUT
//		System.err.println("<---- THE REMAINDER!! --->");
		writeHeap();
		out.close();
		
	}
	
	
	private void writeHeap() {
		for (int i = 0; i < HEAP_SIZE; i++) {
			removeItem();
		}
	}

	/**
	 * 
	 * @return
	 */
	private int removeItem() {
//		System.out.println(heap[0] + "  Item number  "
//				+ (indexRandomArray+1));
		printToOutput(heap[0] + "\t  Item number  "
				+ (indexRandomArray+1));
		output = heap[0];
		heap[0] = heap[HEAP_SIZE];
		HEAP_SIZE--;
		percolateDown(0);
		return output;
	}

	/**
	 * 
	 * @param toInsert
	 */
	private void insert(int toInsert) {
		if (toInsert < output) {
			heap[HEAP_SIZE + 1] = toInsert;
			DEADSPACE_SIZE++;
			if (heap.length - DEADSPACE_SIZE == 0) {
//				System.err.println("<---- NEXT RUN!! ---->");
				out.println("<---- NEXT RUN!! ---->");
				buildHeap();
			}
		} else {
			HEAP_SIZE++;
			heap[HEAP_SIZE] = toInsert;
			percolateUp(HEAP_SIZE);
		}
	}

	/**
	 * 
	 * @param index
	 */
	private void percolateUp(int index) {
		assert (index >= 0);
		int parentIndex = (index - 1) / 2;
		if (index >= 0) {
			if (heap[parentIndex] > heap[index]) {
				// swap parents value with childs value
				swap(parentIndex, index);
				percolateUp(parentIndex);
			}
		}
	}

	/**
	 * 
	 * @param currentIndex
	 */
	private void percolateDown(int currentIndex) {
		assert (currentIndex >= 0);
		int current = heap[currentIndex];

		int left = currentIndex * 2 + 1;
		int right = currentIndex * 2 + 2;

		// both right and left children exist
		if (right <= HEAP_SIZE) {
			// check if either sides is bigger than yourself; if so, swap with
			// the bigger;
			if (current > heap[left] || current > heap[right]) {
				if (heap[left] < heap[right]) {
					// swap left with parent
					swap(left, currentIndex);
					percolateDown(left);
					// right is bigger than left
				} else {
					// swap right with parent
					swap(right, currentIndex);
					percolateDown(right);
				}
			}
		// check if parent has left child
		} else if (left <= HEAP_SIZE) {
			// only has a left child
			if (current > heap[left]) {
				// swap left with parent
				swap(left, currentIndex);
			}
		}

	}
/**
 * 
 * @param current
 * @param toSwapWith
 */
	private void swap(int current, int toSwapWith) {
		int temp = heap[current];
		heap[current] = heap[toSwapWith];
		heap[toSwapWith] = temp;
	}

	/**
	 * Method to print single integer to output file
	 * 
	 * @param smallest
	 */
	private void printToOutput(String string) {
		out.println(string);
	}

	/**
	 * Initial method to setup constants and initialize values
	 * 
	 * @param heapsize
	 *            Desired heapsize (length of array)
	 */
	private void init(int heapsize) {
		HEAP_SIZE = heapsize;
		DEADSPACE_SIZE = 0;
		heap = new int[HEAP_SIZE];
		random = new int[RANDOM_NUMBERS];
		// generate random numbers to fill array
		for (int i = 0; i < random.length; i++) {
			random[i] = (int) (Math.random() * RANDOMIZE_AROUND);
		}
		// Initialize in- and outputs
		try {
			out = new PrintWriter("outputfile.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to (re-)build the heap-array, according to the specification of
	 * binary heap.
	 * 
	 * @param array
	 *            Array to rebuild in good heap format
	 */
	private void buildHeap() {
		// percolate down
		DEADSPACE_SIZE = 0;
		HEAP_SIZE = heap.length - 1;
		for (int i = ((heap.length - 1) / 2); i >= 0; i--) {
			percolateDown(i);
		}
	}

	private void fillHeap() {
		System.err.println("<----   FILLING THINGS UP!   ----->\n");
		for (int i = 0; i < heap.length; i++) {
			System.out.println("Filling index " + i);
			heap[i] = random[i];
		}
	}

	/**
	 * Created a DotString of the heap
	 * 
	 * @return The DotString of the heap
	 */
	private String toDotString() {
		String res = "digraph heap {\n";
		for (int i = 0; i < heap.length; i++) {
			res += "n" + i + "[label=\"" + heap[i] + "\"]\n";
		}
		for (int i = 0; i < heap.length / 2; i++) {
			res += "n" + i + "-> n" + (i * 2 + 1) + ";\n";
			res += "n" + i + "-> n" + (i * 2 + 2) + ";\n";
		}
		res += "}";
		return res;

	}
}
/** 
 * 
 * N-Queens Hill Climbing Local Search Solution 
 * Search runs <HILLCLIMBING_RUNS> times and will exit after <HILLCLIMBING_TIMEOUT> milliseconds of not finding a solution.
 * (Main takes N as input)
 * 
 * **/

public class HillClimbingLocalSearch {
	public static final int HILLCLIMBING_TIMEOUT = 250;
	public static final int HILLCLIMBING_RUNS = 40;

	public static void main (String [] args) {
		if (args.length < 1) {
			System.out.println("N Queens board size required");
			System.exit(1);
		}

		
		long deltas = 0;
		long success = 0;
		int successes = 0;
		int attempt = 0;
		int boardSize = Integer.parseInt(args[0]);
		int [] intBoard = initIntBoard(boardSize);
		int [][] board = initBoard(boardSize);

		printBoard(board);
		//printValues(board);
		System.out.println("Objective value: " + ObjectiveValue(board) + "\nMoves: 0");

		// BitArray
		while (attempt < HILLCLIMBING_RUNS) {
			System.out.printf("\n\n Attempt %d:\n", attempt);
			success = HillClimbing(initBoard(boardSize));
			if(success >= 1)
				successes++;
			deltas = deltas + (success-1);
			attempt++;
		}
		System.out.println("\nDone. Succeeded " + successes + " out of " + HILLCLIMBING_RUNS + " runs (" + (int)(((double)successes/HILLCLIMBING_RUNS)*100) + "%)");
		System.out.println("Solved in an average of " + (deltas/successes) + "ms");

		// IntArray
		while (attempt < HILLCLIMBING_RUNS) {

		}
	}

	// Create bit array with queens located at top row
	public static int[][] initBoard(int n) {
		int [][] board = new int[n][n];

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if(i == 0)
					board[i][j] = 1;
				else
					board[i][j] = 0;

		return board;
	}

	public static int[] initIntBoard(int n) {
		int [] board = new int[n];
		for (int i = 0; i < n; i++)
			board[i] = 1;
		return board;
	}

	// Print board with 1's in the location where a queen is
	public static void printBoard(int [][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++)
				System.out.print(board[i][j] + " ");
			System.out.println();
		}
	}

	// Bit Array Hill Climbing local search
	public static long HillClimbing(int [][] board) {
		int nexti, nextj;
		int moves = 0;

		long startTime = System.currentTimeMillis();
		long finishTime = 0;

		while (finishTime - startTime < HILLCLIMBING_TIMEOUT) {
			finishTime = System.currentTimeMillis();
			if(!ErrorCheck(board)) {
				long delta = finishTime-startTime;
				System.out.println("Solution found in " + delta + "ms");
				System.out.println("Objective value: " + ObjectiveValue(board) + "\nMoves: " + moves);
				printBoard(board);
				//printValues(board);
				// Add one to signify success as delta could be 0 ms
				return 1+delta;
			}

			int [][] bestValues = bestValues(board);

			//			System.out.println("best values: ");
			//			for (int k = 1; k <= bestValues[0][0]; k++)
			//				System.out.print(bestValues[k][0] + "," + bestValues[k][1] + " ");
			//			System.out.println();
			if (bestValues[0][0] == 0) {
				System.out.println("HillClimbing could not find a solution.");
				break;
			}
			int k = (int)(Math.random()*bestValues[0][0])+1;
			nexti = bestValues[k][0];
			nextj = bestValues[k][1];

			ChangeState(board, nexti, nextj);
			moves++;
			//			printBoard(board);
			//			printValues(board);

		}

		System.out.println("No solution found");
		printBoard(board);
		System.out.println("Objective value: " + ObjectiveValue(board) + "\nMoves: " + moves);
		return 0;
	}

	// Int Array Hill Climbing local search
//	public static long HillClimbing(int [] board) {
//		int nexti, nextj;
//		int moves = 0;
//
//		long startTime = System.currentTimeMillis();
//		long finishTime = 0;
//
//		while (finishTime - startTime < HILLCLIMBING_TIMEOUT) {
//			finishTime = System.currentTimeMillis();
//			if(!ErrorCheck(board)) {
//				long delta = finishTime-startTime;
//				System.out.println("Solution found in " + delta + "ms");
//				System.out.println("Objective value: " + ObjectiveValue(board) + "\nMoves: " + moves);
//				printBoard(board);
//				//printValues(board);
//				// Add one to signify success as delta could be 0 ms
//				return 1+delta;
//			}
//
//			int [][] bestValues = bestValues(board);
//
//			//			System.out.println("best values: ");
//			//			for (int k = 1; k <= bestValues[0][0]; k++)
//			//				System.out.print(bestValues[k][0] + "," + bestValues[k][1] + " ");
//			//			System.out.println();
//			if (bestValues[0][0] == 0) {
//				System.out.println("HillClimbing could not find a solution.");
//				break;
//			}
//			int k = (int)(Math.random()*bestValues[0][0])+1;
//			nexti = bestValues[k][0];
//			nextj = bestValues[k][1];
//
//			ChangeState(board, nexti, nextj);
//			moves++;
//			//			printBoard(board);
//			//			printValues(board);
//
//		}
//
//		System.out.println("No solution found");
//		printBoard(board);
//		System.out.println("Objective value: " + ObjectiveValue(board) + "\nMoves: " + moves);
//		return 0;
//	}

	// Heuristic: Number of conflicts with other queens (lower value the better)
	public static int value(int [][] board, int i, int j) {
		int k, l, value = 0;
		int boardSize = board.length;

		// Count conflicts within row
		for (k = 0; k < j; k++)
			if (board[i][k] == 1)
				value++;
		for (k = j+1; k < boardSize; k++)
			if (board[i][k] == 1)
				value++;

		// Count conflicts within -diaganol
		k = i+j;
		l = 0;
		if (k > boardSize-1) {
			k = boardSize-1;
			l = (i+j)- (boardSize-1);
		}
		for (;l < boardSize && k >= 0;) {
			if (k != i && l != j && board[k][l] == 1)
				value++;
			k--;
			l++;
		}

		// Count conflicts within +diaganol
		k = i-j;
		l = 0;
		if (k < 0) {
			l = 0 - k;
			k = 0;
		}
		for (;k < boardSize && l < boardSize;) {
			if (k != i && l != j && board[k][l] == 1)
				value++;
			k++;
			l++;
		}

		return value;
	}

	// Returns an array of best(lowest) values and the count of how many values in the [0,0] index.
	public static int[][] bestValues(int [][] board) {
		int [][] bestValues = new int[board.length*board.length][2];
		int count = 0;
		int minValue = Integer.MAX_VALUE;

		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board.length; j++) {
				if(board[i][j] == 1)
					continue;
				int value = value(board, i, j);
				if(value < minValue) {
					minValue = value;
					count = 1;
					bestValues[count][0] = i;
					bestValues[count][1] = j;
				}
				else if(value == minValue) {
					bestValues[++count][0] = i;
					bestValues[count][1] = j;
				}
			}
		bestValues[0][0] = count;
		return bestValues;
	}

	// Prints board with each positions value
	public static void printValues(int [][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++)
				System.out.printf("%2d", value(board, i, j));
			System.out.println();
		}
	}

	public static int ObjectiveValue(int [][] state) {
		int value = 0;

		for (int i = 0; i < state.length; i++) 
			for (int j = 0; j < state.length; j++)
				if(state[i][j] == 1)
					value += value(state, i, j);
		return value;
	}

	// Moves a queen to nexti, nextj
	public static void ChangeState(int [][] board, int nexti, int nextj) {
		for(int i = 0; i < board.length; i++)
			if(board[i][nextj] == 1) {
				board[i][nextj] = 0;
				board[nexti][nextj] = 1;
			}
	}

	// Moves a queen to nexti, nextj
	public static void ChangeStateIntArray(int [] board, int i, int j) {
		board[j] = i;

	}

	// Checks if any queen location has a conflict with another queen
	public static boolean ErrorCheck(int [][] board) {
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board.length; j++)
				if(board[i][j] == 1 && value(board, i, j) > 0)
					return true;
		return false;
	}
}

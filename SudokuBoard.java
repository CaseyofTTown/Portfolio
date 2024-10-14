
public class SudokuBoard {
	private int[][] board;

	public SudokuBoard(int[][] board) {
		this.board = board;
	}

	// if number is 0, print a dot otherwise print num
	// print a space for readability
	public void printUnsolvedBoard() {
	    // Print column numbers
	    System.out.println("    1   2   3   4   5   6   7   8   9");
	    for (int row = 0; row < board.length; row++) {
	        // Print +===+===+ in between 3x3 blocks
	        if (row == 0 || row == 3 || row == 6) {
	            System.out.println("  +===+===+===+===+===+===+===+===+===+");
	        } else {
	            System.out.println("  +---+---+---+---+---+---+---+---+---+");
	        }

	        // Print row numbers and the grid lines
	        System.out.print((row + 1) + " / ");
	        for (int column = 0; column < board[row].length; column++) {
	            if (column == 3 || column == 6) {
	                System.out.print("/ ");
	            } else if (column != 0) {
	                System.out.print("| ");
	            }
	            // If board has 0 leave blank otherwise add number, add space
	            System.out.print((board[row][column] == 0 ? " " : board[row][column]) + " ");
	            if (column == 8) {
	                System.out.print("/ ");
	            }
	        }
	        System.out.println();
	    }
	    System.out.println("  +===+===+===+===+===+===+===+===+===+");
	    System.out.println();
	}


	 //return value given array cords
	public int getValue(int row, int col) {
		return this.board[row][col];
	}
	public void setValue(int row, int col, int value) {
		this.board[row][col] = value;
	}
	//check if any 0's remain
	public boolean isComplete() {
	    for (int i = 0; i < board.length; i++) {
	        for (int j = 0; j < board[i].length; j++) {
	            if (board[i][j] == 0) {
	                return false;  // There's still an empty spot / 0 
	            }
	        }
	    }
	    System.out.println("congratultaions, you've completed the board");
	    return true; 
	}



}

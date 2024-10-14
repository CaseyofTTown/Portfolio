//for the sudoku game 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainApplication {

	static Scanner input = new Scanner(System.in);
	static int userSel; // for menu input
	public final static int MAX_LIVES = 3; // constant for total lives
	static int lives = MAX_LIVES; // parameter which can be updated for the loop
	public static ArrayList<SudokuBoard> gameList = new ArrayList<SudokuBoard>();
	private static SudokuBoard answerBoard;

	public static void main(String[] args) {
		SudokuBoard easyBoard;
		SudokuBoard mediumBoard;
		SudokuBoard hardBoard;

		// attempt to load the boards

		// create boards from file to present to user
		try {
			easyBoard = fileReader("src/easy.txt");
			mediumBoard = fileReader("src/medium.txt");
			hardBoard = fileReader("src/hard.txt");
			gameList.add(easyBoard);
			gameList.add(mediumBoard);
			gameList.add(hardBoard);
		} catch (Exception e) {
			e.printStackTrace();
		}

		printMenu();

	}

	private static void printMenu() {
		System.out.println("Enter number for selection: ");
		System.out.println("1: Exit game");
		System.out.println("2: Print all Sudoku boards");
		System.out.println("3: Solve Sudoku board");
		handleMenuInput();

	}

	private static void printAllGames() {
		System.out.println("Easy");
		gameList.get(0).printUnsolvedBoard();
		System.out.println("Medium");
		gameList.get(1).printUnsolvedBoard();
		System.out.println("Hard");
		gameList.get(2).printUnsolvedBoard();

	}

	private static void handleMenuInput() {
		int userSel;
		boolean isValid;
		do {
			userSel = input.nextInt();
			isValid = (userSel >= 1 && userSel <= 3);
			if (!isValid) {
				System.out.println("Invalid selection, please try again.");
				printMenu();
			}
		} while (!isValid);

		switch (userSel) {
		case 1: {
			System.exit(1);
		}
		case 2: {
			printAllGames();
			printMenu();
		}
		case 3: {
			sudokuSelector();

		}
		}
	}



// Load in answer board for comparison
private static void solveBoard(int userSelection) {
    switch (userSelection) {
        case 1: {
            answerBoard = fileReader("src/easySolution.txt");
            break;
        }
        case 2: {
            answerBoard = fileReader("src/mediumSolution.txt");
            break;
        }
        case 3: {
            answerBoard = fileReader("src/hardSolution.txt");
            break;
        }

    }
    playGame();
}

	private static void playGame() {
		SudokuBoard currentBoard = gameList.get(userSel - 1); // Select the corresponding board from the list
		lives = MAX_LIVES; // Reset lives at the start of the game

		answerBoard.printUnsolvedBoard();

		while (lives > 0) {
			System.out.println("Enter row (1-9): ");
			int row = input.nextInt() - 1;
			System.out.println("Enter column (1-9): ");
			int col = input.nextInt() - 1;
			System.out.println("Enter value (1-9): ");
			int value = input.nextInt();

			if (row < 0 || row >= 9 || col < 0 || col >= 9) {
				System.out.println("Invalid input! Row and column must be between 1 and 9.");
				continue; // Restart the loop for valid input
			}

			if (answerBoard.getValue(row, col) == value) {
				currentBoard.setValue(row, col, value);
				currentBoard.printUnsolvedBoard(); // Print the board after each correct move
				if (currentBoard.isComplete()) {
					System.out.println("Congratulations! You've completed the Sudoku board.");
					break;
				}
			} else {
				lives--;
				System.out.println("Incorrect value! Lives remaining: " + lives);
				currentBoard.printUnsolvedBoard(); // Print the board to show the state after a wrong move
			}
		}

		if (lives == 0) {
			System.out.println("You've run out of lives. Game over!");
		}
	}

	public static SudokuBoard fileReader(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			StringBuilder content = new StringBuilder();
			while ((line = br.readLine()) != null) {
				content.append(line).append(",");
			}
			String[] elements = content.toString().split(",");

			int[][] board = new int[9][9];
			int index = 0;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					String element = elements[index].trim();
					board[i][j] = element.isEmpty() ? 0 : Integer.parseInt(element);
					index++;
				}
			}
			return new SudokuBoard(board);
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void sudokuSelector() {
		userSel = 0;
		do {
			System.out.println("Which board would you like to solve? ");
			System.out.println("Easy [1] | Medium [2] | Hard[3]");
			userSel = input.nextInt();
		} while (userSel < 1 || userSel > 3);

		solveBoard(userSel);
	}

}

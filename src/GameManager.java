/*
 * Name: Paula Toth
 * Login: cs8bkk
 * Date: April 30, 2015
 * File: GameManager.java
 * Sources of Help: none
 *
 * GameManager is a class that contains the mechanics 
 * for how the game runs.
 * Contains constructors for starting new games/loading 
 * games and contains the play method.
 */
import java.util.*;
import java.io.*;

public class GameManager {
	// Instance variables
	private Board board; // The actual 2048 board
	private String outputFileName;

	// File to save the board to when exiting

	/*
	 * Name: GameManager (Constructor) Purpose: Generate new game 
	 * Parameters: int boardSize, String outputBoard, Random random 
	 * Return: none it's a constructor
	 */
	GameManager(int boardSize, String outputBoard, Random random) {
		this.board = new Board(boardSize, random);
		this.outputFileName = outputBoard;
	}

	/*
	 * Name: GamerManager Purpose: Load a saved game
	 * Parameters: String inputBoard, String outputBoard, Random random
	 * Return: none it's a constructor
	 */
	GameManager(String inputBoard, String outputBoard, Random random)
			throws IOException {
		this.board = new Board(inputBoard, random);
		this.outputFileName = outputBoard;
	}

	// valid moves are:
	// w - Move up
	// s - Move Down
	// a - Move Left
	// d - Move Right
	// q - Quit and Save Board
	//
	// If an invalid command is received then print the controls
	// to remind the user of the valid moves.
	//
	// Once the player decides to quit or the game is over,
	// save the game board to a file based on the outputFileName
	// string that was set in the constructor and then return
	//
	// If the game is over print "Game Over!" to the terminal

	/*
	 * Name: play Purpose: Main play loop, takes in input from the user to
	 * specify moves to execute Parameters: none Return: void
	 */
	public void play() throws IOException {
		printControls();
		System.out.println(board.toString());

		Scanner input = new Scanner(System.in);
		// excutes while game is still in play
		while (!board.isGameOver()) {
			Direction direction;
			char userInput = input.nextLine().charAt(0);

			// codes for 'w' key which refers to
			// up direction
			if (userInput == 'w') {
				direction = Direction.UP;
				// condition for ability to move
				if (board.canMove(direction)) {
					board.move(direction);
					board.addRandomTile();
				}

			} else {
				// codes for 'a' referring to
				// left direction
				if (userInput == 'a') {
					direction = Direction.LEFT;
					// condition for ability to move
					if (board.canMove(direction)) {
						board.move(direction);
						board.addRandomTile();
					}
				} else {
					// codes for 's' referring to
					// right direction
					if (userInput == 's') {
						direction = Direction.DOWN;
						// condition for ability to move
						if (board.canMove(direction)) {
							board.move(direction);
							board.addRandomTile();
						}
					} else {
						// codes for 'd' referring to
						// down direction
						if (userInput == 'd') {
							direction = Direction.RIGHT;
							// condition for ability to move
							if (board.canMove(direction)) {
								board.move(direction);
								board.addRandomTile();
							}
						} else {
							// codes for 'q' referring to
							// quit command
							if (userInput == 'q') {
								// saves board
								board.saveBoard(outputFileName);
								// breaks current game loop
								break;
							} else {
								//codes for invalid input
								printControls();
							}
						}
					}
				}
			}
			System.out.println(board.toString());
		}
		// condition for game ending
		if (board.isGameOver())
			System.out.println("Game Over!");
		input.close();
	}

	// Print the Controls for the Game
	private void printControls() {
		System.out.println("  Controls:");
		System.out.println("    w - Move Up");
		System.out.println("    s - Move Down");
		System.out.println("    a - Move Left");
		System.out.println("    d - Move Right");
		System.out.println("    q - Quit and Save Board");
		System.out.println();
	}
}

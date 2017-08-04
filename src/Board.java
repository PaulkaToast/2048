/*
 * Name: Paula Toth
 * Login: cs8bkk
 * Date: April 30, 2015
 * File: Board.java
 * Sources of Help: none
 *
 * Description: Contains all the constructor to create 
 * board objects as well as methods to 
 * manipulate the board in various ways.
 */

/**     Sample Board
  *
  *      0   1   2   3
  *  0   -   -   -   -
  *  1   -   -   -   -
  *  2   -   -   -   -
  *  3   -   -   -   -
  *
  *  The sample board shows the index values for the columns and rows
  *  Remember that you access a 2D array by first specifying the row
  *  and then the column: grid[row][column]
  */

import java.util.*;
import java.io.*;

public class Board {
  public final int NUM_START_TILES = 2;
  public final int TWO_PROBABILITY = 90;
  public final int GRID_SIZE;
  
  private final Random random;
  private int[][] grid;
  private int score;
  
  // Name: Board (constructor)
  // Purpose: Constructs a fresh board with random tiles
  // Parameters: int boardSize, Random random.
  // Return: no return type, constructor.
  public Board(int boardSize, Random random) {
    // instantiates random object to inputed random seed
    this.random = random;
    // instantiates new board of inputed board size
    this.grid = new int[boardSize][boardSize];
    // initializes score to zero
    this.score = 0;
    // initialized GRID_SIZE to the inputed board size
    GRID_SIZE = boardSize;
    // adds two random tiles
    // to create starting game conditions
    this.addRandomTile();
    this.addRandomTile();
  }// end of constructor body
  
  // Name: Board (constructor)
  // Purpose: Construct a board based off of an input file, basically a load
  // save.
  // Parameters: String inputBoard, Random random.
  // Return: no return type, constructor.
  public Board(String inputBoard, Random random) throws IOException {
    // instantiates random object to inputed random seed
    this.random = random;
    // takes input string name and creates file object
    File savedGame = new File(inputBoard);
    // scanner to read through saved game file
    Scanner input = new Scanner(savedGame);
    // takes first line of file and saves into GRID_SIZE
    GRID_SIZE = Integer.parseInt(input.nextLine());
    // takes second line of file and saves it into score
    this.score = Integer.parseInt(input.nextLine());
    // instantiates new board of saved board size
    this.grid = new int[GRID_SIZE][GRID_SIZE];
    // loop to iterate through grid
    for (int x = 0; x < GRID_SIZE; x++) {
      // splits the strings in the file
      String[] row = input.nextLine().split(" ");
      for (int y = 0; y < GRID_SIZE; y++) {
        // takes the string and makes it an int
        grid[x][y] = Integer.parseInt(row[y]);
      }
    }
    // closes
    input.close();
  }// end of constructor body
  
  // Name: saveBoard
  // Purpose: Saves the current board to a file
  // Parameters: String outputBoard
  // Return: void
  public void saveBoard(String outputBoard) throws IOException {
    // new writer object to write to output board file
    PrintWriter writer = new PrintWriter(outputBoard);
    // writes first line GRID_SIZE
    writer.println(GRID_SIZE);
    // writes second line score
    writer.println(score);
    // loop to iterate through the grid
    for (int x = 0; x < GRID_SIZE; x++) {
      for (int y = 0; y < GRID_SIZE; y++) {
        // writes each tile of board into file
        writer.printf(grid[x][y] + " ");
      }
      // separates by a nextline
      writer.println();
    }
    // closes writer
    writer.close();
  }// end of method body
  
  /*
   * Name: addRandomTile Purpose: Adds a Random Tile (of value 2 or 4) to a
   * random empty space on the board Parameters: none Return: void
   */
  public void addRandomTile() {
    // instantiates variables
    int count = 0;
    int location;
    int value;
    // makes sure count is positive
    
    // loops through the board
    for (int y = 0; y < GRID_SIZE; y++) {
      for (int x = 0; x < GRID_SIZE; x++) {
        // conditional to check if tile is empty
        if (grid[x][y] == 0) {
          // counts up empty tiles
          count++;
        }
      }
    }
    if (count > 0) {
      location = random.nextInt(count);
      value = random.nextInt(100);
      int emptySpaces = 0;
      for (int x = 0; x < GRID_SIZE; x++) {
        for (int y = 0; y < GRID_SIZE; y++) {
          
          if (grid[x][y] == 0) {
            if (location == emptySpaces) {
              grid[x][y] = value < TWO_PROBABILITY ? 2 : 4;
            }
            emptySpaces++;
          }
        }
      }
    }
  }// end of method body
  
  // Helper Method: canMoveLeft
  // Determines if we can move in the direction
  // LEFT(-1, 0)
  // Parameters: none
  // Return: boolean
  private boolean canMoveUp() {
    // iterates through board values
    for (int y = 0; y < GRID_SIZE; y++) {
      for (int x = GRID_SIZE - 1; x > 0; x--) {
        // conditional to prevent
        // ArrayIndexOutOfBoundException
        if (grid[x][y] == 0 && x + 1 == GRID_SIZE)
          continue;
        // conditional to prevent
        // return of true in the case of consecutive zeros
        if (grid[x][y] == 0 && grid[x + 1][y] == 0)
          continue;
        // conditional to detect ideal moving conditions
        if (grid[x][y] == grid[x - 1][y] || grid[x - 1][y] == 0){
          // can move: returns true
          return true;
        }
      }
    }
    // cannot move: returns false
    return false;
  }// end of method body
  
  // Helper Method: canMoveRight
  // Determines if we can move in a the direction
  // RIGHT(1, 0)
  // Parameters: none
  // Return: boolean true/false
  private boolean canMoveDown() {
    // iterates through board values
    for (int y = 0; y < GRID_SIZE; y++) {
      for (int x = 0; x < GRID_SIZE - 1; x++) {
        // conditional to prevent
        // ArrayIndexOutOfBoundException
        if (grid[x][y] == 0 && x - 1 < 0)
          continue;
        // conditional to prevent
        // return of true in the case of consecutive zeros
        if (grid[x][y] == 0 && grid[x - 1][y] == 0)
          continue;
        // conditional to detect ideal moving conditions
        if (grid[x][y] == grid[x + 1][y] || grid[x + 1][y] == 0){
          // can move: returns true
          return true;
        }
      }
    }
    // cannot move: returns false
    return false;
  }// end of method body
  
  // Helper Method: canMoveUp
  // Determines if we can move in a the direction
  // UP(0, -1)
  // Parameters: None
  // Return: boolean true/false
  private boolean canMoveLeft() {
    // iterates through board values
    for (int x = 0; x < GRID_SIZE; x++) {
      for (int y = GRID_SIZE - 1; y > 0; y--) {
        // conditional to prevent
        // ArrayIndexOutOfBoundException
        if (grid[x][y] == 0 && y + 1 == GRID_SIZE)
          continue;
        // conditional to prevent
        // return of true in the case of consecutive zeros
        if (grid[x][y] == 0 && grid[x][y + 1] == 0)
          continue;
        // conditional to detect ideal moving conditions
        if (grid[x][y] == grid[x][y - 1] || grid[x][y - 1] == 0){
          // can move: returns true
          return true;
        }
      }
    }
    // cannot move: returns false
    return false;
  }// end of method body
  
  // Helper Method: canMoveDown
  // Determines if we can move in a the direction
  // DOWN(0, 1)
  // Parameters: none
  // Return: boolean true/false
  private boolean canMoveRight() {
    // iterates through board values
    for (int x = 0; x < GRID_SIZE; x++) {
      for (int y = 0; y < GRID_SIZE - 1; y++) {
        // conditional to prevent
        // ArrayIndexOutOfBoundException
        if (grid[x][y] == 0 && y - 1 < 0)
          continue;
        // conditional to prevent
        // return of true in the case of consecutive zeros
        if (grid[x][y] == 0 && grid[x][y - 1] == 0)
          continue;
        // conditional to detect ideal moving conditions
        if (grid[x][y] == grid[x][y + 1] || grid[x][y + 1] == 0){
          // can move: returns true
          return true;
        }
      }
    }
    // cannot move: returns false
    return false;
  }// end of method body
  
  // Method: canMove
  // Determines if we can move in any given direction
  // UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0)
  // Parameters: Direction direction
  // Returns: boolean true/false
  public boolean canMove(Direction direction) {
    // conditional determines input direction is Left
    if (direction.getX() == -1)
      // helper method checks ability to move left
      // returns true/false
      return canMoveLeft();
    // conditional determines input direction is Right
    if (direction.getX() == 1)
      // helper method checks ability to move right
      // returns true/false
      return canMoveRight();
    // conditional determines input direction is Up
    if (direction.getY() == -1)
      // helper method checks ability to move up
      // returns true/false
      return canMoveUp();
    // conditional determines input direction is Down
    if (direction.getY() == 1)
      // helper method checks ability to move down
      // returns true/false
      return canMoveDown();
    // returns false if all conditions fail
    return false;
  }// end of method body
  
  // Method: isGameOver
  // Check to see if the game has ended/is over
  // Parameters: none
  // Returns: boolean true/false
  public boolean isGameOver() {
    // conditional to check for all possible move directions
    // UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0)
    // using helper methods to see if they all return false
    if (canMoveLeft() == false && canMoveRight() == false
          && canMoveUp() == false && canMoveDown() == false) {
      // Game is over: returns true if all conditions are met
      return true;
    }
    // Game is over: returns false if not all conditions are met
    return false;
  }// end of method body
  
  // Helper Method: moveLeft
  // Performs a move Operation in the direction
  // LEFT(-1, 0)
  // Parameters: none
  // Returns: boolean true/false
  private boolean moveLeft() {
    // column start position for iteration variable
    int colStart = 0;
    // row start position for iteration variable
    int rowStart = 0;
    // loop to iterate through the board row values
    for (int row = rowStart; row < GRID_SIZE; row++) {
      // loop to perform inside task multiple times
      for (int i = 0; i < GRID_SIZE - 1; i++) {
        // loop to iterate through the board column values
        for (int col = colStart; col < GRID_SIZE - 1; col++) {
          // conditional to prevent
          // ArrayIndexOutOfBoundException
          if (col + 1 > GRID_SIZE - 1)
            continue;
          // conditional to swap empty space with a
          // value next to current tile
          if (grid[row][col] == 0 && grid[row][col + 1] != 0) {
            // sets current tile to value of next tile
            grid[row][col] = grid[row][col + 1];
            // set next tile to zero
            grid[row][col + 1] = 0;
          }
          // conditional to check if two adjacent tiles are equal
          // in value and not zero to prevent zero swapping
          if (grid[row][col] != 0
                && grid[row][col] == grid[row][col + 1]) {
            // sets current tile to twice its original value
            grid[row][col] *= 2;
            // sets adjacent tile to zero
            grid[row][col + 1] = 0;
            // increase score by the value of the merged tile
            this.score = score + grid[row][col];
            // increases colStart to move bound
            // prevents double merging
            colStart = col + 1;
          }
        }
      }
      // resets colStart when moving to next row
      colStart = 0;
    }
    // moving complete: returns true
    return true;
  }// end of method body
  
  // Helper Method: moveLeft
  // Performs a move Operation in the direction
  // RIGHT(1, 0)
  // Parameters: none
  // Returns: boolean true/false
  private boolean moveRight() {
    // column start position for iteration variable
    int colStart = GRID_SIZE - 1;
    // row start position for iteration variable
    int rowStart = 0;
    // loop to iterate through the board row values
    for (int row = rowStart; row < GRID_SIZE; row++) {
      // loop to perform inside task multiple times
      for (int i = 0; i < GRID_SIZE - 1; i++) {
        // loop to iterate through the board column values
        for (int col = colStart; col >= 0; col--) {
          // conditional to prevent
          // ArrayIndexOutOfBoundException
          if (col - 1 < 0)
            continue;
          // conditional to swap empty space with a
          // value next to current tile
          if (grid[row][col] == 0 && grid[row][col - 1] != 0) {
            // sets current tile to value of next tile
            grid[row][col] = grid[row][col - 1];
            // set next tile to zero
            grid[row][col - 1] = 0;
          }
          // conditional to check if two adjacent tiles are equal
          // in value and not zero to prevent zero swapping
          if (grid[row][col] != 0
                && grid[row][col] == grid[row][col - 1]) {
            // sets current tile to twice its original value
            grid[row][col] *= 2;
            // sets adjacent tile to zero
            grid[row][col - 1] = 0;
            // increase score by the value of the merged tile
            this.score = score + grid[row][col];
            // increases colStart to move bound
            // prevents double merging
            colStart = col - 1;
          }
        }
      }
      // resets colStart when moving to next row
      colStart = GRID_SIZE - 1;
    }
    // moving complete: returns true
    return true;
  }// end of method body
  
  // Helper Method: moveLeft
  // Performs a move Operation in the direction
  // UP(0, -1)
  // Parameters: none
  // Returns: boolean true/false
  private boolean moveUp() {
    // column start position for iteration variable
    int colStart = 0;
    // row start position for iteration variable
    int rowStart = 0;
    // loop to iterate through the board column values
    for (int col = colStart; col < GRID_SIZE; col++) {
      // loop to perform inside task multiple times
      for (int i = 0; i < GRID_SIZE - 1; i++) {
        // loop to iterate through the board row values
        for (int row = rowStart; row < GRID_SIZE - 1; row++) {
          // conditional to prevent
          // ArrayIndexOutOfBoundException
          if (row + 1 > GRID_SIZE - 1)
            continue;
          // conditional to swap empty space with a
          // value next to current tile
          if (grid[row][col] == 0 && grid[row + 1][col] != 0) {
            // sets current tile to value of next tile
            grid[row][col] = grid[row + 1][col];
            // set next tile to zero
            grid[row + 1][col] = 0;
          }
          // conditional to check if two adjacent tiles are equal
          // in value and not zero to prevent zero swapping
          if (grid[row][col] != 0
                && grid[row][col] == grid[row + 1][col]) {
            // sets current tile to twice its original value
            grid[row][col] *= 2;
            // sets adjacent tile to zero
            grid[row + 1][col] = 0;
            // increase score by the value of the merged tile
            this.score = score + grid[row][col];
            // increases colStart to move bound
            // prevents double merging
            rowStart = row + 1;
          }
        }
      }
      // resets roqStart when moving to next column
      rowStart = 0;
    }
    // moving complete: returns true
    return true;
    
  }// end of method body
  
  // Helper Method: moveLeft
  // Performs a move Operation in the direction
  // DOWN(0, 1)
  // Parameters: none
  // Returns: boolean true/false
  private boolean moveDown() {
    // column start position for iteration variable
    int colStart = 0;
    // row start position for iteration variable
    int rowStart = GRID_SIZE - 1;
    // loop to iterate through the board column values
    for (int col = colStart; col < GRID_SIZE; col++) {
      // loop to perform inside task multiple times
      for (int i = 0; i < GRID_SIZE - 1; i++) {
        // loop to iterate through the board row values
        for (int row = rowStart; row >= 0; row--) {
          // conditional to prevent
          // ArrayIndexOutOfBoundException
          if (row - 1 < 0)
            continue;
          // conditional to swap empty space with a
          // value next to current tile
          if (grid[row][col] == 0 && grid[row - 1][col] != 0) {
            // sets current tile to value of next tile
            grid[row][col] = grid[row - 1][col];
            // set next tile to zero
            grid[row - 1][col] = 0;
          }
          // conditional to check if two adjacent tiles are equal
          // in value and not zero to prevent zero swapping
          if (grid[row][col] != 0
                && grid[row][col] == grid[row - 1][col]) {
            // sets current tile to twice its original value
            grid[row][col] *= 2;
            // sets adjacent tile to zero
            grid[row - 1][col] = 0;
            // increase score by the value of the merged tile
            this.score = score + grid[row][col];
            // increases colStart to move bound
            // prevents double merging
            rowStart = row - 1;
          }
        }
      }
      // resets roqStart when moving to next column
      rowStart = GRID_SIZE - 1;
    }
    // moving complete: returns true
    return true;
    
  }// end of method body
  
  // Method: move
  // Performs a move Operation in a given direction
  // UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
  // Parameters: direction
  // Returns: boolean true/false
  public boolean move(Direction direction) {
    // conditional determines input direction is Left
    if (direction.getX() == -1)
      // helper method performs move to the left
      // returns true/false
      return moveLeft();
    // conditional determines input direction is Right
    if (direction.getX() == 1)
      // helper method performs move to the right
      // returns true/false
      return moveRight();
    // conditional determines input direction is Up
    if (direction.getY() == -1)
      // helper method performs move upwards
      // returns true/false
      return moveUp();
    // conditional determines input direction is Down
    if (direction.getY() == 1)
      // helper method performs move downwards
      // returns true/false
      return moveDown();
    // returns false if all conditions fail
    return false;
  }// end of method body
  
  // Return the reference to the 2048 Grid
  public int[][] getGrid() {
    return grid;
  }// end of method body
  
  // Return the score
  public int getScore() {
    return score;
  }// end of method body
  
  @Override
  public String toString() {
    StringBuilder outputString = new StringBuilder();
    outputString.append(String.format("Score: %d\n", score));
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int column = 0; column < GRID_SIZE; column++)
        outputString.append(grid[row][column] == 0 ? "    -" : String
                              .format("%5d", grid[row][column]));
      
      outputString.append("\n");
    }
    return outputString.toString();
  }// end of method body
}// end of class body

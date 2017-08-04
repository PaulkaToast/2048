/*
 * Name: Paula Toth 
 * Login: cs8bkk
 * Date: May 28, 2015
 * File: Gui2048.java
 * Sources of Help: Piazza, lecture slides, discussion slides
 *
 * This program is the graphical interface and play method for 2048
 */
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/*
 * Name: Gui2048
 * Purpose: represent the graphical interface for 2048 with all
 * needed methods to create the playable environment.
 */
public class Gui2048 extends Application {
  private Random random = new Random();
  private String outputBoard; // The filename for where to save the Board
  private Board board; // The 2048 Game Board
  // Fill colors for each of the Tile values
  private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
  private static final Color COLOR_2 = Color.rgb(238, 228, 218);
  private static final Color COLOR_4 = Color.rgb(237, 224, 200);
  private static final Color COLOR_8 = Color.rgb(242, 177, 121);
  private static final Color COLOR_16 = Color.rgb(245, 149, 99);
  private static final Color COLOR_32 = Color.rgb(246, 124, 95);
  private static final Color COLOR_64 = Color.rgb(246, 94, 59);
  private static final Color COLOR_128 = Color.rgb(237, 207, 114);
  private static final Color COLOR_256 = Color.rgb(237, 204, 97);
  private static final Color COLOR_512 = Color.rgb(237, 200, 80);
  private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
  private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
  private static final Color COLOR_OTHER = Color.BLACK;
  private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);
  //Fill colors for text
  private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
  private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101);
  private static final Color COLOR_VALUE_OVER = Color.rgb(119, 110, 101, 0.73);
  //Instance variables
  private Rectangle tiles[][];
  private Text nums[][];
  private Text score;
  private Rectangle trans;
  private Text gameOver;
  private GridPane pane;
  private static int SCORE_SIZE = 2;
  private static int GRID_SIZE = 4;
  private static int TILE_SIZE = 100;
  private static int FONT_SIZE = 30;
  private static int GAME_OVER = 50;
  
  public static void main(String[] args) {
	  launch(args);
  }
  
  /*
   * Name: start
   * Purpose: this is like our play method, it starts the game and
   * pulls up the graphical window for the user to manipulate
   * Parameters: Stage primaryStage
   * Return: void
   */
  @Override
  public void start(Stage primaryStage) {
    // Process Arguments and Initialize the Game Board
    processArgs(getParameters().getRaw().toArray(new String[0]));
    
    // creates a pain and formats it a bit
    pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
    pane.setHgap(5.5);
    pane.setVgap(5.5);
    
    // sets the background color
    pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
    
    // adds the pane to the scene
    Scene scene = new Scene(pane);
    primaryStage.setTitle("Gui2048");
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setMinWidth(600);
    primaryStage.setMinHeight(600);
    
    //adds event handler to scene
    scene.setOnKeyPressed(new myKeyHandler());
    
    //creates the title of the game gui
    Text title = new Text();
    title.setText("2048");
    title.setFont(Font.font("Helvetica Neue", 
                            FontWeight.BOLD,FONT_SIZE));
    title.setFill(COLOR_OTHER);
    pane.add(title,0,0);
    GridPane.setColumnSpan(title, SCORE_SIZE);
    GridPane.setHalignment(title, HPos.CENTER);
    //creates the score counter for the gui
    score = new Text();
    score.setText("Score: " + board.getScore());
    score.setFont(Font.font("Helvetica Neue", 
                            FontWeight.BOLD,FONT_SIZE));
    score.setFill(COLOR_OTHER);
    pane.add(score,2,0);
    GridPane.setColumnSpan(score, SCORE_SIZE);
    GridPane.setHalignment(score, HPos.CENTER);
    
    //creates the arrays for the tiles and text
    tiles = new Rectangle[GRID_SIZE][GRID_SIZE];
    nums = new Text[GRID_SIZE][GRID_SIZE];
    //creates the tiles and text using for loop
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        //formats the rectangles
        tiles[i][j] = new Rectangle();
        tiles[i][j].setWidth(TILE_SIZE);
        tiles[i][j].setHeight(TILE_SIZE);
        tiles[i][j].setFill(COLOR_EMPTY);
        //formats the text
        nums[i][j] = new Text();
        nums[i][j].setText("");
        nums[i][j].setFont(Font.font("Helvetica Neue", 
                                     FontWeight.BOLD,FONT_SIZE));
        nums[i][j].setFill(COLOR_VALUE_DARK);
        // adds square to pane
        pane.add(tiles[i][j], j, i + 1);
        pane.add(nums[i][j], j, i + 1);
        GridPane.setHalignment(nums[i][j], HPos.CENTER);
      }
    }
    //creates the game over transparent rectangle
    trans = new Rectangle();
    GridPane.setColumnSpan(trans, GRID_SIZE);
    GridPane.setRowSpan(trans, GRID_SIZE + 1);
    trans.setWidth(600);
    trans.setHeight(600);
    GridPane.setValignment(trans, VPos.CENTER);
    GridPane.setHalignment(trans, HPos.CENTER);
    //creates the game over text
    gameOver = new Text();
    GridPane.setColumnSpan(gameOver, GRID_SIZE);
    GridPane.setRowSpan(gameOver, GRID_SIZE + 1);
    GridPane.setValignment(gameOver, VPos.CENTER);
    GridPane.setHalignment(gameOver, HPos.CENTER);
    //try catch 
    try {
      //creates new game
      board = new Board(GRID_SIZE, random);
      //saves the game
      System.out.println("Saving Board to 2048.board");
      board.saveBoard(outputBoard);
    } catch (IOException e) {
      System.out.println("saveBoard threw an Exception");
    }
    //updates the gui
    update();
    //creates new keyHandler object
    scene.setOnKeyPressed(new myKeyHandler());
  }
  
  /*
   * Name: update
   * Purpose: this method updates the graphical interface of the board
   * Parameters: none
   * Return: void
   */
  public void update() {
    
    int boardInfo[][] = board.getGrid();
    //updates the score text
    score.setText("Score: " + board.getScore());
    //updates the tiles
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        if (boardInfo[i][j] == 0) {
          tiles[i][j].setFill(COLOR_EMPTY);
          nums[i][j].setText("");
          nums[i][j].setFill(COLOR_VALUE_DARK);
        }
        if (boardInfo[i][j] == 2) {
          tiles[i][j].setFill(COLOR_2);
          nums[i][j].setText("2");
          nums[i][j].setFill(COLOR_VALUE_DARK);
        }
        if (boardInfo[i][j] == 4) {
          tiles[i][j].setFill(COLOR_4);
          nums[i][j].setText("4");
          nums[i][j].setFill(COLOR_VALUE_DARK);
        }
        if (boardInfo[i][j] == 8) {
          tiles[i][j].setFill(COLOR_8);
          nums[i][j].setText("8");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 16) {
          tiles[i][j].setFill(COLOR_16);
          nums[i][j].setText("16");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 32) {
          tiles[i][j].setFill(COLOR_32);
          nums[i][j].setText("32");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 64) {
          tiles[i][j].setFill(COLOR_64);
          nums[i][j].setText("64");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 128) {
          tiles[i][j].setFill(COLOR_128);
          nums[i][j].setText("128");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 256) {
          tiles[i][j].setFill(COLOR_256);
          nums[i][j].setText("256");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 512) {
          tiles[i][j].setFill(COLOR_512);
          nums[i][j].setText("512");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 1024) {
          tiles[i][j].setFill(COLOR_1024);
          nums[i][j].setText("1024");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] == 2048) {
          tiles[i][j].setFill(COLOR_2048);
          nums[i][j].setText("2048");
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
        if (boardInfo[i][j] > 2048) {
          tiles[i][j].setFill(COLOR_OTHER);
          nums[i][j].setText("" + boardInfo[i][j]);
          nums[i][j].setFill(COLOR_VALUE_LIGHT);
        }
      }
    }
    //updates if the game ends
    if(board.isGameOver()){
      pane.add(trans, 0, 0);
      pane.add(gameOver, 0, 0);
      trans.setFill(COLOR_GAME_OVER);
      gameOver.setText("Game Over");
      gameOver.setFont(Font.font("Helvetica Neue", 
                                 FontWeight.BOLD,GAME_OVER));
      gameOver.setFill(COLOR_VALUE_OVER);
    }
  }
  
  /*
   * Name: myKeyHandler
   * Purpose: this inner class deals with the keystroke of the user
   * and what should be done in reference to those strokes 
   */
  private class myKeyHandler implements EventHandler<KeyEvent> {
    
    /*
     * Name: handle
     * Purpose: this tells the program what to do based on the
     * key presses of the user
     * Parameters: KeyEvent e
     * Return: void
     */
    @Override
    public void handle(KeyEvent e) {
      //checks if game is over
      if (board.isGameOver() == false) {
        //checks what key was pressed
        if (e.getText().equals("a")) {
          Direction direction = Direction.LEFT;
          //checks if a move can be made
          if (board.canMove(direction)) {
            //prints move, moves and adds tile
            System.out.println("Moving Left");
            board.move(direction);
            board.addRandomTile();
            try {
              //prints save, saves and updates
              board.saveBoard(outputBoard);
              System.out.println("Saving Board to 2048.board");
              update();
            } catch (IOException eX) {
              System.out.println("saveBoard threw an Exception");
            }
          }
        }
        //checks what key was pressed
        if (e.getText().equals("w")) {
          Direction direction = Direction.UP;
          //checks if a move can be made
          if (board.canMove(direction)) {
            //prints move, moves and adds tile
            System.out.println("Moving UP");
            board.move(direction);
            board.addRandomTile();
            try {
              //prints save, saves and updates
              board.saveBoard(outputBoard);
              System.out.println("Saving Board to 2048.board");       update();
            } catch (IOException eX) {
              System.out.println("saveBoard threw an Exception");
            }
          }
        }
        //checks what key was pressed
        if (e.getText().equals("d")) {
          Direction direction = Direction.RIGHT;
          //checks if a move can be made
          if (board.canMove(direction)) {
            //prints move, moves and adds tile
            System.out.println("Moving RIGHT");
            board.move(direction);
            board.addRandomTile();
            try {
              //prints save, saves and updates
              board.saveBoard(outputBoard);
              System.out.println("Saving Board to 2048.board");
              update();
            } catch (IOException eX) {
              System.out.println("saveBoard threw an Exception");
            }
          }
        }
        //checks what key was pressed
        if (e.getText().equals("s")) {
          Direction direction = Direction.DOWN;
          //checks if a move can be made
          if (board.canMove(direction)) {
            //prints move, moves and adds tile
            System.out.println("Moving DOWN");
            board.move(direction);
            board.addRandomTile();
            try {
              //prints save, saves and updates
              board.saveBoard(outputBoard);
              System.out.println("Saving Board to 2048.board");
              update();
            } catch (IOException eX) {
              System.out.println("saveBoard threw an Exception");
            }
          }
        }
      }
    }
  }
  
  /** DO NOT EDIT BELOW */
  
  // The method used to process the command line arguments
  private void processArgs(String[] args) {
    String inputBoard = null; // The filename for where to load the Board
    int boardSize = 0; // The Size of the Board
    
    // Arguments must come in pairs
    if ((args.length % 2) != 0) {
      printUsage();
      System.exit(-1);
    }
    
    // Process all the arguments
    for (int i = 0; i < args.length; i += 2) {
      if (args[i].equals("-i")) { // We are processing the argument that
        // specifies
        // the input file to be used to set the
        // board
        inputBoard = args[i + 1];
      } else if (args[i].equals("-o")) { // We are processing the argument
        // that specifies
        // the output file to be used to
        // save the board
        outputBoard = args[i + 1];
      } else if (args[i].equals("-s")) { // We are processing the argument
        // that specifies
        // the size of the Board
        boardSize = Integer.parseInt(args[i + 1]);
      } else { // Incorrect Argument
        printUsage();
        System.exit(-1);
      }
    }
    
    // Set the default output file if none specified
    if (outputBoard == null)
      outputBoard = "2048.board";
    // Set the default Board size if none specified or less than 2
    if (boardSize < 2)
      boardSize = 4;
    
    // Initialize the Game Board
    try {
      if (inputBoard != null)
        board = new Board(inputBoard, new Random());
      else
        board = new Board(boardSize, new Random());
    } catch (Exception e) {
      System.out.println(e.getClass().getName()
                           + " was thrown while creating a " + "Board from file "
                           + inputBoard);
      System.out.println("Either your Board(String, Random) "
                           + "Constructor is broken or the file isn't "
                           + "formated correctly");
      System.exit(-1);
    }
  }
  
  // Print the Usage Message
  private static void printUsage() {
    System.out.println("Gui2048");
    System.out.println("Usage:  Gui2048 [-i|o file ...]");
    System.out.println();
    System.out
      .println("  Command line arguments come in pairs of the form: <command> <argument>");
    System.out.println();
    System.out
      .println("  -i [file]  -> Specifies a 2048 board that should be loaded");
    System.out.println();
    System.out
      .println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
    System.out
      .println("                If none specified then the default \"2048.board\" file will be used");
    System.out
      .println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
    System.out
      .println("                specified.  If both -s and -i are used, then the size of the board");
    System.out
      .println("                will be determined by the input file. The default size is 4.");
  }
}
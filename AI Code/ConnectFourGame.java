import java.util.Scanner;

public class ConnectFourGame {

  // define the number of rows and columns in the game grid
  private static final int ROWS = 6;
  private static final int COLUMNS = 7;

  // define the character to use for empty cells
  private static final char EMPTY_CELL = ' ';

  // define the characters to use for each player's pieces
  private static final char PLAYER_PIECE = 'X';
  private static final char COMPUTER_PIECE = 'O';

  // define the grid as a two-dimensional array of characters
  private char[][] grid = new char[ROWS][COLUMNS];

  // define the scanner to read user input
  private Scanner scanner = new Scanner(System.in);

  public ConnectFourGame() {
    // initialize the grid with empty cells
    for (int row = 0; row < ROWS; row++) {
      for (int column = 0; column < COLUMNS; column++) {
        grid[row][column] = EMPTY_CELL;
      }
    }
  }

  // define a method that starts a new game
  public void start() {
    // print the initial grid
    printGrid();
    // loop until the game is over
    while (true) {
      // prompt the user to make a move
      System.out.print("Enter a column number (1-7): ");
      // read the user's move
      int column = scanner.nextInt() - 1;
      // place the player's piece in the selected column
      int row = placePiece(column, PLAYER_PIECE);
      // if the move was invalid, skip the rest of the loop and ask the user to try again
      if (row == -1) {
        continue;
      }
      // print the updated grid
      printGrid();
      // check if the player has won
      if (checkWin(row, column, PLAYER_PIECE)) {
        System.out.println("You win!");
        break;
      }
      // check if the game is a draw
      if (checkDraw()) {
        System.out.println("Draw!");
        break;
      }
      // let the computer make a move
      column = makeMove();
      // place the computer's piece in the selected column
      row = placePiece(column, COMPUTER_PIECE);
      // print the updated grid
      printGrid();
      // check if the computer has won
      if (checkWin(row, column, COMPUTER_PIECE)) {
        System.out.println("Computer wins!");
        break;
      }
      // check if the game is a draw
      if (checkDraw()) {
        System.out.println("Draw!");
        break;
      }
    }
}
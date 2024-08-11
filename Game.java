import java.util.Scanner;

public class Game {

  private Player currentPlayer;
  private Player playerOne;
  private Player playerTwo;
  private boolean isCheckmate;
  private boolean isStalemate;
  private Board board;

  public Game() {
    this.playerOne = new Player("white");
    this.playerTwo = new Player("black");
    this.currentPlayer = playerOne;
    this.isCheckmate = false;
    this.isStalemate = false;
    this.board = new Board();
  }

  private void switchTurn() {
    this.currentPlayer = this.currentPlayer == playerOne ? playerTwo : playerOne ;
  }

  private Player getOpponent() {
    return this.currentPlayer == playerOne ? playerTwo : playerOne ;
  }

  private static void resetMove(Square[] squares, Piece piece) {
    System.out.println("You are in check! need to revert");
    squares[0].setPiece(piece);
    squares[1].setPiece(null);
  }

  private static void playMove(Square[] squares, Piece piece) {
    squares[1].setPiece(piece);
    squares[0].setPiece(null);
  }

  public static void main(String[] args) {
    Game game = new Game();
    Scanner input = new Scanner(System.in);

    while (game.isCheckmate == false && game.isCheckmate == false) {
      System.out.println(game.currentPlayer.getColor() + " move: ");
      String move = input.nextLine();
  
      Square[] squares = MoveConverter.algebraicToSquares(move, game.currentPlayer.getColor() , game.board);

      // bad moves have empty first square
      if (squares[0] == null) {
        System.out.println("BAD MOVE!!");
        continue;
      } else if (squares[0] == squares[1]) {
        System.out.println("castling move");
      } else {

        Piece piece = squares[0].getPiece();
        playMove(squares, piece);

        // isChecked logic
        boolean stillInCheck = MoveConverter.isKingInCheck(game.getOpponent().getColor(), game.board, null);
        if (stillInCheck) {
          resetMove(squares, piece);
          continue;
        } else {
          game.currentPlayer.setChecked(false);
        }

        if (piece != null && piece.getLabel().equals(PieceType.PAWN.getLabel()) 
          || piece.getLabel().equals(PieceType.ROOK.getLabel()) 
          || piece.getLabel().equals(PieceType.KING.getLabel()) ) {
            piece.setHasMoved();
          }
  
        System.out.println("GOOD MOVE");
        System.out.println("changing " + game.getOpponent().getColor() + " checked status");
      }
      
      game.board.printBoard();
      System.out.println();
      System.out.println();
  
      game.switchTurn();
    }


    input.close();
  }
}

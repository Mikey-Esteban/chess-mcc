import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

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
    // System.out.println("You are in check! need to revert");
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

      if (game.currentPlayer.isChecked()) {
        boolean canGetOutOfCheck = false;
        System.out.println("code to see if checkmate goes here");
        ArrayList<Piece> pieces = game.board.getAllPieces(game.currentPlayer.getColor());

        for (Piece p : pieces) {
          System.out.println("Piece " + p.getLabel());
          List<Square> endSquares = p.generateLegalMoves(game.currentPlayer.getColor(), game.board);
          // if (p.getLabel().equals("P")) {

          //   for (Square eSquare : endSquares) {
          //     System.out.println("s:" + eSquare);
          //   }
          // }
          for (Square endSquare : endSquares) {

            if (canGetOutOfCheck) {
              continue;
            }

            if (p.getLabel().equals(PieceType.PAWN.getLabel())) {
              Square move = MoveUtils.checkPawnMove(endSquare, game.currentPlayer.getColor(), game.board, -1);
              if (move == null) return;
              // System.out.println("pawnmove .. " + move);
              Square[] squares = { move, endSquare};
              playMove(squares, p);
              boolean stillInCheck = KingCheckValidator.isKingInCheck(game.getOpponent().getColor(), game.board, null);
              System.out.println("doing " + move + " to " + endSquare + "...am i still in check? " + stillInCheck);
              if (stillInCheck == false) {
                canGetOutOfCheck = true;
              }
              resetMove(squares, p);
              // canGetOutOfCheck = true;
            } else {
              ArrayList<Square> moves = MoveConverter.findPieceSquare(p.getLabel(), endSquare, game.currentPlayer.getColor(), game.board, -1, -1);

              for (Square m : moves) {
                Square[] squares = { m, endSquare};
                playMove(squares, p);
                boolean stillInCheck = KingCheckValidator.isKingInCheck(game.getOpponent().getColor(), game.board, null);
                System.out.println("doing " + m + " to " + endSquare + "...am i still in check? " + stillInCheck);
                if (stillInCheck == false) {
                  canGetOutOfCheck = true;
                }
                resetMove(squares, p);
              }
            }
          } 
        }

        System.out.println("out of pieces for loop");
      }

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

        // System.out.println("piecelabel: " + piece.getLabel());
        // if (piece.getLabel().equals("P")) {
        //   List<Square> pawnMoves = piece.generateLegalMoves(game.currentPlayer.getColor(), game.board);
        //   System.out.println("pawn moves" + pawnMoves);
        // }

        // if (piece.getLabel().equals("R")) {
        //   List<Square> rookMoves = piece.generateLegalMoves(game.currentPlayer.getColor(), game.board);
        //   System.out.println("rook moves" + rookMoves);
        // }

        // if (piece.getLabel().equals("B")) {
        //   List<Square> bishopMoves = piece.generateLegalMoves(game.currentPlayer.getColor(), game.board);
        //   System.out.println("bishop moves" + bishopMoves);
        // }

        // if (piece.getLabel().equals("Q")) {
        //   List<Square> queenMoves = piece.generateLegalMoves(game.currentPlayer.getColor(), game.board);
        //   System.out.println("queen moves" + queenMoves);
        // }

        // if (piece.getLabel().equals("N")) {
        //   List<Square> knightMoves = piece.generateLegalMoves(game.currentPlayer.getColor(), game.board);
        //   System.out.println("knight moves" + knightMoves);
        // }

        // if (piece.getLabel().equals("K")) {
        //   List<Square> kingMoves = piece.generateLegalMoves(game.currentPlayer.getColor(), game.board);
        //   System.out.println("king moves" + kingMoves);
        // }

        // isChecked logic
        // boolean stillInCheck = MoveConverter.isKingInCheck(game.getOpponent().getColor(), game.board, null);
        boolean stillInCheck = KingCheckValidator.isKingInCheck(game.getOpponent().getColor(), game.board, null);
        boolean isOpponentInCheck = KingCheckValidator.isKingInCheck(game.currentPlayer.getColor(), game.board, null);
        game.getOpponent().setChecked(isOpponentInCheck);
        System.out.println("IS " + game.getOpponent().getColor() + " in check? " + isOpponentInCheck);
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
      }
      
      game.board.printBoard();
      System.out.println();
      System.out.println();
  
      game.switchTurn();
    }


    input.close();
  }
}

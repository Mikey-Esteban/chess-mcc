public class KingCheckValidator {

  public static boolean isKingInCheck(String color, Board board, Square castlingSquare) {
      // System.out.println("isKingInCheck called");
      String opponentColor = color.equals("white") ? "black" : "white";
      Square opponentKing = MoveUtils.findKing(opponentColor, board);

      if (opponentKing == null) {
          throw new IllegalStateException("King not found on the board");
      }

      // System.out.println("going to see if " + opponentColor + " can check my king... " + color);

      for (int row = 0; row < 8; row++) {
          for (int col = 0; col < 8; col++) {
              Square currentSquare = board.getSquare(row, col);
              boolean isChecked;
              if (currentSquare.getPiece() != null && currentSquare.getPiece().getColor().equals(color)) {
                  if (castlingSquare == null) {
                      isChecked = PieceAttackChecker.canPieceAttackKing(currentSquare, opponentKing, color, board);
                  } else {
                      isChecked = PieceAttackChecker.canPieceAttackKing(currentSquare, castlingSquare, opponentColor, board);
                      // isChecked = PieceAttackChecker.canPieceAttackKing(currentSquare, castlingSquare, color, board);
                  }

                  if (isChecked) return true;
              }
          }
      }
      return false;
  }
  
}
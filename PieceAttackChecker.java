public class PieceAttackChecker {

  public static boolean canPieceAttackKing(Square pieceSquare, Square kingSquare, String color, Board board) {
      String pieceLabel = pieceSquare.getPiece().getLabel();

      if (pieceLabel.equals(MoveUtils.getPawnLabel())) {
          return MoveUtils.checkPawnMove(kingSquare, color, board) != null;
      }
      if (pieceLabel.equals(MoveUtils.getKnightLabel())) {
          return MoveUtils.traverse(kingSquare, "L", pieceLabel, color, board) != null;
      }
      if (pieceLabel.equals(MoveUtils.getBishopLabel())) {
          return MoveUtils.traverse(kingSquare, "diagonal", pieceLabel, color, board) != null;
      }
      if (pieceLabel.equals(MoveUtils.getRookLabel())) {
          return canRookAttackKing(kingSquare, pieceLabel, color, board);
      }
      if (pieceLabel.equals(MoveUtils.getQueenLabel())) {
          return canQueenAttackKing(kingSquare, pieceLabel, color, board);
      }

      return false;
  }

  private static boolean canRookAttackKing(Square kingSquare, String pieceLabel, String color, Board board) {
      Square horizontalSquare = MoveUtils.traverse(kingSquare, "horizontal", pieceLabel, color, board);
      Square verticalSquare = MoveUtils.traverse(kingSquare, "vertical", pieceLabel, color, board);
      return horizontalSquare != null || verticalSquare != null;
  }

  private static boolean canQueenAttackKing(Square kingSquare, String pieceLabel, String color, Board board) {
      Square diagonalSquare = MoveUtils.traverse(kingSquare, "diagonal", pieceLabel, color, board);
      Square horizontalSquare = MoveUtils.traverse(kingSquare, "horizontal", pieceLabel, color, board);
      Square verticalSquare = MoveUtils.traverse(kingSquare, "vertical", pieceLabel, color, board);
      return diagonalSquare != null || horizontalSquare != null || verticalSquare != null;
  }
  
}
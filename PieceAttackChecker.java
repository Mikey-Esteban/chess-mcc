import java.util.ArrayList;

public class PieceAttackChecker {

  public static boolean canPieceAttackKing(Square pieceSquare, Square kingSquare, String color, Board board) {
      String pieceLabel = pieceSquare.getPiece().getLabel();

      if (pieceLabel.equals(MoveUtils.getPawnLabel())) {
          return MoveUtils.checkPawnMove(kingSquare, color, board, -1) != null;
      }
      if (pieceLabel.equals(MoveUtils.getKnightLabel())) {
          return MoveUtils.traverse(kingSquare, "L", pieceLabel, color, board, -1, -1).size() > 0;
      }
      if (pieceLabel.equals(MoveUtils.getBishopLabel())) {
          return MoveUtils.traverse(kingSquare, "diagonal", pieceLabel, color, board, -1, -1).size() > 0;
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
    ArrayList<Square> horizontalSquare = MoveUtils.traverse(kingSquare, "horizontal", pieceLabel, color, board, -1, -1);
    ArrayList<Square> verticalSquare = MoveUtils.traverse(kingSquare, "vertical", pieceLabel, color, board, -1, -1);
    return horizontalSquare.size() > 0 || verticalSquare.size() > 0;
  }

  private static boolean canQueenAttackKing(Square kingSquare, String pieceLabel, String color, Board board) {
      ArrayList<Square> diagonalSquare = MoveUtils.traverse(kingSquare, "diagonal", pieceLabel, color, board, -1, -1);
      ArrayList<Square> horizontalSquare = MoveUtils.traverse(kingSquare, "horizontal", pieceLabel, color, board, -1, -1);
      ArrayList<Square> verticalSquare = MoveUtils.traverse(kingSquare, "vertical", pieceLabel, color, board, -1, -1);
      return diagonalSquare.size() > 0 || horizontalSquare.size() > 0 || verticalSquare.size() > 0;
  }
  
}
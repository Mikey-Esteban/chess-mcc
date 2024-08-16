import java.util.ArrayList;
import java.util.List;

public class King extends Piece {


  public King(String color) {
    super(color, PieceType.KING);
  }

  public static int[] getHomeSquare(String color) {
    return color.equals("white") ?
      new int[] {0, 4} :
      new int[] {7, 4} ;
  }

  @Override
  public List<Square> generateLegalMoves(String color, Board board) {
    List<Square> legalMoves = new ArrayList<>();

    int[][] directions = {
      {1, 0},
      {1, 1},
      {0, 1},
      {-1, 1},
      {-1, 0},
      {-1, -1},
      {0, -1},
      {1, -1}
    };

    Square kingSquare = MoveUtils.findKing(color, board);

    for (int[] direction : directions) {
      Square nextSquare = board.getSquare(kingSquare.getRow() + direction[0], kingSquare.getColumn() + direction[1]);
      System.out.println("kingjava nextsquare " + nextSquare);
      if (nextSquare == null) {
        continue;
      }
      if (nextSquare.getPiece() == null || !nextSquare.getPiece().getColor().equals(color)) {
        legalMoves.add(nextSquare);
      }
    }

    return legalMoves;
  }
  
}

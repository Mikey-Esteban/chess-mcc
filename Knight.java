import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
  
  public Knight(String color) {
    super(color, PieceType.KNIGHT);
  }

  @Override
  public List<Square> generateLegalMoves(String color, Board board) {
    List<Square> legalMoves = new ArrayList<>();

    int[][] directions = {
      {1, 2},
      {1, -2},
      {2, 1},
      {2, -1},
      {-1, 2},
      {-1, -2},
      {-2, 1},
      {-2, -1}
    };

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Square square = board.getSquare(i, j);

        if (square.getPiece() == null || !square.getPiece().getColor().equals(color) || !square.getPiece().getLabel().equals(MoveUtils.getKnightLabel())) {
          continue;
        }

        for (int[] direction : directions){

          Square nextSquare = board.getSquare(square.getRow() + direction[0], square.getColumn() + direction[1]);

          if (!MoveUtils.isWithinBoard(nextSquare)) {
            continue;
          }

          if (nextSquare.getPiece() == null || !nextSquare.getPiece().getColor().equals(color)) {
            legalMoves.add(nextSquare);
          }
          
        }
      }
    }

    return legalMoves;
  }
  
}

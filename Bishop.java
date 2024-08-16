import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece  {
  
  public Bishop(String color) {
    super(color, PieceType.BISHOP);
  }


  @Override
  public List<Square> generateLegalMoves(String color, Board board) {
    List<Square> legalMoves = new ArrayList<>();

    int[][] directions = {
      {1, 1},
      {1, -1},
      {-1, 1},
      {-1, -1}
    };

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Square square = board.getSquare(i, j);

        if (square.getPiece() == null || !square.getPiece().getColor().equals(color) || !square.getPiece().getLabel().equals(MoveUtils.getBishopLabel())) {
          continue;
        }

        for (int[] direction : directions){

          int[] directionToMove = { direction[0], direction[1] };
          Square nextSquare = board.getSquare(square.getRow() + direction[0], square.getColumn() + direction[1]);

          directionToMove[0] = direction[0] > 0 ? directionToMove[0]++ : directionToMove[0]-- ;
          directionToMove[1] = direction[1] > 0 ? directionToMove[1]++ : directionToMove[1]-- ;

          while (MoveUtils.isWithinBoard(nextSquare)) {

            if (nextSquare.getPiece() != null) {
              if (!nextSquare.getPiece().getColor().equals(color)) {
                legalMoves.add(nextSquare);
              }
              break;
            }

            legalMoves.add(nextSquare);
            nextSquare = board.getSquare(nextSquare.getRow() + directionToMove[0], nextSquare.getColumn() + directionToMove[1]);
          }
        }
      }
    }

    return legalMoves;
  }
}

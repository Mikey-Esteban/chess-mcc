import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
  
  public Rook(String color) {
    super(color, PieceType.ROOK);
  }

  public static int[] getCastlingSquare(String castlingType, String color) {
    int rookRow = color.equals("white") ? 0 : 7 ;
    int rookColumn = castlingType.equals("O-O") ? 7 : 0;
    return new int[] {rookRow, rookColumn};
  }

  @Override
  public List<Square> generateLegalMoves(String color, Board board) {
    List<Square> legalMoves = new ArrayList<>();

    int[][] directions = {
      {0, -1},
      {0, 1},
      {-1, 0},
      {1, 0}
    };

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Square square = board.getSquare(i, j);

        if (square.getPiece() == null || !square.getPiece().getColor().equals(color) || !square.getPiece().getLabel().equals(MoveUtils.getRookLabel())) {
          continue;
        }

        for (int[] direction : directions) {
  
          int[] directionToMove = { direction[0], direction[1] }; 
          Square nextSquare = board.getSquare(square.getRow() + direction[0], square.getColumn() + direction[1]);

          if (direction[0] == 0) {
            directionToMove[1] = direction[1] > 0 ? directionToMove[1]++ : directionToMove[1]-- ;
          } else {
            directionToMove[0] = direction[0] > 0 ? directionToMove[0]++ : directionToMove[0]-- ;
          }

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

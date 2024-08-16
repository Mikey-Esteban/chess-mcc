import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
  
  public Pawn(String color) {
    super(color, PieceType.PAWN);
  }

  @Override
  public List<Square> generateLegalMoves(String color, Board board) {
    List<Square> legalMoves = new ArrayList<>();

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Square square = board.getSquare(i, j);

        
        if (square.getPiece() == null || !square.getPiece().getColor().equals(color) || !square.getPiece().getLabel().equals(MoveUtils.getPawnLabel())) {
          continue;
        }

        int rowDirection = color == "white" ? 1 : -1 ;

        Square leftSquare = board.getSquare(square.getRow() + rowDirection, square.getColumn() - 1);
        Square rightSquare = board.getSquare(square.getRow() + rowDirection, square.getColumn() + 1);
        Square nextSquare = board.getSquare(square.getRow() + rowDirection, square.getColumn());
        Square next2Square = board.getSquare(square.getRow() + rowDirection + rowDirection, square.getColumn());

        if (MoveUtils.isWithinBoard(leftSquare) && leftSquare.getPiece() != null && !leftSquare.getPiece().getColor().equals(color)) {
          legalMoves.add(leftSquare);
        }

        if (MoveUtils.isWithinBoard(rightSquare) &&  rightSquare.getPiece() != null && !rightSquare.getPiece().getColor().equals(color)) {
          legalMoves.add(rightSquare);
        }

        if (MoveUtils.isWithinBoard(nextSquare) && nextSquare.getPiece() == null) {
          legalMoves.add(nextSquare);
        }

        if (MoveUtils.isWithinBoard(next2Square) && nextSquare.getPiece() == null && next2Square.getPiece() == null && square.getPiece().getHasMoved() == false) {
          legalMoves.add(next2Square);
        }
          
      }
    }

    return legalMoves;
  }
}

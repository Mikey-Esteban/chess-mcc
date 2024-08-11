public class MoveUtilsGetters {

  private static int[][] getDiagonalDirections() {
    return new int[][]{
      {-1, -1}, // leftDown
      {-1, 1}, // leftUp
      {1, -1}, // rightDown
      {1, 1} // rightUp
    };
  }

  private static int[][] getKnightDirections() {
    return new int[][] {
      {-2, -1},
      {-2, 1},
      {-1, -2},
      {-1, 2},
      {1, -2},
      {1, 2},
      {2, -1},
      {2, 1}
    };
  }

  private static int[][] getHorizontalDirections() {
    return new int[][]{
      {0, -1},  // left
      {0, 1}  // right
    };
  }

  private static int[][] getVerticalDirections() {
    return new int[][]{
      {-1, 0},  // down
      {1, 0}  // up
    };
  }

  protected static String getPawnLabel() {
      return PieceType.PAWN.getLabel();
  }

  protected static String getKnightLabel() {
      return PieceType.KNIGHT.getLabel();
  }

  protected static String getBishopLabel() {
      return PieceType.BISHOP.getLabel();
  }

  protected static String getRookLabel() {
      return PieceType.ROOK.getLabel();
  }

  protected static String getQueenLabel() {
      return PieceType.QUEEN.getLabel();
  }

  protected static String getKingLabel() {
    return PieceType.KING.getLabel();
  }

  protected static int getNextRow(Square endSquare, String color) {
    return color == "white" ? endSquare.getRow() - 1 : endSquare.getRow() + 1 ;
  }

  protected static Square getNextSquare(Square startSquare, int[] direction, String dir, int counter, Board board) {
    if (dir.equals("L")) {
      return board.getSquare(startSquare.getRow() + direction[0], startSquare.getColumn() + direction[1]);
    } else if (dir.equals("diagonal")) {
      return board.getSquare(startSquare.getRow() + (direction[0] * counter), startSquare.getColumn() + (direction[1] * counter));
    } else if (dir.equals("vertical")) {
      return board.getSquare(startSquare.getRow() + (direction[0] * counter), startSquare.getColumn());
    } else {
      return board.getSquare(startSquare.getRow(), startSquare.getColumn() + (direction[1] * counter));
    }
  }

  protected static int[][] getDirectionsFromOrientation(String orientation) {
    if (orientation.equals("diagonal")) {
      return getDiagonalDirections();
    }

    if (orientation.equals("L")) {
      return getKnightDirections();
    }

    return orientation == "horizontal" ? getHorizontalDirections() : getVerticalDirections() ;
  }
  
}

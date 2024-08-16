import java.util.ArrayList;

public class MoveUtils extends MoveUtilsGetters {

  private static boolean isPawn(Piece piece) {
    return piece != null && piece.getLabel().equals("P");
  }

  private static boolean isTargetPiece(Square square, String label, String color, int startColumn, int startRow) {
    Piece piece = square.getPiece();
    if (startColumn != -1 && (startColumn != square.getColumn())) {
      return false;
    }
    if (startRow != -1 && (startRow != square.getRow())) {
      return false;
    }
    return piece != null && (piece.getLabel().equals(label) && 
    piece.getColor().equals(color));
  }

  public static boolean isWithinBoard(Square square) {
    return square != null && square.getRow() >= 0 && square.getRow() <= 7 && square.getColumn() >= 0 && square.getColumn() <= 7;
  }
  
  private static boolean isPieceBlocking(Square square) {
    // return square.getPiece() != null && !square.getPiece().getLabel().equals(label);
    return square.getPiece() != null;
  }

  public static boolean isPawnMove(String letter) {

    if (letter.equals(MoveUtils.getBishopLabel())) {
      return false;
    }

    if (letter.equals(MoveUtils.getKnightLabel())) {
      return false;
    }

    if (letter.equals(MoveUtils.getRookLabel())) {
      return false;
    }

    if (letter.equals(MoveUtils.getQueenLabel())) {
      return false;
    }

    if (letter.equals(MoveUtils.getKingLabel())) {
      return false;
    }

    return true;
  }

  public static Square checkPawnMove(Square endSquare, String color, Board board, int startColumn) {
      // Implement the logic for checking pawn move
      int nextRow = getNextRow(endSquare, color);
    
      // check 1 jump, then check 2 jump
      if (endSquare.getPiece() == null) {
  
        if (board.getEnPassantSquare() != null && endSquare == board.getEnPassantSquare()) {
          return handleEnPassantCapture(board, nextRow, startColumn);
        }
  
        return handleNormalMove(endSquare, color, board, nextRow);
      } 
      // capture
      return handleCapture(endSquare, color, board, nextRow, startColumn);
      
  }

  public static ArrayList<Square> traverse(Square endSquare, String orientation, String label, String color, Board board, int startColumn, int startRow) {
    ArrayList<Square> foundSquares = new ArrayList<Square>();
    int[][] directions = getDirectionsFromOrientation(orientation);
    
    for (int[] direction : directions) {

      Square foundSquare = traverseInDirection(endSquare, direction, orientation, label, color, board, startColumn, startRow);
      if (foundSquare != null) {
        // return foundSquare;
        foundSquares.add(foundSquare);
      }
    }

    for (Square square : foundSquares) {
      System.out.println("s: " + square);
    }

    // if (foundSquares.size() == 1) {
    //   board.resetEnPassant();
    //   return foundSquares.get(0);
    // }

    // if (foundSquares.size() > 1) {
    //   duplicatesMessage();
    // }

    return foundSquares;

    // return null;
  }

  public static void duplicatesMessage() {
    System.out.println("uhh ohhh... we have duplicates");
    System.out.println("Follow these rules for the notation:");
    System.out.println("Please specify the file of departure (if they differ) ");
    System.out.println("Please the rank of departure (if the files are the same but the ranks differ) ");
    System.out.println("If neither file nor rank alone is sufficient to identify the piece, then both are specified. ");
  }

  public static Square findKing(String color, Board board) {
      for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
          Square currentSquare = board.getSquare(row, col);
          Piece piece = currentSquare.getPiece();
  
          if (piece != null && piece.getLabel().equals(getKingLabel()) && piece.getColor().equals(color)) {
            return currentSquare;
          }
        }
      }
  
      return null;
  }

  private static Square capturePawn(Board board, Square square) {
    Pawn pawn = (Pawn) square.getPiece();
    pawn.setHasMoved();
    board.resetEnPassant();

    return square;
  }

  private static Square handleEnPassantCapture(Board board, int nextRow, int startColumn) {
    // TODO: mulitple possble enPassant
    Square enPassantSquare = board.getEnPassantSquare();
    Square leftSquare = board.getSquare(nextRow, enPassantSquare.getColumn() - 1);
    Square rightSquare = board.getSquare(nextRow, enPassantSquare.getColumn() + 1);

    Piece leftSquarePiece = leftSquare.getPiece();
    Piece rightSquarePiece = rightSquare.getPiece();

    if (isPawn(leftSquarePiece)) {
      Square pawnSquare = board.getEnPassantPawn();
      pawnSquare.setPiece(null);
      return capturePawn(board, leftSquare);
    }

    if (isPawn(rightSquarePiece)) {
      Square pawnSquare = board.getEnPassantPawn();
      pawnSquare.setPiece(null);
      return capturePawn(board, rightSquare);
    }

    return null;
  }

  private static Square handleCapture(Square endSquare, String color, Board board, int nextRow, int startColumn) {
    ArrayList<Square> foundSquares = new ArrayList<>();
    String pawnLabel = getPawnLabel();
    int leftColumn = endSquare.getColumn() - 1;
    int rightColumn = endSquare.getColumn() + 1;
    Square leftSquare = board.getSquare(nextRow, leftColumn);
    Square rightSquare = board.getSquare(nextRow, rightColumn);

    if (leftSquare != null && isTargetPiece(leftSquare, pawnLabel, color, startColumn, -1)) {
      foundSquares.add(leftSquare);
      // return capturePawn(board, leftSquare);
    }

    if (rightSquare != null && isTargetPiece(rightSquare, pawnLabel, color, startColumn, -1)) {
      // return capturePawn(board, rightSquare);
      foundSquares.add(rightSquare);
    }

    if (foundSquares.size() == 1) {
      return capturePawn(board, foundSquares.get(0));
    }

    if (foundSquares.size() > 1) {
      duplicatesMessage();
    }

    return null;
  }

  public static Square handleKingMove(Square endSquare, String color, Board board) {

    Square kingSquare = findKing(color, board);
    int rowDiff = Math.abs(kingSquare.getRow() - endSquare.getRow());
    int colDiff = Math.abs(kingSquare.getColumn() - endSquare.getColumn());
    
    return rowDiff <= 1 && colDiff <= 1 ? kingSquare : null ;
  }

  private static Square handleNormalMove(Square endSquare, String color, Board board, int nextRow) {

    String pawnLabel = getPawnLabel();
    Square nextSquare = board.getSquare(nextRow, endSquare.getColumn());
    // 1 move
    if (isTargetPiece(nextSquare, pawnLabel, color, -1, -1)) {
      Pawn pawn = (Pawn) nextSquare.getPiece();
      pawn.setHasMoved();
      board.resetEnPassant();

      return nextSquare;
    }

    // 2 jump
    int next2Rows = color == "white" ?
      endSquare.getRow() - 2 : 
      endSquare.getRow() + 2 ;

    Square next2Square = board.getSquare(next2Rows, endSquare.getColumn());

    if (isTargetPiece(next2Square, pawnLabel, color, -1, -1)) {
        Pawn pawn = (Pawn) next2Square.getPiece();
        pawn.setHasMoved();
        board.setEnPassantSquare(nextSquare);
        board.setEnPassantPawn(endSquare);
        
        return next2Square;
    } 

    return null;
  }

  private static Square traverseInDirection(Square startSquare, int[] direction, String dir, String label, String color, Board board, int startColumn, int startRow) {
    return dir.equals("L") ?
      handleNonLinearDirection(startSquare, direction, dir, label, color, board, startColumn, startRow) :
      handleLinearDirection(startSquare, direction, dir, label, color, board, startColumn, startRow) ;
  }

  private static Square handleLinearDirection(Square startSquare, int[] direction, String dir, String label, String color, Board board, int startColumn, int startRow) {
    int counter = 1;
    Square nextSquare = getNextSquare(startSquare, direction, dir, counter, board);

    while (isWithinBoard(nextSquare)) {
  
      if (isTargetPiece(nextSquare,label, color, startColumn, startRow)) {
        // board.resetEnPassant();
        return nextSquare;
      } 

      // cant move past a piece
      if (isPieceBlocking(nextSquare)) {
        return null;
      }
      
      counter++;
      nextSquare = getNextSquare(startSquare, direction, dir, counter, board);
    }

    return null;
  }

  private static Square handleNonLinearDirection(Square startSquare, int[] direction, String dir, String label, String color, Board board, int startColumn, int startRow) {

    Square nextSquare = getNextSquare(startSquare, direction, dir, 1, board);
    if (isWithinBoard(nextSquare) && isTargetPiece(nextSquare,label, color, startColumn, startRow)) {
      // board.resetEnPassant();
      return nextSquare;
    }
      
    return null;
  }

  // private static int[][] getDiagonalDirections() {
  //   return new int[][]{
  //     {-1, -1}, // leftDown
  //     {-1, 1}, // leftUp
  //     {1, -1}, // rightDown
  //     {1, 1} // rightUp
  //   };
  // }

  // private static int[][] getKnightDirections() {
  //   return new int[][] {
  //     {-2, -1},
  //     {-2, 1},
  //     {-1, -2},
  //     {-1, 2},
  //     {1, -2},
  //     {1, 2},
  //     {2, -1},
  //     {2, 1}
  //   };
  // }

  // private static int[][] getHorizontalDirections() {
  //   return new int[][]{
  //     {0, -1},  // left
  //     {0, 1}  // right
  //   };
  // }

  // private static int[][] getVerticalDirections() {
  //   return new int[][]{
  //     {-1, 0},  // down
  //     {1, 0}  // up
  //   };
  // }

  // public static String getPawnLabel() {
  //     return PieceType.PAWN.getLabel();
  // }

  // public static String getKnightLabel() {
  //     return PieceType.KNIGHT.getLabel();
  // }

  // public static String getBishopLabel() {
  //     return PieceType.BISHOP.getLabel();
  // }

  // public static String getRookLabel() {
  //     return PieceType.ROOK.getLabel();
  // }

  // public static String getQueenLabel() {
  //     return PieceType.QUEEN.getLabel();
  // }

  // private static String getKingLabel() {
  //   return PieceType.KING.getLabel();
  // }

  // private static int getNextRow(Square endSquare, String color) {
  //   return color == "white" ? endSquare.getRow() - 1 : endSquare.getRow() + 1 ;
  // }

  // private static Square getNextSquare(Square startSquare, int[] direction, String dir, int counter, Board board) {
  //   if (dir.equals("L")) {
  //     return board.getSquare(startSquare.getRow() + direction[0], startSquare.getColumn() + direction[1]);
  //   } else if (dir.equals("diagonal")) {
  //     return board.getSquare(startSquare.getRow() + (direction[0] * counter), startSquare.getColumn() + (direction[1] * counter));
  //   } else if (dir.equals("vertical")) {
  //     return board.getSquare(startSquare.getRow() + (direction[0] * counter), startSquare.getColumn());
  //   } else {
  //     return board.getSquare(startSquare.getRow(), startSquare.getColumn() + (direction[1] * counter));
  //   }
  // }

  // private static int[][] getDirectionsFromOrientation(String orientation) {
  //   if (orientation.equals("diagonal")) {
  //     return getDiagonalDirections();
  //   }

  //   if (orientation.equals("L")) {
  //     return getKnightDirections();
  //   }

  //   return orientation == "horizontal" ? getHorizontalDirections() : getVerticalDirections() ;
  // }

}
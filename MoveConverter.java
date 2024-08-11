import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MoveConverter {
  
  private static final Map<Character, Integer> RANK_FILE_MAP = createMap(); 

  private static Map<Character, Integer> createMap() { 
    Map<Character, Integer> result = new HashMap<>();
    result.put('a', 0);
    result.put('b', 1);
    result.put('c', 2);
    result.put('d', 3);
    result.put('e', 4);
    result.put('f', 5);
    result.put('g', 6);
    result.put('h', 7);
    return Collections.unmodifiableMap(result);
  }
  private static Square findRookSquare(Square endSquare, String pieceLabel, String currentPlayer, Board board) {
    // Square rookSquare = traverse(endSquare, "vertical", pieceLabel, currentPlayer, board);
    Square rookSquare = MoveUtils.traverse(endSquare, "vertical", pieceLabel, currentPlayer, board);
    if (rookSquare == null) {
      // rookSquare = traverse(endSquare, "horizontal", pieceLabel, currentPlayer, board);
      rookSquare = MoveUtils.traverse(endSquare, "horizontal", pieceLabel, currentPlayer, board);
    }
    return rookSquare;
  }

  private static Square findQueenSquare(Square endSquare, String pieceLabel, String currentPlayer, Board board) {
    // Square queenSquare = traverse(endSquare, "diagonal", pieceLabel, currentPlayer, board);
    Square queenSquare = MoveUtils.traverse(endSquare, "diagonal", pieceLabel, currentPlayer, board);
    if (queenSquare == null) {
      // queenSquare = traverse(endSquare, "vertical", pieceLabel, currentPlayer, board);
      queenSquare = MoveUtils.traverse(endSquare, "vertical", pieceLabel, currentPlayer, board);
    }
    if (queenSquare == null) {
      // queenSquare = traverse(endSquare, "horizontal", pieceLabel, currentPlayer, board);
      queenSquare = MoveUtils.traverse(endSquare, "horizontal", pieceLabel, currentPlayer, board);
    }
    return queenSquare;
  }

  private static boolean isLandingOnOwnPiece(Square endSquare, String currentPlayer) {
    return endSquare.getPiece() != null && endSquare.getPiece().getColor().equals(currentPlayer);
  }

  private static boolean isCastling(String notation) {
    return notation.equals("O-O") || notation.equals("O-O-O");
  }

  private static Square[] checkCastling(String notation, String currentPlayer, Board board) {
    boolean canCastle = canCastle(notation, currentPlayer, board);
    System.out.println("can castle?" + canCastle);
    if (canCastle) {
      return playCastle(currentPlayer, notation, board);
    } else {
      // return empty squares to reset
      return new Square[] {null, null};
    }
  }

  public static Square[] algebraicToSquares(String notation, String currentPlayer, Board board) {
    if (isCastling(notation)) {
      return checkCastling(notation, currentPlayer, board);
    }
    int column = RANK_FILE_MAP.get(notation.charAt(notation.length()- 2));
    int row = Character.getNumericValue(notation.charAt(notation.length() - 1)) - 1;
    // String pieceLabel = notation.length() == 2 ? getPawnLabel() : notation.substring(0, 1) ;
    String pieceLabel = notation.length() == 2 ? MoveUtils.getPawnLabel() : notation.substring(0, 1) ;
    Square endSquare = board.getSquare(row, column);
    
    Square[] squares = new Square[2];
    squares[1] = endSquare;

    // if end square is same color, return null
    if (isLandingOnOwnPiece(endSquare, currentPlayer)) {
      squares[0] = null;
      return squares;
    }

    if (notation.length() == 2) {
      squares[0] = MoveUtils.checkPawnMove(endSquare, currentPlayer, board);
      // squares[0] = checkPawnMove(endSquare, currentPlayer, board);
    } else {
      squares[0] = findPieceSquare(pieceLabel, endSquare, currentPlayer, board);
    }

    return squares;
  }

  private static boolean canKingTravelUnchecked(Square kingSquare, Square rookSquare, String castlingType, String color, Board board) {
    int direction = castlingType.equals("O-O") ? 1 : -1 ;
    int counter = 1;

    Square nextSquare = board.getSquare(kingSquare.getRow(), kingSquare.getColumn() + (direction * counter));

    while (nextSquare.getColumn() != rookSquare.getColumn()) {
      System.out.println(nextSquare);
      if (nextSquare.getPiece() != null) {
        return false;
      }

      boolean isKingChecked = KingCheckValidator.isKingInCheck(color, board, nextSquare);
      // boolean isKingChecked = isKingInCheck(color, board, nextSquare);

      if (isKingChecked) {
        return false;
      }
      counter++;
      nextSquare = board.getSquare(kingSquare.getRow(), kingSquare.getColumn() + (direction * counter));
    }

    return true;
  }

  private static boolean passesCastlingRules(Square kingSquare, Square rookSquare, String castlingType, String color, Board board) {  
    return 
      canKingTravelUnchecked(kingSquare, rookSquare, castlingType, color, board)
      && kingSquare != null && rookSquare != null
      && kingSquare.getPiece().getHasMoved() == false
      && rookSquare.getPiece().getHasMoved() == false
      // && kingSquare.getPiece().getLabel().equals(getKingLabel())
      && kingSquare.getPiece().getLabel().equals(MoveUtils.getKingLabel())
      // && rookSquare.getPiece().getLabel().equals(getRookLabel())
      && rookSquare.getPiece().getLabel().equals(MoveUtils.getRookLabel())
      && kingSquare.getPiece().getColor().equals(color) 
      && rookSquare.getPiece().getColor().equals(color);
  }

  private static Square[] playCastle(String color, String castlingType, Board board) {

    int kingRow = color.equals("white") ? 0 : 7 ;
    int kingEndColumn = castlingType.equals("O-O") ? 6 : 2;
    Square kingSquare = board.getSquare(kingRow, 4);
    Square newKingSquare = board.getSquare(kingSquare.getRow(), kingEndColumn);

    int rookRow = color.equals("white") ? 0 : 7 ; 
    int rookStartColumn = castlingType.equals("O-O") ? 7 : 0;
    int rookEndColumn = castlingType.equals("O-O") ? 5 : 3;
    Square rookSquare = board.getSquare(rookRow, rookStartColumn);
    Square newRookSquare = board.getSquare(rookRow, rookEndColumn);

    Piece kingPiece = kingSquare.getPiece();
    Piece rookPiece = rookSquare.getPiece();
    kingPiece.setHasMoved();
    rookPiece.setHasMoved();

    newKingSquare.setPiece(kingPiece);
    newRookSquare.setPiece(rookPiece);
    rookSquare.setPiece(null);
    kingSquare.setPiece(null);

    System.out.println("end of play castle");

    return new Square[]{kingSquare, kingSquare};
  }

  private static boolean canCastle(String castlingType, String color, Board board) {
    System.out.println("calling canCastle");
    int[] kingHomeSquare = King.getHomeSquare(color);
    int[] rookHomeSquare = Rook.getCastlingSquare(castlingType, color);
    Square kingSquare = board.getSquare(kingHomeSquare[0], kingHomeSquare[1]);
    Square rookSquare = board.getSquare(rookHomeSquare[0], rookHomeSquare[1]);

    return passesCastlingRules(kingSquare, rookSquare, castlingType, color, board);
  }

  private static Square findPieceSquare(String pieceLabel, Square endSquare, String currentPlayer, Board board) {
    // if (pieceLabel.equals(getBishopLabel())) {
    if (pieceLabel.equals(MoveUtils.getBishopLabel())) {
      // return traverse(endSquare, "diagonal", pieceLabel, currentPlayer, board);
      return MoveUtils.traverse(endSquare, "diagonal", pieceLabel, currentPlayer, board);
    }
    // if (pieceLabel.equals(getKnightLabel())) {
    if (pieceLabel.equals(MoveUtils.getKnightLabel())) {
      return findKnightSquare(endSquare, pieceLabel, currentPlayer, board);
    }
    // if (pieceLabel.equals(getRookLabel())) {
    if (pieceLabel.equals(MoveUtils.getRookLabel())) {
      return findRookSquare(endSquare, pieceLabel, currentPlayer, board);
    }
    // if (pieceLabel.equals(getQueenLabel())) {
    if (pieceLabel.equals(MoveUtils.getQueenLabel())) {
      return findQueenSquare(endSquare, pieceLabel, currentPlayer, board);
    }

    return null;
  }

  private static Square findKnightSquare(Square endSquare, String pieceLabel, String currentPlayer, Board board) {
    // return traverse(endSquare, "L", pieceLabel, currentPlayer, board);
    return MoveUtils.traverse(endSquare, "L", pieceLabel, currentPlayer, board);
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

  // private static String getPawnLabel() {
  //   return PieceType.PAWN.getLabel();
  // }

  // private static String getBishopLabel() {
  //   return PieceType.BISHOP.getLabel();
  // }

  // private static String getKnightLabel() {
  //   return PieceType.KNIGHT.getLabel();
  // }

  // private static String getRookLabel() {
  //   return PieceType.ROOK.getLabel();
  // }

  // private static String getQueenLabel() {
  //   return PieceType.QUEEN.getLabel();
  // }

  // private static String getKingLabel() {
  //   return PieceType.KING.getLabel();
  // }



  // private static boolean canRookAttackKing(Square kingSquare, String pieceLabel, String color, Board board) {
  //   return findRookSquare(kingSquare, pieceLabel, color, board) != null;
  // }

  // private static boolean canQueenAttackKing(Square kingSquare, String pieceLabel, String color, Board board) {
  //   return findQueenSquare(kingSquare, pieceLabel, color, board) != null;
  // }

  // private static boolean isWithinBoard(Square square) {
  //   return square != null && square.getRow() >= 0 && square.getRow() <= 7 && square.getColumn() >= 0 && square.getColumn() <= 7;
  // }

  // private static boolean isTargetPiece(Square square, String label, String color) {
  //   return square.getPiece() != null && (square.getPiece().getLabel().equals(label) && 
  //                                         square.getPiece().getColor().equals(color));
  // }

  // private static boolean isPieceBlocking(Square square) {
  //   // return square.getPiece() != null && !square.getPiece().getLabel().equals(label);
  //   return square.getPiece() != null;
  // }

  // private static boolean isPawn(Piece piece) {
  //   return piece != null && piece.getLabel().equals("P");
  // }

  // private static int getNextRow(Square endSquare, String color) {
  //   return color == "white" ? endSquare.getRow() - 1 : endSquare.getRow() + 1 ;
  // }

  // private static Square findKing(String color, Board board) {
  //   for (int row = 0; row < 8; row++) {
  //     for (int col = 0; col < 8; col++) {
  //       Square currentSquare = board.getSquare(row, col);
  //       Piece piece = currentSquare.getPiece();

  //       if (piece != null && piece.getLabel().equals(getKingLabel()) && piece.getColor().equals(color)) {
  //         return currentSquare;
  //       }
  //     }
  //   }

  //   return null;
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
  
  

  // private static boolean canPieceAttackKing(Square pieceSquare, Square kingSquare, String color, Board board) {
  //   String pieceLabel = pieceSquare.getPiece().getLabel();

  //   if (pieceLabel.equals(getPawnLabel())) {
  //     return checkPawnMove(kingSquare, color, board) != null;
  //   }
  //   if (pieceLabel.equals(getKnightLabel())) {
  //     return traverse(kingSquare, "L", pieceLabel, color, board) != null;
  //   }
  //   if (pieceLabel.equals(getBishopLabel())) {
  //     return traverse(kingSquare, "diagonal", pieceLabel, color, board) != null;
  //   }
  //   if (pieceLabel.equals(getRookLabel())) {
  //     return canRookAttackKing(kingSquare, pieceLabel, color, board);
  //   }
  //   if (pieceLabel.equals(getQueenLabel())) {
  //     return canQueenAttackKing(kingSquare, pieceLabel, color, board);
  //   }

  //   return false;
  // }

  // public static boolean isKingInCheck(String color, Board board, Square castlingSquare) {
  //   System.out.println("isKingInCheck called");
  //   String opponentColor = color == "white" ? "black" : "white";
  //   Square opponentKing = findKing(opponentColor, board);

  //   if (opponentKing == null) {
  //     throw new IllegalStateException("King not found on the board");
  //   }

  //   System.out.println("going to see if " + opponentColor + " can check my king... " + color);

  //   for (int row = 0; row < 8; row++) {
  //     for (int col = 0; col < 8; col++) {
  //       Square currentSquare = board.getSquare(row, col);
  //       boolean isChecked;
  //       if (currentSquare.getPiece() != null && currentSquare.getPiece().getColor().equals(color)) {
  //         if (castlingSquare == null) {
  //           isChecked = canPieceAttackKing(currentSquare, opponentKing, color, board);
  //         } else {
  //           isChecked = canPieceAttackKing(currentSquare, castlingSquare, opponentColor, board);
  //         }

  //         if (isChecked) return true;
  //       }

  //     }
  //   }

  //   return false;
  // }

  // private static Square traverse(Square endSquare, String orientation, String label, String color, Board board) {
  //   int[][] directions = getDirectionsFromOrientation(orientation);
    
  //   for (int[] direction : directions) {
  //     Square foundSquare = traverseInDirection(endSquare, direction, orientation, label, color, board);
  //     if (foundSquare != null) {
  //       return foundSquare;
  //     }
  //   }

  //   return null;
  // }

  // private static Square handleLinearDirection(Square startSquare, int[] direction, String dir, String label, String color, Board board) {
  //   int counter = 1;
  //   Square nextSquare = getNextSquare(startSquare, direction, dir, counter, board);

  //   while (isWithinBoard(nextSquare)) {
  
  //     if (isTargetPiece(nextSquare,label, color)) {
  //       board.resetEnPassant();
  //       return nextSquare;
  //     } 

  //     // cant move past a piece
  //     if (isPieceBlocking(nextSquare)) {
  //       return null;
  //     }
      
  //     counter++;
  //     nextSquare = getNextSquare(startSquare, direction, dir, counter, board);
  //   }

  //   return null;
  // }

  // private static Square handleNonLinearDirection(Square startSquare, int[] direction, String dir, String label, String color, Board board) {
  //   Square nextSquare = getNextSquare(startSquare, direction, dir, 1, board);
  //   if (isWithinBoard(nextSquare) && isTargetPiece(nextSquare,label, color)) {
  //     board.resetEnPassant();
  //     return nextSquare;
  //   }
      
  //   return null;
  // }

  // private static Square traverseInDirection(Square startSquare, int[] direction, String dir, String label, String color, Board board) {
  //   return dir.equals("L") ?
  //     handleNonLinearDirection(startSquare, direction, dir, label, color, board) :
  //     handleLinearDirection(startSquare, direction, dir, label, color, board) ;
  // }

  // private static Square checkPawnMove(Square endSquare, String color, Board board) {

  //   int nextRow = getNextRow(endSquare, color);
    
  //   // check 1 jump, then check 2 jump
  //   if (endSquare.getPiece() == null) {

  //     if (board.getEnPassantSquare() != null && endSquare == board.getEnPassantSquare()) {
  //       return handleEnPassantCapture(board, nextRow);
  //     }

  //     return handleNormalMove(endSquare, color, board, nextRow);
  //   } 
  //   // capture
  //   return handleCapture(endSquare, color, board, nextRow);
  // }

  // private static Square handleNormalMove(Square endSquare, String color, Board board, int nextRow) {

  //   String pawnLabel = getPawnLabel();
  //   Square nextSquare = board.getSquare(nextRow, endSquare.getColumn());
  //   // 1 move
  //   if (isTargetPiece(nextSquare, pawnLabel, color)) {
  //     Pawn pawn = (Pawn) nextSquare.getPiece();
  //     pawn.setHasMoved();
  //     board.resetEnPassant();

  //     return nextSquare;
  //   }

  //   // 2 jump
  //   int next2Rows = color == "white" ?
  //     endSquare.getRow() - 2 : 
  //     endSquare.getRow() + 2 ;

  //   Square next2Square = board.getSquare(next2Rows, endSquare.getColumn());

  //   if (isTargetPiece(next2Square, pawnLabel, color)) {
  //       Pawn pawn = (Pawn) next2Square.getPiece();
  //       pawn.setHasMoved();
  //       board.setEnPassantSquare(nextSquare);
  //       board.setEnPassantPawn(endSquare);
        
  //       return next2Square;
  //   } 

  //   return null;
  // }

  // private static Square handleCapture(Square endSquare, String color, Board board, int nextRow) {

  //   String pawnLabel = getPawnLabel();
  //   int leftColumn = endSquare.getColumn() - 1;
  //   int rightColumn = endSquare.getColumn() + 1;
  //   Square leftSquare = board.getSquare(nextRow, leftColumn);
  //   Square rightSquare = board.getSquare(nextRow, rightColumn);

  //   if (leftSquare != null && isTargetPiece(leftSquare, pawnLabel, color)) {
  //     return capturePawn(board, leftSquare);
  //   }

  //   if (rightSquare != null && isTargetPiece(rightSquare, pawnLabel, color)) {
  //     return capturePawn(board, rightSquare);
  //   }

  //   return null;
  // }

  // private static Square capturePawn(Board board, Square square) {
  //   Pawn pawn = (Pawn) square.getPiece();
  //   pawn.setHasMoved();
  //   board.resetEnPassant();

  //   return square;
  // }

  // private static Square handleEnPassantCapture(Board board, int nextRow) {
  //   Square enPassantSquare = board.getEnPassantSquare();
  //   Square leftSquare = board.getSquare(nextRow, enPassantSquare.getColumn() - 1);
  //   Square rightSquare = board.getSquare(nextRow, enPassantSquare.getColumn() + 1);

  //   Piece leftSquarePiece = leftSquare.getPiece();
  //   Piece rightSquarePiece = rightSquare.getPiece();

  //   if (isPawn(leftSquarePiece)) {
  //     Square pawnSquare = board.getEnPassantPawn();
  //     pawnSquare.setPiece(null);
  //     return capturePawn(board, leftSquare);
  //   }

  //   if (isPawn(rightSquarePiece)) {
  //     Square pawnSquare = board.getEnPassantPawn();
  //     pawnSquare.setPiece(null);
  //     return capturePawn(board, rightSquare);
  //   }

  //   return null;
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

}

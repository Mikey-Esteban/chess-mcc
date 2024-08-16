import java.util.ArrayList;
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
  private static ArrayList<Square> findRookSquare(Square endSquare, String pieceLabel, String currentPlayer, Board board, int startColumn, int startRow) {
    ArrayList<Square> rookSquares = MoveUtils.traverse(endSquare, "vertical", pieceLabel, currentPlayer, board, startColumn, startRow);
    if (rookSquares.size() == 0) {
      rookSquares = MoveUtils.traverse(endSquare, "horizontal", pieceLabel, currentPlayer, board, startColumn, startRow);
    }
    return rookSquares;
  }

  private static ArrayList<Square> findQueenSquare(Square endSquare, String pieceLabel, String currentPlayer, Board board, int startColumn, int startRow) {
    ArrayList<Square> queenSquares = MoveUtils.traverse(endSquare, "diagonal", pieceLabel, currentPlayer, board, startColumn, startRow);
    if (queenSquares.size() == 0) {
      queenSquares = MoveUtils.traverse(endSquare, "vertical", pieceLabel, currentPlayer, board, startColumn, startRow);
    }
    if (queenSquares.size() == 0) {
      queenSquares = MoveUtils.traverse(endSquare, "horizontal", pieceLabel, currentPlayer, board, startColumn, startRow);
    }
    return queenSquares;
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

  private static Square playPawnMove(String notation, Square endSquare, String currentPlayer, Board board) {
    return notation.length() > 2 ? 
      MoveUtils.checkPawnMove(endSquare, currentPlayer, board, RANK_FILE_MAP.get(notation.charAt(0))) :
      MoveUtils.checkPawnMove(endSquare, currentPlayer, board, -1) ;
  }

  private static Square getStartSquare(ArrayList<Square> foundSquares) {
    if (foundSquares.size() == 1) {
      return foundSquares.get(0);
    }

    if (foundSquares.size() > 1) {
      MoveUtils.duplicatesMessage();
    } 

    return null;
  }

  public static Square[] algebraicToSquares(String notation, String currentPlayer, Board board) {
    if (isCastling(notation)) {
      return checkCastling(notation, currentPlayer, board);
    }

    // TODO: length 3, figure out off its normal minor piece or specified pawn capture
    String firstLetter = notation.substring(0, 1);
    boolean isPawnMove = MoveUtils.isPawnMove(notation.substring(0, 1));
    String pieceLabel = isPawnMove ? MoveUtils.getPawnLabel() : firstLetter ;

    int column = RANK_FILE_MAP.get(notation.charAt(notation.length()- 2));
    int row = Character.getNumericValue(notation.charAt(notation.length() - 1)) - 1;
    Square endSquare = board.getSquare(row, column);
    
    Square[] squares = new Square[2];
    squares[1] = endSquare;

    // if end square is same color, return null
    if (isLandingOnOwnPiece(endSquare, currentPlayer)) {
      squares[0] = null;
      return squares;
    }

    if (isPawnMove) {
      squares[0] = playPawnMove(notation, endSquare, currentPlayer, board);
      return squares;
    } 

    if (notation.length() == 3) {
      ArrayList<Square> foundSquares = findPieceSquare(pieceLabel, endSquare, currentPlayer, board, -1, -1);
      squares[0] = getStartSquare(foundSquares);

      return squares;
    } 

    try {
      int startColumn = RANK_FILE_MAP.get(notation.charAt(1));
      int startRow = notation.length() == 5 ? 
        Character.getNumericValue(notation.charAt(2)) - 1 :
        -1 ;
      ArrayList<Square> foundSquares = findPieceSquare(pieceLabel, endSquare, currentPlayer, board, startColumn, startRow);
      squares[0] = getStartSquare(foundSquares);
    } catch (Exception e) {
      int startRow = Character.getNumericValue(notation.charAt(1)) - 1;
      ArrayList<Square> foundSquares = findPieceSquare(pieceLabel, endSquare, currentPlayer, board, -1, startRow);
      squares[0] = getStartSquare(foundSquares);
    }

    return squares;
  }

  private static boolean canKingTravelUnchecked(Square kingSquare, Square rookSquare, String castlingType, String color, Board board) {
    int direction = castlingType.equals("O-O") ? 1 : -1 ;
    int counter = 1;

    Square nextSquare = board.getSquare(kingSquare.getRow(), kingSquare.getColumn() + (direction * counter));

    while (nextSquare.getColumn() != rookSquare.getColumn()) {

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
      KingCheckValidator.isKingInCheck(color, board, kingSquare) == false
      && canKingTravelUnchecked(kingSquare, rookSquare, castlingType, color, board)
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

    return new Square[]{kingSquare, kingSquare};
  }

  private static boolean canCastle(String castlingType, String color, Board board) {

    int[] kingHomeSquare = King.getHomeSquare(color);
    int[] rookHomeSquare = Rook.getCastlingSquare(castlingType, color);
    Square kingSquare = board.getSquare(kingHomeSquare[0], kingHomeSquare[1]);
    Square rookSquare = board.getSquare(rookHomeSquare[0], rookHomeSquare[1]);

    return passesCastlingRules(kingSquare, rookSquare, castlingType, color, board);
  }

  public static ArrayList<Square> findPieceSquare(String pieceLabel, Square endSquare, String currentPlayer, Board board, int startColumn, int startRow) {
 
    if (pieceLabel.equals(MoveUtils.getBishopLabel())) {
      return MoveUtils.traverse(endSquare, "diagonal", pieceLabel, currentPlayer, board, startColumn, startRow);
    }

    if (pieceLabel.equals(MoveUtils.getKnightLabel())) {
      return findKnightSquare(endSquare, pieceLabel, currentPlayer, board, startColumn, startRow);
    }

    if (pieceLabel.equals(MoveUtils.getRookLabel())) {
      return findRookSquare(endSquare, pieceLabel, currentPlayer, board, startColumn, startRow);
    }

    if (pieceLabel.equals(MoveUtils.getQueenLabel())) {
      return findQueenSquare(endSquare, pieceLabel, currentPlayer, board, startColumn, startRow);
    }

    if (pieceLabel.equals(MoveUtils.getKingLabel())) {
      ArrayList<Square> kingSquares = new ArrayList<>();
      Square kingSquare = MoveUtils.handleKingMove(endSquare, currentPlayer, board);
      kingSquares.add(kingSquare);
      return kingSquares;
    }

    return null;
  }

  private static ArrayList<Square> findKnightSquare(Square endSquare, String pieceLabel, String currentPlayer, Board board, int startColumn, int startRow) {
    return MoveUtils.traverse(endSquare, "L", pieceLabel, currentPlayer, board, startColumn, startRow);
  }

}
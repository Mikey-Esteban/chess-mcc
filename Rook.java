public class Rook extends Piece {
  
  public Rook(String color) {
    super(color, PieceType.ROOK);
  }

  public static int[] getCastlingSquare(String castlingType, String color) {
    int rookRow = color.equals("white") ? 0 : 7 ;
    int rookColumn = castlingType.equals("O-O") ? 7 : 0;
    return new int[] {rookRow, rookColumn};
  }
  
}

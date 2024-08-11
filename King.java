public class King extends Piece {


  public King(String color) {
    super(color, PieceType.KING);
  }

  public static int[] getHomeSquare(String color) {
    return color.equals("white") ?
      new int[] {0, 4} :
      new int[] {7, 4} ;
  }
  
}

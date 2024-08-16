import java.util.List;

public abstract class Piece {
  private String color;
  private PieceType pieceType;
  private boolean isCaptured;
  private String symbol;
  private boolean hasMoved;


  public Piece(String color, PieceType pieceType) {
    this.color = color;
    this.pieceType = pieceType;
    this.isCaptured = false;
    this.hasMoved = false;
    this.symbol = initializeSymbol(color);
  }

  public abstract List<Square> generateLegalMoves(String color, Board board);

  public boolean getHasMoved() {
    return this.hasMoved;
  }

  public void setHasMoved() {
    this.hasMoved = true;
  }

  public String getColor() {
    return this.color;
  }

  public boolean isCaptured() {
    return this.isCaptured;
  }

  public String getLabel() {
    return this.pieceType.getLabel();
  }

  public String getSymbol() {
    return this.symbol;
  }

  public void print() {
    System.out.println("Piece: " + this.getLabel() + " | " + this.getColor());
  }

  private String initializeSymbol(String color) {
    if (color == "white") {
      return this.pieceType.getWhiteSymbol();
    }

    return this.pieceType.getBlackSymbol();
  }
  
}

public class Square {
  private int row;
  private int column;
  private Piece piece;

  Square(int row, int column, Piece piece) {
    this.row = row;
    this.column = column;
    this.piece = piece;
  }

  public int[] getLocation() {
    int[] location = {this.row, this.column};
    return location;
  }

  public int getRow() {
    return this.row;
  }

  public int getColumn() {
    return this.column;
  }

  public Piece getPiece() {
    return this.piece;
  }

  public void setPiece(Piece piece) {
    this.piece = piece;
  }

  public String toString() {
    return "[" + this.row + "][" + this.column + "]";
  }
}

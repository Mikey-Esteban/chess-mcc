public enum PieceType {
  PAWN("P", 1, "\u2659", "\u265F"),
  KNIGHT("N", 3, "\u2658", "\u265E"),
  BISHOP("B", 3, "\u2657", "\u265D"),
  ROOK("R", 5, "\u2656", "\u265C"),
  QUEEN("Q", 9, "\u2655", "\u265B"),
  KING("K", 0, "\u2654", "\u265A");

  private final String label;
  private final int value;
  private final String whiteSymbol;
  private final String blackSymbol;

  private PieceType(String label, int value, String whiteSymbol, String blackSymbol) {
    this.label = label;
    this.value = value;
    this.whiteSymbol = whiteSymbol;
    this.blackSymbol = blackSymbol;
  }

  public String getLabel() {
    return this.label;
  }

  public int getValue() {
    return this.value;
  }

  public String getWhiteSymbol() {
    return this.whiteSymbol;
  }

  public String getBlackSymbol() {
    return this.blackSymbol;
  }
}

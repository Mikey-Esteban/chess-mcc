public class Board {
  
  private Square[][] board;
  private Square enPassantSquare;
  private Square enPassantPawn;

  public Board() {
    this.board = new Square[8][8];

    initializeBoard();
  }

  public Square getEnPassantSquare() {
    return this.enPassantSquare;
  }

  public Square getEnPassantPawn() {
    return this.enPassantPawn;
  }

  public void resetEnPassant() {
    setEnPassantSquare(null);
    setEnPassantPawn(null);
  }

  public void setEnPassant(Square enPassantSquare, Square enPassantPawn) {
    setEnPassantSquare(enPassantSquare);
    setEnPassantPawn(enPassantPawn);
  }

  public void setEnPassantSquare(Square square) {
    this.enPassantSquare = square;
  }

  public void setEnPassantPawn(Square square) {
    this.enPassantPawn = square;
  }


  public Square getSquare(int row, int column) {
    if (row < 0 || row > 7 || column < 0 || column > 7) {
      return null;
    }
    return board[row][column];
  }

  public void printBoard() {
    System.out.println("=a==b==c==d==e==f==g==h=");
    for (int i = 7; i >= 0; i--) {
      for (int j = 0; j < 8; j++) {
        Square square = board[i][j];
        Piece piece = square.getPiece();
        if (piece != null) {
          System.out.print(" " + piece.getSymbol() + " ");
        } else {
          System.out.print(" - ");
        }
      }
      System.out.print(i + 1);
      System.out.println();
    }
    System.out.println("========================");
  }


  private void initializeBoard() {
    for (int i = 7; i >= 0; i--) {
      for(int j = 0; j < 8; j++) {
        if ((i == 0 && j == 0) || (i == 0 && j == 7)) {  // white rook
          Rook rook = new Rook("white");
          Square square = new Square(i, j, rook);
          board[i][j] = square;
        } else if ((i == 7 && j == 0) || (i == 7 && j == 7)) {  // black rook
          Rook rook = new Rook("black");
          Square square = new Square(i, j, rook);
          board[i][j] = square;
        } else if ((i == 0 && j == 1) || (i == 0 && j == 6)) {  // white knight
          Knight knight = new Knight("white");
          Square square = new Square(i, j, knight);
          board[i][j] = square;
        } else if ((i == 7 && j == 1) || (i == 7 && j == 6)) {  // black knight
          Knight knight = new Knight("black");
          Square square = new Square(i, j, knight);
          board[i][j] = square;
        } else if ((i == 0 && j == 2) || (i == 0 && j == 5)) {  // white bishop
          Bishop bishop = new Bishop("white");
          Square square = new Square(i, j, bishop);
          board[i][j] = square;
        } else if ((i == 7 && j == 2) || (i == 7 && j == 5)) {  // black bishop
          Bishop bishop = new Bishop("black");
          Square square = new Square(i, j, bishop);
          board[i][j] = square;
        } else if (i == 0 && j == 3) {  // white queen
          Queen queen = new Queen("white");
          Square square = new Square(i, j, queen);
          board[i][j] = square;
        } else if (i == 0 && j == 4) {  // white king
          King king = new King("white");
          Square square = new Square(i, j, king);
          board[i][j] = square;
        } else if (i == 7 && j == 3) {  // black queen
          Queen queen = new Queen("black");
          Square square = new Square(i, j, queen);
          board[i][j] = square;
        } else if (i == 7 && j == 4) {  // black king
          King king = new King("black");
          Square square = new Square(i, j, king);
          board[i][j] = square;
        } else if (i == 1) {  // white pawns
          Pawn pawn = new Pawn("white");
          Square square = new Square(i, j, pawn);
          board[i][j] = square;
        } else if (i == 6) {  // black pawns
          Pawn pawn = new Pawn("black");
          Square square = new Square(i, j, pawn);
          board[i][j] = square;
        } else {
          Square square = new Square(i, j, null);
          board[i][j] = square;
        }
      }
    }
  }
}

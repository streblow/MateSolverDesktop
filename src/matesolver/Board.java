package matesolver;

public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[10][8];
        resetBoard();
    }

    public Board(Board b) {
        board = new Piece[10][8];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                if (b.hasPiece(i, j)) {
                    if (b.getSquare(i, j).getColor() == false) { //case that piece is white
                        if (b.getSquare(i, j).getType().equals("King")) {
                            board[i][j] = new King(i, j, false);
                        } else if (b.getSquare(i, j).getType().equals("Queen")) {
                            board[i][j] = new Queen(i, j, false);
                        } else if (b.getSquare(i, j).getType().equals("Rook")) {
                            board[i][j] = new Rook(i, j, false);
                        } else if (b.getSquare(i, j).getType().equals("Bishop")) {
                            board[i][j] = new Bishop(i, j, false);
                        } else if (b.getSquare(i, j).getType().equals("Knight")) {
                            board[i][j] = new Knight(i, j, false);
                        } else if (b.getSquare(i, j).getType().equals("Pawn")) {
                            board[i][j] = new Pawn(i, j, false);
                        }
                    } else { //case that piece is black
                        if (b.getSquare(i, j).getType().equals("King")) {
                            board[i][j] = new King(i, j, true);
                        } else if (b.getSquare(i, j).getType().equals("Queen")) {
                            board[i][j] = new Queen(i, j, true);
                        } else if (b.getSquare(i, j).getType().equals("Rook")) {
                            board[i][j] = new Rook(i, j, true);
                        } else if (b.getSquare(i, j).getType().equals("Bishop")) {
                            board[i][j] = new Bishop(i, j, true);
                        } else if (b.getSquare(i, j).getType().equals("Knight")) {
                            board[i][j] = new Knight(i, j, true);
                        } else if (b.getSquare(i, j).getType().equals("Pawn")) {
                            board[i][j] = new Pawn(i, j, true);
                        }
                    }
                    board[i][j].setX(b.getSquare(i, j).getX());
                    board[i][j].setY(b.getSquare(i, j).getY());
                }
            }
        }
    }

    public void clearBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }

    public void resetBoard() {
        //this for loop interates through board and fills it with null objects
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        //black pieces
        setSquare(4, 0, new King(4, 0, true));
        setSquare(0, 0, new Rook(0, 0, true));
        setSquare(1, 0, new Knight(1, 0, true));
        setSquare(2, 0, new Bishop(2, 0, true));
        setSquare(3, 0, new Queen(3, 0, true));
        setSquare(5, 0, new Bishop(5, 0, true));
        setSquare(6, 0, new Knight(6, 0, true));
        setSquare(7, 0, new Rook(7, 0, true));
        for (int i = 0; i < 8; i++)
            setSquare(i, 1, new Pawn(i, 1, true));
        //white pieces
        setSquare(4, 7, new King(4, 7, false));
        setSquare(0, 7, new Rook(0, 7, false));
        setSquare(1, 7, new Knight(1, 7, false));
        setSquare(2, 7, new Bishop(2, 7, false));
        setSquare(3, 7, new Queen(3, 7, false));
        setSquare(5, 7, new Bishop(5, 7, false));
        setSquare(6, 7, new Knight(6, 7, false));
        setSquare(7, 7, new Rook(7, 7, false));
        for (int i = 0; i < 8;i++)
            setSquare(i, 6, new Pawn(i, 6, false));
        //pieces for setting up a board
        setSquare(8, 0, new King(8, 0, false));
        setSquare(8, 1, new Queen(8, 1, false));
        setSquare(8, 2, new Rook(8, 2, false));
        setSquare(8, 3, new Bishop(8, 3, false));
        setSquare(8, 4, new Knight(8, 4, false));
        setSquare(8, 5, new Pawn(8, 5, false));
        setSquare(9, 0, new King(9, 0, true));
        setSquare(9, 1, new Queen(9, 1, true));
        setSquare(9, 2, new Rook(9, 2, true));
        setSquare(9, 3, new Bishop(9, 3, true));
        setSquare(9, 4, new Knight(9, 4, true));
        setSquare(9, 5, new Pawn(9, 5, true));
    }

    public Piece getSquare(int row, int col) {
        return board[row][col];
    }
	
    public void setSquare(int row, int col, Piece piece) {
            board[row][col] = piece;
    }

    public void clearSquare(int row, int col) {
        board[row][col] = null;
    }
	
    public boolean hasPiece(int row, int col) {
        if (getSquare(row,col) != null) {
            return true;
        }else{
            return false;
        }
    }
	
    public String getFEN() {
        String fen = "";
        int empty = 0;
        for (int j = 0; j < 8; j++) { //up to down
            for (int i = 0; i < 8; i++) { //left to right
                if (hasPiece(i, j)) {
                    if (empty > 0) {
                        fen += String.valueOf(empty);
                        empty = 0;
                    }
                    fen += getSquare(i, j).getFENType();
                } else
                    empty++;
            }
            if (empty > 0) {
                fen += String.valueOf(empty);
                empty = 0;
            }
            if (j < 7)
                fen += "/";
        }
        return fen;
    }
}

package matesolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View extends JComponent {

    private final String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
    public Board board;
    public Game game;
    public int move;
    public boolean boardFlipped;
    private Point mousePoint;
    private JTextArea textArea;
    private String text;
    private boolean check;

    public View(Board board, JTextArea textArea, Game game) {
        boardFlipped = false;
        this.board = board;
        this.game = game;
        move = 1;
        this.textArea  = textArea;
        text = "";
        check = false;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                mousePoint = event.getPoint();
                if (View.this.game.getMode() == 0) {
                    boolean valid = View.this.game.validateGame(View.this.board);
                    if (!View.this.game.getVictory() & valid) {
                        for (int i = 0; i <8 ; i++) { 
                            for (int j = 0; j < 8; j++) {
                                if (View.this.board.hasPiece(i, j)) {
                                    if (View.this.board.getSquare(i, j).contains(event.getPoint())) {
                                        if (View.this.board.getSquare(i, j).getColor() == View.this.game.getTurn()) {
                                            View.this.board.getSquare(i, j).setSelected(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < 10; i++) { 
                        for (int j = 0; j < 8; j++) {
                            if (View.this.board.hasPiece(i, j)) {
                                if (View.this.board.getSquare(i, j).contains(event.getPoint())) {
                                    View.this.board.getSquare(i, j).setSelected(true);
                                }
                            }
                        }
                    }
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent event) {
                Point lastMousePoint = mousePoint;
                mousePoint = event.getPoint();
                if (View.this.game.getMode() == 0) {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (View.this.board.hasPiece(i, j)) {
                                if (View.this.board.getSquare(i, j).isSelected()) {
                                    double dx = mousePoint.getX() - lastMousePoint.getX();
                                    double dy = mousePoint.getY() - lastMousePoint.getY();
                                    View.this.board.getSquare(i, j).translate((int)dx, (int)dy);
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (View.this.board.hasPiece(i, j)) {
                                if (View.this.board.getSquare(i, j).isSelected()){
                                    double dx = mousePoint.getX() - lastMousePoint.getX();
                                    double dy = mousePoint.getY() - lastMousePoint.getY();
                                    View.this.board.getSquare(i, j).translate((int)dx, (int)dy);
                                }
                            }
                        }
                    }
                }
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                Board currentBoard = new Board(View.this.board);
                boolean pieceSelected = false;
                Piece selectedPiece = null;
                Piece eatenPiece = null;
                int oldX = 0;
                int oldY = 0;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (View.this.board.hasPiece(i, j)){
                            if (View.this.board.getSquare(i, j).isSelected()){
                                pieceSelected = true;
                                selectedPiece = View.this.board.getSquare(i, j);
                                oldX = i;
                                oldY = j;
                            }
                        }
                    }
                }
                if (View.this.game.getMode() != 0) {
                    for (int i = 8; i < 10; i++) {
                        for (int j = 0; j < 6; j++) {
                            if (View.this.board.hasPiece(i, j)) {
                                if (View.this.board.getSquare(i, j).isSelected()) {
                                    pieceSelected = true;
                                    selectedPiece = View.this.board.getSquare(i, j);
                                    oldX = i;
                                    oldY = j;
                                }
                            }
                        }
                    }
                }
                if (pieceSelected) {
                    if (View.this.game.getMode() == 0) {  // Analysis Mode: do legal moves
                        int newX = ((int)e.getPoint().getX()) / 62;
                        int newY = ((int)e.getPoint().getY()) / 62;
                        Point flippedTouchPoint = e.getPoint();
                        if ((newX < 8) && boardFlipped) {
                            newX = 7 - newX;
                            newY = 7 - newY;
                            flippedTouchPoint.x = newX * 62;
                            flippedTouchPoint.y = newY * 62;
                        }
                        // Check if move would yield to check for current side
                        boolean valid = true;
                        if (newX == oldX && newY == oldY)
                            valid = false;
                        else
                            valid = !View.this.game.checkCheck(currentBoard, oldX, oldY, newX, newY, View.this.board.hasPiece(newX, newY));
                        if (selectedPiece.checkLegalMove(flippedTouchPoint, View.this.board) && valid) {
                            // Check for castling
                            if (selectedPiece.getType().equals("King") && (Math.abs(newX - oldX) == 2)) {
                                if (selectedPiece.getColor() == false) { //piece is white
                                    View.this.board.clearSquare(oldX, oldY);
                                    View.this.board.setSquare(newX, newY, selectedPiece);
                                    selectedPiece.setLocation(newX, newY);
                                    if (boardFlipped)
                                        selectedPiece.setLocationXY(7 - newX, 7 - newY);
                                    if (oldX < newX) { //short castling
                                        Piece rook = View.this.board.getSquare(7, 7);
                                        View.this.board.setSquare(5, 7, rook);
                                        rook.setLocation(5, 7);
                                        if (boardFlipped)
                                            rook.setLocationXY(2, 0);
                                        View.this.board.clearSquare(7, 7);
                                        View.this.text += move + ". 0-0";
                                    } else { //long castling
                                        Piece rook = View.this.board.getSquare(0, 7);
                                        View.this.board.setSquare(3, 7, rook);
                                        rook.setLocation(3, 7);
                                        if (boardFlipped)
                                            rook.setLocationXY(4, 0);
                                        View.this.board.clearSquare(0, 7);
                                        View.this.text += move + ". 0-0-0";
                                    }
                                } else { //piece is black
                                    View.this.board.clearSquare(oldX, oldY);
                                    View.this.board.setSquare(newX, newY, selectedPiece);
                                    selectedPiece.setLocation(newX, newY);
                                    if (oldX < newX) { //short castling
                                        Piece rook = View.this.board.getSquare(7, 0);
                                        View.this.board.setSquare(5, 0, rook);
                                        rook.setLocation(5, 0);
                                        if (boardFlipped)
                                            rook.setLocationXY(2, 7);
                                        View.this.board.clearSquare(7, 0);
                                        View.this.text += " 0-0";
                                    } else { //long castling
                                        Piece rook = View.this.board.getSquare(0, 0);
                                        View.this.board.setSquare(3, 0, rook);
                                        rook.setLocation(3, 0);
                                        if (boardFlipped)
                                            rook.setLocationXY(4, 7);
                                        View.this.board.clearSquare(0, 0);
                                        View.this.text += " 0-0-0";
                                    }
                                    move += 1;
                                }
                            } else {
                                // Check for promotion
                                String promotedPiece = "";
                                if (selectedPiece.getType().equals("Pawn"))
                                    if (selectedPiece.getColor() == false) { //pawn is white
                                        if ((oldY == 1) && (newY == 0)) {
                                            selectedPiece = new Queen(oldX, oldY, false);
                                            promotedPiece = "Q";
                                            if (boardFlipped)
                                                selectedPiece.setLocationXY(7 - oldX, 7 - oldY);
                                        }
                                    } else { // pawn is black
                                        if ((oldY == 6) && (newY == 7)) {
                                            selectedPiece = new Queen(oldX, newY, true);
                                            promotedPiece = "Q";
                                            if (boardFlipped)
                                                selectedPiece.setLocationXY(7 - oldX, 7 - oldY);
                                        }
                                    }
                                if (selectedPiece.getColor() == true) { //piece is black
                                    View.this.text += " ";
                                } else { //piece is white
                                    View.this.text += move + ". ";
                                }
                                if (View.this.board.hasPiece(newX, newY)) {
                                    eatenPiece = View.this.board.getSquare(newX, newY);
                                }
                                View.this.board.clearSquare(oldX, oldY);
                                View.this.board.setSquare(newX, newY, selectedPiece);
                                selectedPiece.setLocation(newX, newY);
                                if (boardFlipped)
                                    selectedPiece.setLocationXY(7 - newX, 7 - newY);
                                String figurine = "";
                                if (!selectedPiece.getFENType().toUpperCase().equals("P"))
                                    figurine = selectedPiece.getFENType().toUpperCase();
                                View.this.text += figurine + View.this.columns[oldX] +  (7 - oldY+1);
                                if ((eatenPiece != null))
                                    View.this.text += "x" + View.this.columns[newX] +  (7 - newY+1);
                                else 
                                    View.this.text += "-" + View.this.columns[newX] +  (7 - newY+1);
                                View.this.text += promotedPiece;
                            }
                            View.this.textArea.setText(View.this.text);
                            //View.this.game.checkVictory(currentBoard, oldX, oldY, newX, newY, eatenPiece != null);
                            View.this.game.checkVictory(View.this.board);
                            if (View.this.game.getVictory()) {
                                if (View.this.game.getTurn() == false) {
                                    View.this.text += "#\n\nWhite mated black and won the game";
                                } else {
                                    View.this.text += "#\n\nBlack mated white and won the game";
                                }
                                View.this.textArea.setText(View.this.text);
                                repaint();
                                JOptionPane.showMessageDialog(null, "Mate!", "MateSolver", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                ///TODO: Improve check tracing
                                //if (View.this.game.isCheck(currentBoard, oldX, oldY, newX, newY, eatenPiece != null)) {
                                if (View.this.game.isCheck(View.this.board)) {
                                    View.this.text += "+";
                                    View.this.textArea.setText(View.this.text);
                                    repaint();
                                    JOptionPane.showMessageDialog(null, "Check!", "MateSolver", JOptionPane.INFORMATION_MESSAGE);
                                    View.this.check = true;
                                } else {
                                    View.this.check = false;
                                }
                                View.this.game.changeTurn();
                            }
                            if (selectedPiece.getColor() == true) { //piece is black
                                View.this.text += "\n";
                                move += 1;
                            }
                        } else {
                            //snap back to original square
                            selectedPiece.setLocation(oldX, oldY);
                            if (boardFlipped)
                                selectedPiece.setLocationXY(7 - oldX, 7 - oldY);
                            if (oldX != newX | oldY != newY) {
                                repaint();
                            }
                        }
                    } else { // Search Mate: setup board
                        int newX = ((int)e.getPoint().getX()) / 62;
                        int newY = ((int)e.getPoint().getY()) / 62;
                        if ((newX < 8) && boardFlipped) {
                            newX = 7 - newX;
                            newY = 7 - newY;
                        }
                        View.this.board.clearSquare(oldX, oldY);
                        if (oldX > 7) //new piece taken from new piece area, replace it
                            switch (oldY) {
                                case 0:
                                    View.this.board.setSquare(oldX, oldY, new King(oldX, oldY, oldX == 9)); break;
                                case 1:
                                    View.this.board.setSquare(oldX, oldY, new Queen(oldX, oldY, oldX == 9)); break;
                                case 2:
                                    View.this.board.setSquare(oldX, oldY, new Rook(oldX, oldY, oldX == 9)); break;
                                case 3:
                                    View.this.board.setSquare(oldX, oldY, new Bishop(oldX, oldY, oldX == 9)); break;
                                case 4:
                                    View.this.board.setSquare(oldX, oldY, new Knight(oldX, oldY, oldX == 9)); break;
                                case 5:
                                    View.this.board.setSquare(oldX, oldY, new Pawn(oldX, oldY, oldX == 9)); break;
                                default:
                                    break;
                            }
                        if (newX < 8 & newY < 8) { //destination on the board, move selected piece
                            View.this.board.setSquare(newX, newY, selectedPiece);
                            selectedPiece.setLocation(newX, newY);
                            if (boardFlipped)
                                selectedPiece.setLocationXY(7 - newX, 7 - newY);
                        }
                    }
                    selectedPiece.setSelected(false);
                }
                repaint();
            }
        });
    }

    public void flipBoard() {
        boardFlipped = !boardFlipped;
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board.hasPiece(i, j)) {
                    if (boardFlipped)
                        board.getSquare(i, j).setLocationXY(7 - i, 7 - j);
                    else
                        board.getSquare(i, j).setLocationXY(i, j);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        boolean isBlack = false;
        // Draw board and new piece area
        for (int i = 0; i < 10; i++) {
            isBlack = !isBlack;
            for (int j = 0; j < 8; j++) {
                isBlack = !isBlack;
                Rectangle rect = new Rectangle(i*62, j*62, 62, 62);
                if (i < 8 && j < 8) {
                    if (isBlack) {	
                        g2.setColor(new Color(80, 96, 128));
                    } else {
                        g2.setColor(Color.white);
                    }
                    g2.fill(rect);
                }
                if (i < 8 || j < 6) {
                    g2.setColor(Color.black);
                    g.drawLine(rect.x + rect.width - 1, rect.y, rect.x + rect.width - 1, rect.y + rect.height - 1);
                    g.drawLine(rect.x, rect.y + rect.height - 1, rect.x + rect.width - 1, rect.y + rect.height - 1);
                }
            }
        }
        g.drawLine(0, 0, 10 * 62 - 1, 0);
        g.drawLine(0, 0, 0, 8 * 62 - 1);
        // Draw column row labels
        String columns = "ABCDEFGH";
        String rows = "12345678";
        if (boardFlipped) {
            columns = "HGFEDCBA";
            rows = "87654321";
        }
        g.setColor(new Color(40, 40, 63));
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, (int)(0.25f * 62.0f));
        FontMetrics fm = g.getFontMetrics(f);
        int w = fm.stringWidth("W");
        int h = fm.getHeight();
        for(int i = 0; i < 8; i++) {
            g.drawString(columns.substring(i, i + 1), (int)((float)(i + 1) * 62.0f - 0.05f * 62.0f - (float)w), (int)(8.0f * 62.0f - 0.05f * 62.0f));
            g.drawString(rows.substring(i, i + 1), (int)(0.05f * 62.0f), (int)((float)(7 - i) * 62.0f + 0.05f * 62.0f + (float)h));
        }
        // Draw pieces
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                if(board.hasPiece(i, j)) {
                    board.getSquare(i, j).draw(g2);
                }
            }
        }
        // Draws pieces for the new piece area to be visible even when dragged
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                Piece p;
                switch (i) {
                    case 0: p = new King(8 + j, i, (j == 1)); break;
                    case 1: p = new Queen(8 + j, i, (j == 1)); break;
                    case 2: p = new Rook(8 + j, i, (j == 1)); break;
                    case 3: p = new Bishop(8 + j, i, (j == 1)); break;
                    case 4: p = new Knight(8 + j, i, (j == 1)); break;
                    case 5: p = new Pawn(8 + j, i, (j == 1)); break;
                    default: p = null;
                }
                if (p != null)
                    p.draw(g2);
            }
        }
        // Draws the selected piece on top to ensure it is on the top layer
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.hasPiece(i, j)) {
                    if (board.getSquare(i, j).isSelected()) {
                        board.getSquare(i, j).draw(g2);
                    }
                }
            }
        }
    }
    
    public void clearText() {
        text = "";
    }
    
    public void syncTextToTextArea() {
        text = textArea.getText();
    }
}

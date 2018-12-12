package matesolver;

import java.awt.*;
import java.awt.geom.*;

public abstract class Piece {

    private int x;
    private int y;
    private Point squareOn;
    protected boolean color;
    private boolean selected;

    /**
     * The constructor Piece(int x, int y, boolean color) takes a square on the board as parameter, sets the squareOn field
     * to that value, and the x and y location to that value multiplied by 62, as each chess square is 62 pixels
     * wide and tall, color is true for black and false for white  
     * 
     * @param x
     * @param y
     * @param color
     */
    public Piece(int x, int y, boolean color ) {
        squareOn = new Point(x, y);
        this.x = x * 62;
        this.y = y * 62;
        this.selected = false;
        this.color = color;
    }

    /**
     * The getSquareOn method returns the square that the piece is on as a point object
     * @return Point, the square that the piece is on
     */
    public Point getSquareOn() {
        return squareOn;
    }

    /**
     * the setSelected(boolean selected) method sets the selected field to the parameter selected.  
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * The method isSelected returns whether or not the piece is selected.
     * @return boolean, true if piece is selected, false if piece is not selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * the getLocation() method returns the location of the piece as a Point, with the pixel location x,y
     * @return Point, the location of the piece
     */
    public Point getLocation() {
        return new Point(x, y);
    }

    /**
     * the getX() method returns the x value of the location of the piece
     * @return int, the x value of the location of hte piece
     */
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    /**
     * the getY() method returns the y-value of the location of the piece.
     * @return int, the y value of the location of the piece
     */
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    /**
     * The contains(Point2D p) method takes a Point2D as a parameter and checks if the point lies on the piece.
     * 
     * The method checks this by checking if if the point that the user clicked was between x and x+62 and between y and 
     * y+62, as each square has width and height of 62 pixels
     * @param p
     * @return boolean, true if point is contained, false if it is not
     */
    public boolean contains(Point2D p) {
        return x <= p.getX() && (x + 62) >= p.getX() && y <= p.getY() && (y + 62) >= p.getY();
    }

    /**
     * The translate method is used when a piece is being dragged and it moves the piece
     * by parameters dx and dy.
     * 
     * @param dx
     * @param dy
     */
    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * The setLocation(int row, int col) takes a row and column from the board as parameters,
     * sets x and y to those multiplied by 62, and sets the squareOn point to that point.  
     * 
     * Used to set a piece on a given square
     * @param row
     * @param col
     */
    public boolean setLocation(int row, int col) {
        x = row * 62;
        y  = col * 62;
        squareOn.setLocation(row, col);
        return true;
    }

    public void setLocationXY(int row, int col) {
        x = row * 62;
        y  = col * 62;
    }

    public boolean getColor() {
        return color;
    }

    /**
     * The getType() method returns the type of piece (rook, king, bishop)
     * @return String, the type of piece
     */
    public abstract String getType();

    /**
     * The getFENType() method returns the FEN type of piece (r, R, k, K, b, B)
     * @return String, the FEN type of piece
     */
    public abstract String getFENType();

    /**
     * The draw method governs how to draw each piece
     * @param g
     */
    public abstract void draw(Graphics g);

    /**
     * The checkLegalMove(Point p) method takes a point in pixels and
     * determines whether the move is legal or not for the piece
     * 
     * @return boolean, true if move is legal, false if it is not
     */
    public abstract boolean checkLegalMove(Point p, Board b);
}

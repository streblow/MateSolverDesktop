package matesolver;

import chesspresso.move.IllegalMoveException;
import javax.swing.*;
import chesspresso.move.Move;
import chesspresso.position.Position;

public class Game {
    private boolean turn;
    public int mode;
    public boolean victory;
    private JLabel turnLabel;

    private String playerOneName;
    private String playerTwoName;

    public Game(int mode, JLabel turnLabel) {
        turn = false;
        this.mode = mode;
        victory = false;
        this.turnLabel = turnLabel;
    }
    
    public Game(int mode, String playerOneName, String playerTwoName, JLabel turnLabel) {
        turn = false;
        this.mode = mode;
        victory = false;
        this.turnLabel = turnLabel;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
    }
	
    public boolean getVictory() {
        return victory;
    }
	
    public int getMode() {
        return mode;
    }
	
    public boolean getTurn() {
        return turn;
    }
	
    public void changeTurn() {
        turn = !turn;
        if(mode == 0){
            if(!turn){
                turnLabel.setText("(" + MateSolver.MODES[mode] + ") - " + playerOneName + " to move");
            }else{
                turnLabel.setText("(" + MateSolver.MODES[mode] + ") - " + playerTwoName + " to move");
            }
        }
    }
	
    public void checkVictory(Board oldBoard, int oldX, int oldY, int newX, int newY, boolean capturing) {
        String strFEN = oldBoard.getFEN();
        if (turn == true)
            strFEN += " b - - 0 1";
        else
            strFEN += " w - - 0 1";
        Position position = new Position(strFEN);
        short move = Move.getRegularMove((7 - oldY) * 8 + oldX, (7 - newY) * 8 + newX, capturing); //A1:0 H8:63
        try {
            position.doMove(move);
        } catch (IllegalMoveException ex) {
            victory = false;
            return;
        }
        victory = position.isMate();
    }

    public void checkVictory(Board board) {
        String strFEN = board.getFEN();
        if (turn == true)
            strFEN += " w - - 0 1";
        else
            strFEN += " b - - 0 1";
        Position position = new Position(strFEN);
        victory = position.isMate();
    }

    public boolean isCheck(Board oldBoard, int oldX, int oldY, int newX, int newY, boolean capturing) {
        String strFEN = oldBoard.getFEN();
        if (turn == true)
            strFEN += " b - - 0 1";
        else
            strFEN += " w - - 0 1";
        Position position = new Position(strFEN);
        short move = Move.getRegularMove((7 - oldY) * 8 + oldX, (7 - newY) * 8 + newX, capturing); //A1:0 H8:63
        try {
            position.doMove(move);
        } catch (IllegalMoveException ex) {
            return false;
        }
        return position.isCheck();
    }

    public boolean isCheck(Board board) {
        String strFEN = board.getFEN();
        if (turn == true)
            strFEN += " w - - 0 1";
        else
            strFEN += " b - - 0 1";
        Position position = new Position(strFEN);
        return position.isCheck();
    }

    public boolean validateGame(Board board) {
        String strFEN = board.getFEN();
        if (turn == true)
            strFEN += " b - - 0 1";
        else
            strFEN += " w - - 0 1";
        try {
            Position position = new Position(strFEN);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean checkCheck(Board oldBoard, int oldX, int oldY, int newX, int newY, boolean capturing) {
        String strFEN = oldBoard.getFEN();
        if (turn == true)
            strFEN += " b - - 0 1";
        else
            strFEN += " w - - 0 1";
        Position position = new Position(strFEN);
        short move = Move.getRegularMove((7 - oldY) * 8 + oldX, (7 - newY) * 8 + newX, capturing); //A1:0 H8:63
        try {
            position.doMove(move);
        } catch (IllegalMoveException ex) {
            return true;
        }
        position.toggleToPlay();
        return position.isCheck();
    }

}

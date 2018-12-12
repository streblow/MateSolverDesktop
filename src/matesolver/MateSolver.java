/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matesolver;

import chesspresso.Chess;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Lars Streblow
 */
public class MateSolver {

    public static final String[] MODES = {"Analysis Mode", "Mate Search"};
    private static int mode;
    private static Board board;
    private static JButton btnSwitchMode;
    private static JButton btnAnalysePosition;
    private static JButton btnFlipBoard;
    private static JButton btnSwitchSide;
    private static JButton btnClearBoard;
    private static JButton btnResetBoard;
    private static JButton btnSearchMate;
    private static JSpinner spnMate;
    private static JLabel lblMate;
    private static JCheckBox cbxFirstMoveOnly;
    private static JLabel lblFirstMoveOnly;
    private static JTextArea textArea;
    private static JLabel turnLabel;
    private static View boardView;
    private static JFrame mainFrame;
    private static AnalysePositionTask analysePositionTask;
    private static MateSearchTask mateSearchTask;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mode = 0;
        board = new Board();
        // Create all controls
        btnSwitchMode = new JButton("Switch to " + MODES[~mode & 1]);
        btnSwitchMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MateSolver.btnSwitchMode.setText("Switch to " + MateSolver.MODES[MateSolver.mode]);
                MateSolver.mode = ~MateSolver.mode & 1;
                MateSolver.boardView.game.mode = MateSolver.mode;
                MateSolver.boardView.clearText();
                textArea.setText("");
                if (mode == 0) {
                    turnLabel.setText("(" + MODES[mode] + ") - White to move");
                    if (MateSolver.boardView.game.getTurn())
                        MateSolver.boardView.game.changeTurn();
                    if (!MateSolver.boardView.game.validateGame(MateSolver.boardView.board)) {
                        textArea.setText("Invalid position");
                    } else
                        MateSolver.boardView.game.victory = false;
                    btnSwitchSide.setEnabled(true);
                    btnAnalysePosition.setEnabled(true);
                    btnClearBoard.setEnabled(false);
                    btnSearchMate.setEnabled(false);
                    lblMate.setText("Depth");
                    lblFirstMoveOnly.setEnabled(false);
                    cbxFirstMoveOnly.setEnabled(false);
                    board = new Board(MateSolver.boardView.board);
                    MateSolver.boardView.move = 1;
                } else {
                    turnLabel.setText("(" + MODES[mode] + ") - Setup board");
                    btnSwitchSide.setEnabled(false);
                    btnAnalysePosition.setEnabled(false);
                    btnClearBoard.setEnabled(true);
                    btnSearchMate.setEnabled(true);
                    lblMate.setText("Mate in");
                    lblFirstMoveOnly.setEnabled(true);
                    cbxFirstMoveOnly.setEnabled(true);
                    board = null;
                }
            }
        });
        btnFlipBoard = new JButton("Flip board");
        btnFlipBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardView.flipBoard();
                boardView.repaint();
            }
        });
        btnSwitchSide = new JButton("Switch side");
        btnSwitchSide.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (boardView.game.getTurn()) {
                    turnLabel.setText("(" + MODES[mode] + ") - White to move");
                    boardView.game.changeTurn();
                    textArea.setText("1.");
                } else {
                    turnLabel.setText("(" + MODES[mode] + ") - Black to move");
                    boardView.game.changeTurn();
                    textArea.setText("1. ...");
                }
                boardView.repaint();
            }
        });
        btnAnalysePosition = new JButton("Analyse position");
        btnAnalysePosition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mode == 0) {
                    MateSolver.boardView.clearText();
                    textArea.setText("");
                    if (!MateSolver.boardView.game.validateGame(MateSolver.boardView.board)) {
                        textArea.setText("Invalid position");
                    } else {
                        boardView.move = 1;
                        textArea.setText("Analysis running...");
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int mateInMoves = Integer.parseInt(spnMate.getValue().toString());
                                if (!MateSolver.boardView.game.getTurn())
                                    analysePositionTask = new AnalysePositionTask(boardView, textArea,
                                            boardView.board.getFEN() + " w - - 0 1", mateInMoves * 2, Chess.WHITE);
                                else
                                    analysePositionTask = new AnalysePositionTask(boardView, textArea,
                                            boardView.board.getFEN() + " b - - 0 1", mateInMoves * 2, Chess.BLACK);
                                analysePositionTask.execute();
                            }
                        });
                        t.start();
                    }
                }
            }
        });
        btnClearBoard = new JButton("Clear board");
        btnClearBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardView.board.clearBoard();
                boardView.repaint();
            }
        });
        btnClearBoard.setEnabled(false);
        btnResetBoard = new JButton("Reset board");
        btnResetBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardView.board.resetBoard();
                if (boardView.boardFlipped) {
                    boardView.boardFlipped = false;
                    boardView.flipBoard();
                }
                boardView.repaint();
            }
        });
        btnSearchMate = new JButton("Search mate");
        btnSearchMate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!MateSolver.boardView.game.validateGame(MateSolver.boardView.board)) {
                    textArea.setText("Invalid position");
                } else {
                    textArea.setText("Mate search running...");
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int mateInMoves = Integer.parseInt(spnMate.getValue().toString());
                            mateSearchTask = new MateSearchTask(boardView, textArea,
                                    boardView.board.getFEN() + " w - - 0 1", mateInMoves * 2 - 1, Chess.WHITE, cbxFirstMoveOnly.isSelected());
                            mateSearchTask.execute();
                        }
                    });
                    t.start();

                }
            }
        });
        btnSearchMate.setEnabled(false);
        spnMate = new JSpinner();
        spnMate.setModel(new SpinnerNumberModel(2, 1, 8, 1));
        spnMate.setName("Mate");
        lblMate = new JLabel();
        lblMate.setText("Depth");
        lblMate.setLabelFor(spnMate);
        cbxFirstMoveOnly = new JCheckBox();
        cbxFirstMoveOnly.setName("1st move only");
        cbxFirstMoveOnly.setEnabled(false);
        lblFirstMoveOnly = new JLabel();
        lblFirstMoveOnly.setText("1st move only");
        lblFirstMoveOnly.setLabelFor(cbxFirstMoveOnly);
        lblFirstMoveOnly.setEnabled(false);

        // Create container panels
        // textPanel
        JPanel textPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea(5,27);
        // Create textArea context menu
        JPopupMenu textAreaPopupMenu = new JPopupMenu();
        JMenuItem menuItem;
        menuItem = new JMenuItem("Clear text area");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                    MateSolver.boardView.clearText();
                    textArea.setText("");
            }
        });
        textAreaPopupMenu.add(menuItem);
        menuItem = new JMenuItem("Copy text area to clipboard");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(MateSolver.textArea.getText()), null);
            }
        });
        textAreaPopupMenu.add(menuItem);
        textArea.setComponentPopupMenu(textAreaPopupMenu);
        textArea.setMinimumSize(new Dimension(310, 300));
        textArea.setEditable(false);
        textArea.setText("Welcome to MateSolver\n" + Character.toString((char)169) + " 2016-2018 Lars Streblow\n");
        JScrollPane textScroller = new JScrollPane(textArea);
        textScroller.setMinimumSize(new Dimension(310,300));
        if (mode == 0)
            turnLabel = new JLabel("(" + MODES[mode] + ") - White to move");
        else
            turnLabel = new JLabel("(" + MODES[mode] + ") - Setup board");
        textPanel.add(turnLabel, BorderLayout.NORTH);
        textPanel.add(textScroller, BorderLayout.CENTER);
        // matePanel
        JPanel matePanel = new JPanel(new FlowLayout());
        matePanel.add(lblMate);
        matePanel.add(spnMate);
        // firstmovePanel
        JPanel firstmovePanel = new JPanel(new FlowLayout());
        firstmovePanel.add(lblFirstMoveOnly);
        firstmovePanel.add(cbxFirstMoveOnly);
        // buttonPanel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 2, 5, 2);
        c.gridwidth = 2;
        buttonPanel.add(btnSwitchMode, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 2, 5, 2);
        c.gridwidth = 1;
        buttonPanel.add(btnFlipBoard, c);
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 2, 5, 2);
        c.gridwidth = 1;
        buttonPanel.add(btnSwitchSide, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 2, 5, 2);
        c.gridwidth = 2;
        buttonPanel.add(btnAnalysePosition, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 2, 5, 2);
        buttonPanel.add(btnClearBoard, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(5, 2, 5, 2);
        buttonPanel.add(btnResetBoard, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(5, 2, 5, 2);
        buttonPanel.add(matePanel, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(5, 2, 5, 2);
        buttonPanel.add(btnSearchMate, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(5, 2, 5, 2);
        buttonPanel.add(firstmovePanel, c);
        // controlPanel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setMinimumSize(new Dimension(310,496));
        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        controlPanel.add(textPanel, BorderLayout.CENTER);

        // Create boardView and game
        Board board = new Board();
        Game game = new Game(mode, "White", "Black", turnLabel);
        boardView = new View(board, textArea, game);
        // Create boardView context menu
        JPopupMenu boardViewPopupMenu = new JPopupMenu();
        menuItem = new JMenuItem("Copy board to clipboard");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TransferableImage(PDFExport.drawBoard(boardView.board)), null);
            }
        });
        boardViewPopupMenu.add(menuItem);
        boardView.setComponentPopupMenu(boardViewPopupMenu);

        // Create mainFrame
        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(boardView, BorderLayout.CENTER);
        mainFrame.add(controlPanel, BorderLayout.EAST);
        mainFrame.pack();
        try {
            Image icon = ImageIO.read(MateSolver.class.getResourceAsStream("/matesolver.png"));
            mainFrame.setIconImage(icon);
        } catch (IOException ex) {
            Logger.getLogger(MateSolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainFrame.setMinimumSize(new Dimension(10*62+10+300+10,8*62+70));
        mainFrame.setSize(new Dimension(10*62+10+300+10,8*62+70));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create main menu bar
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu menuEntry;
        menuEntry = new JMenu("File");
        menuItem = new JMenuItem("Export game to PDF...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (MateSolver.mode== 0)
                    PDFExport.writeAnalysisPDF(MateSolver.board, MateSolver.textArea.getText());
                else
                    PDFExport.writeMatePDF(MateSolver.boardView.board, MateSolver.textArea.getText(), Integer.parseInt(spnMate.getValue().toString()));
            }
        });
        menuEntry.add(menuItem);
        menuEntry.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        menuEntry.add(menuItem);
        mainMenuBar.add(menuEntry);
        menuEntry = new JMenu("Edit");
        menuItem = new JMenuItem("Copy board to clipboard");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TransferableImage(PDFExport.drawBoard(boardView.board)), null);
            }
        });
        menuEntry.add(menuItem);
        menuItem = new JMenuItem("Copy text area to clipboard");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(MateSolver.textArea.getText()), null);
            }
        });
        menuEntry.add(menuItem);
        mainMenuBar.add(menuEntry);
        menuEntry = new JMenu("Help");
        menuItem = new JMenuItem("About MateSolver...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                AboutDialog about = new AboutDialog(mainFrame);
                about.setLocationRelativeTo(mainFrame);
                about.setVisible(true);
            }
        });
        menuEntry.add(menuItem);
        mainMenuBar.add(menuEntry);
        mainFrame.setTitle("MateSolver");
        mainFrame.setJMenuBar(mainMenuBar);
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
    }
}

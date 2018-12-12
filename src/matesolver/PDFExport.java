/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matesolver;

import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;

/**
 *
 * @author Lars Streblow
 */
public class PDFExport {

    public PDFExport() {
    }

    public static File getFilename() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("solution.pdf"));
        int result = fileChooser.showDialog(null, "Export to PDF");
        if (result == JFileChooser.CANCEL_OPTION) {
            return null;
        } else
            return fileChooser.getSelectedFile();
    }

    public static void writeMatePDF(Board board, String solution, int mate) {
        Document document = new Document( PageSize.A4 );
        try {
            File file = getFilename();
            if (file == null)
                return;
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.addTitle("White mates in " + mate);
            document.addSubject("Problem solved by MateSolver");
            document.addKeywords("Chess Problem Mate");
            document.addAuthor("Lars Streblow");
            document.addCreator("Lars Streblow");
            Font font;
            BaseFont basefont;
            basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            font = new Font(basefont, 14);
            font.setStyle(Font.BOLD);
            document.add(new Paragraph("White mates in " + mate, font));
            java.awt.Image boardImage = drawBoard(board);
            Image image = Image.getInstance(boardImage, null);
            document.add(image);
            font.setSize(12);
            font.setStyle(Font.NORMAL);
            document.add(new Paragraph("Solution found by MateSolver:", font));
            basefont = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1250, BaseFont.EMBEDDED);
            font = new Font(basefont, 12);
            font.setStyle(Font.NORMAL);
            document.add(new Paragraph(solution, font));
        } catch (DocumentException ex) {
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
      document.close();
    }

    public static void writeAnalysisPDF(Board board, String log) {
        Document document = new Document( PageSize.A4 );
        try {
            File file = getFilename();
            if (file == null)
                return;
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.addTitle("White to move");
            document.addSubject("Game recorded by MateSolver");
            document.addKeywords("Chess Game");
            document.addAuthor("Lars Streblow");
            document.addCreator("Lars Streblow");
            Font font;
            BaseFont basefont;
            basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            font = new Font(basefont, 14);
            font.setStyle(Font.BOLD);
            document.add(new Paragraph("White to move", font));
            java.awt.Image boardImage = drawBoard(board);
            Image image = Image.getInstance(boardImage, null);
            document.add(image);
            font.setSize(12);
            font.setStyle(Font.NORMAL);
            document.add(new Paragraph("Game recorded by MateSolver:", font));
            basefont = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1250, BaseFont.EMBEDDED);
            font = new Font(basefont, 12);
            font.setStyle(Font.NORMAL);
            document.add(new Paragraph(log, font));
        } catch (DocumentException ex) {
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
      document.close();
    }

    public static java.awt.Image drawBoard(Board board) {
        final int SIZE = 31;
        BufferedImage boardCanvas = new BufferedImage(8*SIZE, 8*SIZE, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = boardCanvas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean isBlack = false;
        // Draw board
        for (int i = 0; i < 8; i++) {
            isBlack = !isBlack;
            for (int j = 0; j < 8; j++) {
                isBlack = !isBlack;
                java.awt.Rectangle rect = new java.awt.Rectangle(i*SIZE, j*SIZE, SIZE, SIZE);
                if (isBlack) {	
                    g.setColor(new Color(80, 96, 128));
                } else {
                    g.setColor(Color.white);
                }
                g.fill(rect);
                g.setColor(Color.black);
                g.drawLine(rect.x + rect.width - 1, rect.y, rect.x + rect.width - 1, rect.y + rect.height - 1);
                g.drawLine(rect.x, rect.y + rect.height - 1, rect.x + rect.width - 1, rect.y + rect.height - 1);
            }
        }
        g.drawLine(0, 0, 8 * SIZE - 1, 0);
        g.drawLine(0, 0, 0, 8 * SIZE - 1);
        // Draw pieces
        BufferedImage pieceCanvas = new BufferedImage(8*62, 8*62, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2 = pieceCanvas.createGraphics();
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board.hasPiece(i, j)) {
                    board.getSquare(i, j).draw(g2);
                }
            }
        }
        g.drawImage(pieceCanvas, 0, 0, 8*SIZE, 8*SIZE, null);
        g.dispose();
        g2.dispose();
        return boardCanvas;
    }

}

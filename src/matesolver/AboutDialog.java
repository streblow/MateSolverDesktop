/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matesolver;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author streblow
 */
public class AboutDialog extends JDialog {
    public final String versionString = "Version 2.6 (Build 2018-12-13)";
    
    public AboutDialog(JFrame parent) {
        super(parent, "About MateSolver", true);
        Image about = null;
        try {
            about = ImageIO.read(MateSolver.class.getResourceAsStream("/author.png"));

        } catch (IOException ex) {
            Logger.getLogger(MateSolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        Box box = null;
        JLabel label = null;
        box = Box.createVerticalBox();
        //box.add(Box.createGlue());
        if (about != null) {
            label = new JLabel(new ImageIcon(about));
            label.setAlignmentX(CENTER_ALIGNMENT);
            box.add((label));
        }
        box.add(new JLabel(" "));
        label =new JLabel(versionString);
        label.setAlignmentX(CENTER_ALIGNMENT);
        box.add((label));
        box.add(new JLabel(" "));
        label =new JLabel("MateSolver is a chess analysing tool.");
        label.setAlignmentX(CENTER_ALIGNMENT);
        box.add((label));
        label = new JLabel("It can solve problems where white mates in up to 8 moves.");
        label.setAlignmentX(CENTER_ALIGNMENT);
        box.add((label));
        label = new JLabel("A position can be evaluated by a built-in engine.");
        label.setAlignmentX(CENTER_ALIGNMENT);
        box.add((label));
        label = new JLabel("The engine has been derived from Eric Liu's engine Kasparov.");
        label.setAlignmentX(CENTER_ALIGNMENT);
        box.add((label));
        label = new JLabel("Find it at github.com/eliucs/kasparov-chess-ai.");
        label.setAlignmentX(CENTER_ALIGNMENT);
        box.add((label));
        box.add(new JLabel(" "));
        label = new JLabel("MateSolver " + Character.toString((char)169) + " 2016-2018 Lars Streblow");
        label.setAlignmentX(CENTER_ALIGNMENT);
        box.add((label));
        box.add(Box.createGlue());
        getContentPane().add(box, BorderLayout.CENTER);
        JPanel p2 = new JPanel();
        JButton ok = new JButton("OK");
        p2.add(ok);
        getContentPane().add(p2, BorderLayout.SOUTH);
        ok.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            setVisible(false);
          }
        });
        setMinimumSize(new Dimension(460, 340));
        setSize(new Dimension(460, 340));
        setResizable(true);
    }

}
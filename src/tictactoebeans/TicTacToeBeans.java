/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoebeans;

import components.BoardPanel;
import config.Animation;
import guis.*;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * 
 * @author mwwie
 */
public class TicTacToeBeans extends JFrame {

    public Start start;
    
    public TicTacToeBeans()
    {
        start = new Start();
        add(start);
        pack();
        setTitle("Tic-Tac-Toe");
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new TicTacToeBeans();
            ex.setVisible(true);
            // line of code to start animation
            //start.getComponent(1).getCom
        });
    }
    
}

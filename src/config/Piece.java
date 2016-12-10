/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import components.BoardPanel;
import java.awt.Color;
import java.awt.Graphics;
import src.Position;

/**
 * The shape and color associated with a player
 * @author mwwie
 */
public abstract class Piece {    
    public Color color;
    
    public Piece(Color c)
    {
        color = c;
    }
    
    public Color getColor() { return color; }
    
    /**
     * Draws this piece at the given position
     * @param p the position on the board
     * @param g the graphics to draw this with
     * @param b the board panel to draw this on
     */
    public abstract void drawAt(Position p, Graphics g, BoardPanel b, 
            boolean isPlayed);
}

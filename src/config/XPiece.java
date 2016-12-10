/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import components.BoardPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import src.Position;

/**
 * Draws a block letter X
 * @author mwwie
 */
public class XPiece extends Piece{
    
    private Polygon poly;
    
    public XPiece(Color c)
    {
        super(c);        
    }

    @Override
    public void drawAt(Position p, Graphics g, BoardPanel b, boolean isPlayed) {
        g.setColor(isPlayed ? color : color.darker());
        int deltaX = (int)(b.POS_SIZE * (p.col() + b.THICKNESS / 2));
        int deltaY = (int)(b.POS_SIZE * (p.row() + b.THICKNESS / 2));
        poly = getXPoly(b);
        poly.translate(deltaX, deltaY);
        g.fillPolygon(poly);
    }
    
    private Polygon getXPoly(BoardPanel b)
    {
        int s = 4; // width of letter is 1, s >= 2
        double m = ((double)s)/(s-1); // downward slope of line for x
        double[] xDub = new double[]{
            0, 1, s/2, s-1, s, s-(s/2)/m, s, s-1, s/2, 1, 0, (s/2)/m};
        double[] yDub = new double[]{
            0, 0, (s/2-1)*m,   0,   0, s/2,   s, s,   s-(s/2-1)*m, s, s, s/2};
                
        int[] xInt = new int[xDub.length];
        int[] yInt = new int[yDub.length];
        
        double multiplier = (1 - b.THICKNESS) * b.POS_SIZE / s;
        
        for(int i = 0; i < xInt.length; i++)
        {
            xInt[i] = (int)Math.round(xDub[i] * multiplier);
            yInt[i] = (int)Math.round(yDub[i] * multiplier);
        }
        
        return new Polygon(xInt, yInt, xInt.length);
    }
    
}

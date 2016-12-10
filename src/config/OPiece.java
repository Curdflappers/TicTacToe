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
 * A donut shape, like the letter O
 * @author mwwie
 */
public class OPiece extends Piece {

    public OPiece(Color c)
    {
        super(c);
    }
    
    @Override
    public void drawAt(Position p, Graphics g, BoardPanel b, boolean isPlayed) {
        final double FILL_FRACTION = 0.93; // fix overflow glitch
        final double INNER_FRACTION = 0.5; // radius of inner, relative to size
        double emptySize = b.EMPTY_FRAC * b.POS_SIZE;
        double circleSize = emptySize * FILL_FRACTION;
        double x = (b.POS_SIZE * (p.col() + b.THICKNESS / 2));
        double y = (b.POS_SIZE * (p.row() + b.THICKNESS / 2));
        /*/
        double offset = (1 - FILL_FRACTION) * emptySize / 2;
        x += offset;
        y += offset;
        //*/
        
        g.setColor(isPlayed ? color : color.darker());
        
        g.fillArc((int)x, (int)y, (int)circleSize, (int)circleSize, 0, 360);
        
        // fill inner
        g.setColor(b.getBackground());
        g.fillArc((int)(x + (1-INNER_FRACTION) * circleSize/2),
                (int)(y + (1-INNER_FRACTION) * circleSize/2),
                (int)(circleSize*INNER_FRACTION), 
                (int)(circleSize*INNER_FRACTION), 0, 360);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import src.Position;

/**
 *
 * @author mwwie
 */
public class Animation extends Game
{
    public Animation(){
        this(new AI("", Game.XPIECE, 1), new AI("", Game.OPIECE, 2));
    }
    
    public Animation(Player p1, Player p2) {
        super(p1, p2);
    }
    
    public void start()
    {
        play(getCurrentPlayer().getBestMove());
    }
    
    @Override
    public boolean play(Position pos)
    {
        if(getBoard().addNewPiece(getTurn(), pos))
        {
            getBoardPanel().repaint();
            changeTurn();
            updateInfoLabel();
            Player next = getCurrentPlayer();
            if(next != null) {
                Position bestMove = next.getBestMove();
                next.getPiece().drawAt(bestMove, getBoardPanel().getGraphics(),
                        getBoardPanel(), false);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) { }
                
                play(bestMove);
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) { }
                restart();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void restart()
    {
        super.restart();
        start();
    }
}

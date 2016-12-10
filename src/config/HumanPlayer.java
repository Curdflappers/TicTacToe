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
public class HumanPlayer extends Player {
    public HumanPlayer(String n, Piece p)
    {
        super(n, p, null);
    }
    
    @Override
    public Position getBestMove() { return null; }
}

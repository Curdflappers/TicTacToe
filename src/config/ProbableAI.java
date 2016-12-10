/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.util.List;
import src.Position;

/**
 *
 * @author mwwie
 */
public class ProbableAI extends AI {
    /** The probability of playing the best move */
    private double probability;

    public ProbableAI(String n, Piece piece, int p, double prob) {
        super(n, piece, p);
        probability = prob;
    }
    
    @Override
    public Position getBestMove()
    {
        List<Position> emptyPos = getBoard().getEmptyPositions();
        Position randomEmpty = emptyPos.get(
                (int)(Math.random() * emptyPos.size()));
        return Math.random() < probability ? super.getBestMove() : randomEmpty;
    }
    
}

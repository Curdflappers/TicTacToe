/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.awt.Color;
import src.Board;
import src.Position;

/**
 * The player playing on a board
 * @author mwwie
 */
public abstract class Player {
    private String name;
    private Piece piece;
    public Board board;
    
    /** Creates a new, unnamed player with a red X */
    public Player()
    {
        this("", new XPiece(Color.red), null);
    }
    
    public Player(String n, Piece p, Board b)
    {
        name = n;
        piece = p;
        board = b;
    }

    public String getName() { return name; }
    public Piece getPiece() { return piece; }
    public Board getBoard() { return board; }

    public void setName(String name) { this.name = name; }
    public void setPiece(Piece piece) { this.piece = piece; }
    public void setBoard(Board board) {this.board = board; }
    
    public abstract Position getBestMove();
}

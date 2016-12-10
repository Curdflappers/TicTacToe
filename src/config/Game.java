package config;

import components.BoardPanel;
import java.awt.Color;
import javax.swing.JLabel;
import src.Board;
import src.Position;

/**
 * Contains information about the current game
 * @author mwwie
 */
public class Game {
    public static Game game;
    public static final Piece XPIECE = new XPiece(Color.red);
    public static final Piece OPIECE = new OPiece(Color.blue);
    
    /** The value of turn when the game is over */
    public static final int GAME_OVER = 0;
    public static final String TURN_SUFFIX = "'s turn";
    public static final String WIN_SUFFIX = " won!";
    public static final String TIE_MSG = "Tie game!";
    
    /** Code for a gamemode of two human players */
    public static final int PVP = 2;
    /** Code for a gamemode of one human and one AI */
    public static final int PVE = 1;
    /** Code for a gamemode of two AIs */
    public static final int EVE = 0;
    
    /** The board this references */
    private Board board;
    
    /** A player of this game */
    private Player player1, player2;
    
    /** Whose turn it is (GAME_OVER, 1, or 2) */
    private int turn;
    
    /** The label that displays whose turn it is */
    private JLabel infoLabel;
    
    private int gameMode;
    
    private BoardPanel boardPanel;
    
    public Game(Player p1, Player p2)
    {
        board = new Board();
        setPlayers(p1, p2);
        turn = 1;
        infoLabel = null;
        setGameMode();
    }
    
    private void setPlayers(Player p1, Player p2)
    {
        player1 = p1;
        player2 = p2;
        player1.setBoard(board);
        player2.setBoard(board);
    }
    
    private void setGameMode()
    {
        if(player1 instanceof HumanPlayer)
        {
            if(player2 instanceof HumanPlayer) {
                gameMode = Game.PVP;
            } else {
                gameMode = Game.PVE;
            }
        } else {
            if(player2 instanceof HumanPlayer) {
                gameMode = Game.PVE;
            } else {
            gameMode = Game.EVE;
            }
        }
    }
    
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Board getBoard() { return board; }
    public int getTurn() { return turn; }
    
    public Player getPlayer(int turn)
    {
        switch(turn){
            case 1:
                return player1;
            case 2:
                return player2;
            default:
                return null;
        }
    }
    
    public Player getCurrentPlayer()
    {
        return getPlayer(turn);
    }
    
    public void setInfoLabel(JLabel label)
    { 
        infoLabel = label;
        updateInfoLabel();
    }
    
    public void setBoardPanel(BoardPanel b) { boardPanel = b; }
    
    /**
     * Clears the board and sets the turn to the first player
     */
    public void restart()
    {
        board.reset();
        turn = 1;
        updateInfoLabel();
    }
    
    /** Adds a new piece belonging to the player whose turn it is at pos
     * if possible
     * @param pos the position to add the piece
     * @return whether a piece was added */
    public boolean play(Position pos)
    {
        // invalid player index
        if(board.addNewPiece(turn, pos))
        {
            boardPanel.repaint();
            changeTurn();
            updateInfoLabel();
            Player next = getCurrentPlayer();
            if(next != null && next instanceof AI) {
                Position bestMove = next.getBestMove();
                next.getPiece().drawAt(bestMove, boardPanel.getGraphics(),
                        boardPanel, false);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) { }
                
                play(bestMove);
            }
            return true;
        }
        return false;
    }
    
    public void drawPiece(Position pos, boolean isPlayed)
    {
        if(boardPanel != null) {
            getCurrentPlayer().getPiece().drawAt(pos, boardPanel.getGraphics(), 
                    boardPanel, isPlayed);
        }
    }
    
    /** Updates the turn label to display whose turn it is or who has won */
    protected void updateInfoLabel()
    {
        String p1Name = player1.getName();
        String p2Name = player2.getName();
        if(infoLabel != null) {
            switch(turn) {
                case 1:
                    infoLabel.setText(p1Name + TURN_SUFFIX);
                    return;
                case 2:
                    infoLabel.setText(p2Name + TURN_SUFFIX);
                    return;
                default:
                    switch(board.getWinner()) {
                        case 1:
                            infoLabel.setText(p1Name + WIN_SUFFIX);
                            return;
                        case 2:
                            infoLabel.setText(p2Name + WIN_SUFFIX);
                            return;
                        default:
                            infoLabel.setText(TIE_MSG);
                    }
            }
        }
    }

    public BoardPanel getBoardPanel() { return boardPanel; }
    protected void changeTurn() { 
            turn = board.isGameOver() ? Game.GAME_OVER : 3 - turn;
    }
}

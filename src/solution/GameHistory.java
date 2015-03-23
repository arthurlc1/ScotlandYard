package solution;

import scotlandyard.*;

import java.util.*;
import java.io.*;

public class GameHistory implements Spectator
{
    public final List<Piece> pieces;
    public final List<Move> moves;

    
    public GameHistory(ScotlandYardModel model)
    {
        pieces = new ArrayList<Piece>();
        moves = new ArrayList<Move>();
        
        model.spectate(this);
    }
    public GameHistory()
    {
    	pieces = new ArrayList<Piece>();
        moves = new ArrayList<Move>();
        
    }
    
    public void join(Colour c, int l, Map<Ticket, Integer> t)
    {
        Piece newPiece;
        if (c == Colour.Black)
        {
            newPiece = new MrX(null, c, l, false, t);
            pieces.add(0, newPiece);
        }
        else
        {
            newPiece = new Detective(null, c, l, t);
            pieces.add(newPiece);
        }
    }
    
    public void notify(Move m)
    {
        moves.add(m);
    }
    
    public ScotlandYardModel toGame(Player player) throws IOException
    {
        ScotlandYardModel model = ScotlandYardModel.defaultGame(pieces.size());
        
        for (Piece p : pieces) model.join(player, p.colour, p.find(), p.tickets);
        for (Move m : moves)
        {
            if (m instanceof MoveTicket) model.play((MoveTicket) m);
            if (m instanceof MoveDouble) model.play((MoveDouble) m);
            if (m instanceof MovePass) model.play((MovePass) m);
            model.nextPlayer();
        }
        return model;
    }
}
package solution;

import scotlandyard.*;

import java.io.IOException;
import java.util.*;

public class ScotlandYardModel extends ScotlandYard
{
    Graph<Integer,Route> graph;
    List<Boolean> rounds;
    
    int numberOfPlayers;
    List<Piece> pieces;
    
    int currentPlayer;
    int round;
    
    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException
    {
        super(numberOfDetectives, rounds, graphFileName);
        
        GraphReader r = new ScotlandYardGraphReader();
        this.graph = r.readGraph(graphFileName);
        this.rounds = new ArrayList<Boolean>(rounds);
        
        numberOfPlayers = numberOfDetectives + 1;
        pieces = new ArrayList<Piece>(numberOfPlayers);
        
        currentPlayer = 0;
        round = 0;
    }
    
    protected Piece getPiece(Colour colour)
    {
        for (Piece p : pieces) if (p.getColour() == colour) return p;
        return null;
    }
    
    @Override
    protected Move getPlayerMove(Colour colour)
    {
        Piece p = getPiece(colour);
        return p.player.notify(p.getLocation(), validMoves(colour));
    }
    
    @Override
    protected void nextPlayer()
    {
        if (currentPlayer < pieces.size())
        {
            currentPlayer++;
        }
        else
        {
            currentPlayer = 0;
            round++;
        }
    }
    
    @Override
    protected void play(MoveTicket move)
    {
        
    }
    
    @Override
    protected void play(MoveDouble move)
    {
        
    }
    
    @Override
    protected void play(MovePass move)
    {
        
    }
    
    @Override
    protected List<Move> validMoves(Colour player)
    {
        List<Move> moves = new ArrayList<Move>();
        
        return moves;
    }
    
    @Override
    public void spectate(Spectator spectator)
    {
        
    }
    
    @Override
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets)
    {
        boolean mrXHere = false;
        for (Piece p : pieces)
        {
            if (p.getColour() == colour)       return false;
            if (p.getColour() == Colour.Black) mrXHere = true;
        }
        
        Piece newPiece;
        
        if (colour == Colour.Black)
        {
            newPiece = new MrX(player, colour, location, tickets);
            pieces.add(0, newPiece);
        }
        else
        {
            if ( pieces.size() == numberOfPlayers - (mrXHere ? 0 : 1) ) return false;
            newPiece = new Detective(player, colour, location, tickets);
            pieces.add(newPiece);
        }
        return true;
    }
    
    @Override
    public List<Colour> getPlayers()
    {
        List<Colour> colours = new ArrayList<Colour>(numberOfPlayers);
        for (Piece p : pieces) colours.add(p.getColour());
        return colours;
    }
    
    @Override
    public Set<Colour> getWinningPlayers()
    {
        return null;
    }
    
    @Override
    public int getPlayerLocation(Colour colour)
    {
        return getPiece(colour).getLocation();
    }
    
    @Override
    public int getPlayerTickets(Colour colour, Ticket ticket)
    {
        return getPiece(colour).getNumTickets(ticket);
    }
    
    @Override
    public boolean isGameOver()
    {
        return false;
    }
    
    @Override
    public boolean isReady()
    {
        return (pieces.size() == numberOfPlayers);
    }
    
    @Override
    public Colour getCurrentPlayer()
    {
        return pieces.get(currentPlayer).getColour();
    }
    
    @Override
    public int getRound()
    {
        return round;
    }
    
    @Override
    public List<Boolean> getRounds()
    {
        return rounds;
    }
}
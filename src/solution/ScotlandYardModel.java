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
    static Colour black = Colour.Black;
    
    List<Spectator> spectators;
    
    int currentPlayer;
    int round;
    
    boolean detectivesStuck;
    
    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException
    {
        super(numberOfDetectives, rounds, graphFileName);
        
        ScotlandYardGraphReader r = new ScotlandYardGraphReader();
        this.graph = r.readGraph(graphFileName);
        this.rounds = new ArrayList<Boolean>(rounds);
        
        numberOfPlayers = numberOfDetectives + 1;
        pieces = new ArrayList<Piece>(numberOfPlayers);
        
        spectators = new ArrayList<Spectators>();
        
        currentPlayer = 0;
        round = 0;
        
        detectivesStuck = false;
    }
    
    @Override
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets)
    {
        boolean mrXHere = false;
        for (Piece p : pieces)
        {
            if (p.getColour() == colour) return false;
            if (p.getColour() == black)  mrXHere = true;
        }
        
        Piece newPiece;
        
        if (colour == black)
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
    public void spectate(Spectator spectator)
    {
        spectators.add(spectator);
    }
    
    @Override
    public boolean isReady()
    {
        return (pieces.size() == numberOfPlayers);
    }
    
    protected Piece getPiece(Colour colour)
    {
        for (Piece p : pieces) if (p.getColour() == colour) return p;
        return null;
    }
    
    @Override
    protected List<Move> validMoves(Colour player)
    {
        int location = getPiece(player).getLocation();
        Map<Ticket,Integer> tickets = getPiece(player).getTickets();
        
        return validMoves(player, location, tickets);
    }
    
    protected List<Move> validMoves(Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        List<Move> moves = new ArrayList<Move>();
        
        List<MoveTicket> singleMoves = validSingleMoves(colour, location, tickets);
        List<MoveDouble> doubleMoves = new ArrayList<MoveDouble>();
        
        if (tickets.get(Ticket.DoubleMove) > 0)
        {
            Map<Ticket,Integer> newTickets;
            for (MoveTicket m1 : singleMoves)
            {
                Ticket usedTicket = m1.ticket;
                newTickets = new HashMap<Ticket,Integer>(tickets);
                int newNum = tickets.get(usedTicket) - 1;
                newTickets.replace(usedTicket, newNum);
                
                for (MoveTicket m2 : validSingleMoves(colour, m1.target, newTickets))
                {
                    doubleMoves.add(new MoveDouble(colour, m1, m2));
                }
            }
        }
        
        moves.addAll(singleMoves);
        moves.addAll(doubleMoves);
        if (colour != black && moves.size() == 0)
        {
            moves.add(new MovePass(colour));
        }
        return moves;
    }
    
    protected List<MoveTicket> validSingleMoves(Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        List<MoveTicket> moves = new ArrayList<MoveTicket>();
        
        for (Edge<Integer,Route> e : graph.getEdges(location))
        {
            int other = e.other(location);
            boolean occupied = false;
            
            for (Piece p : pieces)
            {
                if (p.colour != black && p.getLocation() == other) occupied = true;
            }
            
            if (occupied) continue;
            
            Ticket defaultTicket = Ticket.fromRoute(e.data());
            Ticket secret = Ticket.SecretMove;
            
            if (tickets.get(defaultTicket) > 0)
            {
                MoveTicket defaultMove = new MoveTicket(colour, other, defaultTicket);
                moves.add(defaultMove);
            }
            
            if (defaultTicket != secret && tickets.get(secret) > 0)
            {
                MoveTicket secretMove = new MoveTicket(colour, other, secret);
                moves.add(secretMove);
            }
        }
        return moves;
    }
    
    @Override
    protected Move getPlayerMove(Colour colour)
    {
        Piece p = getPiece(colour);
        List<Move> moves = validMoves(colour);
        Move move = p.player.notify(p.getLocation(), moves);
        return moves.contains(move) ? move : null;
    }
    
    @Override
    protected void play(MoveTicket move)
    {
        Piece toMove = getPiece(move.colour);
        if (toMove instanceof MrX)
        {
            ((MrX) toMove).play(move, rounds.get(round));
            round++;
            detectivesStuck = true;
        }
        else
        {
            ((Detective) toMove).play(move, (MrX) pieces.get(0));
            detectivesStuck = false;
        }
    }
    
    @Override
    protected void play(MoveDouble move)
    {
        play((MoveTicket) move.moves.get(0));
        play((MoveTicket) move.moves.get(1));
    }
    
    @Override
    protected void play(MovePass move)
    {
        
    }
    
    @Override
    protected void nextPlayer()
    {
        if (currentPlayer < pieces.size() - 1) currentPlayer++;
        else                               currentPlayer = 0;
    }
    
    @Override
    public List<Colour> getPlayers()
    {
        List<Colour> colours = new ArrayList<Colour>(numberOfPlayers);
        for (Piece p : pieces) colours.add(p.getColour());
        return colours;
    }
    
    @Override
    public Colour getCurrentPlayer()
    {
        return pieces.get(currentPlayer).getColour();
    }
    
    @Override
    public int getPlayerLocation(Colour colour)
    {
        Piece piece = getPiece(colour);
        boolean toReveal = rounds.get(round);
        
        if (colour == black && !toReveal) return ((MrX) piece).lastKnownLocation();
        else                              return piece.getLocation();
    }
    
    @Override
    public int getPlayerTickets(Colour colour, Ticket ticket)
    {
        return getPiece(colour).getTickets().get(ticket);
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
    
    protected boolean timeOut()
    {
        for (Piece p : pieces)
        {
            if (p instanceof MrX) continue;
            
            boolean noTickets = true;
            for (Ticket t : Ticket.values())
            {
                if (p.getTickets().get(t) > 0) noTickets = false;
            }
            if (noTickets) return true;
        }
        return (currentPlayer == 0 && (detectivesStuck || round == rounds.size() - 1));
    }
    
    protected boolean mrXCaught()
    {
        for (Piece p : pieces)
        {
            if (p instanceof MrX) continue;
            else if (p.getLocation() == pieces.get(0).getLocation()) return true;
        }
        return (currentPlayer == 0 && validMoves(black).size() == 0);
    }
    
    @Override
    public boolean isGameOver()
    {
        return (isReady() && (timeOut() || mrXCaught() || pieces.size() == 1));
    }
    
    @Override
    public Set<Colour> getWinningPlayers()
    {
        Set<Colour> winners = new HashSet<Colour>();
        
        boolean mrXWins = (timeOut() && !mrXCaught());
        if (isGameOver())
        {
            if (mrXWins) winners.add(black);
            else
            {
                for (Piece p : pieces)
                {
                    if (p instanceof Detective) winners.add(p.getColour());
                }
            }
        }
        return winners;
    }
}
package solution;

import scotlandyard.*;

import java.io.IOException;
import java.util.*;

public class ScotlandYardModel extends ScotlandYard
{
    private final Graph<Integer,Route> graph;
    
    private final int numPlayers;
    private final List<Piece> pieces;
    
    private final static Colour black = Colour.Black;
    private MrX mrX;
    
    private final List<Spectator> spectators;
    
    private final List<Boolean> rounds;
    private int round;
    private int currentPlayer;
    
    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException
    {
        super(numberOfDetectives, rounds, graphFileName);
        
        this.graph = new ScotlandYardGraphReader().readGraph(graphFileName);
        this.rounds = new ArrayList<Boolean>(rounds);
        
        numPlayers = numberOfDetectives + 1;
        pieces = new ArrayList<Piece>(numPlayers);
        mrX = null;
        
        spectators = new ArrayList<Spectator>();
        
        currentPlayer = 0;
        round = 0;
    }
    
    @Override
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets)
    {
        for (Piece p : pieces) if (p.colour == colour)return false;
        
        Piece newPiece;
        
        if (colour == black)
        {
            newPiece = new MrX(player, colour, location, rounds.get(0), tickets);
            pieces.add(0, newPiece);
            mrX = (MrX) pieces.get(0);
        }
        else
        {
            if (pieces.size() == numPlayers - (mrX == null ? 1 : 0)) return false;
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
        return (pieces.size() == numPlayers);
    }
    
    protected Piece getPiece(Colour colour)
    {
        for (Piece p : pieces) if (p.colour == colour) return p;
        return null;
    }
    
    @Override
    protected List<Move> validMoves(Colour colour)
    {
        Piece p = getPiece(colour);
        //if (mrXCaught()) return Arrays.asList(new MovePass(colour));
        return validMoves(colour, p.location(), p.tickets);
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
                newTickets = new HashMap<Ticket,Integer>(tickets);
                newTickets.replace(m1.ticket, tickets.get(m1.ticket) - 1);
                for (MoveTicket m2 : validSingleMoves(colour, m1.target, newTickets))
                {
                    doubleMoves.add(new MoveDouble(colour, m1, m2));
                }
            }
        }
        moves.addAll(singleMoves);
        moves.addAll(doubleMoves);
        if (colour != black && moves.size() == 0) moves.add(new MovePass(colour));
        return moves;
    }
    
    protected List<MoveTicket> validSingleMoves(Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        List<MoveTicket> moves = new ArrayList<MoveTicket>();
        
        for (Edge<Integer,Route> e : graph.getEdges(location))
        {
            int other = e.other(location);
            boolean occupied = false;
            for (Piece p : pieces) if (p != mrX && p.location() == other) occupied = true;
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
        Move move = p.player.notify(p.location(), moves);
        return moves.contains(move) ? move : null;
    }
    
    @Override
    protected void play(MoveTicket move)
    {
        Piece toMove = getPiece(move.colour);
        if (toMove == mrX)
        {
            boolean reveal = rounds.get(++round);
            mrX.play(move, reveal);
            move = new MoveTicket(black, mrX.lastSeen(), move.ticket);
        }
        else
        {
            ((Detective) toMove).play(move, mrX);
        }
        for (Spectator s : spectators) s.notify(move);
    }
    
    @Override
    protected void play(MoveDouble move)
    {
        for (Spectator s : spectators) s.notify(move);
        play((MoveTicket) move.moves.get(0));
        play((MoveTicket) move.moves.get(1));
    }
    
    @Override
    protected void play(MovePass move)
    {
        for (Spectator s : spectators) s.notify(move);
    }
    
    @Override
    protected void nextPlayer()
    {
        currentPlayer = (currentPlayer + 1) % pieces.size();
    }
    
    @Override
    public List<Colour> getPlayers()
    {
        List<Colour> colours = new ArrayList<Colour>(numPlayers);
        for (Piece p : pieces) colours.add(p.colour);
        return colours;
    }
    
    @Override
    public Colour getCurrentPlayer()
    {
        return pieces.get(currentPlayer).colour;
    }
    
    @Override
    public int getPlayerLocation(Colour colour)
    {
        Piece toFind = getPiece(colour);
        return (toFind == mrX) ? mrX.lastSeen() : toFind.location();
    }
    
    @Override
    public int getPlayerTickets(Colour colour, Ticket ticket)
    {
        return getPiece(colour).tickets.get(ticket);
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
        boolean allStuck = true;
        for (Piece p : pieces)
        {
            if (p == mrX) continue;
            if (validMoves(p.colour).get(0) instanceof MoveTicket) allStuck = false;
        }
        return (allStuck || (currentPlayer == 0 && round == rounds.size() - 1));
    }
    
    protected boolean mrXCaught()
    {
        for (Piece p : pieces)
        {
            if (p == mrX) continue;
            else if (p.location() == mrX.location()) return true;
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
        if (isGameOver())
        {
            boolean mrXWins = (timeOut() && !mrXCaught());
            if (mrXWins) winners.add(black);
            else for (Colour c : getPlayers()) if (c != black) winners.add(c);
        }
        return winners;
    }
}
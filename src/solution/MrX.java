package solution;

import scotlandyard.*;

import java.util.*;

public class MrX extends Piece
{
    private int lastSeen;
    
    public final List<Ticket> history;
    
    public MrX(Player player, Colour colour, int location, boolean reveal, Map<Ticket,Integer> tickets)
    {
        super(player, colour, location, tickets);
        lastSeen = reveal ? location : 0;
        history = new ArrayList<Ticket>();
    }
    
    public int lastSeen()
    {
        return lastSeen;
    }
    
    public void giveTicket(Ticket ticket)
    {
        tickets.replace(ticket, tickets.get(ticket) + 1);
    }
    
    public void play(MoveTicket move, boolean reveal)
    {
        play(move);
        history.add(move.ticket);
        if (reveal) lastSeen = location;
    }
}
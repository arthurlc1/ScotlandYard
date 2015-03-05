package solution;

import scotlandyard.*;

import java.util.*;

public class MrX extends Piece
{
    int lastKnownLocation;
    
    public MrX(Player player, Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        super(player, colour, location, tickets);
        
        lastKnownLocation = 0;
    }
    
    public int lastKnownLocation()
    {
        return lastKnownLocation;
    }
    
    public void giveTicket(Ticket ticket)
    {
        int newNum = tickets.get(ticket) + 1;
        tickets.replace(ticket, newNum);
    }
    
    public void play(MoveTicket move, boolean toReveal)
    {
        play(move);
        if (toReveal) lastKnownLocation = location;
    }
}
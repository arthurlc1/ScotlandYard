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
}
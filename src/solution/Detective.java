package solution;

import scotlandyard.*;

import java.util.*;

public class Detective extends Piece
{
    public Detective(Player player, Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        super(player, colour, location, tickets);
    }
}
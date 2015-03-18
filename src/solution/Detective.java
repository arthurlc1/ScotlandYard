package solution;

import scotlandyard.*;

import java.util.*;

public class Detective extends Piece
{
    public Detective(Player player, Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        super(player, colour, location, tickets);
    }
    
    public void play(MoveTicket move, MrX recipient)
    {
        play(move);
        recipient.giveTicket(move.ticket);
    }
    
    public static Detective getDefault(Player p, Colour c, int l)
    {
        return new Detective(p, c, l, Piece.getMap(11, 8, 4, 0, 0));
    }
}
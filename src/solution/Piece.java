package solution;

import scotlandyard.*;

import java.util.*;

public abstract class Piece
{
    Player player;
    Colour colour;
    int location;
    Map<Ticket,Integer> tickets;
    
    public Piece(Player player, Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        this.player = player;
        this.colour = colour;
        this.location = location;
        this.tickets = tickets;
    }
    
    public Colour getColour()
    {
        return colour;
    }
}
package solution;

import scotlandyard.*;

import java.util.*;

public abstract class Piece
{
    public final Player player;
    public final Colour colour;
    int location;
    public final Map<Ticket,Integer> tickets;

    public Piece(Player player, Colour colour, int location, Map<Ticket,Integer> tickets)
    {
        this.player = player;
        this.colour = colour;
        this.location = location;
        this.tickets = tickets;
    }

    public int find()
    {
        return location;
    }

    public void play(MoveTicket move)
    {
        location = move.target;
        tickets.replace(move.ticket, tickets.get(move.ticket) - 1);
    }
}
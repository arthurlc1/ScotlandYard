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
    
    public Player getPlayer() {
		return player;
	}
    
    public Colour getColour()
    {
        return colour;
    }
    
	public int getLocation() {
		return location;
	}
    
    public Map<Ticket,Integer> getTickets() {
		return tickets;
	}
    
    public void play(MoveTicket move)
    {
        location = move.target;
        
        usedTicket = move.ticket;
        newNum = tickets.get(usedTicket) - 1;
        tickets.replace(usedTicket, newNum);
    }
}
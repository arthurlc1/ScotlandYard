package solution;

import scotlandyard.*;

import java.util.*;

public abstract class Piece
{
    Player player;
    Colour colour;
    int location;
    Map<Ticket,Integer> tickets;
}
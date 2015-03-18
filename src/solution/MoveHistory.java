package solution;

import scotlandyard.*;

import java.util.*;

public class MoveHistory implements Spectator
{
    boolean recordAll;              // If false, record only Mr. X's moves.
    private List<Move> history;
    
    public MoveHistory(boolean recordAll)
    {
        this.recordAll = recordAll;
        history = new ArrayList<Move>();
    }
    
    public void notify(Move m)
    {
        if (recordAll || m.colour == Colour.Black) history.add(m);
    }
}
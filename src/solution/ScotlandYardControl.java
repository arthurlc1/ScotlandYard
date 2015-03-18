package solution;

import scotlandyard.*;

import java.util.*;
import java.io.*;

import javax.swing.*;
import java.awt.event.*;

public class ScotlandYardControl implements Player, Spectator, ActionListener
{
    private final static Colour black = Colour.Black;
    
    private ScotlandYardModel model;
    private ScotlandYardDisplay display;
    
    public ScotlandYardControl(List<Colour> colours)
    {
        try { model = ScotlandYardModel.defaultGame(colours.size()); }
        catch (IOException e) { }
        List<Integer> starts = Arrays.asList(11, 22, 33, 44, 55, 66, 77, 88);
        Random r = new Random();
        for (Colour c : colours)
        {
            int s = starts.get(r.nextInt(starts.size()));
            starts.remove(s);
            Map<Ticket,Integer> t = (c == black ? MrX.getMap() : Detective.getMap());
            model.join(this, c, s, t);
        }
        init();
    }
    
    public ScotlandYardControl(File saveFile)
    {
        init();
    }
    
    public void init()
    {
        
    }
    
    public Move notify(int location, List<Move> list)
    {
        return null;
    }
    
    public void notify(Move move)
    {
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
    }
    
    public List<Colour> getColours()
    {
        return model.getPlayers();
    }
    
    public int getLocation(Colour colour)
    {
        return model.getPlayerLocation(colour);
    }
}
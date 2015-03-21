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
    
    private GameHistory history;
    
    public ScotlandYardControl(List<Colour> colours)
    {
        List<Boolean> r = str2bools("0001000010000100001000001");
        int numD = colours.size() - 1;
        try { model = new ScotlandYardModel(numD, r, "resources/dist/graph.txt"); }
        catch (IOException e) { }
        history = new GameHistory(model);
        
        Integer[] xS = {104,51,35,71,166,78,127,146,170,132};
        Integer[] dS = {123,26,34,53,174,13,29,138,94,50,117,155,91,103,112,141};
        List<Integer> xStarts = new ArrayList<Integer>(Arrays.asList(xS));
        List<Integer> dStarts = new ArrayList<Integer>(Arrays.asList(dS));
        
        Random rand = new Random();
        for (Colour c : colours)
        {
            int s;
            if (c == black) s = xStarts.get(rand.nextInt(xStarts.size()));
            else
            {
                s = dStarts.get(rand.nextInt(dStarts.size()));
                dStarts.remove(dStarts.indexOf(s));
            }
            Map<Ticket,Integer> t = (c == black ? MrX.getMap() : Detective.getMap());
            if (model.join(this, c, s, t)) history.join(c, s, t);
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
    
    public Map<Colour,Integer> getLocMap()
    {
        Map<Colour,Integer> out = new HashMap<Colour,Integer>();
        for (Colour c : getColours()) out.put(c, getLocation(c));
        return out;
    }
    
    private static List<Boolean> str2bools(String str)
    {
        List<Boolean> out = new ArrayList<Boolean>();
        for (char c : str.toCharArray()) out.add(c == '1');
        return out;
    }
}
package solution;

import scotlandyard.*;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import javax.swing.*;
import java.awt.event.*;

public class ScotlandYardControl extends MouseAdapter implements Player, ActionListener
{
    private final static Colour black = Colour.Black;
    
    private ScotlandYardModel model;
    private ScotlandYardDisplay display;
    private GameHistory history;
    
    private Colour cP;
    private int[] cT;
    private List<Move> validMoves;
    private boolean[][] usable;
    
    private CountDownLatch moveChosen;
    private boolean dbl = false;
    private Move chosenMove;
    
    public ScotlandYardControl(GameHistory history)
    {
        try { model = history.toGame(this); }
        catch (IOException e) { }
        new Thread(model::start).start();
    }
    
    public ScotlandYardControl(List<Colour> colours)
    {
        try { model = ScotlandYardModel.defaultGame(colours.size() - 1); }
        catch (IOException e) { }
        history = new GameHistory(model);
        init(colours);
        new Thread(model::start).start();
    }
    
    public void init(List<Colour> colours)
    {
        Integer[] xS = {35,51,71,78,104,127,132,146,166,170};
        Integer[] dS = {13,26,29,34,50,53,91,94,103,112,117,123,138,141,155,174};
        List<Integer> xStarts = new ArrayList<Integer>(Arrays.asList(xS));
        List<Integer> dStarts = new ArrayList<Integer>(Arrays.asList(dS));
        Random rand = new Random();
        for (Colour c : colours)
        {
            System.err.println(c.toString() + ":");
            int s;
            if (c == black) s = xStarts.get(rand.nextInt(xStarts.size()));
            else
            {
                s = dStarts.get(rand.nextInt(dStarts.size()));
                dStarts.remove(dStarts.indexOf(s));
            }
            System.err.println("located,");
            Map<Ticket,Integer> t = (c == black ? MrX.getMap() : Detective.getMap());
            System.err.println("joined.");
            if (model.join(this, c, s, t)) history.join(c, s, t);
        }
    }
    
    public void addDisplay(ScotlandYardDisplay display)
    {
        this.display = display;
        display.init(getLocMap(), model.getPiece(black).find());
        display.addMouseListener(this);
    }
    
    public Move notify(int location, List<Move> validMoves)
    {
        this.validMoves = validMoves;
        boolean pass = false;
        boolean ts[] = new boolean[199];
        usable = new boolean[199][5];
        cP = model.getCurrentPlayer();
        if (cP == black)
        {
            JOptionPane.showMessageDialog(display, new JLabel("Mr. X's turn is about to start. Other players, look away now!"), "WARNING", JOptionPane.WARNING_MESSAGE);
            display.setxTurn(true);
        }
        for (Move m : validMoves)
        {
            MoveTicket mt = null;
            if (m instanceof MovePass)
            {
                pass = true;
                break;
            }
            if (m instanceof MoveTicket) mt = (MoveTicket)m;
            if (m instanceof MoveDouble)
            {
                mt = (MoveTicket)((MoveDouble)m).moves.get(0);
                usable[mt.target-1][4] = true;
            }
            else System.err.println(m.toString());
            ts[mt.target-1] = true;
            usable[mt.target-1][t2i(mt.ticket)] = true;
        }
        System.err.println("Moves processed.");
        display.updateTargets(ts);
        cT = new int[5];
        for (Ticket t : Ticket.values()) cT[t2i(t)] = model.getPlayerTickets(cP, t);
        System.err.println("Tickets retrieved.");
        chosenMove = null;
        moveChosen = new CountDownLatch(1);
        try { moveChosen.await(); } catch (Exception e) { }
        return chosenMove;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
    }
    
    public List<Colour> getColours()
    {
        return model.getPlayers();
    }
    
    public Map<Colour,Integer> getLocMap()
    {
        Map<Colour,Integer> out = new HashMap<Colour,Integer>();
        for (Colour c : getColours()) out.put(c, model.getPlayerLocation(c));
        for (Colour c : getColours()) System.err.println(c.toString() + ": " + model.getPlayerLocation(c));
        return out;
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        display.updateMouse(e);
        for (int i=0; i<199; i++) if (display.mouseOver(i))
        {
            display.makeTicketMenu(i, cP == black, cT, usable[i]);
            display.showTicketMenu();
            break;
        }
    }
    
    private static List<Boolean> str2bools(String str)
    {
        List<Boolean> out = new ArrayList<Boolean>();
        for (char c : str.toCharArray()) out.add(c == '1');
        return out;
    }
    
    private static int t2i(Ticket t)
    {
        if      (t == Ticket.Taxi)        return 0;
        else if (t == Ticket.Bus)         return 1;
        else if (t == Ticket.Underground) return 2;
        else if (t == Ticket.SecretMove)  return 3;
        else if (t == Ticket.DoubleMove)  return 4;
        else return -1;
    }
}
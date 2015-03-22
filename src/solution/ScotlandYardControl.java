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
    private int cL;
    private List<Move> validMoves;
    private boolean[] ts;
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
        if (validMoves.get(0) instanceof MovePass) return validMoves.get(0);
        cP = model.getCurrentPlayer();
        cL = model.getPiece(cP).find();
        cT = new int[5];
        for (Ticket t : Ticket.values()) cT[t2i(t)] = model.getPlayerTickets(cP, t);
        if (cP == black)
        {
            JOptionPane.showMessageDialog(display, new JLabel("Mr. X's turn is about to start. Other players, look away now!"), "WARNING", JOptionPane.WARNING_MESSAGE);
            display.setxTurn(true);
        }
        processMoves(true);
        chosenMove = null;
        moveChosen = new CountDownLatch(1);
        try { moveChosen.await(); } catch (Exception e) { }
        return chosenMove;
    }
    
    public void processMoves(boolean m1)
    {
        ts = new boolean[199];
        usable = new boolean[199][5];
        for (Move m : validMoves)
        {
            boolean m2 = false;
            MoveTicket mt = null;
            if (m instanceof MoveTicket && m1) mt = (MoveTicket)m;
            if (m instanceof MoveDouble)
            {
                mt = (MoveTicket)((MoveDouble)m).moves.get(0);
                if (m1) usable[mt.target-1][4] = true;
                else if (mt.target == cL)
                {
                    mt = (MoveTicket)((MoveDouble)m).moves.get(1);
                    m2 = true;
                }
            }
            if (m1 || m2)
            {
                ts[mt.target-1] = true;
                usable[mt.target-1][t2i(mt.ticket)] = true;
            }
            
        }
        display.updateTargets(ts);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("save")) { }
        if (cmd.equals("load")) { }
        if (cmd.equals("quit")) { }
        else
        {
            MoveTicket m;
            String[] terms = cmd.split("-");
            if (terms[1].equals("double"))
            {
                dbl = true;
                for (int i=0; i<199; i++) usable[i][4] = false;
                return;
            }
            display.hideTicketMenu();
            int l = Integer.parseInt(terms[0]);
            Ticket t = s2t(terms[1]);
            m = new MoveTicket(cP, l, t);
            display.play(cP, l, model.getRounds().get(model.getRound()));
            if (chosenMove != null) chosenMove = new MoveDouble(cP, chosenMove, m);
            else chosenMove = m;
            if (dbl)
            {
                cL = l;
                processMoves(dbl = false);
                return;
            }
            display.setxTurn(false);
            moveChosen.countDown();
        }
    }
    
    public List<Colour> getColours()
    {
        return model.getPlayers();
    }
    
    public Map<Colour,Integer> getLocMap()
    {
        Map<Colour,Integer> out = new HashMap<Colour,Integer>();
        for (Colour c : getColours()) out.put(c, model.getPlayerLocation(c));
        return out;
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        display.updateMouse(e);
        for (int i=0; i<199; i++) if (display.mouseOver(i))
        {
            display.makeTicketMenu(i+1, cP == black, cT, usable[i], this);
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
    
    private static Ticket s2t(String s)
    {
        if (s.equals("taxi"))        return Ticket.Taxi;
        else if (s.equals("bus"))    return Ticket.Bus;
        else if (s.equals("tube"))   return Ticket.Underground;
        else if (s.equals("secret")) return Ticket.SecretMove;
        else if (s.equals("double")) return Ticket.DoubleMove;
        else return null;
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
package solution;

import scotlandyard.*;

import java.io.*;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * TODO:
 *      - Fix issue where view jerks as zoom reaches maximum / minimum.
 */

public class ScotlandYardDisplay extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener
{
    private final static Colour black = Colour.Black;
    
    private int[] tickets;
    private boolean[] usable;
    
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    
    private static BufferedImage bg;
    
    private Point[] ls = new Point[199];
    private int[] stations = new int[199];
    private static BufferedImage[] imgS = new BufferedImage[3];
    private int[] highlights = new int[199];
    private static BufferedImage[] imgH = new BufferedImage[3];
    
    private Map<Colour,Integer> locMap;
    private Map<Colour,BufferedImage> imgP;
    private int locX;
    private BufferedImage imgX;
    private boolean xTurn = false;
    
    private AffineTransform mv, imv, sc, isc, sc_mv, imv_isc;
    
    private double MIN_Z, MAX_Z;
    private int MIN_X, MAX_X, MIN_Y, MAX_Y;
    
    private double z;
    private Point s, w, o0, m0, o, m;
    
    private boolean dragging = false;
    private boolean zooming = false;
    
    private JButton menuB;
    private JPopupMenu menu;
    private JMenuItem save, load, quit;
    
    private TicketPanel ticketP;
    private JPopupMenu ticketM;
    
    // Initialise SYC from list of colours, for new game.
    public ScotlandYardDisplay(java.util.List<Colour> colours)
    {
        new ScotlandYardControl(colours).addDisplay(this);
    }
    
    // Initialise SYC from save file.
    public ScotlandYardDisplay(GameHistory history)
    {
        new ScotlandYardControl(history).addDisplay(this);
    }
    
    public void init(Map<Colour,Integer> locMap, int locX)
    {
        this.setPreferredSize(new Dimension(1200,700));
        this.setOpaque(false);
        this.setLayout(null);
        
        tickets = new int[] {11, 8, 5, 5, 2};
        usable = new boolean[] {true, true, true, true, true};
        this.locMap = locMap;
        this.locX = locX;
        
        z = 0.5;
        o = new Point();
        m = new Point();
        try { updateTransforms(); } catch (Exception e) { }
        getImages();
        try { getNodes(); } catch(FileNotFoundException e) { }
        makeMenu();
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }
    
    public void getImages()
    {
        bg = Resources.get("background-re");
        s = new Point(bg.getWidth(), bg.getHeight());
        imgS[0] = Resources.get("s-taxi");
        imgS[1] = Resources.get("s-bus");
        imgS[2] = Resources.get("s-tube");
        imgH[0] = null;
        imgH[1] = Resources.get("h-1");
        imgH[2] = Resources.get("h-2");
        imgP = new HashMap<Colour,BufferedImage>();
        imgP.put(Colour.Black,  Resources.get("mr-x-last"));
        imgP.put(Colour.Blue,   Resources.get("det-b-lo"));
        imgP.put(Colour.Green,  Resources.get("det-g-lo"));
        imgP.put(Colour.Red,    Resources.get("det-r-lo"));
        imgP.put(Colour.White,  Resources.get("det-w-lo"));
        imgP.put(Colour.Yellow, Resources.get("det-y-lo"));
        imgX = Resources.get("mr-x-lo");
    }
    
    public void getNodes() throws FileNotFoundException
    {
        Scanner nodeScanner = new Scanner(new File("resources/dist/nodes.txt"));
        while (nodeScanner.hasNextLine())
        {
            String[] args = nodeScanner.nextLine().split(" ");
            int i = Integer.parseInt(args[0]) - 1;
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            ls[i] = new Point(x, y);
            stations[i] = Integer.parseInt(args[3]);
            highlights[i] = 0;
        }
    }
    
    public void makeMenu()
    {
        menuB = new JButton(new ImageIcon(Resources.get("menu")));
        menu = new JPopupMenu();
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        quit = new JMenuItem("Quit");
        menu.add(save);
        menu.add(load);
        menu.add(quit);
        this.add(menuB);
        Dimension size = menuB.getPreferredSize();
        menuB.setBounds(5, 5, size.width - 3, size.height);
        menuB.setActionCommand("menu");
        menuB.addActionListener(this);
    }
    
    public void updateTargets(boolean[] ts)
    {
        for (int i=0; i<ts.length; i++) highlights[i] = ts[i] ? 1 : 0;
    }
    
    public void setxTurn(boolean xTurn)
    {
        this.xTurn = xTurn;
        repaint();
    }
    
    public void play(Colour c, int target)
    {
        play(c, target, false);
    }
    
    public void play(Colour c, int target, boolean xReveal)
    {
        if (c == black)            locX = target;
        if (c != black || xReveal) locMap.put(c,target);
        repaint();
    }
    
    public void updateTransforms() throws NoninvertibleTransformException
    {
        sc = new AffineTransform();
        sc.scale(z, z);
        isc = sc.createInverse();
        mv = new AffineTransform();
        mv.translate(o.x, o.y);
        imv = mv.createInverse();
        sc_mv = new AffineTransform();
        sc_mv.concatenate(mv);
        sc_mv.concatenate(sc);
        imv_isc = new AffineTransform();
        imv_isc.concatenate(isc);
        imv_isc.concatenate(imv);
    }
    
    public AffineTransform sc_mv_o(Point centre, int oX, int oY)
    {
        AffineTransform out = new AffineTransform();
        out.concatenate(sc_mv);
        out.translate(oX, oY);
        out.translate(centre.x, centre.y);
        return out;
    }
    
    public void updateLimits()
    {
        w = new Point(getWidth(), getHeight());
        MIN_Z = Math.max((double)w.x / (double)s.x, (double)w.y / (double)s.y);
        MIN_Z = Math.ceil(MIN_Z * 10) / 10;
        MAX_Z = 1.0;
        MIN_X = w.x - Math.round(s.x * (float)z);
        MAX_X = 0;
        MIN_Y = w.y - Math.round(s.y * (float)z);
        MAX_Y = 0;
    }
    
    public void enforceLimits()
    {
        if (z < MIN_Z) z = MIN_Z;
        if (z > MAX_Z) z = MAX_Z;
        if (o.x < MIN_X) o.x = MIN_X;
        if (o.x > MAX_X) o.x = MAX_X;
        if (o.y < MIN_Y) o.y = MIN_Y;
        if (o.y > MAX_Y) o.y = MAX_Y;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(textRenderHints);
        g2d.setRenderingHints(imageRenderHints);
        g2d.setRenderingHints(renderHints);
        
        this.updateLimits();
        this.enforceLimits();
        try { updateTransforms(); } catch (Exception e) { }
        
        g2d.drawImage(bg, sc_mv, this);
        for (int i=0; i<199; i++)
        {
            g2d.drawImage(imgH[highlights[i]], sc_mv_o(ls[i], -100, -100), this);
            g2d.drawImage(imgS[stations[i]], sc_mv_o(ls[i], -50, -40), this);
        }
        for (Colour c : locMap.keySet())
        {
            int loc = locMap.get(c);
            if (loc != 0) g2d.drawImage(imgP.get(c),sc_mv_o(ls[loc-1],-50,-150),this);
        }
        if (xTurn) g2d.drawImage(imgX, sc_mv_o(ls[locX-1], -50, -150), this);
        g2d.dispose();
        super.paintComponent(g);
    }
    
    public void makeTicketMenu(int l, boolean x, int[] tickets, boolean[] usable)
    {
        ticketM = new JPopupMenu();
        ticketP = new TicketPanel(l, x, tickets, usable);
        ticketM.add(ticketP);
    }
    
    public void showTicketMenu()
    {
        int i = ticketP.location;
        Point p = new Point(0, 0);
        sc_mv_o(ls[i], 40, -40).transform(p, p);
        ticketM.show(this, p.x, p.y - ticketP.h);
    }
    
    public void hideTicketMenu()
    {
        ticketM.setVisible(false);
        repaint();
    }
    
    public void updateMouse(MouseEvent e)
    {
        imv_isc.transform(e.getPoint(), m);
    }
    
    public boolean mouseOver(int i)
    {
        boolean inXrange = (ls[i].x - 40 < m.x) && (m.x < ls[i].x + 40);
        boolean inYrange = (ls[i].y - 40 < m.y) && (m.y < ls[i].y + 40);
        return inXrange && inYrange;
    }
    
    public void mouseClicked(MouseEvent e) { }
    public void mouseDragged(MouseEvent e)
    {
        if (dragging) return;
        isc.transform(e.getPoint(), m);
        o.x = o0.x + (m.x - m0.x);
        o.y = o0.y + (m.y - m0.y);
        this.repaint();
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseMoved(MouseEvent e)
    {
        updateMouse(e);
        for (int i=0; i<199; i++)
        {
            if (highlights[i] != 0)
            {
                if (mouseOver(i)) highlights[i] = 2;
                else              highlights[i] = 1;
                repaint();
            }
        }
    }
    public void mousePressed(MouseEvent e)
    {
        isc.transform(e.getPoint(), m);
        o0 = new Point(o);
        m0 = new Point(m);
    }
    public void mouseReleased(MouseEvent e) { }
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (zooming) return;
        updateMouse(e);
        float dz = -0.1f * (float)e.getPreciseWheelRotation();
        double z_ = z + dz;
        if (MIN_Z <= z_ && z_ <= MAX_Z)
        {
            z = z_;
            o.x -= Math.round(m.x * dz);
            o.y -= Math.round(m.y * dz);
        }
        this.repaint();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("menu")) menu.show(menuB, 0, menuB.getBounds().height);
    }
}
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
    private ScotlandYardControl control;
    
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    
    private static BufferedImage bg;
    private static BufferedImage[] imgS = new BufferedImage[3];
    private static BufferedImage[] imgH = new BufferedImage[3];
    private Map<Colour,BufferedImage> imgP;
    
    private Point[] ls;
    private int[] stations;
    private int[] highlights;
    private Map<Colour,Integer> locMap;
    
    private AffineTransform mv;
    private AffineTransform imv;
    private AffineTransform sc;
    private AffineTransform isc;
    private AffineTransform sc_mv;
    private AffineTransform imv_isc;
    
    private double MIN_Z;
    private double MAX_Z;
    private int MIN_X;
    private int MAX_X;
    private int MIN_Y;
    private int MAX_Y;
    
    private double z;   // zoom level
    private Point s;    // image size vector
    private Point w;    // window size vector
    private Point o0;   // origin at start of drag
    private Point m0;   // mouse position at start of drag
    private Point o;    // latest origin
    private Point m;    // latest mouse position
    
    private boolean dragging = false;
    private boolean zooming = false;
    
    private JButton menuB;
    private JPopupMenu menu;
    private JMenuItem save;
    private JMenuItem load;
    private JMenuItem quit;
    
    private TicketPanel ticketP;
    private JPopupMenu ticketM;
    
    // Initialise SYC from list of colours, for new game.
    public ScotlandYardDisplay(java.util.List<Colour> colours)
    {
        control = new ScotlandYardControl(colours);
        init(control);
    }
    
    // Initialise SYC from save file.
    public ScotlandYardDisplay(File saveFile)
    {
        control = new ScotlandYardControl(saveFile);
        init(control);
    }
    
    public void init(ScotlandYardControl control)
    {
        this.control = control;
        
        this.setPreferredSize(new Dimension(1000,600));
        this.setOpaque(false);
        this.setLayout(null);
        
        z = 0.5;
        o = new Point();
        m = new Point();
        this.updateTransforms();
        
        ls = new Point[199];
        stations = new int[199];
        highlights = new int[199];
        this.locMap = control.getLocMap();
        
        getImages();
        try { getNodes(); }
        catch(FileNotFoundException e) { System.err.println("nodes not found."); }
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        
        menuB = new JButton(new ImageIcon(Resources.get("menu")));
        menu = new JPopupMenu();
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        quit = new JMenuItem("Quit");
        menu.add(save);
        menu.add(load);
        menu.add(quit);
        this.add(menuB);
        Insets insets = this.getInsets();
        Dimension size = menuB.getPreferredSize();
        menuB.setBounds(5, 5, size.width - 3, size.height);
        menuB.setActionCommand("menu");
        menuB.addActionListener(this);
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
        imgP.put(Colour.Black,  Resources.get("mr-x-lo"));
        imgP.put(Colour.Blue,   Resources.get("det-b-lo"));
        imgP.put(Colour.Green,  Resources.get("det-g-lo"));
        imgP.put(Colour.Red,    Resources.get("det-r-lo"));
        imgP.put(Colour.White,  Resources.get("det-w-lo"));
        imgP.put(Colour.Yellow, Resources.get("det-y-lo"));
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
    
    public void updateTransforms()
    {
        sc = new AffineTransform();
        sc.scale(z, z);
        mv = new AffineTransform();
        mv.translate(o.x, o.y);
        try
        {
            isc = sc.createInverse();
            imv = mv.createInverse();
        }
        catch(NoninvertibleTransformException e) { }
        
        sc_mv = new AffineTransform();
        sc_mv.concatenate(mv);
        sc_mv.concatenate(sc);
        imv_isc = new AffineTransform();
        imv_isc.concatenate(isc);
        imv_isc.concatenate(imv);
    }
    
    public AffineTransform sc_mv_o(Point centre, Point offset)
    {
        AffineTransform out = new AffineTransform();
        out.concatenate(sc_mv);
        out.translate(offset.x, offset.y);
        out.translate(centre.x, centre.y);
        return out;
    }
    
    public void updateLimits()
    {
        w = new Point(getWidth(), getHeight());
        MIN_Z = Math.max((double)w.x / (double)s.x, (double)w.y / (double)s.y);
        MIN_Z = Math.ceil(MIN_Z * 10 + 1) / 10;
        MAX_Z = 0.8;
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
        this.updateLimits();
        this.enforceLimits();
        this.updateTransforms();
        g2d.setRenderingHints(textRenderHints);
        g2d.setRenderingHints(imageRenderHints);
        g2d.setRenderingHints(renderHints);
        g2d.drawImage(bg, sc_mv, this);
        for (int i=0; i<199; i++)
        {
            Point highlight_o = new Point(-100, -100);
            g2d.drawImage(imgH[highlights[i]], sc_mv_o(ls[i], highlight_o), this);
            Point station_o = new Point(-50, -40);
            g2d.drawImage(imgS[stations[i]], sc_mv_o(ls[i], station_o), this);
        }
        for (Colour c : locMap.keySet())
        {
            Point piece_o = new Point(-50, -150);
            g2d.drawImage(imgP.get(c), sc_mv_o(ls[locMap.get(c)], piece_o), this);
        }
        g2d.dispose();
        super.paintComponent(g);
    }
    
    public boolean mouseOver(int i)
    {
        boolean inXrange = (ls[i].x - 40 < m.x) && (m.x < ls[i].x + 40);
        boolean inYrange = (ls[i].y - 40 < m.y) && (m.y < ls[i].y + 40);
        return inXrange && inYrange;
    }
    
    public void mouseClicked(MouseEvent e)
    {
        imv_isc.transform(e.getPoint(), m);
        for (int i=0; i<199; i++)
        {
            if (mouseOver(i))
            {
                ticketM = new JPopupMenu();
                int[] t0 = {11, 8, 5, 1, 1};
                boolean[] m0 = {true, true, true, true, true};
                ticketP = new TicketPanel(0, false, t0, m0);
                ticketM.add(ticketP);
                Point p = new Point(ls[i]);
                sc_mv.transform(p, p);
                System.err.println("menu at: " + p.x + " " + p.y);
                Rectangle bounds = ticketM.getBounds();
                ticketM.show(this, p.x, p.y - bounds.height);
            }
        }
    }
    public void mouseDragged(MouseEvent e)
    {
        if (dragging) return;
        else dragging = true;
        isc.transform(e.getPoint(), m);
        o.x = o0.x + (m.x - m0.x);
        o.y = o0.y + (m.y - m0.y);
        this.repaint();
        dragging = false;
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseMoved(MouseEvent e)
    {
        imv_isc.transform(e.getPoint(), m);
        for (int i=0; i<199; i++)
        {
            /*if (mouseOver(i) && highlights[i] == 1)
            {
                highlights[i] = 2;
                repaint();
            }
            else if (!mouseOver(i) && highlights[i] == 2)
            {
                highlights[i] = 1;
                repaint();
            }*/
            if (mouseOver(i))
            {
                highlights[i] = 1;
                repaint();
            }
            else if (highlights[i] == 1)
            {
                highlights[i] = 0;
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
        else zooming = true;
        imv_isc.transform(e.getPoint(), m);
        float dz = -0.1f * (float) e.getPreciseWheelRotation();
        z += dz;
        if (MIN_Z <= z && z <= MAX_Z)
        {
            o.x -= Math.round(m.x * dz);
            o.y -= Math.round(m.y * dz);
        }
        this.repaint();
        zooming = false;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "menu")
        {
            Rectangle bounds = menuB.getBounds();
            menu.show(menuB, 0, bounds.height);
        }
    }
}
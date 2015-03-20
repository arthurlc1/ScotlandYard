package solution;

import scotlandyard.*;

import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * TODO:
 *      - Fix zoom such that the origin of the transform is always the mouse.
 */

public class GameDisplayPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    
    private static BufferedImage bg;
    private static BufferedImage[] imgS = new BufferedImage[3];
    private static BufferedImage[] imgH = new BufferedImage[3];
    
    private Point[] ls;
    private int[] stations;
    private int[] highlights;
    
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
    
    private double z;
    
    private Point s;    // image size vector
    private Point w;    // window size vector
    
    private Point o0;   // origin at start of drag
    private Point m0;   // mouse position at start of drag
    
    private Point o;    // latest origin
    private Point m;    // latest mouse position
    
    private boolean dragging;
    private boolean zooming;
    
    public GameDisplayPanel()
    {
        z = 1.0;
        o = new Point();
        m = new Point();
        this.updateTransforms();
        
        this.getImages();
        
        ls = new Point[199];
        stations = new int[199];
        highlights = new int[199];
        
        try { this.getNodes(); }
        catch(FileNotFoundException e) { }
        
        dragging = false;
        
        this.setBackground(Color.RED);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }
    
    public void getImages()
    {
        try
        {
            bg = ImageIO.read(new File("resources/dist/background.png"));
            imgS[0] = ImageIO.read(new File("resources/dist/s-taxi.png"));
            imgS[1] = ImageIO.read(new File("resources/dist/s-bus.png"));
            imgS[2] = ImageIO.read(new File("resources/dist/s-tube.png"));
            imgH[0] = null;
            imgH[1] = ImageIO.read(new File("resources/dist/h-1.png"));
            imgH[2] = ImageIO.read(new File("resources/dist/h-2.png"));
        }
        catch (IOException e) { System.err.println("image(s) not found."); }
        s = new Point(bg.getWidth(), bg.getHeight());
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
            stations[i] = 0;
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
        sc_mv.concatenate(sc);
        sc_mv.concatenate(mv);
        imv_isc = new AffineTransform();
        imv_isc.concatenate(imv);
        imv_isc.concatenate(isc);
        
    }
    
    public void updateLimits()
    {
        w = new Point(getWidth(), getHeight());
        MIN_Z = Math.max((double)w.x / (double)s.x, (double)w.y / (double)s.y);
        MIN_Z = Math.ceil(MIN_Z * 10) / 10;
        MAX_Z = 1.2;
        MIN_X = Math.round(w.x / (float)z) - s.x;
        MAX_X = 0;
        MIN_Y = Math.round(w.y / (float)z) - s.y;
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
        g2d.dispose();
    }
    
    public void mouseClicked(MouseEvent e) { }
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
    public void mouseMoved(MouseEvent e) { }
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
        if (MIN_Z < z && z < MAX_Z)
        {
            o.x -= Math.round(m.x * dz);
            o.y -= Math.round(m.y * dz);
        }
        this.repaint();
        zooming = false;
    }
}
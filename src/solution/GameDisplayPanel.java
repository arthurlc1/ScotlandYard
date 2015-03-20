package solution;

import scotlandyard.*;

import java.io.*;

import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

public class GameDisplayPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    
    private BufferedImage bg;
    private AffineTransform at;
    private AffineTransform it;
    
    private double MIN_Z;
    private double MAX_Z;
    private int MIN_X;
    private int MAX_X;
    private int MIN_Y;
    private int MAX_Y;
    
    private double z;
    private Point o;    // image *Origin
    private Point s;    // image *Size vector
    private Point w;    // *Window size vector
    private Point d;    // start of *Drag vector
    private Point m;    // latest *Mouse position
    
    private boolean dragging;
    private boolean zooming;
    
    public GameDisplayPanel()
    {
        z = 1.0;
        o = new Point();
        m = new Point();
        
        this.updateTransforms();
        
        try { bg = ImageIO.read(new File("resources/dist/background.png")); }
        catch (IOException e) { System.err.println("background.png not found."); }
        s = new Point(bg.getWidth(), bg.getHeight());
        
        dragging = false;
        
        if (ConsoleGame.testing) this.setBackground(Color.RED);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }
    
    public void updateTransforms()
    {
        at = new AffineTransform();
        at.scale(z, z);
        at.translate(o.x, o.y);
        try { it = at.createInverse(); }
        catch(NoninvertibleTransformException e) { }
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
        //super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        this.updateLimits();
        this.enforceLimits();
        this.updateTransforms();
        g2d.setRenderingHints(textRenderHints);
        g2d.setRenderingHints(imageRenderHints);
        g2d.setRenderingHints(renderHints);
        g2d.drawImage(bg, at, this);
        g2d.dispose();
    }
    
    public void mouseClicked(MouseEvent e) { }
    public void mouseDragged(MouseEvent e)
    {
        if (dragging) return;
        else dragging = true;
        it.transform(e.getPoint(), m);
        o.translate(m.x - d.x, m.y - d.y);
        d = new Point(m);
        this.repaint();
        dragging = false;
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mousePressed(MouseEvent e)
    {
        it.transform(e.getPoint(), m);
        d = new Point(m);
    }
    public void mouseReleased(MouseEvent e) { }
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (zooming) return;
        else zooming = true;
        it.transform(e.getPoint(), m);
        float dz = -0.1f * (float) e.getPreciseWheelRotation();
        int dx = Math.round(-dz * m.x);
        int dy = Math.round(-dz * m.y);
        z += dz;
        if (MIN_Z < z && z < MAX_Z) o.translate(dx, dy);
        this.repaint();
        zooming = false;
    }
}
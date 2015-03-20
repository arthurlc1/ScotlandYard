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
    private BufferedImage bg;
    
    private double z;
    private int x;
    private int y;
    private int sX;
    private int sY;
    public GameDisplayPanel()
    {
        try { bg = ImageIO.read(new File("resources/dist/background.png")); }
        catch (IOException e) { }
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        at.scale(z, z);
        at.translate(x, y);
        g2d.drawImage(bg, at, this);
        g2d.dispose();
    }
    
    public void mouseClicked(MouseEvent e) { }
    public void mouseDragged(MouseEvent e)
    {
        x += e.getX() - sX;
        y += e.getY() - sY;
        this.repaint();
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mousePressed(MouseEvent e)
    {
        sX = e.getX();
        sY = e.getY();
    }
    public void mouseReleased(MouseEvent e) { }
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        z += 0.05 * e.getPreciseWheelRotation();
        x -= e.getX();
        y -= e.getY();
        this.repaint();
    }
}
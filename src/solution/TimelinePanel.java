package solution;

import scotlandyard.*;

import java.util.*;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.geom.*;

public class TimelinePanel extends JPanel
{
    public static Dimension imgSize;
    
    private static BufferedImage timeline;
    private static BufferedImage[] tickets = new BufferedImage[4];
    
    private java.util.List<BufferedImage> moves;
    
    public TimelinePanel()
    {
        timeline = Resources.get("timeline");
        imgSize = new Dimension(timeline.getWidth(), timeline.getHeight());
        
        tickets[0] = Resources.get("t-taxi-lo");
        tickets[1] = Resources.get("t-bus-lo");
        tickets[2] = Resources.get("t-tube-lo");
        tickets[3] = Resources.get("t-secret-lo");
        
        moves = new ArrayList<BufferedImage>();
        
        this.setOpaque(false);
        this.setPreferredSize(imgSize);
        this.setSize(imgSize);
    }
    
    public void add(int i)
    {
        moves.add(tickets[i]);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        g2d.drawImage(timeline, at, this);
        
        at.translate(12.5, 2.5);
        for (BufferedImage img : moves)
        {
            g2d.drawImage(img, at, this);
            at.translate(30, 0);
        }
    }
}
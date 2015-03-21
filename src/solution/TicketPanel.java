package solution;

import scotlandyard.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class TicketPanel extends JPanel
{
    public class RoundBorder extends AbstractBorder
    {
        private Color colour;
        
        public RoundBorder(Color colour)
        {
            this.colour = colour;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
        {
            super.paintBorder(c, g, x, y, w, h);
            if (g instanceof Graphics2D)
            {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(colour);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x, y, w, h, 10, 10);
            }
        }
        
        @Override
        public Insets getBorderInsets(Component c)
        {
            return (getBorderInsets(c, new Insets(0, 0, 0, 0)));
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets)
        {
            insets.left += 10;
            insets.top += 10;
            insets.right += 10;
            insets.bottom += 10;
            return insets;
        }
        
        @Override
        public boolean isBorderOpaque()
        {
            return true;
        }
    }
    
    public final int location;
    
    private final int[] tickets;
    
    private JLabel[] img = new JLabel[5];
    private JLabel[] num = new JLabel[5];
    private JButton[] play = new JButton[5];
    
    public TicketPanel(int l, boolean x, int[] t, boolean[] m)
    {
        location = l;
        
        int n = x ? 3 : 5;
        
        tickets = new int[n];
        System.arraycopy(t, 0, tickets, 0, tickets.length);
        
        img = new JLabel[n];
        num = new JLabel[n];
        play = new JButton[n];
        
        this.setBorder(new RoundBorder(Color.BLACK));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(2, 5, 2, 5);
        
        String[] f = {"taxi","bus","tube","secret","double"};
        for (int i=0; i<n; i++)
        {
            String ext = m[i] ? "-lo" : "-d";
            img[i] = new JLabel(new ImageIcon(Resources.get("t-"+f[i]+ext)));
            gbc.gridy = i;
            gbc.gridx = 0;
            this.add(img[i], gbc);
            
            num[i] = new JLabel("x" + t[i]);
            if (!m[i]) num[i].setForeground(Color.GRAY);
            gbc.gridx = 1;
            this.add(num[i], gbc);
            
            play[i] = new JButton("Play");
            if (!m[i]) play[i].setEnabled(false);
            gbc.gridx = 2;
            this.add(play[i], gbc);
        }
    }
    
    private void disable(int i)
    {
        int width = img[i].getWidth();
        int height = img[i].getHeight();
        
        
        
        for (int w=0; w<width; w++)
        {
            
        }
    }
}
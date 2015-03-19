package solution;

import scotlandyard.*;

import java.util.Map;

import javax.swing.*;
import java.awt.*;

public class TicketPanel extends JPanel
{
    private JLabel[] img = {null,null,null,null,null};
    private JLabel[] num = {null,null,null,null,null};
    
    public TicketPanel()
    {
        this(false);
    }
    
    public TicketPanel(boolean big)
    {
        String[] t = {"taxi","bus","tube","double","secret"};
        String lo = big ? "" : "-lo";
        for (int i=0; i<5; i++)
        {
            img[i] = new JLabel(new ImageIcon("resources/dist/t-"+t[i]+lo+".png"));
            num[i] = new JLabel("x0");
            num[i].setFont(new Font("SansSerif", Font.BOLD, 16));
        }
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(2, 10, 2, 10);
        
        for (int i=0; i<5; i++)
        {
            gbc.gridy = i;
            gbc.gridx = 0;
            this.add(img[i], gbc);
            gbc.gridx = 1;
            this.add(num[i], gbc);
        }
    }
}
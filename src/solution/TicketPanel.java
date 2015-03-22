package solution;

import scotlandyard.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class TicketPanel extends JPanel implements ActionListener
{
    public final int location;
    public final int w;
    public final int h;
    
    private static String[] f = {"taxi","bus","tube","secret","double"};
    private final int[] tickets;
    
    private JLabel[] img = new JLabel[5];
    private JLabel[] num = new JLabel[5];
    private JButton[] play = new JButton[5];
    
    public TicketPanel(int l, boolean x, int[] t, boolean[] e, ActionListener al)
    {
        location = l;
        w = x ? 155 : 95;
        h = x ? 155 : 95;
        int n = x ? 5 : 3;
        
        tickets = new int[n];
        System.arraycopy(t, 0, tickets, 0, tickets.length);
        
        img = new JLabel[n];
        num = new JLabel[n];
        play = new JButton[n];
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(2, 5, 2, 5);
        
        for (int i=0; i<n; i++)
        {
            String ext = e[i] ? "-lo" : "-d";
            img[i] = new JLabel(new ImageIcon(Resources.get("t-"+f[i]+ext)));
            gbc.gridy = i;
            gbc.gridx = 0;
            this.add(img[i], gbc);
            
            num[i] = new JLabel("x" + t[i]);
            if (!e[i]) num[i].setForeground(Color.GRAY);
            gbc.gridx = 1;
            this.add(num[i], gbc);
            
            play[i] = new JButton("Play");
            if (!e[i]) play[i].setEnabled(false);
            else
            {
                play[i].setActionCommand(location + "-" + f[i]);
                play[i].addActionListener(al);
                play[i].addActionListener(this);
            }
            gbc.gridx = 2;
            this.add(play[i], gbc);
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String name = e.getActionCommand().split("-")[1];
        for (int i=0; i<tickets.length; i++)
        {
            if (f[i].equals(name))
            {
                int newNum = Integer.parseInt(num[i].getText().split("x")[1]);
                num[i].setText("x" + --newNum);
                if (i == 4 || newNum == 0)
                {
                    img[i].setIcon(new ImageIcon(Resources.get("t-"+f[i]+"-d")));
                    num[i].setForeground(Color.GRAY);
                    play[i].setEnabled(false);
                }
                repaint();
            }
        }
    }
}
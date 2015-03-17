package solution;

import scotlandyard.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MenuPanel extends JPanel
{
    JPanel buttons;
    JButton newB;
    JButton loadB;
    JButton quitB;
    
    public MenuPanel()
    {
        buttons = new JPanel();
        newB = new JButton("New Game");
        loadB = new JButton("Load Game");
        quitB = new JButton("Quit");
        
        buttons.setLayout(new GridLayout(3, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttons.setPreferredSize(new Dimension(200, 150));
        
        buttons.add(newB);
        buttons.add(loadB);
        buttons.add(quitB);
        
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(220, 190));
        this.add(buttons, new GridBagConstraints());
    }
}
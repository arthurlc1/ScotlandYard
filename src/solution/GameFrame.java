package solution;

import scotlandyard.*;

import java.util.*;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    public GameFrame()
    {
        this.setTitle("Scotland Yard");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setPreferredSize(new Dimension(800, 600));
        
        if (ConsoleGame.testing) this.setScreen(new ScotlandYardDisplay(Arrays.asList(Colour.class.getEnumConstants())));
        else this.setScreen(new MainMenuPanel());
        
        this.setVisible(true);
    }
    
    public void setScreen(JPanel p)
    {
        this.setMinimumSize(p.getPreferredSize());
        this.setPreferredSize(p.getPreferredSize());
        this.getContentPane().removeAll();
        this.getContentPane().add(p);
        this.pack();
    }
}
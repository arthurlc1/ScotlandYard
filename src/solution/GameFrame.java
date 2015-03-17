package solution;

import scotlandyard.*;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    MenuPanel menu;
    GamePanel game;
    
    public GameFrame()
    {
        menu = new MenuPanel();
        
        this.setTitle("Scotland Yard");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setPreferredSize(new Dimension(800, 600));
        this.setMinimumSize(menu.getPreferredSize());
        
        this.getContentPane().add(menu);
        pack();
    }
}
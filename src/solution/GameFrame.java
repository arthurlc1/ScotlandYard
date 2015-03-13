package solution;

import scotlandyard.*;

import javax.swing.*;

public class GameFrame extends JFrame
{
    GamePanel game;
    JScrollPane view;
    MenuPanel menu;
    
    public GameFrame()
    {
        game = new GamePanel();
        view = new JScrollPane();
        menu = new MenuPanel();
        
        view.add(game);
        
        this.add(menu);
        pack();
        
        setTitle("Scotland Yard");
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
    }
}
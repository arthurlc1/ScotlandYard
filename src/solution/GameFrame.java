package solution;

import scotlandyard.*;

import com.kitfox.svg.*;

import javax.swing.*;

public class GameFrame extends JFrame
{
    GamePanel game;
    JScrollPane view;
    MenuPanel menu;
    
    SVGUniverse universe;
    
    public GameFrame()
    {
        game = new GamePanel();
        view = new JScrollPane();
        menu = new MenuPanel();
        
        universe = SVGCache.getSVGUniverse();
        
        this.setTitle("Scotland Yard");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize(800, 600);
        
        game.setPreferredSize(getSize());
        
        view.add(game);
        this.add(menu);
        pack();
    }
}
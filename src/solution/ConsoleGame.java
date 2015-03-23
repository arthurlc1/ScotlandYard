package solution;

import scotlandyard.*;

import javax.swing.*;

public class ConsoleGame
{
    public static void main(String[] args)
    {
        Resources resources = new Resources();
        new Thread(resources::load).start();
        
        ConsoleGame game = new ConsoleGame();
        SwingUtilities.invokeLater(game::run);
   
        
    
    }
    
    public void run()
    {
        GameFrame w = new GameFrame();
    }
}
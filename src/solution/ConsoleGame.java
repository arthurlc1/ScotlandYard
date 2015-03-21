package solution;

import scotlandyard.*;

import javax.swing.*;

public class ConsoleGame
{
    public static void main(String[] args)
    {
        ConsoleGame game = new ConsoleGame();
        SwingUtilities.invokeLater(game::run);
        
        new Thread(Resources::load).start();
    }
    
    public void run()
    {
        GameFrame w = new GameFrame();
    }
}
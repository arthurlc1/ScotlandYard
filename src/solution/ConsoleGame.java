package solution;

import scotlandyard.*;

import javax.swing.*;

public class ConsoleGame
{
    public static boolean testing = true;
    
    public static void main(String[] args)
    {
        ConsoleGame game = new ConsoleGame();
        SwingUtilities.invokeLater(game::run);
    }
    
    public void run()
    {
        GameFrame w = new GameFrame();
    }
}
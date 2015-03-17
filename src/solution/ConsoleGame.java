package solution;

import scotlandyard.*;

import javax.swing.*;

public class ConsoleGame
{
    public static void main(String[] args)
    {
        ConsoleGame game = new ConsoleGame();
        SwingUtilities.invokeLater(game::run);
    }
    
    public void run()
    {
        new GameFrame().setVisible(true);
    }
}
package solution;

import scotlandyard.*;

import javax.swing.*;

public class ConsoleGame
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater((new ConsoleGame())::run);
    }
    
    private void run()
    {
        new GameFrame().setVisible(true);
    }
}
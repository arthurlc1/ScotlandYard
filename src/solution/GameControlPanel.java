package solution;

import scotlandyard.*;

import javax.swing.*;

public class GameControlPanel extends JPanel
{
    private JLabel toMove;
    private JPanel tickets;
    private JPanel move;
    private JButton play;
    
    public GameControlPanel()
    {
        toMove = new JLabel("Mr. X to move:");
        
        this.add(toMove);
    }
}
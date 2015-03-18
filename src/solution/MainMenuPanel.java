package solution;

import scotlandyard.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel implements ActionListener
{
    JPanel buttons;
    JButton newB;
    JButton loadB;
    JButton quitB;
    
    public MainMenuPanel()
    {
        buttons = new JPanel();
        newB = new JButton("New Game");
        newB.setActionCommand("new");
        newB.addActionListener(this);
        loadB = new JButton("Load Game");
        loadB.setActionCommand("load");
        loadB.addActionListener(this);
        quitB = new JButton("Quit");
        quitB.setActionCommand("quit");
        quitB.addActionListener(this);
        
        buttons.setLayout(new GridLayout(3, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttons.setPreferredSize(new Dimension(200, 150));
        
        buttons.add(newB);
        buttons.add(loadB);
        buttons.add(quitB);
        
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(220, 190));
        this.add(buttons, new GridBagConstraints());
    }
    
    public void newGame()
    {
        GameFrame w = (GameFrame) SwingUtilities.getWindowAncestor(this);
        w.setScreen(new NewGamePanel());
    }
    
    public void loadGame()
    {
        
    }
    
    public void quit()
    {
        System.exit(0);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "new":
                newGame();
                break;
            case "load":
                loadGame();
                break;
            case "quit":
                quit();
                break;
            default:
        }
    }
}
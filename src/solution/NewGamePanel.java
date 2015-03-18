package solution;

import scotlandyard.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class NewGamePanel extends JPanel implements ActionListener
{
    JButton back;
    JButton start;
    PlayerSetupPanel[] players;
    
    public NewGamePanel()
    {
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(this);
        start = new JButton("Start Game");
        start.setActionCommand("start");
        start.addActionListener(this);
        
        players = new PlayerSetupPanel[6];
        for (int i=0; i<6; i++) players[i] = new PlayerSetupPanel(i);
        
        this.setLayout(new GridBagLayout());
        
        // Set out 'back' button.
        GridBagConstraints cBack = new GridBagConstraints();
        cBack.gridx = 0;
        cBack.gridy = 0;
        cBack.weightx = 0.0;
        cBack.weighty = 0.5;
        cBack.anchor = GridBagConstraints.LAST_LINE_START;
        cBack.insets = new Insets(10, 10, 10, 10);
        this.add(back, cBack);
        
        // Set out player setup panels.
        GridBagConstraints cPlayers = new GridBagConstraints();
        cPlayers.gridy = 1;
        cPlayers.weightx = 0.0;
        cPlayers.weighty = 0.0;
        cPlayers.insets = new Insets(10, 10, 10, 10);
        for (int i=0; i<6; i++)
        {
            cPlayers.gridx = i;
            this.add(players[i], cPlayers);
        }
        
        // Set out 'start' button.
        GridBagConstraints cStart = new GridBagConstraints();
        cStart.gridx = 2;
        cStart.gridy = 2;
        cStart.gridwidth = 2;
        cStart.weightx = 0.0;
        cStart.weighty = 0.5;
        cStart.anchor = GridBagConstraints.PAGE_START;
        cStart.insets = new Insets(10, 10, 10, 10);
        this.add(start, cStart);
        
        this.setPreferredSize(new Dimension(800, 375));
    }
    
    public void back()
    {
        GameFrame w = (GameFrame) SwingUtilities.getWindowAncestor(this);
        w.setScreen(new MainMenuPanel());
    }
    
    public void startGame()
    {
        GameFrame w = (GameFrame) SwingUtilities.getWindowAncestor(this);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "back":
                back();
                break;
            case "start":
                startGame();
                break;
            default:
        }
    }
}
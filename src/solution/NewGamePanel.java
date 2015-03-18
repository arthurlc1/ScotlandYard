package solution;

import scotlandyard.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class NewGamePanel extends JPanel implements ActionListener
{
    public enum PlayerType
    {
        Player, Computer, Off
    }
    
    public class PlayerSetupPanel extends JPanel
    {
        String name;
        Colour colour;
        PlayerType pt;
        
        ImageIcon image;
        JComboBox<String> player;
        
        public PlayerSetupPanel(int i)
        {
            switch (i)
            {
                case 0:
                    colour = Colour.Black;
                    name = "Mr. X";
                    image = new ImageIcon("resources/dist/mr-x-lo.png");
                    break;
                case 1:
                    colour = Colour.Blue;
                    name = "Detective 1";
                    image = new ImageIcon("resources/dist/det-b-lo.png");
                    break;
                case 2:
                    colour = Colour.Green;
                    name = "Detective 2";
                    image = new ImageIcon("resources/dist/det-g-lo.png");
                    break;
                case 3:
                    colour = Colour.Red;
                    name = "Detective 3";
                    image = new ImageIcon("resources/dist/det-r-lo.png");
                    break;
                case 4:
                    colour = Colour.White;
                    name = "Detective 4";
                    image = new ImageIcon("resources/dist/det-w-lo.png");
                    break;
                case 5:
                    colour = Colour.Yellow;
                    name = "Detective 5";
                    image = new ImageIcon("resources/dist/det-y-lo.png");
                    break;
            }
            
            pt = PlayerType.Player;
            String[] options = {"Player", "Computer", "Off"};
            player = new JComboBox<String>(options);
            
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 0);
            this.add(new JLabel(name), gbc);
            
            gbc.gridy = 1;
            gbc.insets = new Insets(10, 0, 5, 0);
            this.add(new JLabel(image), gbc);
            
            gbc.gridy = 2;
            gbc.insets = new Insets(0, 0, 0, 0);
            this.add(player, gbc);
        }
    }
    
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
        
        GridBagConstraints cBack = new GridBagConstraints();
        cBack.gridx = 0;
        cBack.gridy = 0;
        cBack.weightx = 0.0;
        cBack.weighty = 0.5;
        cBack.anchor = GridBagConstraints.LAST_LINE_START;
        cBack.insets = new Insets(10, 10, 10, 10);
        this.add(back, cBack);
        
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
            case "quit":
                break;
            default:
        }
    }
}
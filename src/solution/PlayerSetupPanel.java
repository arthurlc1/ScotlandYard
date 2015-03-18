package solution;

import scotlandyard.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerSetupPanel extends JPanel implements ActionListener
{
    public enum PlayerType
    {
        Player, Computer, Off
    }
    
    String name;
    String file;
    Colour colour;
    PlayerType pt;
    
    ImageIcon imgIcon;
    ImageIcon nullIcon;
    JLabel imgLabel;
    JComboBox<String> ptBox;
    
    public PlayerSetupPanel(int i)
    {
        nullIcon = new ImageIcon("resources/dist/null-lo.png");
        switch (i)
        {
            case 0:
                colour = Colour.Black;
                name = "Mr. X";
                imgIcon = new ImageIcon("resources/dist/mr-x-lo.png");
                break;
            case 1:
                colour = Colour.Blue;
                name = "Detective 1";
                imgIcon = new ImageIcon("resources/dist/det-b-lo.png");
                break;
            case 2:
                colour = Colour.Green;
                name = "Detective 2";
                imgIcon = new ImageIcon("resources/dist/det-g-lo.png");
                break;
            case 3:
                colour = Colour.Red;
                name = "Detective 3";
                imgIcon = new ImageIcon("resources/dist/det-r-lo.png");
                break;
            case 4:
                colour = Colour.White;
                name = "Detective 4";
                imgIcon = new ImageIcon("resources/dist/det-w-lo.png");
                break;
            case 5:
                colour = Colour.Yellow;
                name = "Detective 5";
                imgIcon = new ImageIcon("resources/dist/det-y-lo.png");
                break;
        }
        
        pt = PlayerType.Player;
        String[] options;
        if (i == 0) options = new String[] {"Player", "Computer"};
        else        options = new String[] {"Player", "Computer", "Off"};
        ptBox = new JComboBox<String>(options);
        ptBox.addActionListener(this);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(new JLabel(name), gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 5, 0);
        imgLabel = new JLabel(imgIcon);
        this.add(imgLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(ptBox, gbc);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        switch (ptBox.getItemAt(ptBox.getSelectedIndex()))
        {
            case "Player":
                pt = PlayerType.Player;
                imgLabel.setIcon(imgIcon);
                break;
            case "Computer":
                pt = PlayerType.Computer;
                imgLabel.setIcon(imgIcon);
                break;
            default:
                pt = PlayerType.Off;
                imgLabel.setIcon(nullIcon);
        }
        imgLabel.revalidate();
        System.err.println(name + " player type changed to: " + ptBox.getSelectedIndex());
    }
    
    public PlayerType getPlayerType()
    {
        return pt;
    }
}
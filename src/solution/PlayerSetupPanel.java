package solution;

import scotlandyard.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerSetupPanel extends JPanel implements ActionListener
{
    public enum PlayerType
    {
        Player, Off
    }
    
    private Colour colour;
    private String name;
    
    private ImageIcon imgIcon;
    private static ImageIcon nullIcon = new ImageIcon("resources/dist/null-lo.png");
    private JLabel imgLabel;
    
    private PlayerType pt;
    private JComboBox<String> ptBox;
    
    public PlayerSetupPanel(int i)
    {
        colour = Colour.values()[i];
        if (i == 0)
        {
            name = "Mr. X";
            imgIcon = new ImageIcon("resources/dist/mr-x-lo.png");
        }
        else
        {
            name = "Detective " + i;
            String[] cs = {"b","g","r","w","y",};
            imgIcon = new ImageIcon("resources/dist/det-" + cs[i-1] + "-lo.png");
        }
        
        pt = PlayerType.Player;
        if (i == 0) ptBox = new JComboBox<String>(new String[] {"Player"});
        else        ptBox = new JComboBox<String>(new String[] {"Player", "Off"});
        ptBox.addActionListener(this);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Set out name label.
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel(name), gbc);
        
        // Set out image.
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 5, 0);
        imgLabel = new JLabel(imgIcon);
        this.add(imgLabel, gbc);
        
        // Set out combo box.
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(ptBox, gbc);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (ptBox.getItemAt(ptBox.getSelectedIndex()) == "Player")
        {
            pt = PlayerType.Player;
            imgLabel.setIcon(imgIcon);
        }
        else
        {
            pt = PlayerType.Off;
            imgLabel.setIcon(nullIcon);
        }
        imgLabel.revalidate();
    }
    
    public PlayerType getPlayerType()
    {
        return pt;
    }
    
    public Colour getColour()
    {
        return colour;
    }
}
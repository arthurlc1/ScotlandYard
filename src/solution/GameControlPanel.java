package solution;

import scotlandyard.*;

import javax.swing.*;
import java.awt.*;

public class GameControlPanel extends JPanel
{
    private JLabel toMove;
    private TicketPanel tickets;
    private JLabel toL;
    private JComboBox toCB;
    private JLabel byL;
    private JComboBox byCB;
    private JButton useDouble;
    private JButton play;
    private JButton save;
    private JButton quit;
    
    public GameControlPanel()
    {
        toMove = new JLabel("Mr. X:");
        tickets = new TicketPanel(true);
        toL = new JLabel("Move to:");
        toCB = new JComboBox<String>(new String[]{"      ","1","2","3","4","5"});
        byL = new JLabel("by:");
        byCB = new JComboBox<String>(new String[]{"      ","Taxi","Bus","Tube","Secret"});
        useDouble = new JButton("Double Move");
        play = new JButton("Play");
        save = new JButton("Save");
        quit = new JButton("Quit");
        
        toMove.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        if (ConsoleGame.testing) this.setBackground(Color.GREEN);
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        this.add(toMove, gbc);
        
        gbc.gridy = 1;
        this.add(tickets, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(toL, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(toCB, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(byL, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        this.add(byCB, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(useDouble, gbc);
        
        gbc.gridy = 5;
        this.add(play, gbc);
        
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.PAGE_END;
        this.add(save, gbc);
        
        gbc.gridx = 1;
        this.add(quit, gbc);
    }
}
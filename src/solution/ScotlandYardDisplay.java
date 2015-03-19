package solution;

import scotlandyard.*;

import java.util.*;
import java.io.*;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.*;

public class ScotlandYardDisplay extends JPanel
{
    private GameDisplayPanel game;
    private GameControlPanel ctrl;
    
    // Initialise SYC from list of colours, for new game.
    public ScotlandYardDisplay(List<Colour> colours)
    {
        ScotlandYardControl control = new ScotlandYardControl(colours);
        init(control);
    }
    
    // Initialise SYC from save file.
    public ScotlandYardDisplay(File saveFile)
    {
        ScotlandYardControl control = new ScotlandYardControl(saveFile);
        init(control);
    }
    
    // Initialise display components.
    public void init(ScotlandYardControl control)
    {
        game = new GameDisplayPanel();
        ctrl = new GameControlPanel();
        
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints cGame = new GridBagConstraints();
        cGame.gridx = 0;
        cGame.gridy = 0;
        cGame.weightx = 1.0;
        cGame.weighty = 0.5;
        cGame.insets = new Insets(5, 5, 5, 5);
        cGame.fill = GridBagConstraints.BOTH;
        if (ConsoleGame.testing) game.setBackground(Color.RED);
        this.add(game, cGame);
        
        GridBagConstraints cCtrl = new GridBagConstraints();
        cCtrl.gridx = 1;
        cCtrl.gridy = 0;
        cCtrl.weightx = 0.0;
        cCtrl.weighty = 0.5;
        cCtrl.insets = new Insets(5, 5, 5, 5);
        cCtrl.fill = GridBagConstraints.VERTICAL;
        this.add(ctrl, cCtrl);
        
        this.setPreferredSize(new Dimension(1000,600));
    }
}
package solution;

import scotlandyard.*;

import com.kitfox.svg.*;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class MenuPanel extends SVGDisplayPanel
{
    SVGUniverse uni;
    
    public MenuPanel()
    {
        uni = SVGCache.getSVGUniverse();
        
        URL menuURL = null;
        URI menuURI = null;
        
        try
        {
            menuURL = new File("resources/dist/menu.svg").toURI().toURL();
            menuURI = uni.loadSVG(menuURL);
        }
        catch (Exception e) { }
        
        this.setDiagram(uni.getDiagram(menuURI));
    }
}
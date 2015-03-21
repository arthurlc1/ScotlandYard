package solution;

import scotlandyard.*;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

import java.awt.image.*;
import javax.imageio.*;

public class Resources
{
    private static final String[] names = 
    {
        "mr-x-lo",
        "det-b-lo",
        "det-g-lo",
        "det-r-lo",
        "det-w-lo",
        "det-y-lo",
        "null-lo",
        "background",
        "s-taxi",
        "s-bus",
        "s-tube",
        "h-1",
        "h-2",
        "t-taxi-lo",
        "t-bus-lo",
        "t-tube-lo",
        "t-secret-lo",
        "t-double-lo",
        "t-taxi-d",
        "t-bus-d",
        "t-tube-d",
        "t-secret-d",
        "t-double-d"
    };
    private static final Map<String,BufferedImage> images = new HashMap<String,BufferedImage>();
    
    public static void load()
    {
        for (String s : names)
        {
            try
            {
                File f = new File("resources/dist/" + s + ".png");
                BufferedImage img = ImageIO.read(f);
                images.put(s, img);
            }
            catch (IOException e) { System.err.println(s + ".png not found."); }
        }
        System.err.println("Done!");
    }
    
    public static BufferedImage get(String s)
    {
        BufferedImage img;
        do img = images.get(s);
        while (img == null);
        return img;
    }
}
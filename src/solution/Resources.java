package solution;

import scotlandyard.*;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;

import java.awt.*;
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
        "background-lo",
        "s-taxi",
        "s-bus",
        "s-tube",
        "h-1",
        "h-2",
        "menu",
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
    private static Map<String,BufferedImage> images;
    private static Map<String,CountDownLatch> locks;
    
    public Resources()
    {
        images = new HashMap<String,BufferedImage>();
        locks = new HashMap<String,CountDownLatch>();
        for (String s : names) locks.put(s, new CountDownLatch(1));
        locks.put("background-re", new CountDownLatch(1));
    }
    
    public void load()
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
            locks.get(s).countDown();
        }
        Image tmp = images.get("background-lo").getScaledInstance(5625, 4655, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(5625, 4655, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        images.put("background-re", dimg);
        locks.get("background-re").countDown();
        System.err.println("Done!");
    }
    
    public static BufferedImage get(String s)
    {
        try { locks.get(s).await(); }
        catch (InterruptedException e) { }
        return images.get(s);
    }
}
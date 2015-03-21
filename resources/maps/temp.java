import java.io.*;
import java.util.*;

public class temp
{
    public static void main(String[] args) throws Exception
    {
        Scanner s = new Scanner(new File("sy-nodes.svg"));
        PrintWriter w = new PrintWriter("nodes.txt", "UTF-8");
        int i = 0;
        String[] nodes = new String[199];
        while (s.hasNextLine())
        {
            String[] line = s.nextLine().trim().split("\"");
            if (line[0].equals("id="))
            {
                try { i = Integer.parseInt(line[1]); }
                catch (Exception e) { continue; }
            }
            if (0 < i && i < 200)
            {
                String[] xs = s.nextLine().trim().split("\"");
                String[] ys = s.nextLine().trim().split("\"");
                int x = Math.round(Float.parseFloat(xs[1])) * 5;
                int y = Math.round(Float.parseFloat(ys[1])) * 5;
                nodes[i-1] = i + " " + x + " " + y;
            }
            i = 0;
        }
        for (String str : nodes) w.println(str);
        s.close();
        w.close();
    }
}
package solution;

import java.io.*;
import java.util.*;
import scotlandyard.*;

public class SaveFileReader {
	
	void read(String filename) throws FileNotFoundException
	{
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		Map<Ticket, Integer> map = new HashMap<Ticket, Integer>();
    
	    Colour startingplayer = Colour.valueOf(scanner.nextLine());
	    
		int i;
		while (scanner.nextLine() != null)
		{	
			String[] line = scanner.nextLine().split(" ");
			Colour colour = Colour.valueOf(line[0]);
			int location = Integer.parseInt(line[1]);
			i = 2;
			for (Ticket ticket : Ticket.values()) 
			{
				int number = Integer.parseInt(line[i]);
				map.put(ticket, number);
			}
		ScotlandYardModel.join
		map.clear();
		}
	}
    
}

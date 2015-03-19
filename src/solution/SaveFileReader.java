package solution;

import java.io.*;
import java.util.*;
import scotlandyard.*;

public class SaveFileReader {
	
	public static GameHistory read(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
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
		//ScotlandYardModel.join
		map.clear();
		}
        return null;
	}
}

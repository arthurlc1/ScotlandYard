package solution;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scotlandyard.*;

public class SaveFileReader 
{
	public static GameHistory read(String fileName) throws IOException, ClassNotFoundException
	{
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName)); 
		GameHistory history = new GameHistory();
		Piece piece;
		Move move;
		int j = objectInputStream.readInt();
		for (int i = 0; i<j; i++)
		{
			piece = (Piece)objectInputStream.readObject();
			history.join(piece.colour, piece.find(), piece.tickets); 
		}
		while(objectInputStream.readObject() != null)
		{
			move = (Move)objectInputStream.readObject();
			history.notify(move);
		}
		objectInputStream.close();
        return history;
	}

	public static void main(String[] args) throws IOException
	{
		List<Boolean> ting = new ArrayList<Boolean>();
		Player player1;
		for(int i = 0 ; i<20; i++)
		{
			ting.add(true);
		}
		ScotlandYardModel sym = new ScotlandYardModel(1,ting, "graph.txt");
		Map<Ticket, Integer> map = new HashMap<Ticket, Integer>();
		map.add
		sym.join(player1, "BLUE", 143, )
	}


}
/*		File file = new File(fileName);
		Scanner scanner = new Scanner(file);
		Map<Ticket, Integer> map = new HashMap<Ticket, Integer>();
		GameHistory history = new GameHistory(sym);
    
	    List<Move> moves = new ArrayList<Move>();
	    int i = 0;
	    
	    
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
				i++;
			}
			history.join(colour, location, map);
			map.clear();
		
		}
        
	    return null;
	}
}
*/




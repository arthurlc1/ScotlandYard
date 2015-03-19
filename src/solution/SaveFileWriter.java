package solution;

import java.io.*;
import java.util.List;

import scotlandyard.*;

public class SaveFileWriter {

	
	public static void write(String fileName, GameHistory history) throws IOException
	{
		File file = new File(fileName);
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		
		//writer.print(model.getCurrentPlayer());
		for (Colour colour : Colour.values())
		{	
			//if (model.getPiece(colour) == null) return;
			//else
			//{
				writer.print("\n");
				//writer.print(colour + " " + model.getPlayerLocation(colour));
				//for (Ticket ticket : Ticket.values())
				//writer.print( model.getPlayerTickets(colour, ticket) + " ");
			//}
		}
	}
}

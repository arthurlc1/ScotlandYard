package solution;

import java.io.*;
import java.util.List;

import scotlandyard.*;

public class SaveFileWriter {

	
	public static void write(String fileName, ScotlandYardModel model)
	{
		File file = new File(fileName);
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		
		writer.print(ScotlandYardModel.getCurrentPlayer());
		for (Colour colour : Colour.values())
		{	
			if (ScotlandYardModel.getPiece(colour) == null) return null;
			else
			{
				writer.print("\n");
				writer.print(colour + " " + ScotlandYardModel.getPlayerLocation(colour));
				for (Ticket ticket : Ticket.values())
				writer.print( ScotlandYardModel.getPlayerTickets(colour, ticket) + " ");
			}
		}
		return File;
	
	}
}

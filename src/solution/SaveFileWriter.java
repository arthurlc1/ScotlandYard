package solution;

import java.io.*;
import java.util.List;

import scotlandyard.*;

public class SaveFileWriter {

	
	public File Writer(String Filename)
	{
		File File = new File(Filename);
		PrintWriter writer = new PrintWriter(Filename, "UTF-8");
		
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

package solution;

import java.io.*;
import java.util.List;

import scotlandyard.*;

public class SaveFileWriter {

	
	public static void write(String fileName, GameHistory history) throws IOException
	{
		File file = new File(fileName);
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		List<Piece> pieces = history.pieces;	
		List<Move> moves = history.moves;
		for (Move move : moves) 
		{
			writer.print(move.toString() + " ");
		}
		
		for (Piece piece : pieces) 
		{
			writer.print(piece.colour + " " + piece.find());
			for (Ticket ticket : piece.tickets.keySet())
			{
				writer.print(" " + piece.tickets.get(ticket));
			}
			writer.print("\n");
		}
	writer.close();
	}
}


/*writer.print(model.getCurrentPlayer());
for (Colour colour : Colour.values())
{	
	if (model.getPiece(colour) == null) return;
	else
	{
		writer.print("\n");
		writer.print(colour + " " + model.getPlayerLocation(colour));
		for (Ticket ticket : Ticket.values())
		writer.print( model.getPlayerTickets(colour, ticket) + " ");
	}
}*/
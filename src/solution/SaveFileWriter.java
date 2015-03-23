package solution;

import java.io.*;
import java.util.List;

import scotlandyard.*;

public class SaveFileWriter {

	
	public static void write(String fileName, GameHistory history) throws IOException
	{
		File file = new File(fileName);
		List<Piece> pieces = history.pieces;	
		List<Move> moves = history.moves;
		int i = pieces.size();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
		objectOutputStream.writeInt(i);
		for (Piece piece : pieces) 
		{
			objectOutputStream.writeObject(piece);
		}
		for (Move move : moves)
		{
		objectOutputStream.writeObject(move);
		}
		
		objectOutputStream.flush();
		objectOutputStream.close();
		
	}
}
/*		for (Move move : moves) 
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
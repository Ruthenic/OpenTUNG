package de.ecconia.java.opentung.tungboard;

import de.ecconia.java.opentung.tungboard.netremoting.NRParser;
import de.ecconia.java.opentung.tungboard.netremoting.NRFile;
import de.ecconia.java.opentung.tungboard.netremoting.elements.NRClass;
import de.ecconia.java.opentung.tungboard.netremoting.elements.NRObject;
import de.ecconia.java.opentung.tungboard.tungobjects.TungBoard;
import de.ecconia.java.opentung.tungboard.tungobjects.common.TungChildable;
import de.ecconia.java.opentung.tungboard.tungobjects.meta.TungObject;

import java.io.File;

public class PrimitiveParser
{
	public static void main(String[] args)
	{
		new PrimitiveParser();
	}
	
	public PrimitiveParser()
	{
		NRFile pf = NRParser.parse(new File("boards/16Bit-Paralell-CLA-ALU.tungboard"));
		
		NRObject object = pf.getRootElements().get(0);
		NRClass firstClass;
		if(object instanceof NRClass)
		{
			firstClass = (NRClass) object;
		}
		else
		{
			throw new RuntimeException("Unknown first object: " + object.getClass().getSimpleName());
		}
		
		if(TungBoard.NAME.equals(firstClass.getName()))
		{
			TungBoard board = new TungBoard(firstClass);
			
			//Fixer:
			fix(board);
			
			new Exporter(new File("boards/output.tungboard"), board);
		}
		else
		{
			throw new RuntimeException("First Class has wrong type: " + firstClass.getName());
		}
	}
	
	private void fix(TungChildable holder)
	{
		for(TungObject to : holder.getChildren())
		{
			to.getPosition().fix();
			to.getAngles().fix();
			
			if(to instanceof TungChildable)
			{
				fix((TungChildable) to);
			}
		}
	}
}
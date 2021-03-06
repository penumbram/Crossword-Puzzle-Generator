package Utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils 
{

	public static void readFile() throws FileNotFoundException, IOException
	{
		
        ArrayList<String> boardPieces = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader("resources/board templates/boards.txt"))) 
        {
        	String line = br.readLine();
		    StringBuilder aBoard = new StringBuilder();
		    while (line != null) 
		    {    	
		    	if (isNumeric(line))
		    	{
		    		if (aBoard.length() != 0)
		        	{
		        		String temp = aBoard.toString();
		        		boardPieces.add(temp);
		        		aBoard = new StringBuilder();
					}
				}
		        else
		        	aBoard.append(line);
		        	
		    	line = br.readLine();
		    }
        }
        Singleton.getInstance().boards.clear();
        for (String board : boardPieces)
        {
        	int l = getBoardLength(board.length());
        	String[][] aBoard = new String[l][l];
        	int tileCounter = 0;
        	int lineCounter = 0;
        	for (int i = 0; i < board.length(); i++)
        	{
        		if (!String.valueOf(board.charAt(i)).equalsIgnoreCase("|"))
        		{
        			aBoard[lineCounter][tileCounter] = String.valueOf(board.charAt(i));
					tileCounter++;
        			if (tileCounter == l)
        			{
        				lineCounter++;
        				tileCounter = 0;
        				if (lineCounter == l)
        					break;
        			}
        		}
        	}
        	Singleton.getInstance().boards.add(aBoard);
        }
	}
	
	private static int getBoardLength(int chars)
	{
		for (int i = 0; i < 20; i++)
		{
			if (2*(i*i) + i == chars)
			{
				return i;
			}
		}
		return 0;
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
		  Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}

package UserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;



public class BoardPanel extends FadingPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tileWidth;
	private int tileHeight;
	public BoardTile[][] tiles;
	
	public BoardPanel(int width,int height,int row, int column,int tileWidth, int tileHeight,int x,int y)
	{
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		setPreferredSize(new Dimension(width, height));
		//setOpaque(false);
		setBounds(x, y, width,height);
		setLayout(new GridLayout(row, column));
	}



	public void setBoard(String[][] cellz)
    {
		removeAll();
		tiles = new BoardTile[cellz.length][cellz.length];
		for (int i = 0; i < cellz.length; i++)
		{
			for (int j = 0; j < cellz.length; j++) 
			{
				BoardTile boardTile = new BoardTile(tileWidth,tileHeight);
				if (cellz[i][j].equalsIgnoreCase("X"))
					boardTile.setBackground(Color.BLACK);
				else
					boardTile.setBackground(Color.WHITE);
				tiles[i][j] = boardTile;
				add(boardTile);
			}
		}
    }
	
	public void setClickable(boolean b)
	{
		if (b)
		{
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles.length; j++) {
					tiles[i][j].setClickable(b);
				}
			}
		}
	}
}

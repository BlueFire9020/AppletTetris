import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class TileGrid 
{
	public Tile[][] tiles;
	public Dimension gridScale;
	public int spacing;
	public int borderDistance;
	
	public TileGrid(Dimension d, Dimension gs, int space, int border)
	{
		tiles = new Tile[d.width][d.height];
		gridScale = gs;
		Tile.scale = gs;
		spacing = space;
		borderDistance = border;
	}
	
	public void Draw(Graphics page)
	{
		page.setColor(Color.gray);
		for (int x = 0; x < tiles.length; x++) 
		{
			for (int y = 0; y < tiles[x].length; y++)
			{
				int xPos = x * (gridScale.width) + borderDistance;
				int yPos = y * (gridScale.height) + borderDistance;
				if (tiles[x][y] == null)
				{
					Tile.defaultDraw(page, xPos, yPos);
				}
				else if (tiles[x][y] != null)
				{
					tiles[x][y].Draw(page, xPos, yPos);
				}
			}
		}
	}
	
}

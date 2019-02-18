import java.awt.Color;
import java.awt.Graphics;

public class BlockTile extends Tile
{
	public boolean controllable;
	public Color color;
	public Color secondaryColor;
	
	public BlockTile(Color c, boolean canControl, int xPos, int yPos)
	{
		color = c;
		secondaryColor = c.darker();
		controllable = canControl;
		tempX = xPos;
		tempY = yPos;
	}
	
	public BlockTile(Color c, boolean canControl, int xPos, int yPos, boolean centerBlock)
	{
		color = c;
		secondaryColor = c.darker();
		controllable = canControl;
		tempX = xPos;
		tempY = yPos;
		centralBlock = centerBlock;
	}
	
	public void Draw( Graphics page, int xPos, int yPos)
	{
		if (centralBlock && TileGrid.debugMode)
		{
			page.setColor(Color.green);
		}
		else
		{
			page.setColor(color);	
		}
		page.fillRect(xPos, yPos, scale.width, scale.height);
		page.setColor(secondaryColor);
		page.fillRect(xPos + 3, yPos + 3, scale.width - 6, scale.height - 6);
		page.setColor(color);
		page.fillRect(xPos + 6, yPos + 6, scale.width - 12, scale.height - 12);
		if (TileGrid.debugMode)
		{
			page.setColor(Tile.gridColor);
			page.drawString(tempX + "," + tempY, xPos, yPos);	
		}
	}
	
	public void DrawHighlight( Graphics page, int xPos, int yPos)
	{
		if (centralBlock && TileGrid.debugMode)
		{
			page.setColor(Color.green);
		}
		else
		{
			page.setColor(secondaryColor);	
		}
		page.fillRect(xPos, yPos, scale.width, scale.height);
		page.setColor(color);
		page.fillRect(xPos + 3, yPos + 3, scale.width - 6, scale.height - 6);
		page.setColor(color);
		page.fillRect(xPos + 6, yPos + 6, scale.width - 12, scale.height - 12);
		if (TileGrid.debugMode)
		{
			page.setColor(Tile.gridColor);
			page.drawString(tempX + "," + tempY, xPos, yPos);	
		}
	}
}

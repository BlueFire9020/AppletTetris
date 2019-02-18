import java.awt.*;
import java.awt.Graphics;

public class Tile 
{
	public static Dimension scale;
	public boolean centralBlock;
	public static Color gridColor = Color.white;
	public static Color gridFillColor = Color.black;
	public static boolean fillGridSquares = true;
	
	public int tempX, tempY;
	
	public static void defaultDraw(Graphics page, int xPos, int yPos)
	{
		if (fillGridSquares)
		{
			page.setColor(gridFillColor);
			page.fillRect(xPos, yPos, scale.width, scale.height);	
		}
		page.setColor(gridColor);
		page.drawRect(xPos, yPos, scale.width, scale.height);
	}
}

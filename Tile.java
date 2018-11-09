import java.awt.*;
import java.awt.Graphics;

public class Tile 
{
	public boolean occupied;
	public static Dimension scale;
	
	public static void defaultDraw(Graphics page, int xPos, int yPos)
	{
		page.setColor(Color.blue);
		page.drawRect(xPos, yPos, scale.width, scale.height);
	}
	public void Draw(Graphics page, int xPos, int yPos)
	{
		page.setColor(Color.green);
		page.drawRect(xPos, yPos, scale.width, scale.height);
	}
}

import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;

import javax.swing.JApplet;

public class Tetris extends JApplet implements KeyListener
{
	TileGrid grid;
	
	public void init()
	{
		Tile.scale = new Dimension(10,10);
		grid = new TileGrid(new Dimension(20,20), new Dimension(20,20), 10, 5);
	}
	public void paint(Graphics page)
	{
		grid.Draw(page);
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

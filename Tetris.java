import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;

import javax.swing.JPanel;

public class Tetris extends JPanel implements KeyListener
{
	TileGrid grid;
	int rotateBlock, moveLeft, moveRight, moveDown;
	public int delay = 1000; //1 second
	public int period = 1000; //3 seconds
	public Timer gravityTimer;
	
	int currentBlockType = 0;

	public boolean useGravity = true;

	public Tetris()
	{
		Tile.scale = new Dimension(10,10);
		grid = new TileGrid(new Dimension(20,20), new Dimension(20,20), 10, 5);
		addKeyListener(this);
		setFocusable(true);
		grid.CreateBlock();
		rotateBlock = KeyEvent.VK_R;
		moveLeft = KeyEvent.VK_A;
		moveRight = KeyEvent.VK_D;
		moveDown = KeyEvent.VK_S;

		Timer gravityTimer = new Timer();
		gravityTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() 
			{   
				if (useGravity)
				{
					grid.ApplyTileGravity();	
				}
				repaint();
			}
		}, delay, period);
	}
	public void paintComponent(Graphics page)
	{
		super.paintComponent(page);
		grid.Draw(page);
	}
	@Override
	public void keyPressed(KeyEvent kv) {
		// TODO Auto-generated method stub
		int key = kv.getKeyCode();
		if (kv.isShiftDown()) 
		{
			return;
		}
		if (key == moveLeft)
		{
			grid.MoveBlockHorizontal(-1);
		}
		else if (key == moveRight)
		{
			grid.MoveBlockHorizontal(1);
		}
		else if (key == moveDown)
		{
			grid.MoveBlockVertical(1);
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent kv) {
		// TODO Auto-generated method stub
		int key = kv.getKeyCode();
		if (key == rotateBlock)
		{
			grid.RotateBlock();
			repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent kv) 
	{
		if (kv.isShiftDown() && kv.getKeyChar() == 'G')
		{
			useGravity = !useGravity;
		}
		else if (kv.isShiftDown() && kv.getKeyChar() == 'D')
		{
			TileGrid.debugMode = !TileGrid.debugMode;
			repaint();
		}
		else if (kv.isShiftDown() && kv.getKeyChar() == 'S')
		{
			if (currentBlockType > 6)
			{
				currentBlockType = 0;
			}
			currentBlockType++;
			grid.CreateBlock(grid.playerBlocks.get(0).tempX, grid.playerBlocks.get(0).tempY, true, currentBlockType);
			repaint();
		}
	}

}

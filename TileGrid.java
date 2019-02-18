import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TileGrid 
{
	public Tile[][] tiles;
	public Dimension gridScale;
	public Dimension gridTiles;
	public ArrayList<BlockTile> playerBlocks = new ArrayList<BlockTile>();
	public int spacing;
	public int playerRotation = 1;
	public Timer rowDeletionTimer;
	public Timer rowFlashTimer;
	
	public int rowFlashTime = 3000;
	public int rowInterval = 500;
	
	public int yPlaceholder = 0;
	public boolean flashPlaceholder;

	public static int borderDistance;
	public static boolean debugMode = true;

	public TileGrid(Dimension d, Dimension gs, int space, int border)
	{
		tiles = new Tile[d.width][d.height];
		gridTiles = d;
		gridScale = gs;
		Tile.scale = gs;
		spacing = space;
		borderDistance = border;
	}
	public void DrawBackground(Graphics page)
	{
		page.setColor(Color.gray);
		page.fillRect(0,0,1000,1000);
		for (int x = 0; x < tiles.length; x++) 
		{
			for (int y = 0; y < tiles[x].length; y++)
			{
				int xPos = x * (gridScale.width) + borderDistance;
				int yPos = y * (gridScale.height) + borderDistance;
				if (tiles[x][y] != null)
				{
					BlockTile tile = (BlockTile)tiles[x][y];
					tile.Draw(page, xPos, yPos);
				}
				else
				{
					Tile.defaultDraw(page, xPos, yPos);		
				}
			}
		}
	}
	public void Draw(Graphics page)
	{
		DrawBackground(page);
		boolean createNew = false;
		for (BlockTile tile : playerBlocks)
		{
			if (tile.tempX > tiles.length - 1)
			{
				MoveBlockHorizontal((tiles.length - 1) - tile.tempX);
			}
			int newYPos = Math.min(tiles[tile.tempX].length - 1, tile.tempY + 1);
			int xPos = tile.tempX * (gridScale.width) + borderDistance;
			int yPos = tile.tempY * (gridScale.height) + borderDistance;
			BlockTile belowTile = (BlockTile)tiles[tile.tempX][newYPos];
			if (belowTile != null)
			{
				if (debugMode)
				{
					System.out.println("We're going" + " xPos" + xPos + "yPos" + yPos);	
				}
				createNew = true;
			}
			else if (tile.tempY + 1 > tiles[tile.tempX].length - 1) 
			{
				createNew = true;
			}
			tile.Draw(page, xPos, yPos);
		}
		System.out.println();
		if (createNew)
		{
			CreateBlock();
		}
		CheckForFullRows(page);

	}
	
	public void CheckForFullRows(Graphics page)
	{
		for (int y = 0; y < tiles[0].length; y++)
		{
			int counter = 0;
			for (int x = 0; x < tiles.length; x++)
			{
				if (tiles[x][y] != null)
				{
					counter++;
				}
			}
			if (counter == gridTiles.width)
			{
				yPlaceholder = y;
				Timer rowDeletionTimer = new Timer();
				Timer rowFlashTimer = new Timer();
				rowFlashTimer.scheduleAtFixedRate(new TimerTask() {
					public void run() 
					{   
						for (int x = 0; x < tiles.length; x++)
						{
							if (tiles[x][yPlaceholder] != null)
							{
								BlockTile b = (BlockTile)tiles[x][yPlaceholder];
								int xPos = b.tempX * (gridScale.width) + borderDistance;
								int yPos = b.tempY * (gridScale.height) + borderDistance;
								b.DrawHighlight(page, xPos, yPos);
							}
						}
					}
				}, 0, rowInterval);
				rowDeletionTimer.scheduleAtFixedRate(new TimerTask() {
					public void run() 
					{   
						for (int x = 0; x < tiles.length; x++)
						{
							if (tiles[x][yPlaceholder] != null)
							{
								tiles[x][yPlaceholder] = null;
							}
						}
						rowFlashTimer.cancel();
						rowDeletionTimer.cancel();
					}
				}, rowFlashTime, 0);
				ApplyGlobalGravity(y);
			}
		}
	}
	
	public void ApplyTileGravity()
	{
		for (BlockTile tile : playerBlocks)
		{
			tile.tempY++;
		}
	}
	
	public void ApplyGlobalGravity(int stoppingY)
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = stoppingY; j > 0; j--)
			{
				if (tiles[i][j] != null && tiles[i][j + 1] == null)
				{
					if (tiles[i].length - 1 > j)
					{
						if (tiles[i][j + 1] == null)
						{
							BlockTile b = (BlockTile)tiles[i][j];
							tiles[i][j] = null;
							tiles[i][j + 1] = b;
						}
					}
				}
			}
		}
	}

	public void CreateBlock()
	{
		CreateBlock(-1, 0, false, -1);
	}

	public void CreateBlock(int startX, int startY, boolean deleteExisting, int blockType)
	{
		for (BlockTile tile : playerBlocks)
		{
			tiles[tile.tempX][tile.tempY] = tile; 
		}
		if (deleteExisting)
		{
			if (debugMode)
			{
				System.out.println("Delete:" + deleteExisting);
			}
			for (BlockTile tile : playerBlocks)
			{
				tiles[tile.tempX][tile.tempY] = null; 
			}	
		}
		boolean generateX = false;
		if (startX < 0)
		{
			generateX = true;
		}
		if (blockType < 0)
		{
			blockType = (int) (Math.random() * 7);
		}
		playerBlocks = new ArrayList<BlockTile>();
		if (blockType == 0)
		{
			//L block

			//block size is 2, so subtract 3 instead of 1 from the width (1 is because of array limits)
			if (generateX)
			{
				startX = (int) (Math.random() * (gridTiles.width - 3));	
				for (int i = 0; tiles[startX][startY] != null || tiles[startX][startY + 1] != null || tiles[startX][startY + 2] != null || tiles[startX + 1][startY + 2] != null; i++)
				{
					startX = (int) (Math.random() * (gridTiles.width - 3));	
					if (i > 99)
					{
						RestartGame();
						return;
					}
				}
			}
			playerBlocks.add(new BlockTile(Color.red, true, startX, startY));
			playerBlocks.add(new BlockTile(Color.red, true, startX, startY + 1, true));
			playerBlocks.add(new BlockTile(Color.red, true, startX, startY + 2));
			playerBlocks.add(new BlockTile(Color.red, true, startX + 1, startY + 2));
		}
		else if (blockType == 1)
		{
			//2x2 block
			if (generateX)
			{
				startX = (int) (Math.random() * (gridTiles.width - 2));	
				for (int i = 0; tiles[startX][startY] != null || tiles[startX][startY + 1] != null || tiles[startX + 1][startY] != null || tiles[startX + 1][startY + 1] != null; i++)
				{
					startX = (int) (Math.random() * (gridTiles.width - 2));	
					if (i > 99)
					{
						RestartGame();
						return;
					}
				}
			}
			//same as above, but the shape is a 2x2 square so we only subtract 1
			playerBlocks.add(new BlockTile(Color.green, true, startX, startY));
			playerBlocks.add(new BlockTile(Color.green, true, startX, startY + 1));
			playerBlocks.add(new BlockTile(Color.green, true, startX + 1, startY));
			playerBlocks.add(new BlockTile(Color.green, true, startX + 1, startY + 1));
		}
		else if (blockType == 2)
		{
			//Weird squiggly\
			if (generateX)
			{
				startX = (int) (Math.random() * (gridTiles.width - 4));	
				for (int i = 0; tiles[startX][startY + 1] != null || tiles[startX + 1][startY + 1] != null || tiles[startX + 1][startY] != null || tiles[startX + 2][startY] != null; i++)
				{
					startX = (int) (Math.random() * (gridTiles.width - 4));	
					if (i > 99)
					{
						RestartGame();
						return;
					}
				}
			}
			playerBlocks.add(new BlockTile(Color.pink, true, startX, startY + 1));
			playerBlocks.add(new BlockTile(Color.pink, true, startX + 1, startY + 1));
			playerBlocks.add(new BlockTile(Color.pink, true, startX + 1, startY, true));
			playerBlocks.add(new BlockTile(Color.pink, true, startX + 2, startY));
		}
		else if (blockType == 3)
		{
			//Partial + block
			if (generateX)
			{
				startX = (int) (Math.random() * (gridTiles.width - 4));	
				for (int i = 0; tiles[startX][0] != null || tiles[startX + 1][1] != null || tiles[startX + 1][0] != null || tiles[startX + 2][0] != null; i++)
				{
					startX = (int) (Math.random() * (gridTiles.width - 4));	
					if (i > 99)
					{
						RestartGame();
						return;
					}
				}
			}
			playerBlocks.add(new BlockTile(Color.cyan, true, startX, startY));
			playerBlocks.add(new BlockTile(Color.cyan, true, startX + 1, startY + 1));
			playerBlocks.add(new BlockTile(Color.cyan, true, startX + 1, startY, true));
			playerBlocks.add(new BlockTile(Color.cyan, true, startX + 2, startY));
		}
		else if (blockType == 4)
		{
			//Reverse L
			if (generateX)
			{
				startX = (int) (Math.random() * (gridTiles.width - 3));	
				for (int i = 0; tiles[startX + 1][startY] != null || tiles[startX + 1][startY + 1] != null || tiles[startX + 1][startY + 2] != null || tiles[startX][startY + 2] != null; i++)
				{
					startX = (int) (Math.random() * (gridTiles.width - 3));	
					if (i > 99)
					{
						RestartGame();
						return;
					}
				}
			}
			playerBlocks.add(new BlockTile(Color.orange, true, startX + 1, startY));
			playerBlocks.add(new BlockTile(Color.orange, true, startX + 1, startY + 1, true));
			playerBlocks.add(new BlockTile(Color.orange, true, startX + 1, startY + 2));
			playerBlocks.add(new BlockTile(Color.orange, true, startX, startY + 2));
		}
		else if (blockType == 5)
		{
			//Reverse squiggly
			if (generateX)
			{
				startX = (int) (Math.random() * (gridTiles.width - 4));	
				for (int i = 0; tiles[startX][startY] != null || tiles[startX + 1][startY] != null || tiles[startX + 1][startY + 1] != null || tiles[startX + 2][startY + 1] != null; i++)
				{
					startX = (int) (Math.random() * (gridTiles.width - 4));	
					if (i > 99)
					{
						RestartGame();
						return;
					}
				}
			}
			playerBlocks.add(new BlockTile(Color.yellow, true, startX, startY));
			playerBlocks.add(new BlockTile(Color.yellow, true, startX + 1, startY));
			playerBlocks.add(new BlockTile(Color.yellow, true, startX + 1, startY + 1, true));
			playerBlocks.add(new BlockTile(Color.yellow, true, startX + 2, startY + 1));
		}
		else if (blockType >= 6)
		{
			//| Block
			if (generateX)
			{
				startX = (int) (Math.random() * (gridTiles.width - 1));	
				for (int i = 0; tiles[startX][startY] != null || tiles[startX][startY + 1] != null || tiles[startX][startY + 2] != null || tiles[startX][startY + 3] != null || tiles[startX][startY + 4] != null; i++)
				{
					startX = (int) (Math.random() * (gridTiles.width - 1));	
					if (i > 99)
					{
						RestartGame();
						return;
					}
				}
			}
			playerBlocks.add(new BlockTile(Color.white, true, startX, startY));
			playerBlocks.add(new BlockTile(Color.white, true, startX, startY + 1));
			playerBlocks.add(new BlockTile(Color.white, true, startX, startY + 2, true));
			playerBlocks.add(new BlockTile(Color.white, true, startX, startY + 3));
			playerBlocks.add(new BlockTile(Color.white, true, startX, startY + 4));
		}
	}
	public void RotateBlock()
	{
		int smallestX = 99, largestX = 0, smallestY = 99, largestY = 0;
		BlockTile closestTile = null;
		for (BlockTile tile : playerBlocks)
		{
			int xPos = tile.tempX;
			int yPos = tile.tempY;

			if (xPos < smallestX)
			{
				smallestX = xPos;
				closestTile = tile;
			}
			if (xPos > largestX)
			{
				largestX = xPos;
			}

			if (yPos < smallestY)
			{
				smallestY = yPos;
				closestTile = tile;
			}
			if (yPos > largestY)	
			{
				largestY = yPos;
			}

		}
		double midX = (largestX + smallestX) / 2.0;
		double midY = (largestY + smallestY) / 2.0;
		if (debugMode)
		{
			System.out.println(" \nsmallestX: " + smallestX + " largestX: " + largestX + " smallestY: " + smallestY + " largestY: " + largestY + " midX: " + midX + " midY: " + midY);	
		}
		playerRotation *= -1;
		BlockTile centerTile = null;
		int centerX = 0, centerY = 0;
		for (BlockTile tile : playerBlocks)
		{
			double xDist = (tile.tempX - midX);
			double yDist = (tile.tempY - midY);
			if (debugMode)
			{
				System.out.println(" tempX: " + tile.tempX + " tempY: " + tile.tempY + " subX: " + (midX + (yDist * playerRotation)) + " subY: " + (midY + (xDist * playerRotation)));
				System.out.println(" xDist:" + xDist + " yDist:" + yDist + " p:" + playerRotation + " central: " + tile.centralBlock);
			}
			if (tile.centralBlock) 
			{
				centerTile = tile;
				centerX = tile.tempX;
				centerY = tile.tempY;
			}
			tile.tempX = (int) Math.round(midX + (yDist * playerRotation));
			tile.tempY = (int) Math.round(midY + (xDist * playerRotation));
			if (tile.tempX < 0)
			{
				MoveBlockHorizontal(Math.abs(tile.tempX));
			}
			if (tile.tempX > tiles.length - 1)
			{
				//MoveBlockHorizontal((tile.tempX - 1) - tiles.length);
			}
		}

		if (centerTile != null)
		{
			MoveBlockHorizontal(centerX - centerTile.tempX);
			MoveBlockVertical(centerY - centerTile.tempY);
		}
	}
	public void MoveBlockHorizontal(int direction)
	{	
		for (BlockTile tile : playerBlocks)
		{
			BlockTile leftTile = (BlockTile)tiles[Math.max(0, tile.tempX - 1)][tile.tempY];
			BlockTile rightTile = (BlockTile)tiles[Math.min(tiles.length - 1, tile.tempX + 1)][tile.tempY];
			if (playerBlocks.contains(leftTile))
			{
				leftTile = null;
			}
			if (playerBlocks.contains(rightTile))
			{
				rightTile = null;
			}
			int newPos = tile.tempX + direction;
			System.out.println("OldXpos:" + tile.tempX + " newXPos:" + newPos);
			if (direction > 0 && rightTile != null || direction > 0 && newPos > gridTiles.width - 1)
			{
				return;
			}
			else if (direction < 0 && leftTile != null || direction < 0 && newPos < 0)
			{
				return;
			}
		}
		for (BlockTile tile : playerBlocks)
		{
			tile.tempX += direction;
			System.out.println("New X: " + tile.tempX);
		}
	}
	public void MoveBlockVertical(int direction)
	{
		for (BlockTile tile : playerBlocks)
		{
			int newPos = tile.tempY + direction;
			System.out.println("OldYpos:" + tile.tempY + " newYPos:" + newPos);
			if (newPos < 0 || newPos > gridTiles.height - 1)
			{
				return;
			}
		}
		for (BlockTile tile : playerBlocks)
		{
			tile.tempY += direction;
		}
	}

	public void RestartGame()
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j] = null;
			}
		}
		CreateBlock();
	}

}
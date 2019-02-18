import javax.swing.JApplet;

public class Window extends JApplet 
{
	@Override
	public void init()
	{
		add(new Tetris());
	}
}

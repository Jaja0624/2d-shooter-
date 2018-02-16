import javax.swing.JFrame;
import java.awt.*;


public class GameFrame extends JFrame {
	
	private final static int WIDTH = 900;
	public final static int HEIGHT = 700;
	
	private final GameComponent comp;
	public GameFrame() {
		setResizable(false);
		comp = new GameComponent();
		add(comp);		
	}
	
	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Gradius");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.comp.start();
		frame.getContentPane().setBackground(Color.BLACK);
		
	}
}

	
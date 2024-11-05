import javax.swing.JFrame;

public class GameFrame extends JFrame {
    GameFrame() {
		this.add(new GamePanel());
		this.setTitle("snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		//If we add componments to Jframe, takes jFrame and fits snugged with its components
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
}

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 700;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 50;
	//Arrays to handle the coordinates (x,y) of the snakes body parts
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	

	//CLASS CONSTRUCTOR
	GamePanel(){
		//Create instance of the random class
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		//After setting up game configuration, launch the game.
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if(running) {
//			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
//				//Draw grid lines for development
//				g.setColor(Color.LIGHT_GRAY);
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//			}
			//Draw apple that is spawned randomly.
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i<bodyParts; i++) {
				if(i ==0) {
					//Is Head
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}else {
					//Body Color
					g.setColor(new Color(45,180,0));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
	
					
				}
			}
			//Display Score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
			
		}else {
			gameOver(g);
		}

	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; 

	}
	//Allow user to move head of snake 
	public void moveSnake() {
		for(int i = bodyParts; i>0; i-- ) {
			//shift the body of the snake to match the path of the head.
			//Produces a cascade by iterating the body towards the last known position of the head.
			//(i-1) goes towards the direction of head.
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		//Move the direction of the Head, since the body follows the prev coordinates
		switch(direction){
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
		}
			
	}
	//Check to see if snake ate an apple
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
			
		}
	}//Check to see if snake collided anywhere
	public void checkCollisions() {
		//check every known body part for its location
		for(int i = bodyParts; i>0; i--) {
			// if HEAD is @ the same location of any known bodyPart
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false; 
			}
		}//Check left border
		if(x[0] < 0) {
			running = false;
		}//Check Right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}//Check Top border
		if(y[0] < 0) {
			running = false;
		}//Check Bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//Game over text
		g.setColor(Color.cyan);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!"))/2, SCREEN_HEIGHT/2);
		//Display Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 30));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		if(running) {
			moveSnake();
			checkApple();
			checkCollisions();
			
		}
		repaint();
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			//translate the key that is pressed
			switch(e.getKeyCode()) {
			//If the LEFT arrow key is pressed
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			//If the LEFT arrow key is pressed
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			//If the DOWN arrow key is pressed
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			//If the UP arrow key is pressed
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
				
			}
		}
	}
}
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;


public class GameComponent extends JComponent {
	private  Ship ship;
	private static ArrayList<Timer> timer;
	private static Set<Asteroid> asteroids; 
	private KeyListener shipKeyListener;
	public static boolean gameOver;
	private static Set<Bullet> bullets;
	private static int score;
	private int damage;
	private static int time;
	
	public GameComponent() {
		gameOver = false;
		score = 0;
		ship = new ShipImpl(10, GameFrame.HEIGHT/3);
		shipKeyListener = new ShipKeyListener();
		this.addKeyListener(shipKeyListener);
		timer = new ArrayList<Timer>();
		timer.add(new Timer(1000/60, (a) ->  update()));	
		timer.add(new Timer(1000/6, (a) -> makeAsteroid()));	
		timer.add(new Timer(1000, (a) ->  time++));
		asteroids = new HashSet<Asteroid>();
		bullets = new HashSet<Bullet>();
		
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintComponent(g2);
	}
	
	
	private void paintComponent(Graphics2D g) {
		ship.draw(g);
		ship.drawTail(g);
		asteroids.stream().forEach(ast -> ast.draw(g));
		bullets.stream().forEach(b -> b.draw(g));
		ship.drawHealth(g);
		displayScore(g);
		if (gameOver) {
			gameOver(g);
		}
	}
	
	private void makeAsteroid() {
		asteroids.add(AsteroidFactory.getInstance().makeAsteroid(score));
		if (score > 10000) { // make more asteroids 
			asteroids.add(AsteroidFactory.getInstance().makeAsteroid(score));
			asteroids.add(AsteroidFactory.getInstance().makeAsteroid(score));
			asteroids.add(AsteroidFactory.getInstance().makeAsteroid(score));
		} else if (score > 5000) {
			asteroids.add(AsteroidFactory.getInstance().makeAsteroid(score));
		}
		
	}
	
	private void makeBullet() {
		BulletFactory.getInstance().setStartBounds((int)ship.getShape().getBounds2D().getX(),
				(int)ship.getShape().getBounds2D().getY(),
				(int)ship.getShape().getBounds2D().getWidth(),
				(int)ship.getShape().getBounds2D().getHeight());
		bullets.add(BulletFactory.getInstance().makeBullet(score, 
				(int)ship.getShape().getBounds2D().getMaxX(),
				(int)ship.getShape().getBounds2D().getCenterY()));
	}
	
	private class ShipKeyListener extends KeyAdapter {
		 Set<Integer> keys;
		 
		public void keyPressed(KeyEvent e) {
			keys = new HashSet<Integer>();
			keys.add(e.getKeyCode());
			if (e.getKeyCode() == KeyEvent.VK_R && (gameOver)) {
				reset();
			}
			
			if (keys.contains(KeyEvent.VK_UP)
					|| keys.contains(KeyEvent.VK_W)
					|| keys.contains(KeyEvent.VK_KP_UP)) {
				ship.setDirection(Ship.Direction.UP);
			}
	
			if (keys.contains(KeyEvent.VK_RIGHT)
					|| keys.contains(KeyEvent.VK_D)
					|| keys.contains(KeyEvent.VK_KP_RIGHT)) {
				ship.setDirection(Ship.Direction.RIGHT);
			}
			
			if (keys.contains(KeyEvent.VK_DOWN)
					|| keys.contains(KeyEvent.VK_S)
					|| keys.contains(KeyEvent.VK_KP_DOWN)) {
				ship.setDirection(Ship.Direction.DOWN);
			}
			
			if (keys.contains(KeyEvent.VK_LEFT)
					|| keys.contains(KeyEvent.VK_A)
					|| keys.contains(KeyEvent.VK_KP_LEFT)) {
				ship.setDirection(Ship.Direction.LEFT);
			}
			
			if (keys.contains(KeyEvent.VK_ESCAPE)) {
				gameOver = true;
			}
			
			if (keys.contains(KeyEvent.VK_SPACE) && gameOver == false) {
				makeBullet();
			}	
		}
		
		public void keyReleased(KeyEvent e) {
			keys.removeIf(x -> x == e.getKeyCode());
			if (keys.contains(KeyEvent.VK_UP)
					|| keys.contains(KeyEvent.VK_W)
					|| keys.contains(KeyEvent.VK_KP_UP)) {
				ship.setDirection(Ship.Direction.UP);
			}
			
			if (keys.contains(KeyEvent.VK_RIGHT)
					|| keys.contains(KeyEvent.VK_D)
					|| keys.contains(KeyEvent.VK_KP_RIGHT)) {
				ship.setDirection(Ship.Direction.RIGHT);
			}
			
			if (keys.contains(KeyEvent.VK_DOWN)
					|| keys.contains(KeyEvent.VK_S)
					|| keys.contains(KeyEvent.VK_KP_DOWN)) {
				ship.setDirection(Ship.Direction.DOWN);
			}
			
			if (keys.contains(KeyEvent.VK_LEFT)
					|| keys.contains(KeyEvent.VK_A)
					|| keys.contains(KeyEvent.VK_KP_LEFT)) {
				ship.setDirection(Ship.Direction.LEFT);
			}
				
			if (keys.isEmpty()) {
				ship.setDirection(Ship.Direction.NONE);
			}				
		}
		
	}
			
	public void start() {
		ship.setMovementBounds(new Rectangle2D.Double(0,0,getWidth(), GameFrame.HEIGHT));
		AsteroidFactory.getInstance().setStartBounds(getWidth(), 0, GameFrame.HEIGHT);
		timer.stream().forEach(t -> t.start());
	}
	
	public void reset() {
		time = 0;
		score = 0;
		ship.reset();
		gameOver = false;
		asteroids.removeAll(asteroids);
	}
	
	private void update() {
		requestFocusInWindow();
		ship.move();
		moveAsteroids();
		shoot();
		if (ship.getHealth() <= 0) {
			gameOver();
		} else {
			checkCollisions();
		}
		repaint();
	}
	
	private void moveAsteroids() {
	    asteroids.stream().forEach(ast -> ast.move());
	    asteroids.removeIf(ast -> !ast.isVisible());   
	}
	
	private void shoot() {
		bullets.stream().forEach(b -> b.move());
		bullets.removeIf(b -> !b.isVisible());
	}
	
	private boolean checkCollisions() {
		if (shipCollision()) {
			return true;
		} else if (bulletCollision()) {
			score += 100;
			ship.upgrade(score);
			return true;
		}
		return false;
	}
	
	private boolean shipCollision() {
		for (Asteroid x : asteroids) {
			if (x.intersects(ship)) {
				asteroids.remove(x);
				damage = x.getDamage();
				ship.damage(damage);
				score -= 200;
				return true;
			}
		}
		return false;
	}
	
	private boolean bulletCollision() {
		return bullets.removeIf(b -> asteroids.removeIf(x -> x.intersects(b)));
	}
	
	private void displayScore(Graphics2D g) {
		g.drawString("Score: " + Integer.toString(score), 30, GameFrame.HEIGHT-60);
		g.drawString("Time: " + Integer.toString(time), 30, GameFrame.HEIGHT-40);
	}
	
	private void gameOver() {
		if (ship.getHealth() <= 0) {
			gameOver = true;
		} 
	}
	
	private void gameOver(Graphics2D g) {
		if (gameOver) {
			g.setColor(Color.GREEN);
			Font font = new Font("calibri", Font.BOLD, 14);
			g.setFont(font);
			g.drawString("game over", getWidth()/2, GameFrame.HEIGHT/2);
			g.drawString("press 'R' to play again", getWidth()/2, GameFrame.HEIGHT/2 + 25);
		}
	}
	
}

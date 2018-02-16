import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

public class AsteroidFactory {

	private final static AsteroidFactory instance = new AsteroidFactory();
	
	private static Rectangle startBounds;
	
	private AsteroidFactory() {}
	
	public static AsteroidFactory getInstance() {
		return instance;
	}
	
	public void setStartBounds(int x, int minY, int maxY) {
		startBounds = new Rectangle(x, minY, x, maxY);
	}
	
	public Asteroid makeAsteroid(int score) {
		if (score < 1000) {
			return new AsteroidImpl(startBounds.width, random(startBounds.y, startBounds.height), random(15, 30), random(15, 30), random(2, 4), random(0, 100));
		} else if (score < 2000) {
			return new AsteroidImpl(startBounds.width, random(startBounds.y, startBounds.height), random(15, 70), random(20, 40), random(3, 8), random(0, 100));
		} else if (score < 4000) {
			return new AsteroidImpl(startBounds.width, random(startBounds.y, startBounds.height), random(35, 90), random(30, 70), random(5, 12), random(0, 100));
		} else {
			return new AsteroidImpl(startBounds.width, random(startBounds.y, startBounds.height), random(50, 200), random(50, 200), random(7, 20), random(0, 100));
		}
	}
	
	private static int random(int min, int max) {
		Random rand = java.util.concurrent.ThreadLocalRandom.current();
		return min + (int) (rand.nextDouble()*(max-min));
	}
	
	private static class AsteroidImpl implements Asteroid {
		private final static Color COLOR = Color.DARK_GRAY;
		private int velocity;
		private final Ellipse2D.Double shape;
		private int trajectory;

		private AsteroidImpl(int x, int y, int width, int height, int velocity, int trajectory) {
			shape = new Ellipse2D.Double(x,y, width, height);
			this.velocity = velocity;
			this.trajectory = trajectory;
		}
		
		public void draw(Graphics2D g) {
			g.setColor(COLOR);
			g.fill(shape);
			g.setColor(Color.GRAY);
		}
		
		public void move() {
			if (trajectory % 4 == 0) {
				shape.x -= velocity;
				shape.y += velocity;
			} else if (trajectory % 5 == 0) {
				shape.x -= velocity;
				shape.y -= velocity;
			} else {
				shape.x -= velocity; 
			}
		}

		public boolean isVisible() {
			return !(shape.getBounds2D().getMaxX() < -20);			
		}

		public Shape getShape() {
			return shape;
		}
		
		public int getDamage() {
			return (int)(shape.width + shape.height)/2;
		}

		public boolean intersects(Sprite other) {
			Area asteroidArea = new Area(shape);
			Area otherArea = new Area(other.getShape());
			asteroidArea.intersect(otherArea);
			return !asteroidArea.isEmpty();
			
		}
	}
	
}
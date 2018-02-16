import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;



public class BulletFactory {

	private final static BulletFactory instance = new BulletFactory();
	
	private static Rectangle startBounds;
	
	private BulletFactory() {}
	
	public static BulletFactory getInstance() {
		return instance;
	}
	
	public void setStartBounds(int x, int y,  int width, int height) {
		startBounds = new Rectangle(x, y, width, height);
	}
	
	public Bullet makeBullet(int score, int x, int y) {
	
		if (score < 800) {
			return new BulletImpl((int)startBounds.getMaxX(), (int)startBounds.getCenterY(), Bullet.BulletType.ONE);
		} else if (score < 1700) {
			return new BulletImpl((int)startBounds.getMaxX(), (int)startBounds.getCenterY(), Bullet.BulletType.TWO);
		} else if (score < 2700) {
			return new BulletImpl((int)startBounds.getMaxX(), (int)startBounds.getCenterY(), Bullet.BulletType.THREE);
		} else if (score < 4400) {
			return new BulletImpl((int)startBounds.getMaxX(), (int)startBounds.getCenterY(), Bullet.BulletType.FOUR);
		} else {
			return new BulletImpl((int)startBounds.getMaxX(), (int)startBounds.getCenterY(), Bullet.BulletType.FIVE);
		} 
	
	}
	
	private static class BulletImpl implements Bullet { 
		
		private final Ellipse2D.Double shape;
		private BulletType b;
		
		
		private BulletImpl(int x, int y, BulletType b) {
			this.b = b;
			shape = new Ellipse2D.Double(x, y, b.width, b.width);
		}
	
		public void draw(Graphics2D g) {
			g.setColor(b.color);
			g.fill(shape);			
		}
	
		public void move() {
			shape.x += b.velocity;
		}
		
		public boolean isVisible() {
			return !(shape.x > 900);
		}
		
		public Shape getShape() {
			return shape;
		}
	
		public boolean intersects(Sprite other) {
			Area bulletArea = new Area(shape);
			Area otherArea = new Area(other.getShape());
			bulletArea.intersect(otherArea);
			return !otherArea.isEmpty();
		}
	
	}
}

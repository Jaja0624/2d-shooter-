import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class ShipImpl implements Ship {

	private final static Color FILL = Color.GREEN;
	private static Color BORDER = Color.PINK;
	private static BasicStroke shipStroke = new BasicStroke(3.0f);
	private final static int HIGHEST_I = 0; // the array position of the top
	private final static int LOWEST_I = 1;  // the array position of the bottom
	private final static int FRONT_I = 2;
	private final static int HEIGHT = 20;
	private final static int WIDTH = HEIGHT;
	private final Polygon shape;
	private Polygon shapeTail;
	private Direction d;
	private Rectangle2D movementBounds;
	private int health = 100;;
	private int velocity = 3;
	private int dhealth = health; // health bar border
	private Boolean[] upgrades = new Boolean[5];
	private int tailX = 0;
	
	
	public ShipImpl(int x, int y) {
		shape = new Polygon(
			new int[] {0,0,WIDTH}, //top left, bottom left, front middle
			new int[] {0,HEIGHT,HEIGHT/2}, 3);
		shapeTail = new Polygon(
			new int[] {0, 0, -WIDTH/3},
			new int[] {5, HEIGHT-5, HEIGHT/2}, 3);
		shape.translate(x, y);
		shapeTail.translate(x, y);
		d = Direction.NONE;
		Arrays.fill(upgrades, Boolean.FALSE);
		
		
	}

	public void setDirection(Direction d) {
		this.d = d;
		
	}

	public void setMovementBounds(Rectangle2D movementBounds) {
		this.movementBounds = movementBounds;
	}

	public void move() {		
	    if (GameComponent.gameOver == false){
			shape.translate(d.dx*velocity, d.dy*velocity);
			shapeTail.translate(d.dx*velocity, d.dy*velocity);
		}
		
		if (shape.xpoints[FRONT_I] > movementBounds.getMaxX()) {
			shape.translate((int)movementBounds.getMaxX() - shape.xpoints[FRONT_I], 0);
			shapeTail.translate((int)movementBounds.getMaxX() - shape.xpoints[FRONT_I], 0);
			
		} else if (shape.xpoints[HIGHEST_I] < movementBounds.getMinX()) {
			shape.translate(Math.abs(shape.xpoints[HIGHEST_I]), 0);
			shapeTail.translate(Math.abs(shape.xpoints[HIGHEST_I]), 0);
			
		} else if (shape.ypoints[LOWEST_I] > (movementBounds.getMaxY())) {
			shape.translate(0, (int)(movementBounds.getMaxY() - shape.ypoints[LOWEST_I]));
			shapeTail.translate(0, (int)(movementBounds.getMaxY() - shape.ypoints[LOWEST_I]));
			
		} else if (shape.ypoints[HIGHEST_I] < movementBounds.getMinY()) {
			shape.translate(0, Math.abs(shape.ypoints[HIGHEST_I]));
			shapeTail.translate(0, Math.abs(shape.ypoints[HIGHEST_I]));
		}
		
	}
	
	public void draw(Graphics2D g) {
			g.setStroke(shipStroke);
			g.setColor(BORDER);
			g.drawPolygon(shape);
			g.setColor(FILL);
			g.fill(shape);		
	}
	
	public void drawTail(Graphics2D g) {
		g.setStroke(new BasicStroke(2.5f));
		g.setColor(Color.YELLOW);
		g.drawPolygon(shapeTail);
		g.setColor(Color.RED);
		g.fill(shapeTail);
	}
	
	public void drawHealth(Graphics2D g) {
		g.setColor(FILL);
		g.drawRect(30, 30, dhealth*2, 20);
		g.fillRect(30, 30, health*2, 20);
	}

	public Shape getShape() {
		return shape;
	}
	
	public void damage(int damage) {
		health -= damage;
	}
	
	public void upgrade(int score) {
		if (score == 7000 && !upgrades[4]) {
			health = 600;
			dhealth = 600;
			velocity = 10;
			shapeTail.xpoints[2] -= 10;
			tailX -= 10;
			upgrades[4] = true;
		} else if (score == 3800 && !upgrades[3]) {
			health = 350;
			dhealth = 350;
			velocity = 7;
			shapeTail.xpoints[2] -= 5;
			tailX -= 5;
			upgrades[3] = true;
		} else if (score == 2400 && !upgrades[2]) {
			health = 300;
			dhealth = 300;
			velocity = 6;
			shapeTail.xpoints[2] -= 4;
			tailX -= 4;
			upgrades[2] = true;
		} else if (score == 1400 && !upgrades[1]) {
			health = 200;
			dhealth = 200;
			velocity = 5;
			shapeTail.xpoints[2] -= 3;
			tailX -= 3;
			upgrades[1] = true;
		} else if (score == 600 && !upgrades[0]) {
			health = 150;
			dhealth = 150;
			velocity = 4;
			shapeTail.xpoints[2] -= 2;
			tailX -= 2;
			upgrades[0] = true;
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public void reset() {
		health = 100;
		dhealth = 100;
		velocity = 3;
		shapeTail.xpoints[2] -= tailX;
		tailX = 0;
		Arrays.fill(upgrades, Boolean.FALSE);
	}
	
	public boolean intersects(Sprite other) {
		Area shipArea = new Area(shape);
		Area otherArea = new Area(other.getShape());
		shipArea.intersect(otherArea);
		return !shipArea.isEmpty();
	}
}

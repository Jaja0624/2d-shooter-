import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Ship extends Sprite {

	public enum Direction {
		NONE(0, 0), UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
		public final int dy;
		public final int dx;
		Direction(int dx, int dy) {
			this.dy = dy;
			this.dx = dx;
		}
	};

	public void setDirection(Direction d);
	public void setMovementBounds(Rectangle2D bounds);
	public void drawHealth(Graphics2D g);
	public void damage(int health);
	public int getHealth();
	public void upgrade(int score);
	public void drawTail(Graphics2D g);
	public void reset();
}

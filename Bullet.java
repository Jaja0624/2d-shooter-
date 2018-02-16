import java.awt.Color;


public interface Bullet extends Sprite {
	public enum BulletType {
		ONE(5, 10, Color.GREEN), TWO(10, 15, Color.BLUE), THREE(12, 20, Color.YELLOW), FOUR(15, 25, Color.CYAN), FIVE(20, 30, Color.MAGENTA);
	
        public final int velocity;
        public final int width;
        public final Color color;
		BulletType(int width, int velocity, Color color) {
			this.velocity = velocity;
			this.color = color;
			this.width = width;			
		}
	};

	public boolean isVisible();
}

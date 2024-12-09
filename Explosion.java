
public class Explosion extends Entity implements Scrollable, Throwable {

  public static final String EXPLOSION_IMAGE_FILE = "bogushAssets/explosion.png";
  public static final int EXPLOSION_WIDTH = 237;
  public static final int EXPLOSION_HEIGHT = 213;
  public static final int EXPLOSION_DEFAULT_SCROLL_SPEED = 0;

  private int scrollSpeed = EXPLOSION_DEFAULT_SCROLL_SPEED;

  public Explosion() {
    this(0, 0);
  }

  public Explosion(int x, int y) {
    this(x, y, EXPLOSION_IMAGE_FILE);
  }

  public Explosion(int x, int y, String imageFileName) {
    this(x, y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT, imageFileName);
  }

  public Explosion(int x, int y, int width, int height, String imageName) {
    super(x, y, width, height, imageName);
  }

  public int getScrollSpeed() {
    return this.scrollSpeed;
  }

  public void setScrollSpeed(int newSpeed) {
    this.scrollSpeed = newSpeed;
  }

  public void scroll() {
    setX(getX() - this.scrollSpeed);
  }

}

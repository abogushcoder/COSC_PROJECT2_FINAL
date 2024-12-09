
public class Crate extends Entity implements Scrollable, Consumable {

  public static final String CRATE_IMAGE_FILE = "bogushAssets/crate.png";
  public static final int CRATE_WIDTH = 112;
  public static final int CRATE_HEIGHT = 112;
  public static final int CRATE_DEFAULT_SCROLL_SPEED = 5;

  private int scrollSpeed = CRATE_DEFAULT_SCROLL_SPEED;

  public Crate() {
    this(0, 0);
  }

  public Crate(int x, int y) {
    this(x, y, CRATE_IMAGE_FILE);
  }

  public Crate(int x, int y, String imageFileName) {
    this(x, y, CRATE_WIDTH, CRATE_HEIGHT, imageFileName);
  }

  public Crate(int x, int y, int width, int height, String imageName) {
    super(x, y, width, height, imageName);
  }

  public int getScrollSpeed() {
    return this.scrollSpeed;
  }

  public void setScrollSpeed(int newSpeed) {
    this.scrollSpeed = newSpeed;
  }

  public int getScoreModifier() {
    return 0;
  }

  public int getHPModifier() {
    return 0;
  }

  public void scroll() {
    setX(getX() - this.scrollSpeed);
  }

}

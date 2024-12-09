public class Crown extends Entity implements Consumable, Scrollable {

  public static final String CROWN_IMAGE_FILE = "bogushAssets/crown.png";
  public static final int CROWN_WIDTH = 112;
  public static final int CROWN_HEIGHT = 112;
  public static final int CROWN_DEFAULT_SCROLL_SPEED = 5;
  public static final int CROWN_POINT_VALUE = 1;

  private int scrollSpeed = CROWN_DEFAULT_SCROLL_SPEED;

  public Crown() {
    this(0, 0);
  }

  public Crown(int x, int y) {
    this(x, y, CROWN_IMAGE_FILE);
  }

  public Crown(int x, int y, String imageFileName) {
    this(x, y, CROWN_WIDTH, CROWN_HEIGHT, imageFileName);
  }

  public Crown(int x, int y, int width, int height, String imageName) {
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

  public int getScoreModifier() {
    return CROWN_POINT_VALUE;
  }

  public int getHPModifier() {
    return 0;
  }

}

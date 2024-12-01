public class LogToCollect extends Entity implements Consumable, Scrollable, Collectable {

  public static final String LOGTC_IMAGE_FILE = "assets/log.png";
  public static final int LOGTC_WIDTH = 15;
  public static final int LOGTC_HEIGHT = 50;
  public static final int LOGTC_DEFAULT_SCROLL_SPEED = 5;
  public static final int LOGTC_POINT_VALUE = 20;

  private int scrollSpeed = LOGTC_DEFAULT_SCROLL_SPEED;

  public LogToCollect() {
    this(0, 0);
  }

  public LogToCollect(int x, int y) {
    this(x, y, LOGTC_IMAGE_FILE);
  }

  public LogToCollect(int x, int y, String imageFileName) {
    this(x, y, LOGTC_WIDTH, LOGTC_HEIGHT, imageFileName);
  }

  public LogToCollect(int x, int y, int width, int height, String imageName) {
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
    return 0;
  }

  public int getHPModifier() {
    return 0;
  }

  public String getName() {
    return "Log";
  }

}

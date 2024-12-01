public class LogToRoll extends Entity implements Scrollable, Throwable {

  public static final String LOGTR_IMAGE_FILE = "assets/log.png";
  public static final int LOGTR_WIDTH = 62;
  public static final int LOGTR_HEIGHT = 245;
  public static final int LOGTR_DEFAULT_SCROLL_SPEED = 10;

  private int scrollSpeed = LOGTR_DEFAULT_SCROLL_SPEED;

  public LogToRoll() {
    this(0, 0);
  }

  public LogToRoll(int x, int y) {
    this(x, y, LOGTR_IMAGE_FILE);
  }

  public LogToRoll(int x, int y, String imageFileName) {
    this(x, y, LOGTR_WIDTH, LOGTR_HEIGHT, imageFileName);
  }

  public LogToRoll(int x, int y, int width, int height, String imageName) {
    super(x, y, width, height, imageName);
  }

  public int getScrollSpeed() {
    return this.scrollSpeed;
  }

  public void setScrollSpeed(int newSpeed) {
    this.scrollSpeed = newSpeed;
  }

  public void scroll() {
    setX(getX() + this.scrollSpeed);
  }

}

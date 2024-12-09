
public class FireballToCollect extends Entity implements Consumable, Scrollable, Collectable {

  public static final String FBTC_IMAGE_FILE = "bogushAssets/fireball.png";
  public static final int FBTC_WIDTH = 57;
  public static final int FBTC_HEIGHT = 55;
  public static final int FBTC_DEFAULT_SCROLL_SPEED = 5;
  public static final int FBTC_POINT_VALUE = 20;

  private int scrollSpeed = FBTC_DEFAULT_SCROLL_SPEED;

  public FireballToCollect() {
    this(0, 0);
  }

  public FireballToCollect(int x, int y) {
    this(x, y, FBTC_IMAGE_FILE);
  }

  public FireballToCollect(int x, int y, String imageFileName) {
    this(x, y, FBTC_WIDTH, FBTC_HEIGHT, imageFileName);
  }

  public FireballToCollect(int x, int y, int width, int height, String imageName) {
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
    return "Fireball";
  }

}

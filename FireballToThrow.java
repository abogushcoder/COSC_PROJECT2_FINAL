public class FireballToThrow extends Entity implements Scrollable, Throwable {

  public static final String FBTT_IMAGE_FILE = "bogushAssets/fireball.png";
  public static final int FBTT_WIDTH = 110;
  public static final int FBTT_HEIGHT = 110;
  public static final int FBTT_DEFAULT_SCROLL_SPEED = 5;
  private int targetX;
  private int targetY;
  private double dx;
  private double dy;
  private double distance;
  private boolean hasReachedTarget = false;
  private int scrollSpeed = FBTT_DEFAULT_SCROLL_SPEED;

  public FireballToThrow() {
    this(0, 0);
  }

  public FireballToThrow(int x, int y) {
    this(x, y, FBTT_IMAGE_FILE);
  }

  public FireballToThrow(int x, int y, String imageFileName) {
    this(x, y, FBTT_WIDTH, FBTT_HEIGHT, imageFileName);
  }

  public FireballToThrow(int x, int y, int width, int height, String imageName) {
    super(x, y, width, height, imageName);
  }

  public void setTarget(int targetX, int targetY) {
    this.targetX = targetX - FBTT_WIDTH / 2;
    this.targetY = targetY - FBTT_HEIGHT / 2;
    int deltaX = this.targetX - this.getX();
    int deltaY = this.targetY - this.getY();
    this.distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    this.dx = FBTT_DEFAULT_SCROLL_SPEED * (deltaX / this.distance);
    this.dy = FBTT_DEFAULT_SCROLL_SPEED * (deltaY / this.distance);
  }

  public int getTargetX() {
    return this.targetX;
  }

  public int getTargetY() {
    return this.targetY;
  }

  public boolean hasReachedTarget() {
    return hasReachedTarget;
  }

  public int getScrollSpeed() {
    return this.scrollSpeed;
  }

  public void setScrollSpeed(int newSpeed) {
    this.scrollSpeed = newSpeed;
  }

  public void scroll() {
    if (!hasReachedTarget) {

      this.setX((int) (this.getX() + this.dx));
      this.setY((int) (this.getY() + this.dy));

      if (Math.abs(this.getX() - this.targetX) <= FBTT_WIDTH / 2 &&
          Math.abs(this.getY() - this.targetY) <= FBTT_HEIGHT / 2) {
        hasReachedTarget = true;
        this.setX(this.targetX);
        this.setY(this.targetY);
      }
    }
  }
}

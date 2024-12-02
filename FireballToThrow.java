// public class FireballToThrow extends Entity implements Scrollable, Throwable {
//
//   public static final String FBTT_IMAGE_FILE = "assets/fireball.png";
//   public static final int FBTT_WIDTH = 228;
//   public static final int FBTT_HEIGHT = 221;
//   public static final int FBTT_DEFAULT_SCROLL_SPEED = 5;
//   private int targetX;
//   private int targetY;
//   private double dx;
//   private double dy;
//   private double distance;
//   private boolean hasReachedTarget = false;
//   private int scrollSpeed = FBTT_DEFAULT_SCROLL_SPEED;
//
//   public FireballToThrow() {
//     this(0, 0);
//   }
//
//   public FireballToThrow(int x, int y) {
//     this(x, y, FBTT_IMAGE_FILE);
//   }
//
//   public FireballToThrow(int x, int y, String imageFileName) {
//     this(x, y, FBTT_WIDTH, FBTT_HEIGHT, imageFileName);
//   }
//
//   public FireballToThrow(int x, int y, int width, int height, String imageName) {
//     super(x, y, width, height, imageName);
//   }
//
//   public void setTarget(int targetX, int targetY) {
//     this.targetX = targetX - FBTT_WIDTH / 2;
//     this.targetY = targetY - FBTT_HEIGHT / 2;
//
//     int deltaX = this.targetX - this.getX();
//     int deltaY = this.targetY - this.getY();
//     this.distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//
//     this.dx = FBTT_DEFAULT_SCROLL_SPEED * (deltaX / this.distance);
//     this.dy = FBTT_DEFAULT_SCROLL_SPEED * (deltaY / this.distance);
//   }
//
//   public int getTargetX() {
//     return this.targetX;
//   }
//
//   public int getTargetY() {
//     return this.targetY;
//   }
//
//   public boolean hasReachedTarget() {
//     return hasReachedTarget;
//   }
//
//   public int getScrollSpeed() {
//     return this.scrollSpeed;
//   }
//
//   public void setScrollSpeed(int newSpeed) {
//     this.scrollSpeed = newSpeed;
//   }
//
//   public void scroll() {
//     if (!hasReachedTarget) {
//       this.setX((int) (this.getX() + this.dx));
//       this.setY((int) (this.getY() + this.dy));
//
//       if (Math.abs(this.getX() - this.targetX) <= FBTT_WIDTH / 2 &&
//           Math.abs(this.getY() - this.targetY) <= FBTT_HEIGHT / 2) {
//         hasReachedTarget = true;
//         this.setX(this.targetX);
//         this.setY(this.targetY);
//       }
//     }
//   }
// }
//
//

public class FireballToThrow extends Entity implements Scrollable {
  public static final int FBTT_WIDTH = 20; // Initial width of the fireball
  public static final int FBTT_HEIGHT = 20; // Initial height of the fireball
  private int targetX, targetY;
  private double time; // Time for parabolic motion
  private boolean reachedTarget = false;
  private int scrollSpeed = FBTT_DEFAULT_SCROLL_SPEED;

  public static final int FBTT_DEFAULT_SCROLL_SPEED = 5;
  private int initialX, initialY;
  private double velocityX, velocityY, gravity;

  public FireballToThrow(int startX, int startY) {
    super(startX, startY, FBTT_WIDTH, FBTT_HEIGHT, "assets/fireball.png");
    this.initialX = startX;
    this.initialY = startY;
    this.gravity = 0.2; // Controls the curvature of the parabola
    this.time = 0;
  }

  public void setTarget(int targetX, int targetY) {
    this.targetX = targetX;
    this.targetY = targetY;

    // Calculate horizontal and vertical velocities
    double distanceX = targetX - initialX;
    double distanceY = targetY - initialY;
    this.velocityX = distanceX / 50; // Adjust to control speed
    this.velocityY = -distanceY / 50 - (gravity * 50) / 2; // Adjust to control height
  }

  public boolean hasReachedTarget() {
    return reachedTarget;
  }

  public void setScrollSpeed(int newSpeed) {
    this.scrollSpeed = newSpeed;
  }

  public int getScrollSpeed() {
    return this.scrollSpeed;
  }

  @Override
  public void scroll() {
    if (reachedTarget)
      return;

    // Update position using parabolic equations
    double newX = initialX + velocityX * time;
    double newY = initialY + velocityY * time + 0.5 * gravity * time * time;

    setX((int) newX);
    setY((int) newY);

    // Adjust fireball size as it approaches the midpoint
    double totalDistance = Math.hypot(targetX - initialX, targetY - initialY);
    double currentDistance = Math.hypot(getX() - initialX, getY() - initialY);
    double scale = Math.min(1.5, 1.0 + (currentDistance / totalDistance) * 0.5); // Max 1.5x size
    setWidth((int) (FBTT_WIDTH * scale));
    setHeight((int) (FBTT_HEIGHT * scale));

    // Check if the fireball reached the target
    if (Math.abs(getX() - targetX) < 5 && Math.abs(getY() - targetY) < 5) {
      reachedTarget = true;
    }

    // Increment time for the next frame
    time++;
  }
}

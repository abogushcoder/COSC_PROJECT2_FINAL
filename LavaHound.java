public class LavaHound extends Avoid {
  public static final String LHOUND_IMAGE_FILE = "assets/lava_hound.png";

  public static final int LHOUND_WIDTH = 109;
  public static final int LHOUND_HEIGHT = 142;

  protected static final int LHOUND_DMG_VALUE = -1;

  public LavaHound() {
    this(0, 0);
  }

  public LavaHound(int x, int y) {
    this(x, y, LHOUND_IMAGE_FILE);
  }

  public LavaHound(int x, int y, String imageName) {
    this(x, y, LHOUND_WIDTH, LHOUND_HEIGHT, imageName);
  }

  public LavaHound(int x, int y, int width, int height, String imageName) {
    super(x, y, width, height, imageName);
  }

  public int getScoreModifier() {
    return 0;
  }

  public int getHPModifier() {
    return LHOUND_DMG_VALUE;
  }

}

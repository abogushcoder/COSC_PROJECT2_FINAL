public class MiniLavaHound extends LavaHound {
  public static final String MLHOUND_IMAGE_FILE = "assets/Mini_Lavahound_Image.png";

  // Dimensions of the Avoid
  public static final int MLHOUND_WIDTH = 184;
  public static final int MLHOUND_HEIGHT = 120;

  protected static final int MLHOUND_DMG_VALUE = -1;

  public MiniLavaHound() {
    this(0, 0);
  }

  public MiniLavaHound(int x, int y) {
    this(x, y, MLHOUND_IMAGE_FILE);
  }

  public MiniLavaHound(int x, int y, String imageName) {
    this(x, y, MLHOUND_WIDTH, MLHOUND_HEIGHT, imageName);
  }

  public MiniLavaHound(int x, int y, int width, int height, String imageName) {
    super(x, y, width, height, imageName);
  }

  public int getScoreModifier() {
    return 0;
  }

  public int getHPModifier() {
    return MLHOUND_DMG_VALUE;
  }

}

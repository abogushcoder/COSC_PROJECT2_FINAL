public class IceSpirit extends Avoid {
  public static final String ISPIRIT_IMAGE_FILE = "bogushAssets/ice_spirit.png";

  public static final int ISPIRIT_WIDTH = 112;
  public static final int ISPIRIT_HEIGHT = 112;

  protected static final int ISPIRIT_POINTS_PENALTY = 0;

  public IceSpirit() {
    this(0, 0);
  }

  public IceSpirit(int x, int y) {
    this(x, y, ISPIRIT_IMAGE_FILE);
  }

  public IceSpirit(int x, int y, String imageName) {
    this(x, y, ISPIRIT_WIDTH, ISPIRIT_HEIGHT, imageName);
  }

  public IceSpirit(int x, int y, int width, int height, String imageName) {
    super(x, y, width, height, imageName);
  }

  // I'm missing something here...
  // What's special about RareAvoids?
  public int getScoreModifier() {
    return ISPIRIT_POINTS_PENALTY;
  }

  public int getHPModifier() {
    return 0;
  }

}

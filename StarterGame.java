import java.awt.*;
import java.awt.event.*;
import java.util.*;

//A Starter version of the scrolling game, featuring Avoids, Collects, RareAvoids, and RareCollects
//Players must reach a score threshold to win.
//If player runs out of HP (via too many Avoid/RareAvoid collisions) they lose.
public class StarterGame extends GameEngine2D {

  // Starting Player coordinates
  protected static final int STARTING_PLAYER_X = 0;
  protected static final int STARTING_PLAYER_Y = 100;

  // Score needed to win the game
  protected static final int SCORE_TO_WIN = 300;

  // Maximum that the game speed can be increased to
  // (a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
  protected static final int MAX_GAME_SPEED = 300;
  // Interval that the speed changes when pressing speed up/down keys
  protected static final int SPEED_CHANGE_INTERVAL = 20;

  public static final String INTRO_SPLASH_FILE = "assets/splash.gif";
  // Key pressed to advance past the splash screen
  public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;

  // Interval that Entities get spawned in the game window
  // ie: once every how many ticks does the game attempt to spawn new Entities
  protected static final int SPAWN_INTERVAL = 45;

  // A Random object for all your random number generation needs!
  public static final Random rand = new Random();

  // player's current score
  protected int score;

  // Stores a reference to game's Player object for quick reference (Though this
  // Player presumably
  // is also in the DisplayList, but it will need to be referenced often)
  protected Player player;

  public StarterGame() {
    this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  public StarterGame(int gameWidth, int gameHeight) {
    super(gameWidth, gameHeight);
  }

  // Performs all of the initialization operations that need to be done before the
  // game starts
  protected void pregame() {
    this.setBgColor(Color.BLACK);
    this.player = new Player(STARTING_PLAYER_X, STARTING_PLAYER_Y);
    this.entities.add(player);
    this.score = 0;
    this.setSplashImage(INTRO_SPLASH_FILE);
  }

  // Called on each game tick
  protected void updateGame() {
    // scroll all scrollable Entities on the game board
    scrollEntities();
    // Spawn new entities only at a certain interval
    if (super.getTicksElapsed() % SPAWN_INTERVAL == 0) {
      spawnEntities();
    }
    updateTitleText();
    gcOffscreenEntities();
  }

  // Update the text at the top of the game window
  protected void updateTitleText() {
    setTitleText("HP: " + this.player.getHP() + ", Score: " + this.score);
  }

  // Scroll all scrollable entities per their respective scroll speeds
  protected void scrollEntities() {
    for (int i = 0; i < this.entities.size(); i++) {
      if (this.entities.get(i) instanceof Scrollable) {
        Scrollable entityToScroll = (Scrollable) this.entities.get(i);
        entityToScroll.scroll();
      }
    }
    for (Entity ele : this.getAllCollisions(this.player)) {
      this.collidedWithPlayer((Consumable) ele);
    }

  }

  // Handles "garbage collection" of the entities
  // Flags entities in the displaylist that are no longer relevant
  // (i.e. will no longer need to be drawn in the game window).
  protected void gcOffscreenEntities() {
    for (int i = 0; i < this.entities.size(); i++) {
      if (this.entities.get(i).getX() + this.entities.get(i).getWidth() < 0) {
        this.entities.get(i).setGCFlag(true);
      }
    }
  }

  // Called whenever it has been determined that the Player collided with a
  // consumable
  private void collidedWithPlayer(Consumable collidedWith) {
    score += collidedWith.getScoreModifier();
    this.player.modifyHP(collidedWith.getHPModifier());
    ((Entity) (collidedWith)).setGCFlag(true);

  }

  // Spawn new Entities on the right edge of the game board
  private void spawnEntities() {
    int randAvoid = (int) (Math.random() * 2) + 1;
    int randRareAvoid = (int) (Math.random() * 2);
    int randCollect = (int) (Math.random() * 2) + 1;
    int randRareCollect = (int) (Math.random() * 2);

    for (int i = 0; i < randAvoid; i++) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - Avoid.AVOID_HEIGHT));
      Entity entityToSpawn = new Avoid(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }

    }

    for (int i = 0; i < randRareAvoid; i++) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - RareAvoid.RAVOID_HEIGHT));
      Entity entityToSpawn = new RareAvoid(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }

    }

    for (int i = 0; i < randCollect; i++) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - Collect.COLLECT_HEIGHT));
      Entity entityToSpawn = new Collect(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }

    }

    for (int i = 0; i < randRareCollect; i++) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - RareCollect.RCOLLECT_HEIGHT));
      Entity entityToSpawn = new RareCollect(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }
    }

  }

  // Called once the game is over, performs any end-of-game operations
  protected void postgame() {
    if (this.player.getHP() == 0) {
      super.setTitleText("GAME OVER - You Won!");
    } else {
      super.setTitleText("GAME OVER - You Lose!");
    }

  }

  // Returns a boolean indicating if the game is over (true) or not (false)
  // Game can be over due to either a win or lose state
  protected boolean isGameOver() {
    if (this.score >= SCORE_TO_WIN) {
      return true;
    }
    if (this.player.getHP() <= 0) {
      return true;
    }
    return false;

  }

  // Reacts to a single key press on the keyboard
  protected void keyReact(int key) {
    // TODO:code each function

    // if a splash screen is up, only react to the advance splash key
    if (getSplashImage() != null) {
      if (key == ADVANCE_SPLASH_KEY)
        super.setSplashImage(null);
      return;
    }

    if (!isPaused) {
      if (key == KEY_PAUSE_GAME) {
        isPaused = true;
      }
      if (key == UP_KEY && this.player.getY() - Player.DEFAULT_MOVEMENT_SPEED > 0) {
        this.player.setY(this.player.getY() - Player.DEFAULT_MOVEMENT_SPEED);
      }

      if (key == DOWN_KEY
          && this.player.getY() + Player.DEFAULT_MOVEMENT_SPEED < this.getWindowHeight() - this.player.getHeight()) {
        this.player.setY(this.player.getY() + Player.DEFAULT_MOVEMENT_SPEED);
      }
      // TODO: in the future might want to change DEFAULT_HEIGHT to this.getHeight in
      // Window.java
      if (key == RIGHT_KEY
          && this.player.getX() + Player.DEFAULT_MOVEMENT_SPEED < this.getWindowWidth() - this.player.getWidth()) {
        this.player.setX(this.player.getX() + Player.DEFAULT_MOVEMENT_SPEED);
      }

      if (key == LEFT_KEY && this.player.getX() - Player.DEFAULT_MOVEMENT_SPEED > 0) {
        this.player.setX(this.player.getX() - Player.DEFAULT_MOVEMENT_SPEED);
      }

      if (key == SPEED_UP_KEY && this.getGameSpeed() < MAX_GAME_SPEED) {
        this.setGameSpeed(this.getGameSpeed() + SPEED_CHANGE_INTERVAL);
      }

      if (key == SPEED_DOWN_KEY && this.getGameSpeed() > SPEED_CHANGE_INTERVAL) {
        this.setGameSpeed(this.getGameSpeed() + SPEED_CHANGE_INTERVAL);
      }

    } else if (isPaused) {
      if (key == KEY_PAUSE_GAME) {
        isPaused = false;
      }
    }

  }

  // Handles reacting to a single mouse click in the game window
  protected MouseEvent reactToMouseClick(MouseEvent click) {

    // Mouse functionality is not used at all in the Starter game...
    // you may want to override this function for a CreativeGame feature though!

    return click;// returns the mouse event for any child classes overriding this method
  }

}

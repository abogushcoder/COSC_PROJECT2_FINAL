import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class BogushGame extends StarterGame {

  protected static final int STARTING_PLAYER_X = 0;
  protected static final int STARTING_PLAYER_Y = 100;

  protected static final int SCORE_TO_WIN = 3;

  // Maximum that the game speed can be increased to
  // (a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
  protected static final int MAX_GAME_SPEED = 300;
  // Interval that the speed changes when pressing speed up/down keys
  protected static final int SPEED_CHANGE_INTERVAL = 20;

  public static final String INTRO_SPLASH_FILE = "bogushAssets/final_real_splash.png";
  // Key pressed to advance past the splash screen
  public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;

  // Interval that Entities get spawned in the game window
  // ie: once every how many ticks does the game attempt to spawn new Entities
  protected static final int SPAWN_INTERVAL = 45;

  // A Random object for all your random number generation needs!
  public static final Random rand = new Random();

  private Entity ability;
  private String abilityName;
  private Entity currentThrowable;
  private boolean isOnCooldown = false;
  // player's current score
  protected int score;

  // Stores a reference to game's Player object for quick reference (Though this
  // Player presumably
  // is also in the DisplayList, but it will need to be referenced often)
  protected Player player;
  private boolean isFrozen = false;
  private String playerState = "NORMAL";
  private Timer activeTimer;

  public BogushGame() {
    this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
  }

  public BogushGame(int gameWidth, int gameHeight) {
    super(gameWidth, gameHeight);
  }

  // Performs all of the initialization operations that need to be done before the
  // game starts
  protected void pregame() {
    this.setBgImage("bogushAssets/clashRoyaleBackground.jpg");
    this.player = new Player(STARTING_PLAYER_X, STARTING_PLAYER_Y, "bogushAssets/player.gif");
    this.entities.add(player);
    this.score = 0;
    this.ability = null;
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
    setTitleText("HP: " + this.player.getHP() + ", Crowns: " + this.score + ", Ability: " + this.abilityName);
  }

  // Scroll all scrollable entities per their respective scroll speeds
  protected void scrollEntities() {
    for (int i = 0; i < this.entities.size(); i++) {
      if (this.entities.get(i) instanceof Scrollable) {
        Scrollable entityToScroll = (Scrollable) this.entities.get(i);
        entityToScroll.scroll();

        if (entityToScroll instanceof FireballToThrow) {
          FireballToThrow fireball = (FireballToThrow) entityToScroll;

          if (fireball.hasReachedTarget()) {
            Entity explosion = new Explosion(fireball.getX(), fireball.getY());
            this.entities.add(explosion);
            this.currentThrowable = explosion;

            fireball.setGCFlag(true);

            Timer explosionTimer = new Timer();
            explosionTimer.schedule(new TimerTask() {
              public void run() {
                explosion.setGCFlag(true);
              }
            }, 200);
          }
        }
      }
    }

    for (Entity ele : this.getAllCollisions(this.player)) {
      if (ele instanceof Consumable) {
        this.collidedWithPlayer((Consumable) ele);
      }
    }

    if (this.currentThrowable != null) {
      for (Entity ele : this.getAllCollisions(this.currentThrowable)) {
        if (ele instanceof Consumable) {
          this.collidedWithThrowable(ele);
        }
      }
      if (this.currentThrowable.isFlaggedForGC()) {
        this.currentThrowable = null;
      }
    }
  }

  // Handles "garbage collection" of the entities
  // Flags entities in the displaylist that are no longer relevant
  // (i.e. will no longer need to be drawn in the game window).
  protected void gcOffscreenEntities() {
    for (int i = 0; i < this.entities.size(); i++) {
      if (this.entities.get(i).getX() + this.entities.get(i).getWidth() < 0
          || this.entities.get(i).getX() > this.getWindowWidth()) {
        this.entities.get(i).setGCFlag(true);
      }
    }
  }

  private void collidedWithThrowable(Entity collidedWith) {
    if (collidedWith instanceof Crate) {
      Entity crown = new Crown(collidedWith.getX(), collidedWith.getY());
      this.entities.add(crown);
      collidedWith.setGCFlag(true);
    }
    if (!(collidedWith instanceof Collectable) && !(collidedWith instanceof Crown)) {
      collidedWith.setGCFlag(true);
    }
  }

  // Called whenever it has been determined that the Player collided with a
  // consumable
  private void collidedWithPlayer(Consumable collidedWith) {
    if (isOnCooldown && collidedWith instanceof Collectable) {
      return;
    }
    if (this.ability != null && collidedWith instanceof Collectable) {
      return;
    }

    // Process normal collisions
    score += collidedWith.getScoreModifier();
    this.player.modifyHP(collidedWith.getHPModifier());
    ((Entity) collidedWith).setGCFlag(true);

    if (collidedWith instanceof MiniLavaHound) {
      this.setOnFire();
    } else if (collidedWith instanceof IceSpirit) {
      this.freezePlayer();
    } else if (collidedWith instanceof LavaHound) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - Player.PLAYER_HEIGHT));
      this.player.setX(0);
      this.player.setY(randYCoord);
      int currentX = ((Entity) collidedWith).getX();
      int currentY = ((Entity) collidedWith).getY();
      Entity replaceMiniLavaHound = new MiniLavaHound(currentX, currentY);
      this.entities.add(replaceMiniLavaHound);
    } else if (collidedWith instanceof Collectable) {
      this.ability = (Entity) collidedWith;
      this.abilityName = ((Collectable) collidedWith).getName();
    }
  }

  private void startCooldown() {
    isOnCooldown = true;
    this.abilityName = "Cooldown";

    Timer cooldownTimer = new Timer();
    cooldownTimer.schedule(new TimerTask() {
      public void run() {
        isOnCooldown = false;
        BogushGame.this.abilityName = "Ready";
      }
    }, 10000);
  }

  public void rollLog(MouseEvent click) {
    int mouseX = click.getX();
    int mouseY = click.getY();

    Entity log = new LogToRoll(mouseX, mouseY - LogToRoll.LOGTR_HEIGHT / 2);
    this.currentThrowable = log;
    this.entities.add(log);
    this.ability = null;

    startCooldown();

  }

  public void throwFireball(MouseEvent click) {
    int mouseX = click.getX();
    int mouseY = click.getY();

    if (mouseX <= this.player.getX() + Player.PLAYER_WIDTH) {
      return;
    }

    FireballToThrow fireball = new FireballToThrow(this.player.getX() + Player.PLAYER_WIDTH,
        this.player.getY() + Player.PLAYER_HEIGHT / 2 - FireballToThrow.FBTT_HEIGHT / 2);
    fireball.setTarget(mouseX, mouseY);
    this.currentThrowable = fireball;
    this.entities.add(fireball);
    this.ability = null;

    startCooldown();
  }

  public void freezePlayer() {
    if (!playerState.equals("NORMAL")) {
      return;
    }

    playerState = "FROZEN";
    isFrozen = true;
    this.player.setImage("bogushAssets/ice_cube.png");

    if (activeTimer != null) {
      activeTimer.cancel();
    }

    activeTimer = new Timer();

    activeTimer.schedule(new TimerTask() {
      public void run() {
        if (playerState.equals("FROZEN")) {
          BogushGame.this.player.setImage("bogushAssets/cracked_ice_cube.png");
        }
      }
    }, 500);

    activeTimer.schedule(new TimerTask() {
      public void run() {
        if (playerState.equals("FROZEN")) {
          BogushGame.this.player.setImage("bogushAssets/player.gif");
          playerState = "NORMAL";
          isFrozen = false;
        }
      }
    }, 1000);
  }

  public void setOnFire() {
    if (!playerState.equals("NORMAL")) {
      return;
    }

    playerState = "ON_FIRE";
    this.player.setImage("bogushAssets/fire.png");

    if (activeTimer != null) {
      activeTimer.cancel();
    }

    activeTimer = new Timer();

    activeTimer.schedule(new TimerTask() {
      public void run() {
        if (playerState.equals("ON_FIRE")) {
          BogushGame.this.player.setImage("bogushAssets/smoke_after_fire.png");
        }
      }
    }, 2000);

    activeTimer.schedule(new TimerTask() {
      public void run() {
        if (playerState.equals("ON_FIRE")) {
          BogushGame.this.player.setImage("bogushAssets/player.gif");
          playerState = "NORMAL";
        }
      }
    }, 2100);
  }

  // Spawn new Entities on the right edge of the game board
  private void spawnEntities() {
    double crateProbability = 1.0 / 10.0;
    double logFireballProbability = 1.0 / 6.0;

    if (Math.random() < crateProbability) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - Crate.CRATE_HEIGHT));
      Entity entityToSpawn = new Crate(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }
    }

    if (Math.random() < logFireballProbability) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - LogToCollect.LOGTC_HEIGHT));
      Entity entityToSpawn = new LogToCollect(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }
    }

    if (Math.random() < logFireballProbability) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - FireballToCollect.FBTC_HEIGHT));
      Entity entityToSpawn = new FireballToCollect(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }
    }

    int randLavaHound = (int) (Math.random() * 2);
    for (int i = 0; i < randLavaHound; i++) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - LavaHound.LHOUND_HEIGHT));
      Entity entityToSpawn = new LavaHound(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }
    }

    int randIceSpirit = (int) (Math.random() * 2);
    for (int i = 0; i < randIceSpirit; i++) {
      int randYCoord = (int) (Math.random() * (this.getWindowHeight() - IceSpirit.ISPIRIT_HEIGHT));
      Entity entityToSpawn = new IceSpirit(this.getWindowWidth(), randYCoord);
      if (this.getAllCollisions(entityToSpawn).size() < 1) {
        this.entities.add(entityToSpawn);
      }

    }

  }

  // Called once the game is over, performs any end-of-game operations
  protected void postgame() {
    if (this.player.getHP() == 0) {
      super.setTitleText("GAME OVER - You Lose!");
    } else {
      super.setTitleText("GAME OVER - You Win!");
    }

  }

  // Returns a boolean indicating if the game is over (true) or not (false)
  // Game can be over due to either a win or lose state
  protected boolean isGameOver() {
    if (this.score >= SCORE_TO_WIN) {
      for (int i = 0; i < this.entities.size(); i++) {
        this.entities.get(i).setGCFlag(true);
      }
      this.entities.performGC();
      setSplashImage("bogushAssets/hehe.gif");
      return true;
    }
    if (this.player.getHP() <= 0) {
      for (int i = 0; i < this.entities.size(); i++) {
        this.entities.get(i).setGCFlag(true);
      }
      this.entities.performGC();
      setSplashImage("bogushAssets/crycry.gif");

      return true;
    }
    return false;

  }

  // Reacts to a single key press on the keyboard
  protected void keyReact(int key) {

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
      if (!isFrozen) {
        if (key == UP_KEY && this.player.getY() - Player.DEFAULT_MOVEMENT_SPEED > 0) {
          this.player.setY(this.player.getY() - Player.DEFAULT_MOVEMENT_SPEED);
        }

        if (key == DOWN_KEY
            && this.player.getY() + Player.DEFAULT_MOVEMENT_SPEED < this.getWindowHeight() - this.player.getHeight()) {
          this.player.setY(this.player.getY() + Player.DEFAULT_MOVEMENT_SPEED);
        }
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
          this.setGameSpeed(this.getGameSpeed() - SPEED_CHANGE_INTERVAL);
        }
      }

    } else if (isPaused) {
      if (key == KEY_PAUSE_GAME) {
        isPaused = false;
      }
    }

  }

  // Handles reacting to a single mouse click in the game window
  protected MouseEvent reactToMouseClick(MouseEvent click) {
    if (this.ability != null && this.ability instanceof LogToCollect) {
      this.rollLog(click);
    }
    if (this.ability != null && this.ability instanceof FireballToCollect) {
      this.throwFireball(click);
    }

    return click;// returns the mouse event for any child classes overriding this method
  }

}

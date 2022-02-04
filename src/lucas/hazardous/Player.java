package lucas.hazardous;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private final int GAME_WIDTH;
    private int GAME_HEIGHT;

    public Player(int GAME_WIDTH, int GAME_HEIGHT, int playerX, int playerY) {
        this.GAME_HEIGHT = GAME_HEIGHT;
        this.GAME_WIDTH = GAME_WIDTH;
        this.playerX = playerX;
        this.playerY = playerY;
        this.lineX = this.playerX;
        this.lineY = this.playerY;
    }

    public static final int MAX_PLAYER_SPEED = 2;

    public static final int PLAYER_SIZE = 20;

    //checks for player movement
    private boolean isDriving = false;
    private boolean goingRight = false;
    private boolean goingLeft = false;

    //current player's position
    private int playerX;
    private int playerY;

    //values used for calculating speed vector
    private int speedTime = 0;
    private static final int TIME_TO_REACH_MAX_SPEED = 7;

    private float directionPlayerY = -1f;
    private boolean mainDir = true;
    private int directionChanger1 = 1;
    private int directionChanger2 = 1;

    //player's image
    public static BufferedImage PLAYER_IMG;
    static {
        try {
            PLAYER_IMG = ImageIO.read(new File("img.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //coordinates for drawing vector
    private int lastPlayerX = playerX;
    private int lastPlayerY = playerY;
    private int lineX;
    private int lineY;

    //methods for accessing variables from MainPanel
    public void setIsDriving(boolean isDriving) {
        this.isDriving = isDriving;
    }

    public void setGoingRight(boolean goingRight) {
        this.goingRight = goingRight;
    }

    public void setGoingLeft(boolean goingLeft) {
        this.goingLeft = goingLeft;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getLineX() {
        return lineX;
    }

    public int getLineY() {
        return lineY;
    }

    private void movePlayerWithDirection() {
        //save current velocity for later to draw vector
        lastPlayerX = playerX;
        lastPlayerY = playerY;

        //change speed on x-axis
        if(goingLeft) {
            directionChanger1 = 1;
            directionChanger2 = -1;
        } else if(goingRight) {
            directionChanger1 = -1;
            directionChanger2 = 1;
        }

        if(directionPlayerY > 0f) {
            if (mainDir) {
                playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * directionChanger1*(1f - directionPlayerY);
            } else {
                playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 *directionChanger2* (1f - directionPlayerY);
            }
        } else {
            if (mainDir) {
                playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * directionChanger1*(1f + directionPlayerY);
            } else {
                playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 *directionChanger2* (1f + directionPlayerY);
            }
        }

        //change speed on y-axis
        playerY += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * directionPlayerY;
        lineX = playerX*2-lastPlayerX;
        lineY = playerY*2-lastPlayerY;
    }

    public void playerMove() {
        if (isDriving) {
            //change rotation of speed vector
            if (goingRight || goingLeft) {
                if (directionPlayerY <= -1f) {
                    mainDir = false;
                }

                if (directionPlayerY >= 1f) {
                    mainDir = true;
                }

                if (mainDir) {
                    directionPlayerY -= .2f;
                } else {
                    directionPlayerY += .2f;
                }
            }

            //change speed
            movePlayerWithDirection();

            //increase time required to reach maximum speed
            if (speedTime < TIME_TO_REACH_MAX_SPEED) {
                speedTime += 1;
            }
        } else {
            //slowly stop player
            if (speedTime > 0) {
                speedTime -= 1;
                movePlayerWithDirection();
            }
        }
        //checks to prevent player from going out of the window
        if (playerX < 0) {
            playerX = 0;
        }
        if (playerY < 0) {
            playerY = 0;
        }

        if (playerX > GAME_WIDTH - PLAYER_SIZE) {
            playerX = GAME_WIDTH - PLAYER_SIZE;
        }

        if (playerY > GAME_HEIGHT - PLAYER_SIZE) {
            playerY = GAME_HEIGHT - PLAYER_SIZE;
        }
    }
}

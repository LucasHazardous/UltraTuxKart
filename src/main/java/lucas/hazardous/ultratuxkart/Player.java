package lucas.hazardous.ultratuxkart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {
    private final int GAME_WIDTH;
    private final int GAME_HEIGHT;

    public Player(int GAME_WIDTH, int GAME_HEIGHT, int playerX, int playerY) {
        this.GAME_HEIGHT = GAME_HEIGHT;
        this.GAME_WIDTH = GAME_WIDTH;
        this.playerX = playerX;
        this.playerY = playerY;
        this.lineX = this.playerX;
        this.lineY = this.playerY;
    }

    public static final double MAX_PLAYER_SPEED = 0.8;

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

    //player's image
    public static BufferedImage PLAYER_IMG;
    {
        try {
            PLAYER_IMG = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("img.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //coordinates for drawing vector
    private int lastPlayerX = playerX;
    private int lastPlayerY = playerY;
    private int lineX;
    private int lineY;

    //speed vector rotation angle
    private double angle = 270;

    //temporary storage for changing angle to radians
    private double angleRadians = Math.toRadians(angle);

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

    public double getAngleRadians() {
        return angleRadians;
    }

    private void movePlayerWithDirection() {
        //save current velocity for later to draw vector
        lastPlayerX = playerX;
        lastPlayerY = playerY;

        playerX += (int) ((Math.cos(angleRadians) * (Math.pow(speedTime, 2) * MAX_PLAYER_SPEED/2)) - Math.sin(angleRadians)*(Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED/2));
        playerY += (int) ((Math.sin(angleRadians) * (Math.pow(speedTime, 2) * MAX_PLAYER_SPEED/2)) + Math.cos(angleRadians)*(Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED/2));

        //change values used for drawing vectors
        lineX = playerX*2-lastPlayerX;
        lineY = playerY*2-lastPlayerY;
    }

    public void playerMove() {
        if (isDriving) {
            //change rotation of speed vector
            if(goingRight) {
                if(speedTime > 3) speedTime -= 1;
                angle += 20;
                if(angle >= 360) angle=0;
                angleRadians = Math.toRadians(angle);
            } else if(goingLeft) {
                if(speedTime > 3) speedTime -= 1;
                angle -= 20;
                if(angle <= 0) angle=360;
                angleRadians = Math.toRadians(angle);
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
        if (playerX <= 0) {
            playerX = 1;
        }
        if (playerY <= 0) {
            playerY = 1;
        }

        if (playerX >= GAME_WIDTH - PLAYER_SIZE) {
            playerX = GAME_WIDTH - PLAYER_SIZE-1;
        }

        if (playerY >= GAME_HEIGHT - PLAYER_SIZE) {
            playerY = GAME_HEIGHT - PLAYER_SIZE-1;
        }
    }
}

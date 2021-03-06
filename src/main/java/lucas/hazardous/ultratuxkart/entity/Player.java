package lucas.hazardous.ultratuxkart.entity;

import lucas.hazardous.ultratuxkart.sounds.PlayerSounds;

import java.awt.image.BufferedImage;

public class Player {
    private final int GAME_WIDTH;
    private final int GAME_HEIGHT;

    public static final double ACCELERATION = 0.8;

    public static final int PLAYER_SIZE = 20;

    private boolean isMovingForward = false;
    private boolean isMovingRight = false;
    private boolean isMovingLeft = false;

    private int playerX;
    private int playerY;

    private int speedTime = 0;
    public static final int TIME_TO_REACH_MAX_SPEED = 7;

    public int playerBoosts = 3;
    private static final int BOOST_STRENGTH = 3;

    public static BufferedImage PLAYER_IMG;

    private int lastPlayerX = playerX;
    private int lastPlayerY = playerY;

    private int lineX;
    private int lineY;

    private double speedVectorAngle = 270;

    private double speedVectorAngleRadians = Math.toRadians(speedVectorAngle);

    private final PlayerSounds playerSounds = new PlayerSounds();
    private boolean isSoundPlaying = true;

    public Player(int GAME_WIDTH, int GAME_HEIGHT, int playerX, int playerY) {
        this.GAME_HEIGHT = GAME_HEIGHT;
        this.GAME_WIDTH = GAME_WIDTH;

        this.playerX = playerX;
        this.playerY = playerY;

        this.lineX = this.playerX;
        this.lineY = this.playerY;
    }

    public int getSpeedTime() {
        return speedTime;
    }

    public void setIsDriving(boolean isDriving) {
        this.isMovingForward = isDriving;
    }

    public void setMovingRight(boolean movingRight) {
        this.isMovingRight = movingRight;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.isMovingLeft = movingLeft;
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

    public double getSpeedVectorAngleRadians() {
        return speedVectorAngleRadians;
    }

    public int getPlayerBoosts() {
        return playerBoosts;
    }

    public void useBoost() {
        if(playerBoosts > 0) {
            playerSounds.playBoost();
            playerBoosts--;
            speedTime += BOOST_STRENGTH;
        }
    }

    private void calculateNewPlayerPosition() {
        playerX += (int) ((Math.cos(speedVectorAngleRadians) * (Math.pow(speedTime, 2) * ACCELERATION/2)) - Math.sin(speedVectorAngleRadians)*(Math.pow(speedTime, 1.3) * ACCELERATION/2));
        playerY += (int) ((Math.sin(speedVectorAngleRadians) * (Math.pow(speedTime, 2) * ACCELERATION/2)) + Math.cos(speedVectorAngleRadians)*(Math.pow(speedTime, 1.3) * ACCELERATION/2));
    }

    private void saveCurrentPlayerPosition() {
        lastPlayerX = playerX;
        lastPlayerY = playerY;
    }

    private void calculateLinePosition() {
        lineX = playerX*2-lastPlayerX;
        lineY = playerY*2-lastPlayerY;
    }

    public void playerMove() {
        if (isMovingForward) {
            startPlayingEngineSoundIfNotStarted();

            if(isMovingRight)
                rotateRight();
            else if(isMovingLeft)
                rotateLeft();

            changeSpeedTime();

            if(isMovingRight || isMovingLeft)
                decreaseSpeedTimeWhenRotating();
        } else {
            decreaseSpeedIfNotMoving();
        }

        stopPlayingEngineSoundIfNotMoving();

        saveCurrentPlayerPosition();

        calculateNewPlayerPosition();

        calculateLinePosition();

        movePlayerBackIfOutOfWindow();
    }

    private void startPlayingEngineSoundIfNotStarted() {
        if(!isSoundPlaying || !playerSounds.isEngineSoundRunning()){
            isSoundPlaying = true;
            playerSounds.resumeEngineSound();
        }
    }

    private void stopPlayingEngineSoundIfNotMoving() {
        if(speedTime == 0) {
            isSoundPlaying = false;
            playerSounds.stopPlayingEngineSound();
        }
    }

    private void changeSpeedTime() {
        if (speedTime < TIME_TO_REACH_MAX_SPEED)
            speedTime++;
        else if(speedTime > TIME_TO_REACH_MAX_SPEED)
            speedTime--;
    }

    private void decreaseSpeedIfNotMoving() {
        if (speedTime > 0)
            speedTime--;
    }

    private void decreaseSpeedTimeWhenRotating() {
        if(speedTime > 3)
            speedTime -= 1;
    }

    private void rotateLeft() {
        speedVectorAngle -= 20;

        if(speedVectorAngle <= 0)
            speedVectorAngle = 360;

        speedVectorAngleRadians = Math.toRadians(speedVectorAngle);
    }

    private void rotateRight() {
        speedVectorAngle += 20;

        if(speedVectorAngle >= 360)
            speedVectorAngle = 0;

        speedVectorAngleRadians = Math.toRadians(speedVectorAngle);
    }

    private void movePlayerBackIfOutOfWindow() {
        if (playerX <= 0)
            playerX = 1;

        if (playerY <= 0)
            playerY = 1;

        if (playerX >= GAME_WIDTH - PLAYER_SIZE)
            playerX = GAME_WIDTH - PLAYER_SIZE - 1;

        if (playerY >= GAME_HEIGHT - PLAYER_SIZE)
            playerY = GAME_HEIGHT - PLAYER_SIZE - 1;
    }

    public void stopAllPlayerSounds() {
        playerSounds.stopPlayingEngineSound();
    }
}

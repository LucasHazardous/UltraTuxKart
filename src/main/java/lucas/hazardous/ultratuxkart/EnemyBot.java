package lucas.hazardous.ultratuxkart;

import java.util.List;

public class EnemyBot extends Thread{
    private List<List<Integer>> path;
    private int TILE_SIZE;

    private int botX;
    private int botY;

    private boolean isEndPointReached = false;

    public static final int BOT_SIZE = 20;

    private static final int MOVE_DELAY = 50;

    public EnemyBot(List<List<Integer>> path, int TILE_SIZE) {
        this.path = path;
        this.TILE_SIZE = TILE_SIZE;

        calculateBotStartingPosition();
    }

    public int getBotX() {
        return botX;
    }

    public int getBotY() {
        return botY;
    }

    public boolean isEndPointReached() {
        return isEndPointReached;
    }

    private void calculateBotStartingPosition() {
        botX = path.get(0).get(1) * TILE_SIZE+TILE_SIZE/2-BOT_SIZE/2;
        botY = path.get(0).get(0) * TILE_SIZE+TILE_SIZE/2-BOT_SIZE/2;
    }

    @Override
    public void run() {
        int targetX;
        int targetY;

        for(int i = 1; i < path.size(); i++) {
            targetX = path.get(i).get(1) * TILE_SIZE+TILE_SIZE/2-BOT_SIZE/2;
            targetY = path.get(i).get(0) * TILE_SIZE+TILE_SIZE/2-BOT_SIZE/2;

            while (
                botX != targetX ||
                botY != targetY
            ) {
                try {
                    Thread.sleep(MOVE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                changePositionX(targetX);

                changePositionY(targetY);
            }
        }

        isEndPointReached = true;
    }

    private void changePositionX(int targetX) {
        if(targetX > botX)
            botX += BOT_SIZE/4;
        else if (targetX < botX)
            botX -= BOT_SIZE/4;
    }

    private void changePositionY(int targetY) {
        if(targetY > botY)
            botY += BOT_SIZE/4;
        else if(targetY < botY)
            botY -= BOT_SIZE/4;
    }
}

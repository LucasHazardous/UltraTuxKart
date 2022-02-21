package lucas.hazardous.ultratuxkart;

import java.util.List;

//game bot that follows calculated path
public class GameBot extends Thread{
    private List<List<Integer>> path;
    private int TILE_SIZE;
    private int botX;
    private int botY;
    private boolean taskCompleted = false;

    public static final int BOT_SIZE = 20;

    public int getBotX() {
        return botX;
    }

    public int getBotY() {
        return botY;
    }

    public boolean isTaskCompleted() {
        return taskCompleted;
    }

    public GameBot(List<List<Integer>> path, int TILE_SIZE) {
        this.path = path;
        this.TILE_SIZE = TILE_SIZE;

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
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                if(targetX > botX) {
                    botX += BOT_SIZE/2;
                } else if (targetX < botX){
                    botX -= BOT_SIZE/2;
                }

                if(targetY > botY) {
                    botY += BOT_SIZE/2;
                } else if(targetY < botY) {
                    botY -= BOT_SIZE/2;
                }
            }
        }
        taskCompleted = true;
    }
}

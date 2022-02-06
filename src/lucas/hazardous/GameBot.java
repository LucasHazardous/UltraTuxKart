package lucas.hazardous;

import java.util.List;

//game bot that follows calculated path
public class GameBot extends Thread{
    private List<List<Integer>> path;
    private int TILE_SIZE;
    private int botX;
    private int botY;

    public static final int BOT_SIZE = 20;

    public int getBotX() {
        return botX;
    }

    public int getBotY() {
        return botY;
    }

    public GameBot(List<List<Integer>> path, int TILE_SIZE) {
        this.path = path;
        this.TILE_SIZE = TILE_SIZE;
    }

    @Override
    public void run() {
        for(int i = 0; i < path.size(); i++) {
            botX = path.get(i).get(1) * TILE_SIZE+TILE_SIZE/2-BOT_SIZE/2;
            botY = path.get(i).get(0) * TILE_SIZE+TILE_SIZE/2-BOT_SIZE/2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}

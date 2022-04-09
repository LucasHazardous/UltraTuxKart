package lucas.hazardous.ultratuxkart;

import java.awt.*;

public class MapEngine {
    private byte[][] map;
    private final int TILE_SIZE;

    public MapEngine(byte[][] map, int TILE_SIZE) {
        this.map = map;
        this.TILE_SIZE = TILE_SIZE;
    }

    public void drawMap(Graphics g) {
        for (int row = 0; row < map.length; row++) {
            for (int tile = 0; tile < map[row].length; tile++) {
                if (map[row][tile] == 1) {
                    g.setColor(Color.gray);
                } else if(map[row][tile] == 0) {
                    g.setColor(Color.green);
                }
                g.fillRect(tile * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}

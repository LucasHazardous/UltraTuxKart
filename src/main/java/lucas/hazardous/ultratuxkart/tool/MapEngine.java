package lucas.hazardous.ultratuxkart.tool;

import lucas.hazardous.ultratuxkart.Main;

import java.awt.*;

public record MapEngine(byte[][] map) {

    public void drawMap(Graphics g) {
        for (int row = 0; row < map.length; row++) {
            for (int tile = 0; tile < map[row].length; tile++) {

                if (map[row][tile] == 1)
                    g.setColor(Color.gray);
                else if (map[row][tile] == 0)
                    g.setColor(Color.green);

                g.fillRect(tile * Main.TILE_SIZE, row * Main.TILE_SIZE, Main.TILE_SIZE, Main.TILE_SIZE);
            }
        }
    }
}

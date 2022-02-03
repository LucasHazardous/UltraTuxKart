package lucas.hazardous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainPanel extends JPanel implements ActionListener {
    private final int GAME_WIDTH = 500;
    private final int GAME_HEIGHT = 500;
    private final int TILE_SIZE = 100;
    private static final int DELAY = 100;

    private Timer timer;

    private boolean isGameRunning;

    private byte[][] map = new byte[][]{
            {0,0,0,1,1},
            {0,0,1,1,1},
            {0,0,1,1,0},
            {0,0,0,1,1},
            {0,0,0,1,1}
    };

    MainPanel() {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new ControllKeyadapter());

        isGameRunning = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawEverything(g);
    }

    private void drawEverything(Graphics g) {
        if(isGameRunning) {
            for(int row = 0; row < map.length; row++) {
                for(int tile = 0; tile < map[row].length; tile++) {
                    if(map[row][tile] == 1) {
                        g.setColor(Color.gray);
                    } else {
                        g.setColor(Color.green);
                    }
                    g.fillRect(tile*TILE_SIZE, row*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class ControllKeyadapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

        }
    }
}

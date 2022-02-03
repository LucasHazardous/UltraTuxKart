package lucas.hazardous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainPanel extends JPanel implements ActionListener {
    //sizes of components
    private static final int GAME_WIDTH = 500;
    private static final int GAME_HEIGHT = 500;
    private static final int TILE_SIZE = 100;

    //game essential variables
    private static final int DELAY = 100;
    private Timer timer;
    private boolean isGameRunning;

    //game map
    private byte[][] map = new byte[][]{
            {0,0,0,1,1},
            {0,0,1,1,1},
            {0,0,1,1,0},
            {0,0,0,1,1},
            {1,1,1,1,1}
    };

    //player stats
    private static final int MAX_PLAYER_SPEED = 50;
    private static final int PLAYER_SIZE = 10;
    private int currentPlayerSpeed = 0;
    private int player_direction_x = 0;
    private int player_direction_y = 0;
    private static final int PLAYER_DIRECTION_RANGE = 3;
    private boolean isDriving = false;
    private int playerX = 0;
    private int playerY = GAME_WIDTH-TILE_SIZE/2;

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
            //draw map
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

            //draw player
            g.setColor(Color.red);
            g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        }
    }

    private void playerMove() {
        if(isDriving) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isGameRunning) {
            playerMove();
        }
        repaint();
    }

    private class ControllKeyadapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    isDriving = true;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    isDriving = false;
                    break;
            }
        }
    }
}

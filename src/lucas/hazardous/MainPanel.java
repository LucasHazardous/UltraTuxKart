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
            {0, 0, 0, 1, 1},
            {0, 0, 1, 1, 1},
            {0, 0, 1, 1, 0},
            {0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1}
    };

    //player stats
    private static final int MAX_PLAYER_SPEED = 1;
    private static final int PLAYER_SIZE = 10;

    private boolean isDriving = false;
    private boolean goingRight = false;
    private boolean goingLeft = false;
    private boolean goingBack = false;

    private int playerX = 9;
    private int playerY = GAME_HEIGHT / 2;

    private int speedTime = 0;
    private static final int TIME_TO_REACH_MAX_SPEED = 7;

    private float directionPlayerY = -1f;
    private boolean mainDir = true;

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
        if (isGameRunning) {
            //draw map
            for (int row = 0; row < map.length; row++) {
                for (int tile = 0; tile < map[row].length; tile++) {
                    if (map[row][tile] == 1) {
                        g.setColor(Color.gray);
                    } else {
                        g.setColor(Color.green);
                    }
                    g.fillRect(tile * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

            //draw player
            g.setColor(Color.red);
            g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        }
    }

    private void playerMove() {
        if (isDriving) {
            if (goingRight) {
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

            if(directionPlayerY > 0f) {
                if (mainDir) {
                    playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * -(1f - directionPlayerY);
                } else {
                    playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * (1f - directionPlayerY);
                }
            } else {
                if (mainDir) {
                    playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * -(1f + directionPlayerY);
                } else {
                    playerX += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * (1f + directionPlayerY);
                }
            }



            playerY += (Math.pow(speedTime, 1.3) * MAX_PLAYER_SPEED) / 2 * directionPlayerY;


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

            if (speedTime < TIME_TO_REACH_MAX_SPEED) {
                speedTime += 1;
            }
        } else {
            if (speedTime > 0) {
                speedTime -= 1;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameRunning) {
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
                case KeyEvent.VK_D:
                    goingRight = true;
                    break;
                case KeyEvent.VK_A:
                    goingLeft = true;
                    break;
                case KeyEvent.VK_S:
                    goingBack = true;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    isDriving = false;
                    speedTime = 0;
                    break;
                case KeyEvent.VK_D:
                    goingRight = false;
                    break;
                case KeyEvent.VK_A:
                    goingLeft = false;
                    break;
                case KeyEvent.VK_S:
                    goingBack = false;
                    break;
            }
        }
    }
}

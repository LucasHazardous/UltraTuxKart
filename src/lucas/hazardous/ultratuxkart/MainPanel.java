package lucas.hazardous.ultratuxkart;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Photo by Carlos from Pexels - vulcan.jpg
//Photo by Kateryna Babaieva from Pexels - factory.jpg

public class MainPanel extends JPanel implements ActionListener {
    //sizes of components
    private static final int GAME_WIDTH = 500;
    private static final int GAME_HEIGHT = 500;
    private static final int TILE_SIZE = 100;

    //is bot present in the game
    private static boolean isBotEnabled;

    public static void setIsBotEnabled(boolean isBotEnabled) {
        MainPanel.isBotEnabled = isBotEnabled;
    }

    //game map
    private static byte[][] map = new byte[][]{
            {0, 0, 0, 1, 1},
            {0, 0, 1, 1, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 1, 1},
            {1, 1, 1, 1, 1}
    };

    public static void setMap(byte[][] map) {
        MainPanel.map = map;
    }

    private final List<Integer> mapStartingPoint = new ArrayList<>();
    private final List<Integer> mapTargetPoint = new ArrayList<>();

    //path from mapStartingPoint to mapTargetPoint
    private List<List<Integer>> path;

    private GameBot bot1;

    {
        //set points for player and bot's path
        mapTargetPoint.add(0);
        mapTargetPoint.add(map[0].length - 1);

        mapStartingPoint.add(map.length - 1);
        mapStartingPoint.add(0);

        //calculate the fastest path for bot
        path = findPathToTarget(mapTargetPoint, mapStartingPoint, new ArrayList<List<Integer>>(), new ArrayList<List<Integer>>());

        if(isBotEnabled) {
            //initialize new game bot
            bot1 = new GameBot(path, TILE_SIZE);
            bot1.start();
        }
    }

    //essential variables
    private static final int DELAY = 100;
    private Timer timer;
    private boolean isGameRunning;
    private final Player player = new Player(GAME_WIDTH, GAME_HEIGHT,
            mapStartingPoint.get(1) * TILE_SIZE + TILE_SIZE / 2-Player.PLAYER_SIZE/2,
            mapStartingPoint.get(0) * TILE_SIZE + TILE_SIZE / 2-Player.PLAYER_SIZE/2);

    MainPanel() {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new ControlKeyAdapter());

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
                        if((tile+1)*TILE_SIZE > player.getPlayerX() &&
                                tile*TILE_SIZE < player.getPlayerX() &&
                                (row+1)*TILE_SIZE > player.getPlayerY() &&
                                row*TILE_SIZE < player.getPlayerY()
                        ) {
                            isGameRunning = false;
                        }
                    }
                    g.fillRect(tile * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

            //draw path for bot
            for (List<Integer> point : path) {
                g.setColor(Color.cyan);
                g.fillRect(point.get(1) * TILE_SIZE + TILE_SIZE / 2, point.get(0) * TILE_SIZE + TILE_SIZE / 2, 2, 2);
            }

            //rotating player's image
            AffineTransform transform = AffineTransform.getRotateInstance(player.getAngleRadians(), Player.PLAYER_IMG.getWidth()/2, Player.PLAYER_IMG.getHeight()/2);
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

            //draw player
            g.drawImage(op.filter(Player.PLAYER_IMG, null), player.getPlayerX(), player.getPlayerY(), Player.PLAYER_SIZE, Player.PLAYER_SIZE, null);

            //draw speed vector
            g.setColor(Color.red);
            g.drawLine(player.getPlayerX(), player.getPlayerY(), player.getLineX(), player.getLineY());

            //draw bot
            if(isBotEnabled)
            g.fillRect(bot1.getBotX(), bot1.getBotY(), GameBot.BOT_SIZE, GameBot.BOT_SIZE);

            //check if player reached the target
            if(player.getPlayerX() > mapTargetPoint.get(1)*TILE_SIZE &&
                    player.getPlayerX() < (mapTargetPoint.get(1)+1)*TILE_SIZE &&
                    player.getPlayerY() > mapTargetPoint.get(0)*TILE_SIZE &&
                    player.getPlayerY() < (mapTargetPoint.get(0)+1)*TILE_SIZE) {
                endGame(g, "assets/factory.jpg", "Noxious conditions evaded.");
                timer.stop();
            }

            if(isBotEnabled && bot1.isTaskCompleted()) isGameRunning = false;
        } else {
            endGame(g, "assets/vulcan.jpg", "Ethereal operator ceased existence.");
            timer.stop();
        }
    }

    //show endgame screen when player interacts with the green
    private void endGame(Graphics g, String imagePath, String text) {
        try {
            Image endPicture = ImageIO.read(new File(imagePath));
            g.drawImage(endPicture, 0, 0, null);
            g.setFont(new Font("Monospaced", Font.BOLD, TILE_SIZE/5));
            g.setColor(new Color(55, 255, 0));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(text, (GAME_WIDTH - metrics.stringWidth(text))/2, GAME_HEIGHT/2);
        } catch (IOException e) {}
    }

    //find path on map from starting point to target
    private List findPathToTarget(List<Integer> targetPosition, List<Integer> currentPosition, List solution, List visited) {
        //base case - found point is target point
        if (currentPosition.equals(targetPosition)) {
            solution.add(currentPosition);
            return solution;
        } else {
            int bestResultLength = 2147483646;

            List result = new ArrayList();
            result.addAll(solution);
            result.add(currentPosition);

            //all locations that are possible to reach from current point
            int[][] options = new int[][]{
                    {currentPosition.get(0) + 1, currentPosition.get(1)},
                    {currentPosition.get(0) - 1, currentPosition.get(1)},
                    {currentPosition.get(0), currentPosition.get(1) - 1},
                    {currentPosition.get(0), currentPosition.get(1) + 1}
            };

            //store temporary result from recursion
            List tmpResult;

            //placeholder for int[] from options
            List currentOptionButList;

            //add current position to visited to avoid visiting it again
            visited.add(currentPosition);

            //check every possible location
            for (int[] option : options) {
                currentOptionButList = new ArrayList<Integer>();
                currentOptionButList.add(option[0]);
                currentOptionButList.add(option[1]);

                //check if location exists, if it's a part if path and was not visited
                if ((option[0] >= 0) && (option[0] < map.length) && (option[1] >= 0) && (option[1] < map[0].length)
                        && map[option[0]][option[1]] == 1 && !visited.contains(currentOptionButList)) {

                    tmpResult = findPathToTarget(targetPosition, currentOptionButList, new ArrayList<List<Integer>>(result), new ArrayList<List<Integer>>(visited));

                    if (tmpResult.size() < bestResultLength && tmpResult.size() != 0 && tmpResult.get(tmpResult.size()-1).equals(targetPosition)) {
                        bestResultLength = tmpResult.size();
                        result = new ArrayList<List<Integer>>(tmpResult);
                    }
                }
            }

            return result;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameRunning) {
            player.playerMove();
        }
        repaint();
    }

    //player controls
    private class ControlKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setIsDriving(true);
                    break;
                case KeyEvent.VK_D:
                    player.setGoingRight(true);
                    break;
                case KeyEvent.VK_A:
                    player.setGoingLeft(true);
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setIsDriving(false);
                    break;
                case KeyEvent.VK_D:
                    player.setGoingRight(false);
                    break;
                case KeyEvent.VK_A:
                    player.setGoingLeft(false);
                    break;
            }
        }
    }
}
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//Photo by Carlos from Pexels - vulcan.jpg
//Photo by Kateryna Babaieva from Pexels - factory.jpg

public class GamePanel extends JPanel implements ActionListener {
    //sizes of components
    private static final int GAME_WIDTH = 500;
    private static final int GAME_HEIGHT = 500;
    public static final int TILE_SIZE = 100;

    //is bot present in the game
    private static boolean isBotEnabled;

    public static void setIsBotEnabled(boolean isBotEnabled) {
        GamePanel.isBotEnabled = isBotEnabled;
    }

    //game map
    private static byte[][] map = new byte[][]{
            {0, 0, 0, 1, 2},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 1, 1, 0}
    };

    public static void setMap(byte[][] map) {
        GamePanel.map = map;
    }

    private final List<Integer> mapStartingPoint = new ArrayList<>();
    private static List<Integer> mapTargetPoint = new ArrayList<>();

    public static void setMapTargetPoint(List<Integer> mapTargetPoint) {
        GamePanel.mapTargetPoint = mapTargetPoint;
    }

    //path from mapStartingPoint to mapTargetPoint
    private List<List<Integer>> path;

    private EnemyBot bot1;

    //variables for pathfinding
    private int bestResultLength = map.length*map[0].length;
    private List<List<List<Integer>>> results = new ArrayList<>();

    private MapEngine mapEngine = new MapEngine(map, TILE_SIZE);

    static {
        mapTargetPoint.add(0);
        mapTargetPoint.add(4);
    }

    {
        //set starting point
        mapStartingPoint.add(map.length - 1);
        mapStartingPoint.add(0);

        //calculate the fastest path for bot
        findPathToTarget(mapTargetPoint, mapStartingPoint, new ArrayList<>());
        if(results.size() > 0) {
            path = results.get(results.size() - 1);
        } else {
            isBotEnabled = false;
        }

        if(isBotEnabled) {
            //initialize new game bot
            bot1 = new EnemyBot(path, TILE_SIZE);
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

    private MainFrame parentFrame;

    public void setPlayerSkin(String chosenSkin) {
        switch (chosenSkin) {
            case "Ice":
                try {
                    Player.PLAYER_IMG = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("iceSkin.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    GamePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;

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
            mapEngine.drawMap(g);

            //check if player is alive
            if(map[player.getPlayerY()/TILE_SIZE][player.getPlayerX()/TILE_SIZE] == 0) {
                isGameRunning = false;
            }

            //draw path for bot
            if(isBotEnabled) {
                for (List<Integer> point : path) {
                    g.setColor(Color.cyan);
                    g.fillRect(point.get(1) * TILE_SIZE + TILE_SIZE / 2, point.get(0) * TILE_SIZE + TILE_SIZE / 2, 2, 2);
                }
            }

            //draw target point
            g.setColor(Color.orange);
            g.fillRect(mapTargetPoint.get(1)*TILE_SIZE, mapTargetPoint.get(0)*TILE_SIZE, TILE_SIZE, TILE_SIZE);

            //rotating player's image
            AffineTransform transform = AffineTransform.getRotateInstance(player.getSpeedVectorAngleRadians(), Player.PLAYER_IMG.getWidth()/2, Player.PLAYER_IMG.getHeight()/2);
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

            //draw player
            g.drawImage(op.filter(Player.PLAYER_IMG, null), player.getPlayerX(), player.getPlayerY(), Player.PLAYER_SIZE, Player.PLAYER_SIZE, null);

            //draw player's boost
            g.setColor(Color.CYAN);
            g.drawString("|".repeat(player.getPlayerBoosts()), player.getPlayerX(), player.getPlayerY());

            //draw speed vector
            g.setColor(Color.red);
            g.drawLine(player.getPlayerX(), player.getPlayerY(), player.getLineX(), player.getLineY());

            //draw bot
            if(isBotEnabled)
            g.fillRect(bot1.getBotX(), bot1.getBotY(), EnemyBot.BOT_SIZE, EnemyBot.BOT_SIZE);

            //check if player reached the target
            if(player.getPlayerX() > mapTargetPoint.get(1)*TILE_SIZE &&
                    player.getPlayerX() < (mapTargetPoint.get(1)+1)*TILE_SIZE &&
                    player.getPlayerY() > mapTargetPoint.get(0)*TILE_SIZE &&
                    player.getPlayerY() < (mapTargetPoint.get(0)+1)*TILE_SIZE) {
                endGame(g, this.getClass().getClassLoader().getResourceAsStream("factory.jpg"), "\uD83D\uDE03\uD83D\uDC4D");
                timer.stop();
            }

            if(isBotEnabled && bot1.isEndPointReached()) isGameRunning = false;
        } else {
            endGame(g, this.getClass().getClassLoader().getResourceAsStream("vulcan.jpg"), "\uD83D\uDE1E\uD83D\uDC4E");
            timer.stop();
        }
    }

    //show endgame screen when player interacts with the green
    private void endGame(Graphics g, InputStream image, String text) {
        try {
            Image endPicture = ImageIO.read(image);
            g.drawImage(endPicture, 0, 0, null);

            g.setFont(new Font("Monospaced", Font.BOLD, TILE_SIZE/5));
            g.setColor(Color.white);
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(text, (GAME_WIDTH - metrics.stringWidth(text))/2, GAME_HEIGHT/2);
            image.close();
        } catch (IOException e) {}
    }

    //find path on map from starting point to target
    private List<List<Integer>> findPathToTarget(List<Integer> targetPosition, List<Integer> currentPosition, List<List<Integer>> solution) {
        //base case - found point is target point
        if (currentPosition.equals(targetPosition)) {
            solution.add(currentPosition);
            return solution;
        } else {
            List<List<Integer>> result = new ArrayList<>(solution);
            result.add(currentPosition);

            //all locations that are possible to reach from current point
            int[][] options = new int[][]{
                    {currentPosition.get(0) + 1, currentPosition.get(1)},
                    {currentPosition.get(0) - 1, currentPosition.get(1)},
                    {currentPosition.get(0), currentPosition.get(1) - 1},
                    {currentPosition.get(0), currentPosition.get(1) + 1}
            };

            //store temporary result from recursion
            List<List<Integer>> tmpResult;

            //placeholder for int[] from options
            List<Integer> currentOptionButList;

            //check every possible location
            for (int[] option : options) {
                currentOptionButList = new ArrayList<>();
                currentOptionButList.add(option[0]);
                currentOptionButList.add(option[1]);

                //check if location exists, if it's a part if path and was not visited
                if ((option[0] >= 0) && (option[0] < map.length) && (option[1] >= 0) && (option[1] < map[0].length)
                        && map[option[0]][option[1]] != 0 && !result.contains(currentOptionButList)) {

                    tmpResult = findPathToTarget(targetPosition, currentOptionButList, new ArrayList<>(result));

                    if (tmpResult.size() < bestResultLength && tmpResult.size() > 1 && tmpResult.get(tmpResult.size()-1).equals(targetPosition)) {
                        bestResultLength = tmpResult.size();
                        result = new ArrayList<>(tmpResult);
                        results.add(result);
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
                    player.setMovingRight(true);
                    break;
                case KeyEvent.VK_A:
                    player.setMovingLeft(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    parentFrame.changePanelToMenu();
                    break;
                case KeyEvent.VK_SPACE:
                    player.useBoost();
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
                    player.setMovingRight(false);
                    break;
                case KeyEvent.VK_A:
                    player.setMovingLeft(false);
                    break;
            }
        }
    }
}
package lucas.hazardous.ultratuxkart.panel;

import lucas.hazardous.ultratuxkart.MainFrame;
import lucas.hazardous.ultratuxkart.entity.EnemyBot;
import lucas.hazardous.ultratuxkart.entity.Player;
import lucas.hazardous.ultratuxkart.tool.MapEngine;
import lucas.hazardous.ultratuxkart.tool.Pathfinder;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel implements ActionListener {
    private static final int GAME_WIDTH = 500;
    private static final int GAME_HEIGHT = 500;

    public static final int TILE_SIZE = 100;

    private static boolean isBotEnabled;

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

    private final List<Integer> mapStartingPoint = new ArrayList<>(Arrays.asList(map.length-1, 0));

    private static List<Integer> mapTargetPoint = new ArrayList<>();

    private List<List<Integer>> enemyBotPath;

    private EnemyBot enemyBot;

    private final MapEngine mapEngine = new MapEngine(map, TILE_SIZE);

    private static final int TIMER_DELAY = 100;
    private final Timer timer;

    private boolean canPlayerMove = true;

    private final Player player = new Player(GAME_WIDTH, GAME_HEIGHT,
            mapStartingPoint.get(1) * TILE_SIZE + TILE_SIZE / 2-Player.PLAYER_SIZE/2,
            mapStartingPoint.get(0) * TILE_SIZE + TILE_SIZE / 2-Player.PLAYER_SIZE/2);

    private final MainFrame parentFrame;

    public GamePanel(MainFrame parentFrame) {
        Pathfinder pathfinder = new Pathfinder(map);

        if(isBotEnabled) {
            enemyBotPath = pathfinder.findPathToTarget(mapTargetPoint, mapStartingPoint);
            enemyBot = new EnemyBot(enemyBotPath, TILE_SIZE);
            enemyBot.start();
        }

        this.parentFrame = parentFrame;

        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new PlayerControls());

        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }

    static
    {
        mapTargetPoint.add(0);
        mapTargetPoint.add(4);
    }

    public static void setIsBotEnabled(boolean isBotEnabled) {
        GamePanel.isBotEnabled = isBotEnabled;
    }

    public static void setMapTargetPoint(List<Integer> mapTargetPoint) {
        GamePanel.mapTargetPoint = mapTargetPoint;
    }

    public void setPlayerSkin(String chosenSkin) throws IOException {
        switch (chosenSkin) {
            case "Ice" ->
                    Player.PLAYER_IMG = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("images/iceSkin.png")));
            case "Fire" ->
                    Player.PLAYER_IMG = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("images/fireSkin.png")));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            drawEverything(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawEverything(Graphics g) throws IOException {
        mapEngine.drawMap(g);

        drawTargetPoint(g);

        if(isBotEnabled) {
            drawBotPath(g);
            drawBot(g);
        }

        drawPlayer(g);

        drawPlayerBoosts(g);

        drawPlayerSpeedVector(g);

        endGameIfPlayerWon(g);

        stopGameIfPlayerLost(g);
    }

    private void drawBotPath(Graphics g) {
        for (List<Integer> point : enemyBotPath) {
            g.setColor(Color.cyan);
            g.fillRect(point.get(1) * TILE_SIZE + TILE_SIZE / 2, point.get(0) * TILE_SIZE + TILE_SIZE / 2, 2, 2);
        }
    }

    private void drawTargetPoint(Graphics g) {
        g.setColor(Color.orange);
        g.fillRect(mapTargetPoint.get(1)*TILE_SIZE, mapTargetPoint.get(0)*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void drawPlayer(Graphics g) {
        AffineTransform transform = AffineTransform.getRotateInstance(player.getSpeedVectorAngleRadians(), Player.PLAYER_IMG.getWidth()/2, Player.PLAYER_IMG.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        g.drawImage(op.filter(Player.PLAYER_IMG, null), player.getPlayerX(), player.getPlayerY(), Player.PLAYER_SIZE, Player.PLAYER_SIZE, null);
    }

    private void drawPlayerBoosts(Graphics g) {
        g.setColor(Color.CYAN);
        g.drawString("|".repeat(player.getPlayerBoosts()), player.getPlayerX(), player.getPlayerY());
    }

    private void drawPlayerSpeedVector(Graphics g) {
        g.setColor(Color.red);
        g.drawLine(player.getPlayerX(), player.getPlayerY(), player.getLineX(), player.getLineY());
    }

    private void drawBot(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(enemyBot.getBotX(), enemyBot.getBotY(), EnemyBot.BOT_SIZE, EnemyBot.BOT_SIZE);
    }

    private void endGameIfPlayerWon(Graphics g) throws IOException {
        if(player.getPlayerX() > mapTargetPoint.get(1)*TILE_SIZE &&
                player.getPlayerX() < (mapTargetPoint.get(1)+1)*TILE_SIZE &&
                player.getPlayerY() > mapTargetPoint.get(0)*TILE_SIZE &&
                player.getPlayerY() < (mapTargetPoint.get(0)+1)*TILE_SIZE) {

            //Photo by Kateryna Babaieva from Pexels
            showEndScreen(g, this.getClass().getClassLoader().getResourceAsStream("images/factory.jpg"), "\uD83D\uDE03\uD83D\uDC4D");
            timer.stop();
            canPlayerMove = false;
        }
    }

    private void stopGameIfPlayerLost(Graphics g) throws IOException {
        if((isBotEnabled && enemyBot.isEndPointReached()) ||
                (map[player.getPlayerY()/TILE_SIZE][player.getPlayerX()/TILE_SIZE] == 0)) {
            //Photo by Carlos from Pexels
            showEndScreen(g, this.getClass().getClassLoader().getResourceAsStream("images/vulcan.jpg"), "\uD83D\uDE1E\uD83D\uDC4E");
            timer.stop();
            canPlayerMove = false;
        }
    }

    private void showEndScreen(Graphics g, InputStream image, String text) throws IOException {
        Image endPicture = ImageIO.read(image);
        g.drawImage(endPicture, 0, 0, null);
        image.close();

        g.setFont(new Font("Monospaced", Font.BOLD, TILE_SIZE/5));
        g.setColor(Color.white);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(text, (GAME_WIDTH - metrics.stringWidth(text))/2, GAME_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (canPlayerMove) {
            player.playerMove();
        }
        repaint();
    }

    private class PlayerControls extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> player.setIsDriving(true);
                case KeyEvent.VK_D -> player.setMovingRight(true);
                case KeyEvent.VK_A -> player.setMovingLeft(true);
                case KeyEvent.VK_ESCAPE -> parentFrame.changePanelToMenu();
                case KeyEvent.VK_SPACE -> player.useBoost();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> player.setIsDriving(false);
                case KeyEvent.VK_D -> player.setMovingRight(false);
                case KeyEvent.VK_A -> player.setMovingLeft(false);
            }
        }
    }
}
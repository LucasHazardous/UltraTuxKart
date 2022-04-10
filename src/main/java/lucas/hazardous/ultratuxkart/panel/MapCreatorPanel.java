package lucas.hazardous.ultratuxkart.panel;

import lucas.hazardous.ultratuxkart.MainFrame;
import lucas.hazardous.ultratuxkart.tool.MapEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapCreatorPanel extends JPanel {
    private MainFrame parentFrame;

    private static List<Integer> mapTargetPoint = new ArrayList<>();

    private static final int TILE_SIZE = GamePanel.TILE_SIZE;

    private static byte[][] map = new byte[][]{
            {0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
    };
    private MapEngine mapEngine = new MapEngine(map, TILE_SIZE);

    public MapCreatorPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.setFocusable(true);
        this.addMouseListener(new PanelMouseListener());
        this.addKeyListener(new PanelKeyListener());
    }

    static {
        mapTargetPoint.add(0);
        mapTargetPoint.add(4);
    }

    public static void setMap(byte[][] newMap) {
        map = newMap;
    }

    public static void setMapTargetPoint(List<Integer> newTargetPoint) {
        mapTargetPoint = newTargetPoint;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        mapEngine.drawMap(g);

        drawTargetPoint(g);
    }

    private void drawTargetPoint(Graphics g) {
        g.setColor(Color.orange);
        g.fillRect(mapTargetPoint.get(1)*TILE_SIZE, mapTargetPoint.get(0)*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private class PanelMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int tmpX = e.getX()/TILE_SIZE;
            int tmpY = e.getY()/TILE_SIZE;

            if(map[tmpY][tmpX] != 2)
                map[tmpY][tmpX] = (byte) (map[tmpY][tmpX] == 0 ? 1 : 0);

            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    private class PanelKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) parentFrame.changePanelToMenu();

            else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                try {
                    saveMapToFile();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(parentFrame, ex);
                }
            }
        }
    }

    private void saveMapToFile() throws IOException{
        String filename = generateRandomFilename();
        File mapFile = new File(filename);

        if(mapFile.createNewFile()) {
            FileWriter fileWriter = new FileWriter(filename);

            String result = "";
            for (int row = 0; row < map.length; row++) {
                for (int tile = 0; tile < map[row].length; tile++) {
                    result += map[row][tile];
                }
                result += "\n";
            }

            fileWriter.write(result);
            fileWriter.close();
        }
    }

    private String generateRandomFilename() {
        Random random = new Random();
        String filename = random.nextInt(99999999) + ".map";
        File file = new File(filename);

        while(file.exists()) {
            filename = random.nextInt(99999999) + ".map";
            file = new File(filename);
        }

        return filename;
    }
}

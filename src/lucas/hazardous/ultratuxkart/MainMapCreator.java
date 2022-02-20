package lucas.hazardous.ultratuxkart;

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

public class MainMapCreator extends JPanel {
    private MainFrame parentFrame;
    private static List<Integer> mapTargetPoint = new ArrayList<>();
    private static final int TILE_SIZE = MainPanel.TILE_SIZE;

    private static byte[][] map = new byte[][]{
            {0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
    };

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

    MainMapCreator(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.setFocusable(true);
        this.addMouseListener(new PanelMouseListener());
        this.addKeyListener(new PanelKeyListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw map
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

        //draw target point
        g.setColor(Color.orange);
        g.fillRect(mapTargetPoint.get(1)*TILE_SIZE, mapTargetPoint.get(0)*TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private class PanelMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int tmpX = e.getX()/TILE_SIZE;
            int tmpY = e.getY()/TILE_SIZE;
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
                File savedMap = new File("custom_map.map");

                try {
                    if(savedMap.createNewFile()) {
                        FileWriter fileWriter = new FileWriter("custom_map.map");

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
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(parentFrame, ex);
                }
            }
        }
    }
}

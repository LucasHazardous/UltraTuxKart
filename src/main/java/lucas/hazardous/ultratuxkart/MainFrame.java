package lucas.hazardous.ultratuxkart;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private MainMenu menu = new MainMenu(this);

    private byte[][] map = new byte[5][5];
    private List<Integer> mapTargetPoint;

    private boolean isBotEnabled = true;

    public static final int PANEL_WIDTH = 500;
    public static final int PANEL_HEIGHT = 500;

    private GameSettingsMenuPanel gameSettingsMenuPanel = new GameSettingsMenuPanel(this);

    MainFrame() {
        this.add(menu);
        this.setTitle("UltraTuxKart");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void setBotEnabled(boolean botEnabled) {
        isBotEnabled = botEnabled;
    }

    public void setMap(File mapPath) {
        try {
            loadMapFile(mapPath);

            GamePanel.setMap(map);
            GamePanel.setMapTargetPoint(mapTargetPoint);

            MapCreatorPanel.setMap(map);
            MapCreatorPanel.setMapTargetPoint(mapTargetPoint);

            JOptionPane.showMessageDialog(this, "Map loaded.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found.");
        }
    }

    private void loadMapFile(File mapPath) throws FileNotFoundException {
        Scanner in = new Scanner(mapPath);
        int j = 0;
        mapTargetPoint = new ArrayList<>();

        while (in.hasNextLine()) {
            String tmp = in.nextLine();

            for(int i = 0; i < tmp.length(); i++) {
                this.map[j][i] = (byte) (tmp.charAt(i)-48);

                if(this.map[j][i] == 2) {
                    mapTargetPoint.add(j);
                    mapTargetPoint.add(i);
                }
            }
            j++;
        }
    }

    public void changePanelToGame(String chosenSkin) {
        getContentPane().removeAll();

        GamePanel.setIsBotEnabled(isBotEnabled);
        GamePanel game = new GamePanel(this);
        game.setPlayerSkin(chosenSkin);
        add(game);

        refocusRevalidateRepaint();
        game.requestFocus(false);
    }

    public void changePanelToMenu() {
        getContentPane().removeAll();
        add(menu);

        refocusRevalidateRepaint();
    }

    public void changePanelToMapCreator() {
        getContentPane().removeAll();

        var tmpCreator = new MapCreatorPanel(this);
        add(tmpCreator);

        refocusRevalidateRepaint();
        tmpCreator.requestFocus(false);
    }

    public void changePanelToGameSettings() {
        getContentPane().removeAll();
        add(gameSettingsMenuPanel);

        refocusRevalidateRepaint();
        gameSettingsMenuPanel.requestFocus();
    }

    private void refocusRevalidateRepaint() {
        this.setFocusable(true);
        this.revalidate();
        this.repaint();
    }
}

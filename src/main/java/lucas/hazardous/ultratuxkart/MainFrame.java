package lucas.hazardous.ultratuxkart;

import lucas.hazardous.ultratuxkart.panel.*;
import lucas.hazardous.ultratuxkart.sounds.MenuSounds;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private final MainMenuPanel menu = new MainMenuPanel(this);

    private final byte[][] map = new byte[5][5];
    private List<Integer> mapTargetPoint;

    private boolean isBotEnabled = true;

    private final GameSettingsMenuPanel gameSettingsMenuPanel = new GameSettingsMenuPanel(this);

    public static final Dimension GAME_PANEL_SIZE = new Dimension(Main.PANEL_WIDTH, (int) (Main.PANEL_HEIGHT+Main.DASHBOARD_PANEL_HEIGHT*1.75));

    public static final Dimension MENU_SIZE = new Dimension(new Dimension(Main.PANEL_WIDTH, Main.PANEL_HEIGHT));

    private final MenuSounds menuSounds;

    public MainFrame() {
        this.add(menu);
        this.setTitle("UltraTuxKart");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        menuSounds = new MenuSounds();
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

        JPanel masterPanel = getNewMasterPanel();

        GamePanel game = getNewGamePanel(chosenSkin);

        masterPanel.add(new DashboardPanel(game.getPlayer()));
        masterPanel.add(game);

        add(masterPanel);

        setSize(GAME_PANEL_SIZE);

        game.requestFocus(false);
        refocusRevalidateRepaint();
    }

    private GamePanel getNewGamePanel(String chosenSkin) {
        GamePanel.setIsBotEnabled(isBotEnabled);
        GamePanel game = new GamePanel(this);
        try {
            game.setPlayerSkin(chosenSkin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return game;
    }

    private JPanel getNewMasterPanel() {
        JPanel masterPanel = new JPanel();
        masterPanel.setPreferredSize(GAME_PANEL_SIZE);
        masterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        masterPanel.setBorder(BorderFactory.createEmptyBorder());
        return masterPanel;
    }

    public void changePanelToMenu() {
        getContentPane().removeAll();
        add(menu);
        setSize(MENU_SIZE);

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
        menuSounds.stopMenuSound();
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

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
    private boolean isBotEnabled = true;

    public void setBotEnabled(boolean botEnabled) {
        isBotEnabled = botEnabled;
    }

    public void setMap(File mapPath) {
        try {
            Scanner in = new Scanner(mapPath);
            int j = 0;
            List<Integer> mapTargetPoint = new ArrayList<>();
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
            MainPanel.setMap(map);
            MainPanel.setMapTargetPoint(mapTargetPoint);
            MainMapCreator.setMap(map);
            MainMapCreator.setMapTargetPoint(mapTargetPoint);
            JOptionPane.showMessageDialog(this, "Map loaded.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found.");
        }
    }

    public void changePanelToGame(String chosenSkin) {
        this.getContentPane().removeAll();
        MainPanel.setIsBotEnabled(isBotEnabled);
        MainPanel game = new MainPanel(this);
        game.setPlayerSkin(chosenSkin);
        this.add(game);
        this.setFocusable(true);
        this.revalidate();
        this.repaint();
        game.requestFocus(false);
    }

    public void changePanelToMenu() {
        this.getContentPane().removeAll();
        this.add(menu);
        this.revalidate();
        this.repaint();
    }

    public void changePanelToMapCreator() {
        var tmpCreator = new MainMapCreator(this);
        this.getContentPane().removeAll();
        this.add(tmpCreator);
        this.setFocusable(true);
        this.revalidate();
        this.repaint();
        tmpCreator.requestFocus(false);
    }

    MainFrame() {
        this.add(menu);
        this.setTitle("UltraTuxKart");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

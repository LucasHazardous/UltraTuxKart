package lucas.hazardous;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private MainMenu Menu = new MainMenu(this);
    private byte[][] map = new byte[5][5];
    private boolean isBotEnabled = true;

    public void setBotEnabled(boolean botEnabled) {
        isBotEnabled = botEnabled;
    }

    public void setMap(File mapPath) {
        try {
            Scanner in = new Scanner(mapPath);
            int j = 0;
            while (in.hasNextLine()) {
                String tmp = in.nextLine();
                for(int i = 0; i < tmp.length(); i++) {
                    this.map[j][i] = (byte) (tmp.charAt(i)-48);
                }
                j++;
            }
            MainPanel.setMap(map);
            JOptionPane.showMessageDialog(this, "Map loaded.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found.");
        }
    }

    public void changePanel() {
        this.getContentPane().removeAll();
        MainPanel.setIsBotEnabled(isBotEnabled);
        MainPanel game = new MainPanel();
        this.add(game);
        this.setFocusable(true);
        this.revalidate();
        this.repaint();
        game.requestFocus(false);
    }

    MainFrame() {
        this.add(Menu);
        this.setTitle("UltraTuxKart");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

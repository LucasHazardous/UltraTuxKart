package lucas.hazardous;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private MainMenu Menu = new MainMenu(this);
    private byte[][] map = new byte[5][5];

    public void setMap(File mapPath) {
        try {
            Scanner in = new Scanner(mapPath);
            int j = 0;
            while (in.hasNextLine()) {
                String tmp = in.nextLine();
                for(int i = 0; i < tmp.length(); i++) {
                    this.map[j][i] = (byte) (tmp.charAt(i)-48);
                    System.out.println(this.map[j][i]);
                }
                j++;
            }
            MainPanel.setMap(map);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found.");
        }
    }

    public void changePanel() {
        this.getContentPane().removeAll();
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

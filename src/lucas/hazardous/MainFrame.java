package lucas.hazardous;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
    private MainMenu Menu = new MainMenu(this);

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

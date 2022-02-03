package lucas.hazardous;

import javax.swing.*;

public class MainFrame extends JFrame {
    MainFrame() {
        this.add(new MainPanel());
        this.setTitle("UltraTuxKart");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

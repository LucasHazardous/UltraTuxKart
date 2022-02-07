package lucas.hazardous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    private static final int MENU_WIDTH = 500;
    private static final int MENU_HEIGHT = 500;

    public MainMenu(MainFrame parentFrame) {
        this.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        this.setFocusable(true);

        JButton btnSwitch = new JButton("Play");
        btnSwitch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.changePanel();
            }
        });
        btnSwitch.setBounds(MENU_WIDTH/2, MENU_HEIGHT/2, 100, 50);
        this.add(btnSwitch);
    }
}

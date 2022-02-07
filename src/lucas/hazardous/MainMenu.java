package lucas.hazardous;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private static final int MENU_WIDTH = 500;
    private static final int MENU_HEIGHT = 500;

    private Image background;

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

        try {
            //Photo by thiago japyassu from Pexels
            background = ImageIO.read(new File("assets/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0,0, null);
    }
}

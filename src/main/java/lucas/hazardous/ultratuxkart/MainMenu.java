package lucas.hazardous.ultratuxkart;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainMenu extends JPanel {
    private Image background;

    public MainMenu(MainFrame parentFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(MainFrame.PANEL_WIDTH, MainFrame.PANEL_HEIGHT));
        setFocusable(true);

        //add author's nick
        JLabel authorLabel = new JLabel("Lucas Hazardous");
        authorLabel.setForeground(Color.RED);
        add(authorLabel);

        //place buttons roughly in the center
        add(Box.createRigidArea(new Dimension(MainFrame.PANEL_WIDTH/4, MainFrame.PANEL_HEIGHT/4)));

        JButton gameSettings = new JButton("Play");
        gameSettings.addActionListener(e -> parentFrame.changePanelToGameSettings());
        add(gameSettings);

        //loading main menu background image
        try {
            //Photo by thiago japyassu from Pexels
            background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("background.jpg"));
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

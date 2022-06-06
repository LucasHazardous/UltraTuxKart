package lucas.hazardous.ultratuxkart.panel;

import lucas.hazardous.ultratuxkart.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    private Image background;

    private final MainFrame parentFrame;

    public MainMenuPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(MainFrame.MENU_SIZE);
        setFocusable(true);

        addAuthorLabel();

        add(Box.createRigidArea(new Dimension(MainFrame.PANEL_WIDTH/4, MainFrame.PANEL_HEIGHT/4)));

        addPlayButton();

        try {
            loadBackgroundImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAuthorLabel() {
        JLabel authorLabel = new JLabel("Lucas Hazardous");
        authorLabel.setForeground(Color.RED);
        add(authorLabel);
    }

    private void addPlayButton() {
        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> parentFrame.changePanelToGameSettings());
        add(playButton);
    }

    private void loadBackgroundImage() throws IOException {
        //Photo by thiago japyassu from Pexels
        background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/background.jpg"));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0,0, null);
    }
}

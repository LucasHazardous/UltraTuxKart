package lucas.hazardous.ultratuxkart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private static final int MENU_WIDTH = 500;
    private static final int MENU_HEIGHT = 500;

    private boolean isBotEnabled = true;

    private Image background;

    public MainMenu(MainFrame parentFrame) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        this.setFocusable(true);

        //button for starting the game
        JButton btnSwitch = new JButton("Play");
        btnSwitch.addActionListener(e -> parentFrame.changePanelToGame());
        this.add(btnSwitch);

        //loading custom map
        JButton btnMap = new JButton("Load map");
        btnMap.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Map files", "map");
            chooser.setFileFilter(extensionFilter);
            int chooserResponse = chooser.showOpenDialog(null);
            if(chooserResponse == JFileChooser.APPROVE_OPTION) parentFrame.setMap(new File(chooser.getSelectedFile().getAbsolutePath()));
        });
        this.add(btnMap);

        JButton btnEnableBot = new JButton("Disable bot");
        btnEnableBot.addActionListener(e -> {
            isBotEnabled = !isBotEnabled;
            if(isBotEnabled) {
                btnEnableBot.setText("Disable bot");
                parentFrame.setBotEnabled(true);
            } else {
                btnEnableBot.setText("Enable bot");
                parentFrame.setBotEnabled(false);
            }
        });
        this.add(btnEnableBot);

        //loading main menu background image
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

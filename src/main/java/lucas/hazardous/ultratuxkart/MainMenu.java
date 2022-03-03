package lucas.hazardous.ultratuxkart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainMenu extends JPanel {

    private static final int MENU_WIDTH = 500;
    private static final int MENU_HEIGHT = 500;

    private boolean isBotEnabled = true;

    private Image background;

    private static final String[] skinChoices = new String[]{"Fire", "Ice"};

    public MainMenu(MainFrame parentFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        setFocusable(true);

        //place buttons roughly in the center
        add(Box.createRigidArea(new Dimension(MENU_WIDTH/4, MENU_HEIGHT/4)));

        //add combobox for choosing player's skin
        JComboBox<String> comboBox = new JComboBox<>(skinChoices);
        comboBox.setPrototypeDisplayValue("skin");
        comboBox.setMaximumSize(comboBox.getPreferredSize());
        add(comboBox);

        String[] texts = new String[6];
        //load texts for buttons
        Scanner textsScanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream("english.language"));
        byte i = 0;
        while (textsScanner.hasNext() && i < 7) {
            texts[i] = textsScanner.nextLine();
            i++;
        }

        //button for starting the game
        JButton btnSwitch = new JButton(texts[0]);
        btnSwitch.addActionListener(e -> parentFrame.changePanelToGame((String) comboBox.getSelectedItem()));
        add(btnSwitch);

        //button for creating maps
        JButton btnCreator = new JButton(texts[1]);
        btnCreator.addActionListener(e -> parentFrame.changePanelToMapCreator());
        add(btnCreator);

        //loading custom map
        JButton btnMap = new JButton(texts[2]);
        btnMap.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(texts[3], "map");
            chooser.setFileFilter(extensionFilter);
            int chooserResponse = chooser.showOpenDialog(null);
            if(chooserResponse == JFileChooser.APPROVE_OPTION) parentFrame.setMap(new File(chooser.getSelectedFile().getAbsolutePath()));
        });
        add(btnMap);

        JButton btnEnableBot = new JButton(texts[4]);
        btnEnableBot.addActionListener(e -> {
            isBotEnabled = !isBotEnabled;
            if(isBotEnabled) {
                btnEnableBot.setText(texts[4]);
                parentFrame.setBotEnabled(true);
            } else {
                btnEnableBot.setText(texts[5]);
                parentFrame.setBotEnabled(false);
            }
        });
        add(btnEnableBot);

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

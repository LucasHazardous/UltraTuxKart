package lucas.hazardous.ultratuxkart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GameSettingsMenuPanel extends JPanel {
    private static final String[] skinChoices = new String[]{"Fire", "Ice"};
    private boolean isBotEnabled = true;
    private Image background;
    private MainFrame parentFrame;

    public GameSettingsMenuPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setFocusable(true);
        addKeyListener(new PanelKeyListener());

        String[] texts = new String[6];
        //load texts for buttons
        Scanner textsScanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream("english.language"));
        byte i = 0;
        while (textsScanner.hasNext() && i < 7) {
            texts[i] = textsScanner.nextLine();
            i++;
        }

        //add combobox for choosing player's skin
        JComboBox<String> comboBox = new JComboBox<>(skinChoices);
        comboBox.setPrototypeDisplayValue("skin");
        comboBox.setMaximumSize(comboBox.getPreferredSize());
        add(comboBox);

        //add empty space
        add(Box.createRigidArea(new Dimension(MainFrame.PANEL_WIDTH/4, MainFrame.PANEL_HEIGHT/4)));

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

        //button for disabling/enabling bot
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

        //button for starting the game
        JButton btnSwitch = new JButton(texts[0]);
        btnSwitch.addActionListener(e -> parentFrame.changePanelToGame((String) comboBox.getSelectedItem()));
        add(btnSwitch);

        //load background
        try {
            //Photo by Binyamin Mellish from Pexels
            background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("settings.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0,0, null);
    }

    private class PanelKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) parentFrame.changePanelToMenu();

        }
    }
}

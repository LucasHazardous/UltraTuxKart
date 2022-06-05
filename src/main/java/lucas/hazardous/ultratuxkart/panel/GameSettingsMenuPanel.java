package lucas.hazardous.ultratuxkart.panel;

import lucas.hazardous.ultratuxkart.MainFrame;

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
    private static final String[] SKIN_OPTIONS = new String[]{"Fire", "Ice"};
    private boolean isBotEnabled = true;
    private Image background;
    private final MainFrame parentFrame;
    private String[] texts;
    private JComboBox<String> comboBoxPlayerSkin;

    public GameSettingsMenuPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setFocusable(true);
        addKeyListener(new PanelKeyListener());

        loadButtonTexts("english.language");

        addSkinSelectorComboBox();

        add(Box.createRigidArea(new Dimension(MainFrame.PANEL_WIDTH/4, MainFrame.PANEL_HEIGHT/4)));

        addMapCreatorButton();

        addLoadCustomMapButton();

        addEnableDisableBotButton();

        addGameStartButton();

        try {
            loadBackground();
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

    private void addSkinSelectorComboBox() {
        comboBoxPlayerSkin = new JComboBox<>(SKIN_OPTIONS);
        comboBoxPlayerSkin.setPrototypeDisplayValue("skin");
        comboBoxPlayerSkin.setMaximumSize(comboBoxPlayerSkin.getPreferredSize());
        add(comboBoxPlayerSkin);
    }

    private void loadButtonTexts(String languageFile) {
        texts = new String[6];
        Scanner textsScanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream(languageFile));
        byte i = 0;
        while (textsScanner.hasNext() && i < 7) {
            texts[i] = textsScanner.nextLine();
            i++;
        }
    }

    private void addMapCreatorButton() {
        JButton btnCreator = new JButton(texts[1]);
        btnCreator.addActionListener(e -> parentFrame.changePanelToMapCreator());
        add(btnCreator);
    }

    private void addLoadCustomMapButton() {
        JButton btnMap = new JButton(texts[2]);

        btnMap.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(texts[3], "map");
            chooser.setFileFilter(extensionFilter);

            int chooserResponse = chooser.showOpenDialog(null);
            if(chooserResponse == JFileChooser.APPROVE_OPTION)
                parentFrame.setMap(new File(chooser.getSelectedFile().getAbsolutePath()));
        });

        add(btnMap);
    }

    private void addEnableDisableBotButton() {
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
    }

    private void addGameStartButton() {
        JButton btnSwitch = new JButton(texts[0]);
        btnSwitch.addActionListener(e -> parentFrame.changePanelToGame((String) comboBoxPlayerSkin.getSelectedItem()));
        add(btnSwitch);
    }

    private void loadBackground() throws IOException {
        //Photo by Binyamin Mellish from Pexels
        background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/settings.jpg"));
    }
}

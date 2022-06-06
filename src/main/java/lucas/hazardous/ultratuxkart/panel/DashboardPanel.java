package lucas.hazardous.ultratuxkart.panel;

import lucas.hazardous.ultratuxkart.MainFrame;
import lucas.hazardous.ultratuxkart.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static lucas.hazardous.ultratuxkart.MainFrame.TIMER_DELAY;

public class DashboardPanel extends JPanel implements ActionListener {
    private final Timer timer;

    private final Player player;

    public DashboardPanel(Player player) {
        this.player = player;

        this.setPreferredSize(new Dimension(MainFrame.PANEL_WIDTH, MainFrame.DASHBOARD_PANEL_HEIGHT));
        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlayerStats(g);
    }

    private void drawPlayerStats(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0,MainFrame.PANEL_WIDTH, MainFrame.DASHBOARD_PANEL_HEIGHT);
        for(int i = 0; i < player.getPlayerBoosts(); i++) {
            g.setColor(Color.cyan);
            g.fillRect(MainFrame.PANEL_WIDTH/5 + 50*i, MainFrame.DASHBOARD_PANEL_HEIGHT/2, 10, 10);
        }
    }
}

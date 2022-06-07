package lucas.hazardous.ultratuxkart.panel;

import lucas.hazardous.ultratuxkart.Main;
import lucas.hazardous.ultratuxkart.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPanel extends JPanel implements ActionListener {

    private final Player player;
    private final int PLAYER_STARTING_BOOSTS;

    public DashboardPanel(Player player) {
        this.player = player;
        PLAYER_STARTING_BOOSTS = player.getPlayerBoosts();

        this.setPreferredSize(new Dimension(Main.PANEL_WIDTH, Main.DASHBOARD_PANEL_HEIGHT));
        Timer timer = new Timer(Main.TIMER_DELAY, this);
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
        g.fillRect(0,0,Main.PANEL_WIDTH, Main.DASHBOARD_PANEL_HEIGHT);
        g.setColor(Color.cyan);
        g.drawRect(Main.PANEL_WIDTH/5-5, Main.DASHBOARD_PANEL_HEIGHT/2-5, PLAYER_STARTING_BOOSTS*20, 20);
        for(int i = 0; i < player.getPlayerBoosts(); i++) {
            g.fillRect(Main.PANEL_WIDTH/5 + 20*i, Main.DASHBOARD_PANEL_HEIGHT/2, 10, 10);
        }
    }
}

package lucas.hazardous.ultratuxkart.panel;

import lucas.hazardous.ultratuxkart.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPanel extends JPanel implements ActionListener {

    public DashboardPanel() {
        this.setPreferredSize(new Dimension(MainFrame.PANEL_WIDTH, MainFrame.DASHBOARD_PANEL_HEIGHT));
        this.setBackground(Color.black);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

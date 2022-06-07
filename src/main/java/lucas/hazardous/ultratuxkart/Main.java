package lucas.hazardous.ultratuxkart;

import java.io.IOException;

public class Main {

    private static final PropertyLoader propertyLoader;

    static {
        try {
            propertyLoader = new PropertyLoader("game.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int PANEL_WIDTH = propertyLoader.getIntProperty("panel_width");
    public static final int PANEL_HEIGHT = propertyLoader.getIntProperty("panel_height");
    public static final int DASHBOARD_PANEL_HEIGHT = propertyLoader.getIntProperty("dashboard_panel_height");
    public static final int TIMER_DELAY = propertyLoader.getIntProperty("timer_delay");
    public static final int TILE_SIZE = propertyLoader.getIntProperty("tile_size");

    public static void main(String[] args) {
        new MainFrame();
    }
}

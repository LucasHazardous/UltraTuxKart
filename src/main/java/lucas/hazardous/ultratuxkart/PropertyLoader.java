package lucas.hazardous.ultratuxkart;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    private final Properties properties;

    public PropertyLoader(String filename) throws IOException {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(filename));
    }

    public int getIntProperty(String property) {
        return Integer.parseInt(properties.getProperty(property));
    }
}

package ru.sberbank.user.api.managers;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;

public class PropertyManager {

    private final Properties properties = new Properties();

    private static PropertyManager INSTANCE = null;

    private PropertyManager() {
        try {
            String propertyFileName = "app";
            properties.load(new FileInputStream(
                    new File("src/main/resources/" + propertyFileName + ".properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyManager getPropertyManager() {
        if (INSTANCE == null) {
            INSTANCE = new PropertyManager();
        }
        return INSTANCE;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String def) {
        return properties.getProperty(key, def);
    }
}

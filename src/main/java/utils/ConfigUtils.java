package utils;

import org.yaml.snakeyaml.Yaml;
import utils.annotations.ConfigProperty;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

public class ConfigUtils {
    private static Map<String, Object> config;

    static {
        try (InputStream input = ConfigUtils.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.yaml");
            } else {
                Yaml yaml = new Yaml();
                config = yaml.load(input);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error loading config.yaml", ex);
        }
    }

    public static String getProperty(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentMap = config;
        for (int i = 0; i < keys.length - 1; i++) {
            currentMap = (Map<String, Object>) currentMap.get(keys[i]);
        }
        return currentMap.get(keys[keys.length - 1]).toString();
    }

    public static void loadConfig(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                String value = getProperty(annotation.value());
                field.setAccessible(true);
                try {
                    if (field.getType() == int.class) {
                        field.setInt(obj, Integer.parseInt(value));
                    } else {
                        field.set(obj, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger log =
            LogManager.getLogger(ConfigReader.class);

    private static final Properties properties =
            new Properties();

    static {

        String env =
                System.getProperty(
                        "env",
                        "staging"
                );

        loadProperties(env);
    }

    private static void loadProperties(
            String env
    ) {

        String fileName =
                "config/" + env + ".properties";

        try (InputStream input =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream(fileName)) {

            if (input == null) {

                throw new RuntimeException(
                        "Configuration file not found : "
                                + fileName
                );
            }

            properties.load(input);

            log.info(
                    "Configuration loaded : {}",
                    fileName
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load configuration : "
                            + fileName,
                    e
            );
        }
    }

    public static String getProperty(
            String key
    ) {

        String systemValue =
                System.getProperty(key);

        if (systemValue != null) {
            return systemValue;
        }

        return properties.getProperty(key);
    }

    public static int getInt(
            String key
    ) {

        return Integer.parseInt(
                getProperty(key)
        );
    }

    public static boolean getBoolean(
            String key
    ) {

        return Boolean.parseBoolean(
                getProperty(key)
        );
    }

}
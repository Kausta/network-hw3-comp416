package group1.hw3.util.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoggerFactory {
    private static final String BANNER_FILE_NAME = "banner.txt";
    private static volatile LoggerFactory _instance = null;

    private LoggerFactory() {
        this.printBanner(BANNER_FILE_NAME);
    }

    public static LoggerFactory getInstance() {
        if (_instance == null) {
            synchronized (LoggerFactory.class) {
                if (_instance == null) {
                    _instance = new LoggerFactory();
                }
            }
        }
        return _instance;
    }

    public static Logger createLogger(Class<?> clazz) {
        return getInstance().getLogger(clazz);
    }

    public Logger getLogger(Class<?> clazz) {
        return new ClassLogger(clazz);
    }

    public void printBanner(String bannerFileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream fileStream = classloader.getResourceAsStream(bannerFileName);

        BannerLogger bannerLogger = new BannerLogger();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                bannerLogger.i(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


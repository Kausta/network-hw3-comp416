package group1.hw3.util.logging;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        URL filePath = classloader.getResource(bannerFileName);

        BannerLogger bannerLogger = new BannerLogger();

        try {
            if (filePath == null) {
                throw new IOException();
            }
            Files.readAllLines(Paths.get(filePath.toURI()))
                    .forEach(bannerLogger::i);
        } catch (IOException | URISyntaxException e) {
            bannerLogger.i("GROUP 1 PROJECT");
        }
    }
}


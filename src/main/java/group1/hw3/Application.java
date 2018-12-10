package group1.hw3;

import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

import java.util.Arrays;

public class Application {
    private final Logger logger = LoggerFactory.createLogger(getClass());

    public Application(String[] args) {
        this.initialize(args);
    }

    public static void main(String[] args) {
        Application application = new Application(args);
        application.run();
    }

    private void initialize(String[] args) {
        logger.i("Starting distance vector routing program with arguments " + Arrays.toString(args));
    }

    public void run() {
        logger.i("Running distance vector routing program");
    }
}

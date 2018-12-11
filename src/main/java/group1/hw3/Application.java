package group1.hw3;

import group1.hw3.routing.MessageRouter;
import group1.hw3.routing.RouteSim;
import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Application {
    private final static String DEFAULT_INPUT_FILE_NAME = "input.txt";

    private final Logger logger = LoggerFactory.createLogger(getClass());
    private Path inputFilePath;

    public Application(String[] args) {
        this.initialize(args);
    }

    public static void main(String[] args) {
        Application application = new Application(args);
        application.run();
    }

    private void initialize(String[] args) {
        logger.i("Starting distance vector routing program with arguments " + Arrays.toString(args));
        String fileName = args.length == 0 ? DEFAULT_INPUT_FILE_NAME : args[1];
        if (!Files.exists(Paths.get(fileName))) {
            if (DEFAULT_INPUT_FILE_NAME.equals(fileName)) {
                logger.e("Cannot open default input file " + DEFAULT_INPUT_FILE_NAME + ", maybe you forgot to add it?");
                throw new RuntimeException("Input file " + DEFAULT_INPUT_FILE_NAME + "not found");
            }
            logger.w("Given input file " + fileName + "wasn't found, falling back to " + DEFAULT_INPUT_FILE_NAME);
            fileName = DEFAULT_INPUT_FILE_NAME;
        }
        inputFilePath = Paths.get(fileName);
        logger.i("Using input file " + inputFilePath.toAbsolutePath().toString());
    }

    public void run() {
        logger.i("Running distance vector routing program");
        RouteSim routeSim = new RouteSim(inputFilePath);
        MessageRouter.getInstance().init(routeSim);
        routeSim.run();
    }
}

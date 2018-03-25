import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Logger {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DatabaseManager.class.getName());
    private static FileHandler fileHandler;

    public static void startLogger(Long currentTime) throws IOException {
        File logsDir = new File("." + File.separator + "logs");
        if (!logsDir.exists() || !logsDir.isDirectory()) {
            logsDir.mkdir();
        }
        fileHandler = new FileHandler("." + File.separator + "logs" + File.separator + currentTime + "_log.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandler);
        LOGGER.setLevel(Level.FINE);
    }
    public static void debug(String message) {
        LOGGER.log(Level.FINE, message + System.lineSeparator());
    }
}

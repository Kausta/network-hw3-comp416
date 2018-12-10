package group1.hw3.util.logging;

public class BannerLogger extends Logger {
    @Override
    protected void print(String logLevel, String message) {
        System.out.println(message);
    }
}

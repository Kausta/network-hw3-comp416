package group1.hw3.util.error;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        this("");
    }

    public NotImplementedException(String methodName) {
        super("Method not yet implemented " + methodName);
    }
}

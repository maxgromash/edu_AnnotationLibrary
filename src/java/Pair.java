public class Pair {

    public Pair(String message, Object failedValue) {
        Message = message;
        FailedValue = failedValue;
    }

    private final String Message;
    private final Object FailedValue;

    public String getMessage() {
        return Message;
    }

    public Object getFailedValue() {
        return FailedValue;
    }
}

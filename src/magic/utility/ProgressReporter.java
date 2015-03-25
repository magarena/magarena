package magic.utility;

public class ProgressReporter {

    private String msg;

    public void setMessage(final String message) {
        // suppress progress messages by default.
        // Extend class to create implementation specific reporting.
        msg = message;
    }

    public String getMessage() {
        return msg;
    }
}

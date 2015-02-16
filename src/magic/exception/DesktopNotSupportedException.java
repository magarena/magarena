package magic.exception;

@SuppressWarnings("serial")
public class DesktopNotSupportedException extends Exception {

    public DesktopNotSupportedException(final String message) {
        super(message);
    }

    public DesktopNotSupportedException() {
        this("Operation is not supported by operating system.");
    }

}

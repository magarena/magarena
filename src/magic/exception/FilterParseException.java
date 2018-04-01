package magic.exception;

@SuppressWarnings("serial")
public class FilterParseException extends RuntimeException {
    public FilterParseException(final String desc) {
        super(desc);
    }
}

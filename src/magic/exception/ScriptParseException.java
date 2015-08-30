package magic.exception;

@SuppressWarnings("serial")
public class ScriptParseException extends RuntimeException {
    public ScriptParseException(final String msg) {
        super(msg);
    }
}

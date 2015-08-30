package magic.model;

import java.util.EventObject;

@SuppressWarnings("serial")
public class MagicLogBookEvent extends EventObject {

    private final MagicMessage _msg;

    public MagicLogBookEvent(final Object source, final MagicMessage msg) {
        super(source);
        _msg = msg;
    }

    public MagicMessage getMagicMessage() {
        return _msg;
    }

}

package magic.model.event;

import magic.model.MagicSource;

public interface MagicEventSource {
    MagicEvent getEvent(final MagicSource source);
}

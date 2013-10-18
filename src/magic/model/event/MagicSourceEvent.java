package magic.model.event;

import magic.model.MagicSource;

public interface MagicSourceEvent {
    MagicEvent getEvent(final MagicSource source);
    MagicRuleEventAction getRule();
}

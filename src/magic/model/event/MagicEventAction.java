package magic.model.event;

import magic.model.MagicGame;

@FunctionalInterface
public interface MagicEventAction {
    void executeEvent(final MagicGame game, final MagicEvent event);

    MagicEventAction NONE = (final MagicGame game, final MagicEvent event) -> {
        //do nothing
    };
}

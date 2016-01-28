package magic.model.event;

import magic.model.MagicGame;

public class MagicEventActionFactory {
    static MagicEventAction compose(final MagicEventAction... acts) {
        return (final MagicGame game, final MagicEvent event) -> {
            for (final MagicEventAction act : acts) {
                act.executeEvent(game, event);
            }
        };
    }
}

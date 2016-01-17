package magic.model.event;

import magic.model.MagicGame;

public class MagicEventActionFactory {
    static MagicEventAction compose(final MagicEventAction... acts) {
        return (final MagicGame game, final MagicEvent event) -> {
            if (event.getTargetChoice().isValid() &&
                event.getTargetChoice().isTargeted() &&
                event.hasLegalTarget(game) == false) {
                //countered on resolution due to illegal target
                return;
            }

            // has legal target
            for (final MagicEventAction act : acts) {
                act.executeEvent(game, event);
            }
        };
    }
}

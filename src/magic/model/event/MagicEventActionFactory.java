package magic.model.event;

import magic.model.MagicGame;

public class MagicEventActionFactory {
    static MagicEventAction compose(final MagicEventAction act1, final MagicEventAction act2) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.getTargetChoice().isValid() && 
                    event.getTargetChoice().isTargeted() &&
                    event.hasLegalTarget(game) == false) {
                    //countered on resolution due to illegal target
                    return;
                }

                // has legal target
                act1.executeEvent(game, event);
                act2.executeEvent(game, event);
            }
        };
    }
}

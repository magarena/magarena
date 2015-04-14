package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.PlayTokenAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicCopyPermanentPicker;

public class MagicPopulateEvent extends MagicEvent {

    public MagicPopulateEvent(final MagicSource source) {
        super(
            source,
            MagicTargetChoice.CREATURE_TOKEN_YOU_CONTROL,
            MagicCopyPermanentPicker.create(),
            EA,
            "Put a token onto the battlefield that's a copy of a creature token$ you control."
        );
    }

    private static final MagicEventAction EA = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new PlayTokenAction(event.getPlayer(), creature.getCardDefinition()));
                }
            });
        }
    };
}

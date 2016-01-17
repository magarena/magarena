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
            EVENT_ACTION,
            "Put a token onto the battlefield that's a copy of a creature token$ you control."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent creature) -> {
            game.doAction(new PlayTokenAction(event.getPlayer(), creature.getCardDefinition()));
        });
    };
}

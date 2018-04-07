package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;

public class MagicAddCounterChosenEvent extends MagicEvent {

    public MagicAddCounterChosenEvent(final MagicSource source, final MagicCounterType counterType) {
        super(
            source,
            MagicTargetChoice.A_CREATURE_YOU_CONTROL,
            counterType,
            EventAction,
            "Put a " + counterType.getName() + " counter on a creature$ you control."
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent perm) ->
            game.doAction(new ChangeCountersAction(
                perm,
                event.getRefCounterType(),
                1
            ))
        );
    };
}

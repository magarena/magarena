package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;

public class MagicAddCounterChosenEvent extends MagicEvent {

    final MagicCounterType ctype;

    public MagicAddCounterChosenEvent(final MagicSource source, final MagicCounterType counterType) {
        super(
            source,
            MagicTargetChoice.A_CREATURE_YOU_CONTROL,
            EventAction,
            "Put a " + counterType.getName() + " counter on a creature$ you control."
        );
        ctype = counterType;
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicCounterType ctype = ((MagicAddCounterChosenEvent)event).ctype;
        event.processTargetPermanent(game, (final MagicPermanent perm) ->
            game.doAction(new ChangeCountersAction(
                perm,
                ctype,
                1
            ))
        );
    };

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicAddCounterChosenEvent(
            copyMap.copy(getSource()),
            ctype
        );
    }
}

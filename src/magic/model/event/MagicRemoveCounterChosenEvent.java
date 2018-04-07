package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetFilterFactory.Control;

public class MagicRemoveCounterChosenEvent extends MagicEvent {

    public MagicRemoveCounterChosenEvent(final MagicSource source, final MagicCounterType counterType) {
        super(
            source,
            new MagicTargetChoice(
                MagicTargetFilterFactory.creature(counterType, Control.You),
                "a creature you control with a " + counterType.getName() + " counter on it"
            ),
            counterType,
            EventAction,
            "Remove a " + counterType.getName() + " counter from a creature$ you control."
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent perm) ->
            game.doAction(new ChangeCountersAction(
                perm,
                event.getRefCounterType(),
                -1
            ))
        );
    };
}

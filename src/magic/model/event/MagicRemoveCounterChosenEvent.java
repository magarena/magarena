package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetFilterFactory.Control;

public class MagicRemoveCounterChosenEvent extends MagicEvent {

    final MagicCounterType ctype;

    public MagicRemoveCounterChosenEvent(final MagicSource source, final MagicCounterType counterType) {
        super(
            source,
            new MagicTargetChoice(
                MagicTargetFilterFactory.creature(counterType, Control.You),
                "a creature you control with a " + counterType.getName() + " counter on it"
            ),
            EventAction,
            "Remove a " + counterType.getName() + " counter from a creature$ you control."
        );
        ctype = counterType;
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicCounterType ctype = ((MagicRemoveCounterChosenEvent)event).ctype;
        event.processTargetPermanent(game, (final MagicPermanent perm) ->
            game.doAction(new ChangeCountersAction(
                perm,
                ctype,
                -1
            ))
        );
    };

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicRemoveCounterChosenEvent(
            copyMap.copy(getSource()),
            ctype
        );
    }
}

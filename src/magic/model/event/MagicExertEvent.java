package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.ChangeStateAction;

public class MagicExertEvent extends MagicEvent {

    public MagicExertEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Exert SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        game.doAction(ChangeStateAction.Set(
            event.getPermanent(),
            MagicPermanentState.Exerted
        ));
        game.doAction(ChangeStateAction.DoesNotUntapDuringNext(
            event.getPermanent(),
            event.getPlayer()
        ));
    };
}

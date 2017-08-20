package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCopyMap;
import magic.model.action.UntapAction;
import magic.model.condition.MagicCondition;

public class MagicUntapEvent extends MagicEvent {

    private static final MagicCondition cond = MagicCondition.CAN_UNTAP_CONDITION;

    public MagicUntapEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Untap SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new UntapAction(event.getPermanent()));

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicUntapEvent(copyMap.copy(getPermanent()));
    }
}

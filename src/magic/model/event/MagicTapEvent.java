package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCopyMap;
import magic.model.action.TapAction;
import magic.model.condition.MagicCondition;

public class MagicTapEvent extends MagicEvent {

    private static final MagicCondition cond = MagicCondition.CAN_TAP_CONDITION;

    public MagicTapEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Tap SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new TapAction(event.getPermanent()));

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicTapEvent(copyMap.copy(getPermanent()));
    }
}

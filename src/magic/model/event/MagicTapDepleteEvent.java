package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;

public class MagicTapDepleteEvent extends MagicEvent {

    private static final MagicCondition[] conds = new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION};

    public MagicTapDepleteEvent(final MagicSource source) {
        super(
            source,
            EVENT_ACTION,
            "Tap SN. Put a depletion counter on SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicTapAction(permanent));
            game.doAction(new MagicChangeCountersAction(permanent, MagicCounterType.Depletion, 1));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

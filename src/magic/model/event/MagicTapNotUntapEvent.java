package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicSource;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;

public class MagicTapNotUntapEvent extends MagicEvent {

    private static final MagicCondition[] conds = new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION};

    public MagicTapNotUntapEvent(final MagicSource source) {
        super(
            source,
            EVENT_ACTION,
            "Tap SN. SN doesn't untap during PN's next untap step."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicTapAction(permanent));
            game.doAction(MagicChangeStateAction.Set(
                    event.getPermanent(),
                    MagicPermanentState.DoesNotUntapDuringNext
                ));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

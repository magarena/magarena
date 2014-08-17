package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;

public class MagicPayLifeTapEvent extends MagicEvent {

    private static final MagicCondition[] conds = new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION};

    public MagicPayLifeTapEvent(final MagicSource source) {
        super(
            source,
            EVENT_ACTION,
            "Tap SN. Pay 1 life."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-1));
            game.doAction(new MagicTapAction(permanent));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

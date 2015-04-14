package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.UntapAction;
import magic.model.condition.MagicCondition;

public class MagicUntapEvent extends MagicEvent {

    private static final MagicCondition[] conds = new MagicCondition[]{MagicCondition.CAN_UNTAP_CONDITION};

    public MagicUntapEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Untap SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new UntapAction(event.getPermanent()));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

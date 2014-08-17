package magic.model.event;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;

public class MagicPainTapEvent extends MagicEvent {

    private static final MagicCondition[] conds = new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION};

    public MagicPainTapEvent(final MagicSource source) {
        super(
            source,
            EVENT_ACTION,
            "Tap SN. SN deals 1 damage to you."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicDamage damage=new MagicDamage(permanent,permanent.getController(),1);
            game.doAction(new MagicTapAction(permanent));
            game.doAction(new MagicDealDamageAction(damage));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

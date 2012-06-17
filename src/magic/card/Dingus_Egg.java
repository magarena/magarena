package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Dingus_Egg {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent.isLand()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{otherPermanent.getController()},
                    this,
                    permanent + " deals 2 damage to " +
                            otherPermanent.getController() + ".") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    (MagicTarget)data[0],
                    2,
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

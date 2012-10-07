package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Meglonoth {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent defender) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
            return (permanent==defender && blocked.isValid()) ?
                new MagicEvent(
                        permanent,
                        blocked.getController(),
                        this,
                        "SN deals damage to the blocked creature's controller equal to SN's power."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicDamage damage=new MagicDamage(permanent,event.getPlayer(),permanent.getPower(),false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

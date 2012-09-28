package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Ashmouth_Hound {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }

            final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
            return new MagicEvent(
                permanent,
                new Object[]{plist},
                this,
                "SN deals 1 damage to each blocking creature."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanentList plist = (MagicPermanentList)data[0];
            for (final MagicPermanent blocker : plist) {
                final MagicDamage damage = new MagicDamage(event.getPermanent(),blocker,1,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent attacker = permanent.getBlockedCreature();
            return (permanent == blocker && attacker.isValid()) ?
                new MagicEvent(
                    permanent,
                    new Object[]{attacker},
                    this,
                    "SN deals 1 damage to " + attacker + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    (MagicPermanent)data[0],
                    1,
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

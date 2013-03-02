package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Inferno_Elemental {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }

            final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
            
            return new MagicEvent(
                permanent,
                plist,
                this,
                "SN deals 3 damage to each blocking creature."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                final MagicDamage damage = new MagicDamage(event.getSource(),blocker,3);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == blocker && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    blocked,
                    this,
                    "SN deals 3 damage to " + blocked + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    event.getPermanent(),
                    event.getRefPermanent(),
                    3,
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

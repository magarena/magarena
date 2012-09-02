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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        this,
                        permanent + " deals 3 damage to blocking creature.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPermanentList plist = permanent.getBlockingCreatures();
            for (final MagicPermanent blocker : plist) {
                final MagicDamage damage = new MagicDamage(permanent,blocker,3,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == data && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    this,
                    permanent + " deals 3 damage to " + blocked + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                    event.getPermanent(),
                    event.getPermanent().getBlockedCreature(),
                    3,
                    false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}

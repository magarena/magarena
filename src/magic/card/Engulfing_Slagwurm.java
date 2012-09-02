package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Engulfing_Slagwurm {
    public static final MagicWhenBecomesBlockedTrigger T1 = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            final MagicPermanentList plist = permanent.getBlockingCreatures();
            return (permanent == attacker && plist.size() > 0) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    this,
                    plist.size() > 1 ?
                        "Destroy blocking creatures. You gain life equal to those creatures toughness." :
                        "Destroy " + plist.get(0) + ". You gain life equal to its toughness."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            //make a copy as destory modifies getBlockingCreatures
            final MagicPermanentList plist = new MagicPermanentList(event.getPermanent().getBlockingCreatures());
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicDestroyAction(blocker));
                game.doAction(new MagicChangeLifeAction(
                        event.getPlayer(),
                        blocker.getToughness()));
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
                    permanent.getController(),
                    this,
                    "Destroy " + attacker + ". You gain life equal to its toughness."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent attacker = event.getPermanent().getBlockedCreature();
            game.doAction(new MagicDestroyAction(attacker));
            game.doAction(new MagicChangeLifeAction(
                    event.getPlayer(),
                    attacker.getToughness()));
        }
    };
}

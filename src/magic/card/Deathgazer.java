package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Deathgazer {
    public static final MagicWhenBecomesBlockedTrigger T1 = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent !=  attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = new MagicPermanentList();
            for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                if (!blocker.hasColor(MagicColor.Black)) {
                    plist.add(blocker);
                }
            }
            return !plist.isEmpty() ? 
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    plist,
                    this,
                    plist.size() == 1 ?
                        "Destroy " + plist.get(0) + " at end of combat." :
                        "Destroy blocking nonblack creatures at end of combat."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicChangeStateAction(blocker,MagicPermanentState.DestroyAtEndOfCombat,true));
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == blocker &&
                    blocked.isValid() &&
                    !blocked.hasColor(MagicColor.Black)) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    blocked,
                    this,
                    "Destroy " + blocked + " at end of combat."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.DestroyAtEndOfCombat,true));
        }
    };
}

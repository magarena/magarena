package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Wooden_Stake {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            if (equippedCreature != attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = new MagicPermanentList();
            for (final MagicPermanent blocker : equippedCreature.getBlockingCreatures()) {
                if (blocker.hasSubType(MagicSubType.Vampire)) {
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
                        "Destroy " + plist.get(0) + ". It can't be regenerated." :
                        "Destroy blocking Vampires. They can't be regenerated."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicChangeStateAction(blocker,MagicPermanentState.CannotBeRegenerated,true));
                game.doAction(new MagicDestroyAction(blocker));
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            final MagicPermanent blocked = equippedCreature.getBlockedCreature();
            return (equippedCreature == blocker &&
                    blocked.isValid() &&
                    blocked.hasSubType(MagicSubType.Vampire)) ?
                new MagicEvent(
                    permanent,
                    blocked,
                    this,
                    "Destroy RN. It can't be regenerated."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
            game.doAction(new MagicDestroyAction(creature));
        }
    };
}

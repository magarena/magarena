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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            if (creature == equippedCreature) {
                final MagicPermanentList plist = new MagicPermanentList();
                for (final MagicPermanent blocker : equippedCreature.getBlockingCreatures()) {
                    if (blocker.hasSubType(MagicSubType.Vampire)) {
                        plist.add(blocker);
                    }
                }
                if (!plist.isEmpty()) {
                    return new MagicEvent(
                            permanent,
                            permanent.getController(),
                            new Object[]{plist},
                            this,
                            plist.size() > 1 ?
                                "Destroy blocking Vampires. They can't be regenerated." :
                                "Destroy " + plist.get(0) + ". It can't be regenerated.");
                }
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanentList plist = (MagicPermanentList)data[0];
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicChangeStateAction(blocker,MagicPermanentState.CannotBeRegenerated,true));
                game.doAction(new MagicDestroyAction(blocker));
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            final MagicPermanent blocked = equippedCreature.getBlockedCreature();
            return (equippedCreature == data &&
                    blocked.isValid() &&
                    blocked.hasSubType(MagicSubType.Vampire)) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{blocked},
                    this,
                    "Destroy " + blocked + ". It can't be regenerated."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent creature = (MagicPermanent)data[0];
            game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
            game.doAction(new MagicDestroyAction(creature));
        }
    };
}

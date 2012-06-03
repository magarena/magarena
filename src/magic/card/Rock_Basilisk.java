package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Rock_Basilisk {
    public static final MagicWhenBecomesBlockedTrigger T1 = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
            	final MagicPermanentList plist = new MagicPermanentList();
            	for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
            		if (!blocker.hasSubType(MagicSubType.Wall)) {
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
                					"Destroy blocking non-Wall creatures at end of combat." :
                					"Destroy " + plist.get(0) + " at end of combat.");
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
				game.doAction(new MagicChangeStateAction(blocker,MagicPermanentState.DestroyAtEndOfCombat,true));
        	}
		}
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
			return (permanent == data &&
					blocked.isValid() &&
					!blocked.hasSubType(MagicSubType.Wall)) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{blocked},
                    this,
                    "Destroy " + blocked + " at end of combat."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent creature = (MagicPermanent)data[0];
			game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.DestroyAtEndOfCombat,true));
		}
    };
}

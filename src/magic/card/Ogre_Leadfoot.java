package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Ogre_Leadfoot {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
            	final MagicPermanentList plist = new MagicPermanentList();
            	for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
            		if (blocker.isArtifact() && blocker.isCreature()) {
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
                					"Destroy blocking artifact creatures." :
                					"Destroy " + plist.get(0) + ".");
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
				game.doAction(new MagicDestroyAction(blocker));
        	}
		}
    };
}

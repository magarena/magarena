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
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
            	final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
            	return new MagicEvent(
            			permanent,
            			permanent.getController(),
            			new Object[]{plist,permanent.getController()},
            			this,
            			plist.size() > 1 ?
            				"Destroy blocking creatures. You gain life equal to those creature's toughness." :
            				"Destroy blocking creature. You gain life equal to its toughness.");
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
				game.doAction(new MagicChangeLifeAction(
						(MagicPlayer)data[1],
						blocker.getToughness(game)));
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
                    new Object[]{blocked,permanent.getController()},
                    this,
                    "Destroy " + blocked + ". You gain life equal to its toughness."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent blocker = (MagicPermanent)data[0];
			game.doAction(new MagicDestroyAction(blocker));
			game.doAction(new MagicChangeLifeAction(
					(MagicPlayer)data[1],
					blocker.getToughness(game)));
		}
    };
}

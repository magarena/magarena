package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;
import magic.model.trigger.MagicWhenDiscardedTrigger;

public class Sangromancer {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T1 = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			final MagicPlayer otherController = otherPermanent.getController();
			return (otherController != player && otherPermanent.isCreature()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new MagicSimpleMayChoice(
                            "You may gain 3 life.",
                            MagicSimpleMayChoice.GAIN_LIFE,
                            3,
                            MagicSimpleMayChoice.DEFAULT_YES),
                    new Object[]{player},
                    this,
                    player + " may$ gain 3 life.") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
			}
		}
    };
    
    public static final MagicWhenDiscardedTrigger T2 =new MagicWhenDiscardedTrigger() {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
    		final MagicPlayer otherController = data.getOwner();
    		final MagicPlayer player = permanent.getController();
    		return (otherController != player) ?
    			new MagicEvent(
                    permanent,
                    player,
                    new MagicSimpleMayChoice(
                            "You may gain 3 life.",
                            MagicSimpleMayChoice.GAIN_LIFE,
                            3,
                            MagicSimpleMayChoice.DEFAULT_YES),
                    new Object[]{player},
                    this,
                    player + " may$ gain 3 life.") :
                MagicEvent.NONE;
    	}

    	@Override
    	public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
    		if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
			}
    	}
    };
}

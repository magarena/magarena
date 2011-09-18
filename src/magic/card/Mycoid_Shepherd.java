package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Mycoid_Shepherd {
    public static final MagicWhenPutIntoGraveyardTrigger T1 = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer player = permanent.getController();
			return (MagicLocationType.Play == triggerData.fromLocation) ?
					new MagicEvent(
		                    permanent,
		                    player,
		                    new MagicSimpleMayChoice(
		                            "You may gain 5 life.",
		                            MagicSimpleMayChoice.GAIN_LIFE,
		                            5,
		                            MagicSimpleMayChoice.DEFAULT_YES),
		                    new Object[]{player},
		                    this,
		                    player + " may$ gain 5 life.") :
		                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],5));
			}
		}
    };

    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T2 = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player=permanent.getController();
			return (otherPermanent!=permanent &&
                    otherPermanent.getController()==player &&
                    otherPermanent.isCreature(game) && 
                    otherPermanent.getPower(game)>=5) ?
                    		new MagicEvent(
        		                    permanent,
        		                    player,
        		                    new MagicSimpleMayChoice(
        		                            "You may gain 5 life.",
        		                            MagicSimpleMayChoice.GAIN_LIFE,
        		                            5,
        		                            MagicSimpleMayChoice.DEFAULT_YES),
        		                    new Object[]{player},
        		                    this,
        		                    player + " may$ gain 5 life.") :
        		                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],5));
			}
		}
    };
}

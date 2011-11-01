package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Ondu_Cleric {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally,game)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                        		player + " may gain life equal to " +
                        		"the number of Allies he or she controls.",
                                MagicSimpleMayChoice.GAIN_LIFE,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{player},
                        this,
                        player + " may$ gain life equal to " +
                        "the number of Allies he or she controls.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player = (MagicPlayer)data[0];
				final int amount =
            			player.getNrOfPermanentsWithSubType(MagicSubType.Ally,game);
                if (amount > 0) {
                	game.doAction(new MagicChangeLifeAction(player,amount));
                }
			}			
		}		
    };
}

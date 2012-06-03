package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;


public class Demon_s_Horn {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
			final MagicPlayer player = permanent.getController();
			final MagicCard card = data.getCard();
			return (MagicColor.Black.hasColor(card.getColorFlags())) ?
					new MagicEvent(
	                        permanent,
	                        player,
	                        new MagicSimpleMayChoice(
	                                player + " may gain 1 life.",
	                                MagicSimpleMayChoice.GAIN_LIFE,
	                                1,
	                                MagicSimpleMayChoice.DEFAULT_YES),
	                        new Object[]{player},
	                        this,
	                        player + " may$ gain 1 life.") :
	                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
			}
		}
    };
}

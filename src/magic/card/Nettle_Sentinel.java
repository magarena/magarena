package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenSpellIsPlayedTrigger;

public class Nettle_Sentinel {
    public static final MagicWhenSpellIsPlayedTrigger T2 = new MagicWhenSpellIsPlayedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
			final MagicPlayer player = permanent.getController();
			final MagicCard card = data.getCard();
			return (card.getOwner() == player &&
					MagicColor.Green.hasColor(card.getColorFlags(game)) &&
					permanent.isTapped()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                        		"You may untap " + permanent + ".",
                                MagicSimpleMayChoice.UNTAP,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                                new Object[]{permanent},
                        this,
                        player + "  may$ untap " + permanent + ".") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
			}
		}
    };
}

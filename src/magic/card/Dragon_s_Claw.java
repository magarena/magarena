package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Dragon_s_Claw {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
			final MagicPlayer player = permanent.getController();
			final MagicCard card = data.getCard();
			return (MagicColor.Red.hasColor(card.getColorFlags())) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice("You may gain 1 life."),
                        new Object[]{player},
                        this,
                        "You may$ gain 1 life.") :
                null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
			}
		}
    };
}

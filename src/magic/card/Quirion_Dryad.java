package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;

public class Quirion_Dryad {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
			final MagicPlayer player = permanent.getController();
			final MagicCard card = data.getCard();
			return (card.getOwner() == player &&
					(MagicColor.White.hasColor(card.getColorFlags()) ||
					MagicColor.Blue.hasColor(card.getColorFlags()) ||
					MagicColor.Black.hasColor(card.getColorFlags()) ||
					MagicColor.Red.hasColor(card.getColorFlags())) ) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent},
                        this,
                        "Put a +1/+1 counter on " + permanent + ".") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
		}
    };
}

package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDiscardCardAction;
import magic.model.choice.MagicCardChoice;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.choice.MagicRandomCardChoice;

public class MagicDiscardEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
			final MagicPlayer player=(MagicPlayer)data[0];
			final MagicCardChoiceResult cards=(MagicCardChoiceResult)choices[0];
			for (final MagicCard card : cards) {
				game.doAction(new MagicDiscardCardAction(player,card));
			}
		}
	};
	
	public MagicDiscardEvent(final MagicSource source,final MagicPlayer player,final int amount,final boolean random) {
        super(
            source,
            player,
            random ? new MagicRandomCardChoice(amount) : new MagicCardChoice(amount),
            new Object[]{player},
            EVENT_ACTION,
            getDescription(amount,random)
        );
	}
	
	private static final String getDescription(final int amount,final boolean random) {
		if (amount != 1) {
			return "You discard " + amount + " cards$.";
		} else {
			return "You discard a card$.";
		}
	}
}

package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDrawAction;

public class MagicDrawEvent extends MagicEvent {
		
	public MagicDrawEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        super(
            source,
            player,
            MagicEvent.NO_DATA,
            new MagicEventAction() {
                @Override
                public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object data[],
                    final Object[] choices) {
                    game.doAction(new MagicDrawAction(player.map(game), amount));		
                }
            },
            player.getName() + getDescription(amount));
	}
	
	private static final String getDescription(final int amount) {
		if (amount!=1) {
			return " draws "+amount+" cards.";
		} else {
			return " draws a card.";
		}
	}
}

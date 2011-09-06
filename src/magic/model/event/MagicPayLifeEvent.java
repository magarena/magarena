package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeLifeAction;

public class MagicPayLifeEvent extends MagicEvent {
	public MagicPayLifeEvent(final MagicSource source,final MagicPlayer player,final int amount) {
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
                    game.doAction(new MagicChangeLifeAction(player.map(game),-amount));
                }
	        },
            "Pay "+amount+" life.");		
	}
}

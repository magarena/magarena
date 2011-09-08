package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class MagicRefugeLandTrigger extends MagicWhenComesIntoPlayTrigger {
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
		return new MagicEvent(
            permanent,
            player,
            MagicEvent.NO_DATA,
            this,
            player + " gains 1 life.");
	}
    @Override
    public void executeEvent(
        final MagicGame game,
        final MagicEvent event,
        final Object[] data,
        final Object[] choices) {
        game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
    }
}

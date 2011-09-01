package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;

public class MagicRefugeLandTrigger extends MagicTrigger {
	
    public MagicRefugeLandTrigger() {
		super(MagicTriggerType.WhenComesIntoPlay);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent) {
		final MagicPlayer player = permanent.getController();
		return new MagicEvent(
                permanent,
                player,
                new Object[]{permanent,player},
                this,
                player + " gains 1 life.");
	}
	
	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],1));
	}	
}

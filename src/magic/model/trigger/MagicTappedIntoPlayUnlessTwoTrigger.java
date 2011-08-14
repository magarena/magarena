package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayUnlessTwoTrigger extends MagicTrigger {
	
    public MagicTappedIntoPlayUnlessTwoTrigger() {
		super(MagicTriggerType.WhenComesIntoPlay);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		final MagicPlayer player=permanent.getController();
		return (player.getNrOfPermanentsWithType(MagicType.Land) > 3) ?
			new MagicEvent(
                permanent,
                player,
                new Object[]{permanent},
                this,
                permanent+" enters the battlefield tapped."):
            null;
	}
	
	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		game.doAction(new MagicTapAction((MagicPermanent)data[0],false));
	}
	
	@Override
	public boolean usesStack() {
		return false;
	}
}

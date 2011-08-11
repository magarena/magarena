package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayTrigger extends MagicTrigger {
	
    public MagicTappedIntoPlayTrigger() {
		super(MagicTriggerType.WhenComesIntoPlay);
	}

	public MagicTappedIntoPlayTrigger(final String name) {
		super(MagicTriggerType.WhenComesIntoPlay,name);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
	
		return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,permanent.getName()+" enters the battlefield tapped.");
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

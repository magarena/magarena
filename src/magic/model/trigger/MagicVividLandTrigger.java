package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicVividLandTrigger extends MagicTrigger {

	public MagicVividLandTrigger(final String name) {
		
		super(MagicTriggerType.WhenComesIntoPlay,name);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
	
		return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
			permanent.getName()+" enters the battlefield tapped with two charge counters on it.");
	}
	
	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		
		final MagicPermanent permanent=(MagicPermanent)data[0];
		game.doAction(new MagicTapAction(permanent,false));
		game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.Charge,2,false));
	}

	@Override
	public boolean usesStack() {

		return false;
	}
}
package magic.model.trigger;

import magic.model.*;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;

public class MagicPersistTrigger extends MagicTrigger {

	private static final MagicTrigger INSTANCE=new MagicPersistTrigger();
	
	private MagicPersistTrigger() {
		
		super(MagicTriggerType.WhenPutIntoGraveyard);
	}
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

		final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
		if (triggerData.fromLocation==MagicLocationType.Play&&permanent.getCounters(MagicCounterType.MinusOne)==0) {
			final MagicCard card=triggerData.card;
			return new MagicEvent(permanent,permanent.getController(),new Object[]{card},this,
				"Return "+card.getName()+" to play under its owner's control with a -1/-1 counter on it.");
		}
		return null;
	}

	@Override
	public void executeEvent(final MagicGame game,MagicEvent event,final Object[] data,final Object[] choiceResults) {

		final MagicCard card=(MagicCard)data[0];
		game.doAction(new MagicReanimateAction(card.getOwner(),card,MagicPlayCardAction.PERSIST));
	}
	
	public static final MagicTrigger getInstance() {
		
		return INSTANCE;
	}
}
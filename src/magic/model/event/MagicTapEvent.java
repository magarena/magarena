package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicTapAction;

public class MagicTapEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
	
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {

			game.doAction(new MagicTapAction((MagicPermanent)data[0],true));
		}
	};
	
	public MagicTapEvent(final MagicPermanent permanent) {
		
		super(permanent,permanent.getController(),new Object[]{permanent},EVENT_ACTION,"Tap "+permanent.getName()+".");
	}
}
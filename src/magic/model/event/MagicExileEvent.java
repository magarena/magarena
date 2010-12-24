package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicRemoveFromPlayAction;

public class MagicExileEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
	
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {

			game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)data[0],MagicLocationType.Exile));
		}
	};
		
	public MagicExileEvent(final MagicPermanent permanent) {
		
		super(permanent,permanent.getController(),new Object[]{permanent},EVENT_ACTION,"Exile "+permanent.getName()+".");
	}	
}
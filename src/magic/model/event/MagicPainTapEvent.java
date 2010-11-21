package magic.model.event;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTapAction;

public class MagicPainTapEvent extends MagicEvent {
	
	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,permanent.getController(),1,false);
			game.doAction(new MagicTapAction(permanent,true));
			game.doAction(new MagicDealDamageAction(damage));
		}
	};
	
	public MagicPainTapEvent(final MagicSource source) {
		
		super(source,source.getController(),new Object[]{source},EVENT_ACTION,"Tap "+source.getName()+". "+source.getName()+" deals 1 damage to you.");
	}
}
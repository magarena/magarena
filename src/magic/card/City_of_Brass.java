package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;



public class City_of_Brass {
	
	public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
		
		
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			final MagicPlayer player=permanent.getController();
			return (permanent==data) ?
					new MagicEvent(
							permanent,
							player,
							new Object[]{permanent,player},
							this,
							permanent + " deals 1 damage to you.") :
				    MagicEvent.NONE;

		}

		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],1,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
	};
}
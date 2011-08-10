package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Souls_of_the_Faultless {

    public static final MagicTrigger V8759 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Souls of the Faultless") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicPlayer opponent=damage.getSource().getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,opponent,amount},this,
					"You gain "+amount+" life and attacking player loses "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final int life=(Integer)data[2];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],life));
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],-life));
		}
    };

}

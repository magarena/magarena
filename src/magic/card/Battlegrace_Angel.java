package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Battlegrace_Angel {

    public static final MagicTrigger V6857 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Battlegrace Angel") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (creature.getController()==player&&player.getNrOfAttackers()==1) {
				return new MagicEvent(permanent,player,new Object[]{creature},this,creature.getName()+" gains lifelink until end of turn.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {			

			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.LifeLink));
		}
    };
    
}

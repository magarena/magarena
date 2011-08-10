package magic.card;

import magic.model.*;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Dissipation_Field {

    public static final MagicTrigger V10048 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Dissipation Field") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicSource source=damage.getSource();
			if (damage.getTarget()==player&&source.isPermanent()) {
				return new MagicEvent(permanent,player,new Object[]{source},this,"Return "+source.getName()+" to its owner's hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)data[0],MagicLocationType.OwnersHand));
		}
    };
        
}

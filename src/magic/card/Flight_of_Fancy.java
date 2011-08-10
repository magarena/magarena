package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicFlyingTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Flight_of_Fancy {

	public static final MagicSpellCardEvent V6498 =new MagicPlayAuraEvent("Flight of Fancy",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicFlyingTargetPicker.getInstance());
    public static final MagicTrigger V10530 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Flight of Fancy") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You draw two cards.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],2));
		}		
    };
    
}

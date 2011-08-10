package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicGainControlAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Keiga__the_Tide_Star {

    public static final MagicTrigger V7739 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Keiga, the Tide Star") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE,MagicExileTargetPicker.getInstance(),
					new Object[]{player},this,"Gain control of target creature$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicGainControlAction((MagicPlayer)data[0],creature));
			}
		}
    };

}

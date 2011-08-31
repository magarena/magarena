package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Keiga__the_Tide_Star {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			return (MagicLocationType.Play==triggerData.fromLocation) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.TARGET_CREATURE,
                        MagicExileTargetPicker.getInstance(),
                        new Object[]{permanent.getController()},
                        this,
                        "Gain control of target creature$."):
                null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainControlAction((MagicPlayer)data[0],creature));
                }
			});
		}
    };
}

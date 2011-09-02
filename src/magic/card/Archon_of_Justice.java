package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Archon_of_Justice {
	public static final MagicWhenPutIntoGraveyardTrigger T =new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
			return (MagicLocationType.Play==triggerData.fromLocation) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.TARGET_PERMANENT,
                        MagicExileTargetPicker.getInstance(),
                        MagicEvent.NO_DATA,
                        this,
                        "Exile target permanent$."):
                null;
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
                }
			});
		}
    };
}

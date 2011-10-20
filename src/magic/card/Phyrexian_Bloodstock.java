package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Phyrexian_Bloodstock {
    public static final MagicWhenLeavesPlayTrigger T = new MagicWhenLeavesPlayTrigger() {
    	@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			return (permanent == data) ?
				new MagicEvent(
						permanent,
	                    permanent.getController(),
	                    MagicTargetChoice.TARGET_WHITE_CREATURE,
	                    new MagicDestroyTargetPicker(true),
	                    MagicEvent.NO_DATA,
	                    this,
	                    "Destroy target white creature$. It can't be regenerated.") :
			MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                	game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
                    game.doAction(new MagicDestroyAction(creature));
                }
			});
		}
    };
}

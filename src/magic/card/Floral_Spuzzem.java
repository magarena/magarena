package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Floral_Spuzzem {
	public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
            	final MagicPlayer player = permanent.getController();
            	return new MagicEvent(
						permanent,
						player,
						new MagicMayChoice(
	                            player + " may destroy target artifact.",
	                            MagicTargetChoice.TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS),
	                    new MagicDestroyTargetPicker(false),
						new Object[]{permanent},
						this,
						player + " may$ destroy target artifact$.");
            }
            return MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
	                public void doAction(final MagicPermanent target) {
	                    game.doAction(new MagicDestroyAction(target));
	                }
	            });
				game.doAction(new MagicChangeStateAction(
						(MagicPermanent)data[0],
						MagicPermanentState.NoCombatDamage,true));
			}
		}
    };
}

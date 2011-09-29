package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Reaper_from_the_Abyss {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
    	@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
    		return game.getCreatureDiedThisTurn() ?
    				new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.NEG_TARGET_NON_DEMON,
                        new MagicDestroyTargetPicker(false),
                        MagicEvent.NO_DATA,
                        this,
                        "Destroy target non-Demon creature.") :
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
                    game.doAction(new MagicDestroyAction(creature));
                }
			});
		}
	};
}

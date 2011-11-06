package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Morkrut_Banshee {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return game.getCreatureDiedThisTurn() ?
				new MagicEvent(
						permanent,
						player,
						MagicTargetChoice.TARGET_CREATURE,
						new MagicWeakenTargetPicker(4,4),
						MagicEvent.NO_DATA,
						this,
						"Target creature$ gets -4/-4 until end of turn.") :
               MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                	game.doAction(new MagicChangeTurnPTAction(creature,-4,-4));
                }
			});
		}
    };
}

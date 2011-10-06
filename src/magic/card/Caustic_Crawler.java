package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Caustic_Crawler {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
			final MagicPlayer player = permanent.getController();
			return (player == played.getController() && played.isLand()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            player + " may have target creature get -1/-1 until end of turn.",
                            MagicTargetChoice.NEG_TARGET_CREATURE),
                    new MagicWeakenTargetPicker(1,1),
                    MagicEvent.NO_DATA,
                    this,
                    player + " may have target creature get -1/-1 until end of turn."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
	                public void doAction(final MagicPermanent creature) {
	                    game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
	                }
				});
			}
		}		
    };
}

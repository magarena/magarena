package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicMillLibraryAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicTiming;

import java.util.Arrays;

public class Nephalia_Drownyard {
    public static final MagicManaActivation M = new MagicTapManaActivation(
            Arrays.asList(MagicManaType.Colorless),0);
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
				MagicCondition.CAN_TAP_CONDITION,
				MagicManaCost.ONE_BLUE_BLACK.getCondition()
			},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE_BLUE_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
					source,
					source.getController(),
					MagicTargetChoice.TARGET_PLAYER,
					MagicEvent.NO_DATA,
					this,
                    "Target player$ puts the top three cards of " +
                    "his or her library into his or her graveyard.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                	game.doAction(new MagicMillLibraryAction(player,3));
                }
			});
		}
	};
}

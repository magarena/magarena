package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicGraveyardTargetPicker;

public class Skyshroud_Ranger {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
					MagicCondition.CAN_TAP_CONDITION,
					MagicCondition.SORCERY_CONDITION},
            new MagicActivationHints(MagicTiming.Land),
            "Land") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new MagicMayChoice(
							player + " may put a land card from his or her hand into play.",
							MagicTargetChoice.TARGET_LAND_CARD_FROM_HAND),
					new MagicGraveyardTargetPicker(true),
                    new Object[]{player},
                    this,
					player + " may$ put a land card$ from his or her hand into play.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
					public void doAction(final MagicCard card) {
						game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
						game.doAction(new MagicPlayCardAction(card,(MagicPlayer)data[0],MagicPlayCardAction.NONE));
					}
				});
			}
		}
	};
}

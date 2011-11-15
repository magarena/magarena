package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicGraveyardTargetPicker;

public class Arcbound_Reclaimer {
	public static final MagicPermanentActivation A = new MagicPermanentActivation( 
			new MagicCondition[] {MagicCondition.PLUS_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Main),
            "Card") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.PlusOne,1)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_ARTIFACT_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(false),
				    MagicEvent.NO_DATA,
                    this,
                    "Put target artifact card$ from your graveyard on top of your library");
		}
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(
                            targetCard,
                            MagicLocationType.Graveyard,
                            MagicLocationType.TopOfOwnersLibrary));
                }
			});
		}
	};
}

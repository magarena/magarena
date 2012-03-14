package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicGraveyardTargetPicker;

public class Recurring_Nightmare {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
	                MagicCondition.ONE_CREATURE_CONDITION,
	                MagicCondition.SORCERY_CONDITION,
	                MagicCondition.GRAVEYARD_CONTAINS_CREATURE
	            },
            new MagicActivationHints(MagicTiming.Pump),
            "Return") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
		    MagicTargetChoice creatureChoice = MagicTargetChoice.SACRIFICE_CREATURE;
			return new MagicEvent[]{new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    creatureChoice)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{source,source.getController()},
                    this,
                    "Return " + source + " to its owner's hand. Return " +
                    "target creature card$ from your graveyard to the battlefield.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)data[0],MagicLocationType.OwnersHand));
			event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicReanimateAction((MagicPlayer)data[1],targetCard,MagicPlayCardAction.NONE));
                }
			});
		}
	};
}

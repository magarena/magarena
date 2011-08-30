package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.*;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicCardAction;

public class Cemetery_Reaper {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.TWO_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.TWO_BLACK)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{source.getController()},
                    this,
                    "Exile target creature card from a graveyard. " +
                    "Put a 2/2 black Zombie creature token onto the battlefield.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    final MagicPlayer player=(MagicPlayer)data[0];
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
                    game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
                }
			});
		}
	};
}

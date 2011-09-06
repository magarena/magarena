package magic.card;

import magic.model.*;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicGraveyardTargetPicker;

// Backed out this card because it doesn't function properly.
// It creates two effects on the stack where it should be one.
//
//>Quicksilver Amulet
//image=http://magiccards.info/scans/en/m12/214.jpg
//value=4
//rarity=R
//type=Artifact
//converted=4
//cost={4}
//timing=artifact
public class Quicksilver_Amulet {

	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicManaCost.FOUR.getCondition()},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.FOUR)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_HAND,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{source.getController()},
                    this,
					"Put a creature card$ from your hand onto the battlefield.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                	game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
    				game.doAction(new MagicPlayCardAction(card,(MagicPlayer)data[0],MagicPlayCardAction.NONE));
                }
            });
		}
	};
}

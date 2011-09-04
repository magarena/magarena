package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Wrexial__the_Risen_Deep {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
			return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                        "You may cast target instant or sorcery card from your opponent's graveyard.",
                        MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD),
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{player},
                    this,
                    "You may$ cast target instant or sorcery card$ from your opponent's graveyard " + 
                    "without paying its mana cost. "+
                    "If that card would be put into a graveyard this turn, exile it instead."):
                MagicEvent.NONE;
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
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                        final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,(MagicPlayer)data[0],MagicPayedCost.NO_COST);
                        cardOnStack.setMoveLocation(MagicLocationType.Exile);
                        game.doAction(new MagicPutItemOnStackAction(cardOnStack));
                    }
                });
            }
		}
    };
}

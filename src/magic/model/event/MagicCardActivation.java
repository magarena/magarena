package magic.model.event;

import magic.data.CardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;

public class MagicCardActivation extends MagicActivation {
	
	public MagicCardActivation(final int cardIndex) {
		super(0,
              new MagicCondition[]{
                  MagicCondition.CARD_CONDITION,
                  CardDefinitions.getInstance().getCard(cardIndex).getCost().getCondition()},
              CardDefinitions.getInstance().getCard(cardIndex).getActivationHints(),
              "Play"
             );
        setCardIndex(cardIndex);
	}
	
	@Override
	public boolean usesStack() {
		return getCardDefinition().usesStack();
	}

	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
		final MagicManaCost cost=getCardDefinition().getCost();
		if (cost!=MagicManaCost.ZERO) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),cost)};
		} else {
			return MagicEvent.NO_EVENTS;
		}
	}

	@Override
	public MagicEvent getEvent(final MagicSource source) {
		return new MagicEvent(source,source.getController(),new Object[]{source},this,"Play "+source.getName()+".");
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		final MagicCard card=(MagicCard)data[0];
		if (card.getCardDefinition().isLand()) {
			game.incLandPlayed();
		}
		game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
		if (card.getCardDefinition().usesStack()) {
			final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,game.getPayedCost());
			game.doAction(new MagicPutItemOnStackAction(cardOnStack));
		} else {
			game.doAction(new MagicPlayCardAction(card,card.getController(),MagicPlayCardAction.NONE));
		}
	}

	@Override
	public final MagicTargetChoice getTargetChoice() {
		// Not the cleanest way to do this by far...
		final MagicCard card=new MagicCard(getCardDefinition(),MagicPlayer.NONE,0);
		final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,MagicPayedCost.NO_COST);
		return cardOnStack.getEvent().getTargetChoice();
	}	
}

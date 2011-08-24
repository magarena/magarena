package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Reanimate {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{cardOnStack,player},
                    this,
                    "Put target creature card$ from a graveyard onto the battlefield under your control. " +
                    		player + " loses life equal to its converted mana cost.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				final MagicPlayer player=(MagicPlayer)data[1];
				game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
				game.doAction(new MagicChangeLifeAction(player,-targetCard.getCardDefinition().getConvertedCost()));
			}
		}
	};
}

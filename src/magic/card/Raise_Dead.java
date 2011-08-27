package magic.card;

import magic.model.*;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Raise_Dead {
	public static final MagicSpellCardEvent S =new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{cardOnStack,player},
                    this,
                    "Return target creature card$ from your graveyard to your hand.");
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
				game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
				game.doAction(new MagicMoveCardAction(targetCard,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
			}
		}
	};
}

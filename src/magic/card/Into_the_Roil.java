package magic.card;

import magic.model.*;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.action.MagicPermanentAction;

public class Into_the_Roil {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
            final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(
                        MagicTargetChoice.TARGET_NONLAND_PERMANENT,
                        MagicManaCost.ONE_BLUE,false),
                    MagicBounceTargetPicker.getInstance(),
                    new Object[]{cardOnStack,player},
                    this,
                    "Return target nonland permanent$ to its owner's hand. If " + card + " was kicked$, draw a card.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
                    if (((Integer)choiceResults[1])>0) {
                        game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
                    }
                }
			});
		}
	};
}

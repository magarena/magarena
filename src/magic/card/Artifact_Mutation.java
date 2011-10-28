package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;

public class Artifact_Mutation {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.NEG_TARGET_ARTIFACT,
                    new MagicDestroyTargetPicker(true),
                    new Object[]{cardOnStack,cardOnStack.getController()},
                    this,
                    "Destroy target artifact$. It can't be regenerated.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                	game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.CannotBeRegenerated,true));
                    game.doAction(new MagicDestroyAction(permanent));
                    int amount = permanent.getCardDefinition().getConvertedCost();
                    for (;amount>0;amount--) {
                    	game.doAction(new MagicPlayTokenAction(
                    			(MagicPlayer)data[1],
                    			TokenCardDefinitions.getInstance().getTokenDefinition("Saproling")));
                    }
                }
			});
		}
	};
}

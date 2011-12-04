package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Corrupted_Resolve {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.NEG_TARGET_SPELL,
                    new Object[]{cardOnStack},
                    this,
                    "Counter target spell$ if its controller is poisoned.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetCardOnStack(game,choiceResults,0,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                	if (targetSpell.getController().getPoison() > 0) {
                		game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                	}
                }
			});
		}
	};
}

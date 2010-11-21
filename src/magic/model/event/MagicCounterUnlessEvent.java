package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.stack.MagicCardOnStack;

public class MagicCounterUnlessEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
	
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object choiceResults[]) {
			
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.payManaCost(game,cardOnStack.getController(),choiceResults,1);
			} else {
				game.doAction(new MagicCounterItemOnStackAction(cardOnStack));
			}
		}
	};

	public MagicCounterUnlessEvent(final MagicSource source,final MagicCardOnStack cardOnStack,final MagicManaCost cost) {
	
		super(source,cardOnStack.getController(),new MagicMayChoice("You may pay "+cost.getText()+'.',new MagicPayManaCostChoice(cost)),
			new Object[]{cardOnStack},EVENT_ACTION,"You may$ pay "+cost.getText()+"$. If you don't, counter "+cardOnStack.getName()+".");
	}
}
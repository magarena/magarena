package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTriggerType;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicItemOnStack;

public class MagicExecuteFirstEventAction extends MagicAction {

	private final Object choiceResults[];
	private MagicEvent firstEvent;
	
	public MagicExecuteFirstEventAction(final Object choiceResults[]) {
		this.choiceResults=choiceResults;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		firstEvent=game.getEvents().removeFirst();
		game.executeEvent(firstEvent,choiceResults);
        if (firstEvent.getChoice() instanceof MagicTargetChoice) {
            final MagicTargetChoice tchoice = (MagicTargetChoice)firstEvent.getChoice();
            final MagicItemOnStack item = game.getStack().get(firstEvent);
            if (tchoice.isTargeted() && item != null) {
                game.executeTrigger(MagicTriggerType.WhenTargeted, item);
            }
        }
	}

	@Override
	public void undoAction(final MagicGame game) {

		game.getEvents().addFirst(firstEvent);
	}
}

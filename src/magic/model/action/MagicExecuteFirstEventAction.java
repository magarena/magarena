package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTriggerType;
import magic.model.choice.MagicTargetChoice;

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
            if (tchoice.isTargeted()) {
                game.executeTrigger(MagicTriggerType.WhenTargeted, firstEvent.getSource());
            }
        }
	}

	@Override
	public void undoAction(final MagicGame game) {

		game.getEvents().addFirst(firstEvent);
	}
}

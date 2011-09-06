package magic.model.event;

import magic.model.MagicGame;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicTriggerType;

public class MagicStackGetChoicesEvent extends MagicEvent {
	public MagicStackGetChoicesEvent(final MagicItemOnStack itemOnStack) {
		super(
            itemOnStack.getSource(),
            itemOnStack.getController(),
            itemOnStack.getEvent().getChoice(),
            itemOnStack.getEvent().getTargetPicker(),
            MagicEvent.NO_DATA,
            new MagicEventAction() {
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] data,
                    final Object[] choiceResults) {
                final MagicItemOnStack EEitemOnStack = itemOnStack.map(game);
                EEitemOnStack.setChoiceResults(choiceResults);

                // pay mana cost when there is one.
                event.payManaCost(game,EEitemOnStack.getController(),choiceResults);

                // trigger WhenTargeted
                final MagicTargetChoice tchoice = event.getChoice().getTargetChoice();
                if (tchoice != null && tchoice.isTargeted()) {
                    game.executeTrigger(MagicTriggerType.WhenTargeted,EEitemOnStack);
                }
            }},
            "");
	}
}

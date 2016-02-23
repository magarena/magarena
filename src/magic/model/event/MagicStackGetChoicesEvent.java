package magic.model.event;

import magic.model.MagicGame;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicTriggerType;

public class MagicStackGetChoicesEvent extends MagicEvent {
    public MagicStackGetChoicesEvent(final MagicItemOnStack itemOnStack) {
        super(
            itemOnStack.getEvent().getSource(),
            itemOnStack.getEvent().getPlayer(),
            itemOnStack.getEvent().getChoice(),
            itemOnStack.getEvent().getTargetPicker(),
            itemOnStack,
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicItemOnStack itemOnStack = event.getRefItemOnStack();
        itemOnStack.setChoiceResults(event.getChosen());

        // trigger WhenTargeted
        final MagicTargetChoice tchoice = event.getTargetChoice();
        if (tchoice != null && tchoice.isTargeted()) {
            game.executeTrigger(MagicTriggerType.WhenTargeted,itemOnStack);
        }
    };
}

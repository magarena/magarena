package magic.model.event;

import magic.model.MagicGame;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicTriggerType;

public class MagicStackChangeTargetsEvent extends MagicEvent {
    public MagicStackChangeTargetsEvent(final MagicItemOnStack itemOnStack) {
        super(
            itemOnStack.getEvent().getSource(),
            itemOnStack.getEvent().getPlayer(),
            itemOnStack.getEvent().getTargetChoice(),
            itemOnStack.getEvent().getTargetPicker(),
            itemOnStack,
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicItemOnStack itemOnStack = event.getRefItemOnStack();
        event.setTargetChoice(itemOnStack.getChoiceResults());

        // trigger WhenTargeted
        final MagicTargetChoice tchoice = event.getTargetChoice();
        if (tchoice != null && tchoice.isTargeted()) {
            game.executeTrigger(MagicTriggerType.WhenTargeted,itemOnStack);
        }
    };
}

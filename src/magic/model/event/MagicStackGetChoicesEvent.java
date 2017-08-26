package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicCopyMap;
import magic.model.stack.MagicItemOnStack;
import magic.model.choice.MagicTargetChoice;
import magic.model.trigger.MagicTriggerType;

public class MagicStackGetChoicesEvent extends MagicEvent {

    private final MagicItemOnStack item;

    public MagicStackGetChoicesEvent(final MagicItemOnStack itemOnStack) {
        super(
            itemOnStack.getEvent().getSource(),
            itemOnStack.getEvent().getPlayer(),
            itemOnStack.getEvent().getChoice(),
            itemOnStack.getEvent().getTargetPicker(),
            itemOnStack.getEvent().getRef(),
            EVENT_ACTION,
            ""
        );
        item = itemOnStack;
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicItemOnStack itemOnStack = ((MagicStackGetChoicesEvent)event).item;
        itemOnStack.setChoiceResults(event.getChosen());

        // trigger WhenTargeted
        final MagicTargetChoice tchoice = event.getTargetChoice();
        if (tchoice != null && tchoice.isTargeted()) {
            game.executeTrigger(MagicTriggerType.WhenTargeted,itemOnStack);
        }
    };

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicStackGetChoicesEvent(copyMap.copy(item));
    }
}

package magic.model.event;

import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

public class MagicStackChangeTargetsEvent extends MagicEvent {

    public MagicStackChangeTargetsEvent(final MagicItemOnStack itemOnStack) {
        super(
            itemOnStack.getSource(),
            itemOnStack.getController(),
            itemOnStack.getEvent().getTargetChoice(),
            itemOnStack.getEvent().getTargetPicker(),
            itemOnStack,
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicItemOnStack itemOnStack = event.getRefItemOnStack();
            event.setTargetChoice(itemOnStack.getChoiceResults());
        }
    };
}

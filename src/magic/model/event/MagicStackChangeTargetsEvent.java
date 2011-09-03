package magic.model.event;

import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

public class MagicStackChangeTargetsEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			final MagicItemOnStack itemOnStack=(MagicItemOnStack)data[0];
			itemOnStack.getChoiceResults()[0]=choiceResults[0];
		}
	};

	public MagicStackChangeTargetsEvent(final MagicItemOnStack itemOnStack) {
		super(
            itemOnStack.getSource(),
            itemOnStack.getController(),
            itemOnStack.getEvent().getTargetChoice(),
            itemOnStack.getEvent().getTargetPicker(),
            new Object[]{itemOnStack},
            EVENT_ACTION,
            "");
	}
}

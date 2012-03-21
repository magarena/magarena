package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicReplicateTrigger extends MagicWhenSpellIsCastTrigger {

    private static final MagicReplicateTrigger INSTANCE = new MagicReplicateTrigger();

    private MagicReplicateTrigger() {}

    public static final MagicReplicateTrigger create() {
        return INSTANCE;
    }

    @Override
	public MagicEvent executeTrigger(
			final MagicGame game,
			final MagicPermanent permanent,
			final MagicCardOnStack data) {
		return new MagicEvent(
				data.getSource(),
				data.getController(),
                new MagicKickerChoice(data.getCardDefinition().getCost(),true,true),
                new Object[]{data},
                this,
				"");
	}
	
	@Override
	public void executeEvent(
			final MagicGame game,
			final MagicEvent event,
			final Object data[],
			final Object[] choiceResults) {
		int kickerCount = (Integer)choiceResults[1];
		final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
		for (;kickerCount>0;kickerCount--) {
			game.doAction(new MagicCopyCardOnStackAction(
					cardOnStack.getController(),
					cardOnStack));
		}
	}
	
	@Override
	public boolean usesStack() {
		return false;
	}
}

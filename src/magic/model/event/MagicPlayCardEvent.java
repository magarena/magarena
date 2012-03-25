package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.stack.MagicCardOnStack;

public class MagicPlayCardEvent implements MagicCardEvent,MagicEventAction {

	private static final MagicPlayCardEvent INSTANCE=new MagicPlayCardEvent();
	
	private MagicPlayCardEvent() {}
	
	public static MagicPlayCardEvent create() {
		return INSTANCE;
	}
	
	@Override
	public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
		return new MagicEvent(
                cardOnStack.getCard(),
                cardOnStack.getController(),
                new Object[]{cardOnStack},
                this,
                "Put " + cardOnStack.getName() + " onto the battlefield.");
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
		game.doAction(new MagicPlayCardFromStackAction(cardOnStack));
	}
}

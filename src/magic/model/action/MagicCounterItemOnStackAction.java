package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;

public class MagicCounterItemOnStackAction extends MagicAction {

	private final MagicItemOnStack itemOnStack;
	private final MagicLocationType toLocation;
	
	public MagicCounterItemOnStackAction(final MagicItemOnStack itemOnStack,final MagicLocationType toLocation) {
		this.itemOnStack=itemOnStack;
		this.toLocation=toLocation;
	}
	
	public MagicCounterItemOnStackAction(final MagicItemOnStack itemOnStack) {
		this(itemOnStack,MagicLocationType.Graveyard);
	}

	@Override
	public void doAction(final MagicGame game) {
		if (game.getStack().contains(itemOnStack) && itemOnStack.canBeCountered()) {
			game.doAction(new MagicRemoveItemFromStackAction(itemOnStack));
			if (itemOnStack.isSpell()) {
				final MagicCardOnStack cardOnStack=(MagicCardOnStack)itemOnStack;
				game.doAction(new MagicMoveCardAction(cardOnStack.getCard(),MagicLocationType.Stack,toLocation));
			}
			game.setStateCheckRequired();
		}
	}

	@Override
	public void undoAction(final MagicGame game) {
        game.setStateCheckRequired();
	}
}

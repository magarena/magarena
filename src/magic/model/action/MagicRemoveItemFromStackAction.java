package magic.model.action;

import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;
import magic.model.stack.MagicStack;

public class MagicRemoveItemFromStackAction extends MagicAction {

	private final MagicItemOnStack itemOnStack;
	private int position;
	
	public MagicRemoveItemFromStackAction(final MagicItemOnStack itemOnStack) {

		this.itemOnStack=itemOnStack;
	}
	
	@Override
	public void doAction(final MagicGame game) {

		final MagicStack stack=game.getStack();
		position=stack.indexOf(itemOnStack);
		game.getStack().removeFrom(position);
	}

	@Override
	public void undoAction(final MagicGame game) {

		game.getStack().addTo(position,itemOnStack);
	}
}
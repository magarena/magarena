package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicStackChangeTargetsEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicCopyCardOnStackAction extends MagicAction {

	private final MagicPlayer player;
	private final MagicCardOnStack cardOnStack;
	
	public MagicCopyCardOnStackAction(final MagicPlayer player,final MagicCardOnStack cardOnStack) {
		this.player=player;
		this.cardOnStack=cardOnStack;
	}

	@Override
	public void doAction(final MagicGame game) {
		final MagicCardOnStack copyCardOnStack=cardOnStack.copyCardOnStack(player);
		final long id=game.incTime(); //createIdentifier(MagicIdentifierType.ItemOnStack);
		copyCardOnStack.setId(id);
		game.getStack().addToTop(copyCardOnStack);
		if (copyCardOnStack.getEvent().getTargetChoice()!=null) {
			copyCardOnStack.getChoiceResults()[0]=null;
			game.addEvent(new MagicStackChangeTargetsEvent(copyCardOnStack));
		}
	}

	@Override
	public void undoAction(final MagicGame game) {
		game.getStack().removeFromTop();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+" ("+cardOnStack.getName()+')';
	}
}

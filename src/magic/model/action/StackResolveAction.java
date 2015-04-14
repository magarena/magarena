package magic.model.action;

import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

public class StackResolveAction extends MagicAction {

    private MagicItemOnStack itemOnStack;

    @Override
    public void doAction(final MagicGame game) {
        itemOnStack=game.getStack().removeFromTop();
        itemOnStack.resolve(game);
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.getStack().addToTop(itemOnStack);
    }
}

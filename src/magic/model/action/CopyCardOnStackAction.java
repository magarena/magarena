package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicStackChangeTargetsEvent;
import magic.model.stack.MagicCardOnStack;

public class CopyCardOnStackAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicCardOnStack cardOnStack;

    public CopyCardOnStackAction(final MagicPlayer player,final MagicCardOnStack cardOnStack) {
        this.player=player;
        this.cardOnStack=cardOnStack;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardOnStack copyCardOnStack=cardOnStack.copyCardOnStack(player);
        game.getStack().addToTop(copyCardOnStack);
        if (copyCardOnStack.getEvent().getTargetChoice().isValid()) {
            copyCardOnStack.getEvent().clearTargetChoice(copyCardOnStack.getChoiceResults());
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

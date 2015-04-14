package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.event.MagicSpellCastEvent;
import magic.model.event.MagicStackGetChoicesEvent;
import magic.model.stack.MagicItemOnStack;

public class PutItemOnStackAction extends MagicAction {

    private final MagicItemOnStack itemOnStack;

    public PutItemOnStackAction(final MagicItemOnStack itemOnStack) {
        this.itemOnStack=itemOnStack;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.getStack().addToTop(itemOnStack);
        if (itemOnStack.hasChoice()) {
            game.addEvent(new MagicStackGetChoicesEvent(itemOnStack));
        }
        if (itemOnStack.isSpell()) {
            game.addEvent(new MagicSpellCastEvent(itemOnStack));
        }
        // Avoid unnecessary actions on stack by reducing score.
        setScore(itemOnStack.getController(),ArtificialScoringSystem.ITEM_ON_STACK_SCORE);
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.getStack().removeFromTop();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+itemOnStack.getClass().getSimpleName()+','+itemOnStack.getName()+')';
    }
}

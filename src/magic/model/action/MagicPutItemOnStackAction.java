package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicStackGetChoicesEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class MagicPutItemOnStackAction extends MagicAction {

    private final MagicItemOnStack itemOnStack;
    
    public MagicPutItemOnStackAction(final MagicItemOnStack itemOnStack) {
        this.itemOnStack=itemOnStack;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        itemOnStack.setId(game.getUniqueId());
        game.getStack().addToTop(itemOnStack);
        if (itemOnStack.hasChoices()) {
            game.addEvent(new MagicStackGetChoicesEvent(itemOnStack));
        }
        if (itemOnStack.isSpell()) {
            // execute spell is cast triggers
            for (final MagicTrigger<?> trigger : itemOnStack.getSource().getCardDefinition().getSpellIsCastTriggers()) {
                game.executeTrigger(trigger,MagicPermanent.NONE,itemOnStack.getSource(),(MagicCardOnStack)itemOnStack);
            }
            
            // execute other spell is cast triggers
            game.executeTrigger(MagicTriggerType.WhenOtherSpellIsCast,itemOnStack);
            
            game.setSpellsPlayed(game.getSpellsPlayed() + 1);
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

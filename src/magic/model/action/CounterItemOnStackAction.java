package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;

public class CounterItemOnStackAction extends MagicAction {

    private final MagicItemOnStack itemOnStack;
    private final MagicLocationType toLocation;

    public CounterItemOnStackAction(final MagicItemOnStack aItemOnStack,final MagicLocationType aToLocation) {
        itemOnStack = aItemOnStack;
        toLocation = aToLocation;
    }

    public CounterItemOnStackAction(final MagicItemOnStack itemOnStack) {
        this(itemOnStack, MagicLocationType.Stack);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (game.getStack().contains(itemOnStack) && itemOnStack.canBeCountered()) {
            game.doAction(new MagicRemoveItemFromStackAction(itemOnStack));
            if (itemOnStack.isSpell()) {
                final MagicCardOnStack cardOnStack = (MagicCardOnStack)itemOnStack;
                final MagicLocationType destination = (toLocation == MagicLocationType.Stack) ? 
                    cardOnStack.getMoveLocation() :
                    toLocation;
                
                game.doAction(new MagicMoveCardAction(
                    cardOnStack.getCard(),
                    MagicLocationType.Stack,
                    destination
                ));
            }
            game.setStateCheckRequired();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {

    }
}

package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;

public class MagicChangeCardDestinationAction extends MagicAction {

    private final MagicCardOnStack cardOnStack;
    private final MagicLocationType newLocation;
    private MagicLocationType oldLocation;

    public MagicChangeCardDestinationAction(final MagicCardOnStack aCardOnStack, final MagicLocationType dest) {
        cardOnStack = aCardOnStack;
        newLocation = dest;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldLocation = cardOnStack.getMoveLocation();
        cardOnStack.setMoveLocation(newLocation);
    }

    @Override
    public void undoAction(final MagicGame game) {
        cardOnStack.setMoveLocation(oldLocation);
    }
}

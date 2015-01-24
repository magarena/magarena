package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicObject;
import magic.model.stack.MagicCardOnStack;

public class MagicEnterAsCopyAction extends MagicAction {

    private final MagicCardOnStack cardOnStack;
    private final MagicObject obj;
    private MagicLocationType oldLocation;

    public MagicEnterAsCopyAction(final MagicCardOnStack aCardOnStack, final MagicObject aObj) {
        cardOnStack = aCardOnStack;
        obj = aObj;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldLocation = cardOnStack.getMoveLocation();
        cardOnStack.setMoveLocation(MagicLocationType.Play);
        final MagicCardOnStack replacement = new MagicCardOnStack(
            cardOnStack.getCard(),
            obj,
            cardOnStack.getController(),
            obj.getCardDefinition().getCardEvent(),
            cardOnStack.getPayedCost(),
            cardOnStack.getModifications()
        );
        game.doAction(new MagicPutItemOnStackAction(replacement));
    }

    @Override
    public void undoAction(final MagicGame game) {
        cardOnStack.setMoveLocation(oldLocation);
    }
}

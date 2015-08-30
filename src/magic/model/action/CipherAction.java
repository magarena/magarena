package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.event.MagicCipherEvent;
import magic.model.stack.MagicCardOnStack;

public class CipherAction extends MagicAction {

    private final MagicCardOnStack cardOnStack;
    private final MagicPlayer controller;

    public CipherAction(final MagicCardOnStack aCardOnStack, final MagicPlayer aController) {
        cardOnStack = aCardOnStack;
        controller = aController;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (cardOnStack.isRepresentedByACard()) {
            // prevent auto move to graveyard
            game.doAction(new ChangeCardDestinationAction(cardOnStack, MagicLocationType.Play));
            game.addEvent(new MagicCipherEvent(cardOnStack, controller));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}

package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.event.MagicCipherEvent;

public class MagicCipherAction extends MagicAction {

    private final MagicCardOnStack cardOnStack;
    private final MagicPlayer controller;

    public MagicCipherAction(final MagicCardOnStack aCardOnStack, final MagicPlayer aController) {
        cardOnStack = aCardOnStack;
        controller = aController;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (cardOnStack.getCard().isToken() == false) {
            // prevent auto move to graveyard
            game.doAction(new MagicChangeCardDestinationAction(cardOnStack, MagicLocationType.Play));
            game.addEvent(new MagicCipherEvent(cardOnStack, controller));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}

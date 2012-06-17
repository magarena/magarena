package magic.model.action;

import magic.model.stack.MagicCardOnStack;

public interface MagicCardOnStackAction {
    void doAction(final MagicCardOnStack card);
}

package magic.model.action;

import magic.model.stack.MagicCardOnStack;

@FunctionalInterface
public interface MagicCardOnStackAction {
    void doAction(final MagicCardOnStack card);
}

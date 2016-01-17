package magic.model.action;

import magic.model.stack.MagicItemOnStack;

@FunctionalInterface
public interface MagicItemOnStackAction {
    void doAction(final MagicItemOnStack item);
}

package magic.model.action;

import magic.model.MagicCard;

@FunctionalInterface
public interface MagicCardAction {
    void doAction(final MagicCard card);
}

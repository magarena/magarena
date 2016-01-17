package magic.model.action;

import magic.model.MagicPermanent;

@FunctionalInterface
public interface MagicPermanentAction {
    void doAction(final MagicPermanent perm);
}

package magic.model.action;

import magic.model.MagicCopyable;
import magic.model.MagicPermanent;

@FunctionalInterface
public interface MagicPermanentAction extends MagicCopyable {
    void doAction(final MagicPermanent perm);
}

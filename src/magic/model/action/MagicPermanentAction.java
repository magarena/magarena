package magic.model.action;

import magic.model.MagicPermanent;
import magic.model.MagicCopyable;

@FunctionalInterface
public interface MagicPermanentAction extends MagicCopyable {
    void doAction(final MagicPermanent perm);
}

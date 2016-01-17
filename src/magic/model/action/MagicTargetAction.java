package magic.model.action;

import magic.model.target.MagicTarget;

@FunctionalInterface
public interface MagicTargetAction {
    void doAction(final MagicTarget perm);
}

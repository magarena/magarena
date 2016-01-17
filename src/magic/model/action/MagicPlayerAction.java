package magic.model.action;

import magic.model.MagicPlayer;

@FunctionalInterface
public interface MagicPlayerAction {
    void doAction(final MagicPlayer player);
}

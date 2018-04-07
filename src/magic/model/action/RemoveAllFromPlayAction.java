package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

import java.util.Collection;
import java.util.ArrayList;

public class RemoveAllFromPlayAction extends MagicAction {

    private final Collection<MagicPermanent> perms = new ArrayList<>();
    private final MagicLocationType toLocation;

    public RemoveAllFromPlayAction(final Collection<MagicPermanent> aPerms, final MagicLocationType aToLocation) {
        perms.addAll(aPerms);
        toLocation = aToLocation;
    }

    @Override
    public void doAction(final MagicGame game) {
        final boolean isLibrary = toLocation == MagicLocationType.OwnersLibrary;
        final MagicLocationType tempLocation = isLibrary ? MagicLocationType.TopOfOwnersLibrary : toLocation;
        final boolean[] shouldShuffle = {false, false};
        for (final MagicPermanent perm : perms) {
            if (perms.size() > 1) {
                game.doAction(RemoveFromPlayAction.NoUpdate(perm, tempLocation));
            } else {
                game.doAction(new RemoveFromPlayAction(perm, tempLocation));
            }
            if (isLibrary) {
                shouldShuffle[perm.getOwner().getIndex()] = true;
            }
        }
        for (final MagicPlayer player : game.getAPNAP()) {
            if (shouldShuffle[player.getIndex()]) {
                game.doAction(new ShuffleLibraryAction(player));
            }
        }
        if (perms.size() > 1) {
            game.update();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {}
}

package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

import java.util.Collection;
import java.util.ArrayList;

public class RemoveAllFromPlayAction extends MagicAction {
    
    private final Collection<MagicPermanent> perms = new ArrayList<MagicPermanent>();
    private MagicLocationType toLocation;
    
    public RemoveAllFromPlayAction(final Collection<MagicPermanent> aPerms, final MagicLocationType aToLocation) {
        perms.addAll(aPerms);
        toLocation = aToLocation;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        for (final MagicPermanent perm : perms) {
            if (perms.size() > 1) {
                game.doAction(RemoveFromPlayAction.NoUpdate(perm, toLocation));
            } else {
                game.doAction(new RemoveFromPlayAction(perm, toLocation));
            }
        }
        if (perms.size() > 1) {
            game.update();
        }
    }
    
    @Override
    public void undoAction(final MagicGame game) {}
}

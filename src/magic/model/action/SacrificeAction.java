package magic.model.action;

import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class SacrificeAction extends RemoveFromPlayAction {
    public SacrificeAction(final MagicPermanent permanent) {
        super(permanent,MagicLocationType.Graveyard);
    }
}

package magic.model.action;

import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class MagicSacrificeAction extends RemoveFromPlayAction {
    public MagicSacrificeAction(final MagicPermanent permanent) {
        super(permanent,MagicLocationType.Graveyard);
    }
}

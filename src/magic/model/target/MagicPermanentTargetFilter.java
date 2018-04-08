package magic.model.target;

import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

// Permanent reference can not be used because game is copied.
public class MagicPermanentTargetFilter extends MagicPermanentFilterImpl {

    private final long id;

    public MagicPermanentTargetFilter(final MagicPermanent validPermanent) {
        id = validPermanent.getId();
    }
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isPermanent() &&
               target.getId() == id;
    }
}

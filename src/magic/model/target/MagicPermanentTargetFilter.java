package magic.model.target;

import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicGame;

// Permanent reference can not be used because game is copied.
public class MagicPermanentTargetFilter extends MagicPermanentFilterImpl {

    private final long id;

    public MagicPermanentTargetFilter(final MagicPermanent validPermanent) {
        id = validPermanent.getId();
    }
    @Override
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isPermanent() &&
               target.getId() == id;
    }
};

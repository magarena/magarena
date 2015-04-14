package magic.model.target;

import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

// Permanent reference can not be used because game is copied.
public class MagicOtherPermanentTargetFilter extends MagicPermanentFilterImpl {

    private final MagicTargetFilter<MagicPermanent> targetFilter;
    private final long id;

    public MagicOtherPermanentTargetFilter(final MagicTargetFilter<MagicPermanent> targetFilter,final MagicPermanent invalidPermanent) {
        this.targetFilter=targetFilter;
        this.id=invalidPermanent.getId();
    }
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return targetFilter.accept(source,player,target) &&
               target.getId() != id;
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

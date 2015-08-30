package magic.model.target;

import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

// Permanent reference can not be used because game is copied.
public class MagicOtherPermanentTargetFilter extends MagicPermanentFilterImpl {

    private final MagicTargetFilter<MagicPermanent> targetFilter;
    private final long id;
    
    public MagicOtherPermanentTargetFilter(final MagicTargetFilter<MagicPermanent> aTargetFilter) {
        targetFilter = aTargetFilter;
        id = 0;
    }

    public MagicOtherPermanentTargetFilter(final MagicTargetFilter<MagicPermanent> aTargetFilter,final MagicPermanent invalidPermanent) {
        targetFilter = aTargetFilter;
        id = invalidPermanent.getId();
    }

    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return targetFilter.accept(source,player,target) &&
               ((id != 0 && id != target.getId()) ||
                (id == 0 && source != target));
    }

    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

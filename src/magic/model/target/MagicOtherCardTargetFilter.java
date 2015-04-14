package magic.model.target;

import magic.model.MagicCard;
import magic.model.MagicSource;
import magic.model.MagicPlayer;

// Permanent reference can not be used because game is copied.
public class MagicOtherCardTargetFilter extends MagicCardFilterImpl {

    private final MagicTargetFilter<MagicCard> targetFilter;
    private final long id;

    public MagicOtherCardTargetFilter(final MagicTargetFilter<MagicCard> targetFilter,final MagicCard invalidCard) {
        this.targetFilter=targetFilter;
        this.id=invalidCard.getId();
    }
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return targetFilter.accept(source,player,target) &&
               target.getId() != id;
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

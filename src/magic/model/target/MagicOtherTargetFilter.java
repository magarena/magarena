package magic.model.target;

import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicSource;

// reference can not be used because game is copied.
public class MagicOtherTargetFilter extends MagicTargetFilterImpl {

    private final MagicTargetFilter<MagicTarget> targetFilter;
    private final long id;

    public MagicOtherTargetFilter(final MagicTargetFilter<MagicTarget> aTargetFilter) {
        targetFilter = aTargetFilter;
        id = 0;
    }

    public MagicOtherTargetFilter(final MagicTargetFilter<MagicTarget> aTargetFilter, final MagicTarget invalid) {
        targetFilter = aTargetFilter;
        id = invalid.getId();
    }

    private static boolean different(final MagicSource source, final MagicTarget target) {
        final MagicSource sourceCard = source.isPermanent() ? ((MagicPermanent)source).getCard() : source;
        return source != target && sourceCard != target;
    }

    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicTarget target) {
        return targetFilter.accept(source,player,target) &&
               ((id != 0 && id != target.getId()) ||
                (id == 0 && different(source, target)));
    }

    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.target.MagicTarget;

// reference can not be used because game is copied.
public class MagicOtherTargetFilter extends MagicTargetFilterImpl {

    private final MagicTargetFilter<MagicTarget> targetFilter;
    private final long id;

    public MagicOtherTargetFilter(final MagicTargetFilter<MagicTarget> aTargetFilter, final MagicTarget invalid) {
        targetFilter = aTargetFilter;
        id = invalid.getId();
    }
    @Override
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        return targetFilter.accept(game,player,target) &&
               target.getId() != id;
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

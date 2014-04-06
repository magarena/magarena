package magic.model.target;

import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicGame;

public class MagicPowerTargetFilter extends MagicPermanentFilterImpl {

    private final MagicPermanentFilterImpl targetFilter;
    private final int power;

    public MagicPowerTargetFilter(final MagicPermanentFilterImpl targetFilter,final int power) {
        this.targetFilter = targetFilter;
        this.power = power;
    }
    @Override
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return targetFilter.accept(game,player,target) &&
               target.getPower() <= power;
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
};

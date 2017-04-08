package magic.model.target;

import magic.model.MagicCard;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public class MagicCMCCardFilter extends MagicCardFilterImpl {

    private final MagicTargetFilter<MagicCard> targetFilter;
    private final Operator operator;
    private final int cmc;

    public MagicCMCCardFilter(final MagicTargetFilter<MagicCard> targetFilter,final Operator operator,final int cmc) {
        this.targetFilter = targetFilter;
        this.operator = operator;
        this.cmc = cmc;
    }

    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return targetFilter.accept(source,player,target) &&
               operator.cmp(target.getCardDefinition().getConvertedCost(), cmc) ;
    }

    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

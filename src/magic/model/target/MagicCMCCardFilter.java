package magic.model.target;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

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
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return targetFilter.accept(game,player,target) &&
               operator.cmp(target.getCardDefinition().getConvertedCost(), cmc) ;
    }

    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

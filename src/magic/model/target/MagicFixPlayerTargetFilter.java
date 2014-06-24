package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

import java.util.List;

public class MagicFixPlayerTargetFilter extends MagicTargetFilterImpl {

    private final MagicTargetFilter<MagicTarget> targetFilter;
    private final int pIndex;

    public MagicFixPlayerTargetFilter(final MagicTargetFilter<MagicTarget> aTargetFilter,final MagicPlayer player) {
        targetFilter = aTargetFilter;
        pIndex = player.getIndex();
    }
    @Override 
    public List<MagicTarget> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        return super.filter(game, game.getPlayer(pIndex), MagicTargetHint.None);
    }
    @Override
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        return targetFilter.accept(game,game.getPlayer(pIndex),target);
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetFilter.acceptType(targetType);
    }
}

package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class MagicPermanentFilterImpl implements MagicTargetFilter<MagicPermanent> {
    public List<MagicPermanent> filter(final MagicGame game, final MagicPlayer player) {
        return filter(game, player, MagicTargetHint.None);
    }

    public List<MagicPermanent> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicPermanent> targets=new ArrayList<MagicPermanent>();
        if (acceptType(MagicTargetType.Permanent)) {
            for (final MagicPlayer controller : game.getPlayers()) {
                for (final MagicPermanent targetPermanent : controller.getPermanents()) {
                    if (accept(game,player,targetPermanent) &&
                        targetHint.accept(player,targetPermanent)) {
                        targets.add(targetPermanent);
                    }
                }
            }
        }
        return targets;
    }
    
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Permanent;
    }

    public MagicPermanentFilterImpl except(final MagicPermanent invalid) {
        return new MagicOtherPermanentTargetFilter(this, invalid);
    }
}

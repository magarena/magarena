package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class MagicPermanentFilterImpl implements MagicTargetFilter<MagicPermanent> {
    public List<MagicPermanent> filter(final MagicGame game) {
        return filter(MagicSource.NONE, game.getTurnPlayer(), MagicTargetHint.None);
    }
    
    public List<MagicPermanent> filter(final MagicPlayer player) {
        return filter(MagicSource.NONE, player, MagicTargetHint.None);
    }

    public List<MagicPermanent> filter(final MagicEvent event) {
        return filter(event.getSource(), event.getPlayer(), MagicTargetHint.None);
    }
    
    public List<MagicPermanent> filter(final MagicSource source, MagicPlayer player, final MagicTargetHint targetHint) {
        final MagicGame game = player.getGame();
        final List<MagicPermanent> targets=new ArrayList<MagicPermanent>();
        if (acceptType(MagicTargetType.Permanent)) {
            for (final MagicPlayer controller : game.getPlayers()) {
                for (final MagicPermanent targetPermanent : controller.getPermanents()) {
                    if (accept(source,player,targetPermanent) &&
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

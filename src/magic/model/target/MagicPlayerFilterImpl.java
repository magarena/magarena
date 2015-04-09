package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class MagicPlayerFilterImpl implements MagicTargetFilter<MagicPlayer> {
    public List<MagicPlayer> filter(final MagicGame game, final MagicPlayer player) {
        return filter(game, player, MagicTargetHint.None);
    }
    
    public List<MagicPlayer> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicPlayer> targets=new ArrayList<MagicPlayer>();

        // Players
        if (acceptType(MagicTargetType.Player)) {
            for (final MagicPlayer targetPlayer : game.getPlayers()) {
                if (accept(game,player,targetPlayer) &&
                    targetHint.accept(player,targetPlayer)) {
                    targets.add(targetPlayer);
                }
            }
        }

        return targets;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Player;
    }
}

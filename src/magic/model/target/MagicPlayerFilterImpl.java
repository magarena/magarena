package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class MagicPlayerFilterImpl implements MagicTargetFilter<MagicPlayer> {
    @Override
    public List<MagicPlayer> filter(final MagicSource source) {
        return filter(source, source.getController(), MagicTargetHint.None);
    }

    @Override
    public List<MagicPlayer> filter(final MagicPlayer player) {
        return filter(MagicSource.NONE, player, MagicTargetHint.None);
    }

    @Override
    public List<MagicPlayer> filter(final MagicEvent event) {
        return filter(event.getSource(), event.getPlayer(), MagicTargetHint.None);
    }

    @Override
    public List<MagicPlayer> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint) {
        final MagicGame game = player.getGame();
        final List<MagicPlayer> targets= new ArrayList<>();

        // Players
        if (acceptType(MagicTargetType.Player)) {
            for (final MagicPlayer targetPlayer : game.getPlayers()) {
                if (accept(source,player,targetPlayer) &&
                    targetHint.accept(player,targetPlayer)) {
                    targets.add(targetPlayer);
                }
            }
        }

        return targets;
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Player;
    }
}

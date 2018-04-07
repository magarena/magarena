package magic.model.target;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public abstract class MagicTargetFilterImpl implements MagicTargetFilter<MagicTarget> {
    @Override
    public List<MagicTarget> filter(final MagicSource source) {
        return filter(source, source.getController(), MagicTargetHint.None);
    }

    @Override
    public List<MagicTarget> filter(final MagicPlayer player) {
        return filter(MagicSource.NONE, player, MagicTargetHint.None);
    }

    @Override
    public List<MagicTarget> filter(final MagicEvent event) {
        return filter(event.getSource(), event.getPlayer(), MagicTargetHint.None);
    }

    @Override
    public List<MagicTarget> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint) {
        final MagicGame game = player.getGame();
        final List<MagicTarget> targets= new ArrayList<>();

        // Players
        if (acceptType(MagicTargetType.Player)) {
            for (final MagicPlayer targetPlayer : game.getPlayers()) {
                if (accept(source,player,targetPlayer) &&
                    targetHint.accept(player,targetPlayer)) {
                    targets.add(targetPlayer);
                }
            }
        }

        // Permanents
        if (acceptType(MagicTargetType.Permanent)) {
            for (final MagicPlayer controller : game.getPlayers()) {
                targets.addAll(controller.getPermanents().stream().filter(targetPermanent -> accept(source, player, targetPermanent) &&
                    targetHint.accept(player, targetPermanent)).collect(Collectors.toList()));
            }
        }

        // Items on stack
        if (acceptType(MagicTargetType.Stack)) {
            targets.addAll(game.getStack().stream().filter(targetItemOnStack -> accept(source, player, targetItemOnStack) &&
                targetHint.accept(player, targetItemOnStack)).collect(Collectors.toList()));
        }

        // Cards in graveyard
        if (acceptType(MagicTargetType.Graveyard)) {
            targets.addAll(player.getGraveyard().stream().filter(targetCard -> accept(source, player, targetCard)).collect(Collectors.toList()));
        }

        // Cards in opponent's graveyard
        if (acceptType(MagicTargetType.OpponentsGraveyard)) {
            targets.addAll(player.getOpponent().getGraveyard().stream().filter(targetCard -> accept(source, player, targetCard)).collect(Collectors.toList()));
        }

        // Cards in opponent's exile
        if (acceptType(MagicTargetType.OpponentsExile)) {
            targets.addAll(player.getOpponent().getExile().stream().filter(targetCard -> accept(source, player, targetCard)).collect(Collectors.toList()));
        }

        // Cards in hand
        if (acceptType(MagicTargetType.Hand)) {
            targets.addAll(player.getHand().stream().filter(targetCard -> accept(source, player, targetCard)).collect(Collectors.toList()));
        }

        // Cards in library
        if (acceptType(MagicTargetType.Library)) {
            targets.addAll(player.getLibrary().stream().filter(targetCard -> accept(source, player, targetCard)).collect(Collectors.toList()));
        }

        return targets;
    }
}

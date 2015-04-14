package magic.model.target;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.stack.MagicItemOnStack;

import java.util.ArrayList;
import java.util.List;

public abstract class MagicTargetFilterImpl implements MagicTargetFilter<MagicTarget> {
    public List<MagicTarget> filter(final MagicGame game) {
        return filter(MagicSource.NONE, game.getTurnPlayer(), MagicTargetHint.None);
    }
    
    public List<MagicTarget> filter(final MagicPlayer player) {
        return filter(MagicSource.NONE, player, MagicTargetHint.None);
    }
    
    public List<MagicTarget> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint) {
        final MagicGame game = player.getGame();
        final List<MagicTarget> targets=new ArrayList<MagicTarget>();

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
                for (final MagicPermanent targetPermanent : controller.getPermanents()) {
                    if (accept(source,player,targetPermanent) &&
                        targetHint.accept(player,targetPermanent)) {
                        targets.add(targetPermanent);
                    }
                }
            }
        }

        // Items on stack
        if (acceptType(MagicTargetType.Stack)) {
            for (final MagicItemOnStack targetItemOnStack : game.getStack()) {
                if (accept(source,player,targetItemOnStack) &&
                    targetHint.accept(player,targetItemOnStack)) {
                    targets.add(targetItemOnStack);
                }
            }
        }

        // Cards in graveyard
        if (acceptType(MagicTargetType.Graveyard)) {
            for (final MagicCard targetCard : player.getGraveyard()) {
                if (accept(source,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }

        // Cards in opponent's graveyard
        if (acceptType(MagicTargetType.OpponentsGraveyard)) {
            for (final MagicCard targetCard : player.getOpponent().getGraveyard()) {
                if (accept(source,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }

        // Cards in hand
        if (acceptType(MagicTargetType.Hand)) {
            for (final MagicCard targetCard : player.getHand()) {
                if (accept(source,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }
        
        // Cards in library
        if (acceptType(MagicTargetType.Library)) {
            for (final MagicCard targetCard : player.getLibrary()) {
                if (accept(source,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }

        return targets;
    }
}

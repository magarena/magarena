package magic.model.target;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.EnumSet;
import java.util.HashSet;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.choice.MagicTargetChoice;

public abstract class MagicTargetFilterImpl implements MagicTargetFilter<MagicTarget> {
    public List<MagicTarget> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicTarget> targets=new ArrayList<MagicTarget>();

        // Players
        if (acceptType(MagicTargetType.Player)) {
            for (final MagicPlayer targetPlayer : game.getPlayers()) {
                if (accept(game,player,targetPlayer) &&
                    targetHint.accept(player,targetPlayer)) {
                    targets.add(targetPlayer);
                }
            }
        }

        // Permanents
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

        // Items on stack
        if (acceptType(MagicTargetType.Stack)) {
            for (final MagicItemOnStack targetItemOnStack : game.getStack()) {
                if (accept(game,player,targetItemOnStack) &&
                    targetHint.accept(player,targetItemOnStack)) {
                    targets.add(targetItemOnStack);
                }
            }
        }

        // Cards in graveyard
        if (acceptType(MagicTargetType.Graveyard)) {
            for (final MagicCard targetCard : player.getGraveyard()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }

        // Cards in opponent's graveyard
        if (acceptType(MagicTargetType.OpponentsGraveyard)) {
            for (final MagicCard targetCard : player.getOpponent().getGraveyard()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }

        // Cards in hand
        if (acceptType(MagicTargetType.Hand)) {
            for (final MagicCard targetCard : player.getHand()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }
        
        // Cards in library
        if (acceptType(MagicTargetType.Library)) {
            for (final MagicCard targetCard : player.getLibrary()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }
            }
        }

        return targets;
    }
}

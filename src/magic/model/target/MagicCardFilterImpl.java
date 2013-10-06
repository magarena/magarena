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

public abstract class MagicCardFilterImpl implements MagicTargetFilter<MagicCard> {
    public List<MagicCard> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicCard> targets = new ArrayList<MagicCard>();

        // Cards in graveyard
        if (acceptType(MagicTargetType.Graveyard)) {
            add(game, player, player.getGraveyard(), targets, false);
        }

        // Cards in opponent's graveyard
        if (acceptType(MagicTargetType.OpponentsGraveyard)) {
            add(game, player, player.getOpponent().getGraveyard(), targets, false);
        }

        // Cards in hand
        if (acceptType(MagicTargetType.Hand)) {
            add(game, player, player.getHand(), targets, false);
        }
        
        // Cards in library
        if (acceptType(MagicTargetType.Library)) {
            // only consider unique cards, possible as cards in library will not be counted
            add(game, player, player.getLibrary(), targets, true);
        }

        return targets;
    }

    private void add(final MagicGame game, final MagicPlayer player, final MagicCardList cards, final List<MagicCard> targets, final boolean unique) {
        final Set<Long> added = new HashSet<Long>();
        for (final MagicCard card : cards) {
            if (accept(game,player,card) && (unique == false || added.contains(card.getStateId()) == false)) {
                targets.add(card);
                added.add(card.getStateId());
            }
        }
    }
}

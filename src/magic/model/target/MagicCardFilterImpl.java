package magic.model.target;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private void add(final MagicGame game, final MagicPlayer player, final List<MagicCard> cards, final List<MagicCard> targets, final boolean unique) {
        final Set<Long> added = new HashSet<Long>();
        for (final MagicCard card : cards) {
            if (accept(game,player,card) && (unique == false || added.contains(card.getStateId()) == false)) {
                targets.add(card);
                added.add(card.getStateId());
            }
        }
    }
}

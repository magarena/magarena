package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

import java.util.List;
import magic.model.MagicMessage;

public class MillLibraryAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;
    private final MagicCardList milledCards = new MagicCardList();

    public MillLibraryAction(final MagicPlayer aPlayer,final int aAmount) {
        player = aPlayer;
        amount = aAmount;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList topN = player.getLibrary().getCardsFromTop(amount);
        for (final MagicCard card : topN) {
            milledCards.add(card);
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.OwnersLibrary,
                MagicLocationType.Graveyard
            ));
        }
        final int count = topN.size();
        if (count > 0) {
            setScore(player,ArtificialScoringSystem.getMillScore(count));
            game.logAppendMessage(
                player,
                String.format(
                    "%s puts the top %d cards of his or her library into his or her graveyard. (%s)",
                    player,
                    count,
                    count > 5 ? "..." : MagicMessage.getTokenizedCardNames(topN)
                )
            );
        }
    }

    public List<MagicCard> getMilledCards() {
        return milledCards;
    }

    @Override
    public void undoAction(final MagicGame game) {}
}

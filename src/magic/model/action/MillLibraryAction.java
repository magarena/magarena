package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

import java.util.List;

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
            game.doAction(new MagicRemoveCardAction(
                card,
                MagicLocationType.OwnersLibrary
            ));
            game.doAction(new MoveCardAction(
                card,
                MagicLocationType.OwnersLibrary,
                MagicLocationType.Graveyard
            ));
        }
        final int count = topN.size();
        if (count > 0) {
            setScore(player,ArtificialScoringSystem.getMillScore(count));
            game.logMessage(
                player,
                player + " puts the top " + count +
                " cards of his or her library into his or her graveyard."
            );
        }
    }

    public List<MagicCard> getMilledCards() {
        return milledCards;
    }

    @Override
    public void undoAction(final MagicGame game) {}
}

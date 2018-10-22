package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;

import java.util.List;

/**
 * Ancestor of various library-milling actions
 */
public abstract class AbstractMillAction extends MagicAction {
    protected final MagicPlayer player;
    protected final MagicCardList milledCards = new MagicCardList();

    protected AbstractMillAction(final MagicPlayer player) {
        this.player = player;
    }

    public List<MagicCard> getMilledCards() {
        return milledCards;
    }

    @Override
    public void undoAction(final MagicGame game) {
    }

    protected String getMillDescription(int finalCount) {
        return String.format("top %d cards", finalCount);
    }

    protected abstract void setCardsToMill(MagicGame game);


    @Override
    public void doAction(final MagicGame game) {
        getMilledCards().clear();
        setCardsToMill(game);
        final int count = getMilledCards().size();
        if (count > 0) {
            setScore(player,ArtificialScoringSystem.getMillScore(count));
            game.logAppendMessage(
                player,
                String.format(
                    "%s puts the %s of his or her library into his or her graveyard. (%s)",
                    player,
                    getMillDescription(count),
                    count > 5 ? "..." : MagicMessage.getTokenizedCardNames(getMilledCards())
                )
            );
        }
    }
}

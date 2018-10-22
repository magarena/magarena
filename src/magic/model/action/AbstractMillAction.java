package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;

import java.util.List;

/**
 * Ancestor of various library-milling actions
 */
public abstract class AbstractMillAction extends MagicAction {
    protected final MagicPlayer player;

    protected AbstractMillAction(final MagicPlayer player) {
        this.player = player;
    }

    protected abstract List<MagicCard> getMilledCards();

    @Override
    public void undoAction(final MagicGame game) {
    }

    protected String getMillDescription(int finalCount) {
        return String.format("top %d cards", finalCount);
    }

    @Override
    public void doAction(final MagicGame game) {
        List<MagicCard> toMill = getMilledCards();
        for (final MagicCard card : toMill) {
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.OwnersLibrary,
                MagicLocationType.Graveyard
            ));
        }
        final int count = toMill.size();
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

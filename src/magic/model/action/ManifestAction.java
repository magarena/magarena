package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class ManifestAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;

    public ManifestAction(final MagicPlayer aPlayer,final int aAmount) {
        player = aPlayer;
        amount = aAmount;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList topN = player.getLibrary().getCardsFromTop(amount);
        for (final MagicCard card : topN) {
            game.doAction(new RemoveCardAction(
                card,
                MagicLocationType.OwnersLibrary
            ));
            game.doAction(new ManifestCardAction(card, player));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {

    }
}

package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayMod;

import java.util.List;

public class MagicManifestAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;

    public MagicManifestAction(final MagicPlayer aPlayer,final int aAmount) {
        player = aPlayer;
        amount = aAmount;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList topN = player.getLibrary().getCardsFromTop(amount);
        for (final MagicCard card : topN) {
            game.doAction(new MagicRemoveCardAction(
                card,
                MagicLocationType.OwnersLibrary
            ));
            game.doAction(new MagicPlayCardAction(
                card,
                player,
                MagicPlayMod.MANIFEST
            ));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
    
    }
}

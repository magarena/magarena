package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class ReturnExiledAction extends MagicAction {

    private MagicCardList oldExiledUEOT;

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList exiledUEOT = game.getExiledUntilEndOfTurn();
        oldExiledUEOT = new MagicCardList(exiledUEOT);
        for (final MagicCard card : exiledUEOT) {
            if (card.isInExile() && card.isPermanentCard()) {
                final MagicPlayer owner = card.getOwner();
                game.doAction(new RemoveCardAction(card,MagicLocationType.Exile));
                game.doAction(new PlayCardAction(card,owner));
                game.logMessage(
                    owner,
                    "Return "+card.getName()+" to the battlefield under its owner's control (end of turn)."
                );
            }
        }
        exiledUEOT.clear();
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.getExiledUntilEndOfTurn().addAll(oldExiledUEOT);
    }
}

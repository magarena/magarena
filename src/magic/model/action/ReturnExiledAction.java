package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class ReturnExiledAction extends MagicAction {

    private MagicCardList exiledUntilEndOfTurn;

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList gameExiledUntilEndOfTurn=game.getExiledUntilEndOfTurn();
        if (!gameExiledUntilEndOfTurn.isEmpty()) {
            exiledUntilEndOfTurn=new MagicCardList(gameExiledUntilEndOfTurn);
            for (final MagicCard card : gameExiledUntilEndOfTurn) {
                final MagicPlayer owner=card.getOwner();
                if (owner.getExile().contains(card)) {
                    game.doAction(new RemoveCardAction(card,MagicLocationType.Exile));
                    game.doAction(new PlayCardAction(card,owner));
                    game.logMessage(
                            owner,
                            "Return "+card.getName()+" to the battlefield under its owner's control (end of turn).");
                }
            }
            gameExiledUntilEndOfTurn.clear();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (exiledUntilEndOfTurn!=null) {
            game.getExiledUntilEndOfTurn().addAll(exiledUntilEndOfTurn);
        }
    }
}

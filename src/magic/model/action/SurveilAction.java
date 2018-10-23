package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

/**
 * This action corresponds to "surveil 1" action.
 */
public class SurveilAction extends MagicAction {

    private final MagicPlayer player;
    private MagicCard card;

    public SurveilAction(final MagicPlayer aPlayer) {
        player = aPlayer;
    }

    @Override
    public void doAction(final MagicGame game) {
        card = player.getLibrary().removeCardAtTop();
        player.getGraveyard().addToTop(card);
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.getGraveyard().removeCardAtTop();
        player.getLibrary().addToTop(card);
    }
}

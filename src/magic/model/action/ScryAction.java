
package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class ScryAction extends MagicAction {

    private final MagicPlayer player;
    private MagicCard card;

    public ScryAction(final MagicPlayer aPlayer) {
        player = aPlayer;
    }

    @Override
    public void doAction(final MagicGame game) {
        card = player.getLibrary().removeCardAtTop();
        player.getLibrary().addToBottom(card);
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.getLibrary().removeCardAtBottom();
        player.getLibrary().addToTop(card);
    }
}

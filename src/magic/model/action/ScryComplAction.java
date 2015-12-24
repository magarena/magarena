
package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class ScryComplAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicCard card;
    private int index;
    private final boolean down;

    public ScryComplAction(final MagicPlayer aPlayer, final MagicCard aCard, final boolean aDown) {
        player = aPlayer;
        card = aCard;
        down = aDown; 
    }

    @Override
    public void doAction(final MagicGame game) {
        index = player.getLibrary().indexOf(card);
        if (index < 0) {
            throw new RuntimeException(card + " not in " + player + "'s library");
        }
        player.getLibrary().removeCard(card);
        if (down) {
            player.getLibrary().addToBottom(card);
        } else {
            player.getLibrary().addToTop(card);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (down) {
            player.getLibrary().removeCardAtBottom();
        } else {
            player.getLibrary().removeCardAtTop();
        }
        player.getLibrary().add(index, card);
    }
}

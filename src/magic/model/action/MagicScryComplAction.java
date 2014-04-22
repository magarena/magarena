
package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicScryComplAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicCard card;
    private int index;
    private final boolean down;

    public MagicScryComplAction(final MagicPlayer player, final MagicCard card, final boolean down) {
        this.player = player;
        this.card = card;
        this.down = down; 
    }

    @Override
    public void doAction(final MagicGame game) {
       this.index = player.getLibrary().indexOf(card);
       player.getLibrary().removeCard(card);
       if(down) { player.getLibrary().addToBottom(card); }
       else { player.getLibrary().addToTop(card); }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if(down) { player.getLibrary().removeCardAtBottom(); }
        else { player.getLibrary().removeCardAtTop(); }
        player.getLibrary().add(index, card);
    }
}

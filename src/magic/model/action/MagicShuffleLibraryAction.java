package magic.model.action;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicShuffleLibraryAction extends MagicAction {

    private MagicCardList oldLibrary;
    private MagicPlayer player;

    public MagicShuffleLibraryAction(final MagicPlayer aPlayer) {
        player = aPlayer;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldLibrary=new MagicCardList(player.getLibrary());
        player.getLibrary().shuffle();
        game.doAction(AIRevealAction.Hide(player.getLibrary()));
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.getLibrary().setCards(oldLibrary);
    }
}

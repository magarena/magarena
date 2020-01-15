package magic.model.action;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.mstatic.MagicPermanentStatic;

public class CleanupTurnStaticsAction extends MagicAction {

    private Collection<MagicPermanentStatic> removedStatics;

    @Override
    public void doAction(final MagicGame game) {
        removedStatics = game.removeTurnStatics();
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.addStatics(removedStatics);
    }
}

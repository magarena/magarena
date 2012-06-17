package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class MagicExileUntilEndOfTurnAction extends MagicAction {

    private final MagicPermanent permanent;
    
    public MagicExileUntilEndOfTurnAction(final MagicPermanent permanent) {
        this.permanent=permanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
        game.getExiledUntilEndOfTurn().add(permanent.getCard());
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.getExiledUntilEndOfTurn().remove(permanent.getCard());
    }
}

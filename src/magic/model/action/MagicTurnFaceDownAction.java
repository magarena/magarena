package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicTurnFaceDownAction extends MagicAction {

    public final MagicPermanent permanent;

    public MagicTurnFaceDownAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (!permanent.isFaceDown()) {
            game.doAction(MagicChangeStateAction.Set(permanent, MagicPermanentState.FaceDown));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}

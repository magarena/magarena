package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicFlipAction extends MagicAction {

    public final MagicPermanent permanent;

    public MagicFlipAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (!permanent.isFlipped()) {
            game.doAction(MagicChangeStateAction.Set(permanent, MagicPermanentState.Flipped));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}

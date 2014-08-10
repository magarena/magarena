package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

public class MagicTransformAction extends MagicAction {

    public final MagicPermanent permanent;

    public MagicTransformAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent.isDoubleFaced()) {
            if (permanent.isTransformed()) {
                game.doAction(MagicChangeStateAction.Clear(permanent, MagicPermanentState.Transformed));
            } else {
                game.doAction(MagicChangeStateAction.Set(permanent, MagicPermanentState.Transformed));
            }
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}

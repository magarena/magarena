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
        if (!permanent.isTransformed() && permanent.isDoubleFaced()) {
            game.doAction(MagicChangeStateAction.Set(permanent, MagicPermanentState.Transformed));
        } else if (permanent.isTransformed() && permanent.isDoubleFaced()){
            game.doAction(MagicChangeStateAction.Clear(permanent, MagicPermanentState.Transformed));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}

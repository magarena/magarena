package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.mstatic.MagicStatic;

import java.util.Collections;
import java.util.Collection;

public class MagicTransformAction extends MagicAction {

    public final MagicPermanent permanent;
    private Collection<MagicStatic> oldStatics = Collections.emptyList();
    private Collection<MagicStatic> newStatics = Collections.emptyList();

    public MagicTransformAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent.isDoubleFaced()) {
            oldStatics = permanent.getStatics();
            
            if (permanent.isTransformed()) {
                game.doAction(ChangeStateAction.Clear(permanent, MagicPermanentState.Transformed));
            } else {
                game.doAction(ChangeStateAction.Set(permanent, MagicPermanentState.Transformed));
            }
            
            newStatics = permanent.getStatics();
            game.removeStatics(permanent, oldStatics);
            game.addStatics(permanent, newStatics);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.removeStatics(permanent, newStatics);
        game.addStatics(permanent, oldStatics);
    }
}

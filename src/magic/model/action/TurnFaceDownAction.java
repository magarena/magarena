package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.mstatic.MagicStatic;

import java.util.Collections;
import java.util.Collection;

public class TurnFaceDownAction extends MagicAction {

    public final MagicPermanent permanent;
    private Collection<MagicStatic> oldStatics = Collections.emptyList();
    private Collection<MagicStatic> newStatics = Collections.emptyList();

    public TurnFaceDownAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (!permanent.isFaceDown() && !permanent.isDoubleFaced()) {
            oldStatics = permanent.getStatics();

            game.doAction(ChangeStateAction.Set(permanent, MagicPermanentState.FaceDown));

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

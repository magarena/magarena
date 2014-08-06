package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.mstatic.MagicStatic;

import java.util.Collections;
import java.util.Collection;

public class MagicFlipAction extends MagicAction {

    public final MagicPermanent permanent;
    private Collection<MagicStatic> oldStatics = Collections.emptyList();
    private Collection<MagicStatic> newStatics = Collections.emptyList();
    private boolean toFlip;

    public MagicFlipAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        toFlip = permanent.isFlipped() == false;
        if (toFlip) {
            oldStatics = permanent.getStatics();
            
            game.doAction(MagicChangeStateAction.Set(permanent, MagicPermanentState.Flipped));

            newStatics = permanent.getStatics();

            game.removeStatics(permanent, oldStatics);
            game.addStatics(permanent, newStatics);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (toFlip) {
            game.removeStatics(permanent, newStatics);
            game.addStatics(permanent, oldStatics);
        }
    }
}

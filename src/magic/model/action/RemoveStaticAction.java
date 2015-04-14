package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicStatic;

public class RemoveStaticAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicStatic mstatic;

    public RemoveStaticAction(final MagicPermanent aPermanent, final MagicStatic aStatic) {
        permanent = aPermanent;
        mstatic = aStatic;
    }

    public RemoveStaticAction(final MagicStatic aStatic) {
        this(MagicPermanent.NONE, aStatic);
    }

    @Override
    public void doAction(final MagicGame game) {
        game.removeStatic(permanent, mstatic);
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.addStatic(permanent, mstatic);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+permanent+','+mstatic+')';
    }
}

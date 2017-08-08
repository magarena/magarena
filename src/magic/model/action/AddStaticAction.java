package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicStatic;

public class AddStaticAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicStatic mstatic;
    private final boolean force;
    private boolean done;

    private AddStaticAction(final MagicPermanent aPermanent, final MagicStatic aStatic, final boolean aForce) {
        permanent = aPermanent;
        mstatic = aStatic;
        force = aForce;
    }

    public AddStaticAction(final MagicPermanent aPermanent, final MagicStatic aStatic) {
        this(aPermanent, aStatic, false);
    }

    public AddStaticAction(final MagicStatic aStatic) {
        this(MagicPermanent.NONE, aStatic);
    }

    public static AddStaticAction Force(final MagicPermanent aPermanent,final MagicStatic aStatic) {
        return new AddStaticAction(aPermanent, aStatic, true);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent == MagicPermanent.NONE || permanent.isValid() || force) {
            done = true;
            game.addStatic(permanent, mstatic);
            game.setStateCheckRequired();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (done) {
            game.removeStatic(permanent, mstatic);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+permanent+','+mstatic+')';
    }
}

package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.mstatic.MagicStatic;

import java.util.Collections;
import java.util.Collection;

public class FlipAction extends MagicAction {

    public final MagicPermanent permanent;
    private Collection<MagicStatic> oldStatics = Collections.emptyList();
    private Collection<MagicStatic> newStatics = Collections.emptyList();

    public FlipAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent.isFlipCard() && !permanent.isFlipped()) {
            oldStatics = permanent.getStatics();

            game.doAction(ChangeStateAction.Set(permanent, MagicPermanentState.Flipped));

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

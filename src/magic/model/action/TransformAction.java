package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicTriggerType;

import java.util.Collections;
import java.util.Collection;

public class TransformAction extends MagicAction {

    public final MagicPermanent permanent;
    private Collection<MagicStatic> oldStatics = Collections.emptyList();
    private Collection<MagicStatic> newStatics = Collections.emptyList();

    public TransformAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent.isValid() && permanent.isDoubleFaced()) {
            oldStatics = permanent.getStatics();

            final ChangeStateAction act = permanent.isTransformed() ?
                ChangeStateAction.Clear(permanent, MagicPermanentState.Transformed) :
                ChangeStateAction.Set(permanent, MagicPermanentState.Transformed);

            game.doAction(act);

            // update static abilities
            newStatics = permanent.getStatics();
            game.removeStatics(permanent, oldStatics);
            game.addStatics(permanent, newStatics);

            // update and execute transform triggers
            game.update();
            game.executeTrigger(MagicTriggerType.WhenBecomesState, act);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.removeStatics(permanent, newStatics);
        game.addStatics(permanent, oldStatics);
    }
}

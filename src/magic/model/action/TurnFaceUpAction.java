package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicTriggerType;

import java.util.Collections;
import java.util.Collection;

public class TurnFaceUpAction extends MagicAction {

    public final MagicPermanent permanent;
    private Collection<MagicStatic> oldStatics = Collections.emptyList();
    private Collection<MagicStatic> newStatics = Collections.emptyList();

    public TurnFaceUpAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent.isFaceDown() && permanent.getRealCardDefinition().isPermanent()) {
            oldStatics = permanent.getStatics();

            game.doAction(ChangeStateAction.Clear(permanent, MagicPermanentState.FaceDown));
            game.doAction(ChangeStateAction.Clear(permanent, MagicPermanentState.Manifest));

            newStatics = permanent.getStatics();
            game.removeStatics(permanent, oldStatics);
            game.addStatics(permanent, newStatics);

            // force an update so that triggers are registered
            game.update();

            game.executeTrigger(MagicTriggerType.WhenTurnedFaceUp, permanent);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.removeStatics(permanent, newStatics);
        game.addStatics(permanent, oldStatics);
    }
}

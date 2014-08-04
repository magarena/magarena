package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

public class MagicTurnFaceUpAction extends MagicAction {

    public final MagicPermanent permanent;

    public MagicTurnFaceUpAction(final MagicPermanent aPermanent) {
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (permanent.isFaceDown()) {
            game.doAction(MagicChangeStateAction.Clear(permanent, MagicPermanentState.FaceDown));

            // force an update so that triggers are registered
            game.update();

            game.executeTrigger(MagicTriggerType.WhenTurnedFaceUp, permanent);
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}

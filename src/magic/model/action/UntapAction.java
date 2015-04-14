package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

// Must check if creature is tapped.
public class UntapAction extends MagicAction {

    private final MagicPermanent permanent;
    private boolean isTapped;

    public UntapAction(final MagicPermanent permanent) {
        this.permanent=permanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        isTapped=permanent.hasState(MagicPermanentState.Tapped);
        if (isTapped) {
            permanent.clearState(MagicPermanentState.Tapped);
            setScore(permanent.getController(),-ArtificialScoringSystem.getTappedScore(permanent));
            game.executeTrigger(MagicTriggerType.WhenBecomesUntapped,permanent);
            game.setStateCheckRequired();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (isTapped) {
            permanent.setState(MagicPermanentState.Tapped);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+permanent.getName()+')';
    }
}

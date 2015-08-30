package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

// Must check if creature is untapped.
public class TapAction extends MagicAction {

    private final MagicPermanent permanent;
    private boolean isUntapped;
    private final boolean hasScore;
    
    public TapAction(final MagicPermanent permanent) {
        this(permanent, true);
    }

    public static TapAction Enters(final MagicPermanent permanent) {
        return new TapAction(permanent, false);
    }

    private TapAction(final MagicPermanent aPermanent,final boolean aHasScore) {
        permanent = aPermanent;
        hasScore = aHasScore;
    }

    @Override
    public void doAction(final MagicGame game) {
        isUntapped=!permanent.hasState(MagicPermanentState.Tapped);
        if (isUntapped) {
            permanent.setState(MagicPermanentState.Tapped);
            if (hasScore) {
                setScore(permanent.getController(),ArtificialScoringSystem.getTappedScore(permanent));
                game.executeTrigger(MagicTriggerType.WhenBecomesTapped,permanent);
            }
            game.setStateCheckRequired();
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (isUntapped) {
            permanent.clearState(MagicPermanentState.Tapped);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" ("+permanent.getName()+')';
    }
}

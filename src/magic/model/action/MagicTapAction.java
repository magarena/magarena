package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicTriggerType;

// Must check if creature is untapped.
public class MagicTapAction extends MagicAction {

	private final MagicPermanent permanent;
	private boolean isUntapped;
	private final boolean hasScore;
	
	public MagicTapAction(final MagicPermanent permanent,final boolean hasScore) {
		this.permanent=permanent;
		this.hasScore=hasScore;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		isUntapped=!permanent.hasState(MagicPermanentState.Tapped);
		if (isUntapped) {
			permanent.setState(MagicPermanentState.Tapped);
			if (hasScore) {
				setScore(permanent.getController(),ArtificialScoringSystem.getTappedScore(permanent,game));
			}
			game.executeTrigger(MagicTriggerType.WhenBecomesTapped,permanent);
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

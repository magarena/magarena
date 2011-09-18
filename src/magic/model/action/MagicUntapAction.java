package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;

// Must check if creature is tapped.
public class MagicUntapAction extends MagicAction {

	private final MagicPermanent permanent;
	private boolean untap;
	
	public MagicUntapAction(final MagicPermanent permanent) {
		this.permanent=permanent;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		untap=permanent.hasState(MagicPermanentState.Tapped);
		if (untap) {
			permanent.clearState(MagicPermanentState.Tapped);
			setScore(permanent.getController(),-ArtificialScoringSystem.getTappedScore(permanent,game));
			game.setStateCheckRequired();
		}
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (untap) {
			permanent.setState(MagicPermanentState.Tapped);
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+" ("+permanent.getName()+')';
	}
}

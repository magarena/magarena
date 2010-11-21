package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public abstract class MagicAction {
	
	private MagicPlayer scorePlayer=null;
	private int score=0;
	
	protected final void setScore(final MagicPlayer scorePlayer,final int score) {
		
		this.scorePlayer=scorePlayer;
		this.score=score;
	}
	
	public final int getScore(final MagicPlayer player) {
		
		if (player==scorePlayer) {
			return score;
		} else {
			return -score;
		}
	}
	
	public abstract void doAction(final MagicGame game);
	
	public abstract void undoAction(final MagicGame game);
	
	@Override
	public String toString() {
		
		return "";
	}
}
package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;

public class MagicChangePlayerStateAction extends MagicAction {

	private final MagicPlayer player;
	private final MagicPlayerState state;
	private final boolean set;
	private boolean changed;

	public MagicChangePlayerStateAction(final MagicPlayer player,final MagicPlayerState state,final boolean set) {
		
		this.player=player;
		this.state=state;
		this.set=set;
	}
	
	@Override
	public void doAction(final MagicGame game) {

		changed=player.hasState(state)!=set;
		if (changed) {
			if (set) {
				player.setState(state);
			} else {
				player.clearState(state);
			}
		}
	}

	@Override
	public void undoAction(final MagicGame game) {

		if (changed) {
			if (set) {
				player.clearState(state);				
			} else {
				player.setState(state);
			}
		}
	}	
}
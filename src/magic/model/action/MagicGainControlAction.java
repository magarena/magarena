package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;

public class MagicGainControlAction extends MagicAction {

	private final MagicPlayer player;
	private final MagicPermanent permanent;
	private MagicPlayer oldController;
	
	public MagicGainControlAction(final MagicPlayer player,final MagicPermanent permanent) {
		
		this.player=player;
		this.permanent=permanent;
	}
	
	@Override
	public void doAction(final MagicGame game) {

		oldController=permanent.getController();
		if (player!=oldController) {
			int score=permanent.getScore(game);
			oldController.removePermanent(permanent);
			permanent.setController(player);
			player.addPermanent(permanent);
			permanent.setState(MagicPermanentState.Summoned);
			score+=permanent.getScore(game);
			setScore(player,score);
			game.setStateCheckRequired();
		}
	}

	@Override
	public void undoAction(final MagicGame game) {

		if (player!=oldController) {
			player.removePermanent(permanent);
			permanent.setController(oldController);
			oldController.addPermanent(permanent);
			permanent.clearState(MagicPermanentState.Summoned);
		}
	}
}
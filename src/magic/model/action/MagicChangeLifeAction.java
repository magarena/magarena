package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

/** Keeping the player life is done in the marker action. */
public class MagicChangeLifeAction extends MagicAction {
	
	private final MagicPlayer player;
	private final int life;
	
	public MagicChangeLifeAction(final MagicPlayer player,final int life) {
		this.player=player;
		this.life=life;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		final int oldLife=player.getLife();
		final int newLife=oldLife+life;
		player.setLife(newLife);
		setScore(player,ArtificialScoringSystem.getLifeScore(newLife)-ArtificialScoringSystem.getLifeScore(oldLife));
		if (newLife > oldLife) {
			game.executeTrigger(MagicTriggerType.WhenLifeIsGained,game.getTurnPlayer());
		}
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {

	}
	
	@Override
	public String toString() {

		return getClass().getSimpleName()+" ("+player.getName()+','+life+')';
	}
}

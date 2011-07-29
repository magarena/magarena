package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicLoseGameAction extends MagicAction {

	public static final String LIFE_REASON="{L} You lose the game.";
	public static final String POISON_REASON="{L} You are poisoned. You lose the game.";
	public static final String DRAW_REASON="{L} You attempt to draw from an empty library. You lose the game.";
	
	private final MagicPlayer player;
	private final String reason;
	private MagicPlayer oldLosingPlayer;
	
	public MagicLoseGameAction(final MagicPlayer player,final String reason) {
		this.player=player;
		this.reason=reason;
	}

	@Override
	public void doAction(final MagicGame game) {
		oldLosingPlayer=game.getLosingPlayer();
		if (oldLosingPlayer==null&&player.canLose()) {
			setScore(player,ArtificialScoringSystem.getLoseGameScore(game));
			game.setLosingPlayer(player);
			game.logMessage(player,reason);
		}
	}

	@Override
	public void undoAction(final MagicGame game) {
		game.setLosingPlayer(oldLosingPlayer);
	}
}

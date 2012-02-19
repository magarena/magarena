package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

public class MagicLoseGameAction extends MagicAction {

	public static final String LIFE_REASON = " lost the game.";
	public static final String POISON_REASON = " lost the game because of poisoning.";
	static final String DRAW_REASON = " lost the game because of an attempt to draw from an empty library.";
	
	private final MagicPlayer player;
	private final String reason;
	private MagicPlayer oldLosingPlayer = MagicPlayer.NONE;
	
	public MagicLoseGameAction(final MagicPlayer player,final String reason) {
		this.player=player;
		this.reason=reason;
	}

	@Override
	public void doAction(final MagicGame game) {
		oldLosingPlayer=game.getLosingPlayer();
		if (!oldLosingPlayer.isValid()) {
            MagicPlayer[] playerRef = new MagicPlayer[]{player};
            game.executeTrigger(MagicTriggerType.IfPlayerWouldLose, playerRef);
            if (playerRef[0].isValid()) {
                setScore(player,ArtificialScoringSystem.getLoseGameScore(game));
                game.setLosingPlayer(player);
                game.logMessage(player,"{L} " + player + reason);
            }
		}
	}

	@Override
	public void undoAction(final MagicGame game) {
		game.setLosingPlayer(oldLosingPlayer);
	}
}

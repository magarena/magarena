package magic.model.score;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicCombatDamageAction;
import magic.model.action.MagicDeclareBlockersAction;
import magic.model.choice.MagicDeclareBlockersResult;

public class MagicGameCombatScore implements MagicCombatScore {

	private final MagicGame game;
	private final MagicPlayer attackingPlayer;
	private final MagicPlayer defendingPlayer;
	
	public MagicGameCombatScore(final MagicGame game,final MagicPlayer attackingPlayer,final MagicPlayer defendingPlayer) {
		
		this.game=game;
		this.attackingPlayer=attackingPlayer;
		this.defendingPlayer=defendingPlayer;
	}

	@Override
	public int getScore(final MagicDeclareBlockersResult result) {

		game.startActions();
		game.doAction(new MagicDeclareBlockersAction(result));
		game.doAction(new MagicCombatDamageAction(attackingPlayer,defendingPlayer));
		// Give extra points for extra blocked creatures.
		final int score=game.getScore()+result.size();
		game.undoActions();
		return score;
	}
}
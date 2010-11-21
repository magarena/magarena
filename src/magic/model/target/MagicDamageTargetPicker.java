package magic.model.target;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

/** Creature permanent or player. Can be your own creatures. */
public class MagicDamageTargetPicker extends MagicTargetPicker {

	private final int amount;
	private final boolean noRegeration;
	
	public MagicDamageTargetPicker(final int amount,final boolean noRegeneration) {
		
		this.amount=amount;
		this.noRegeration=noRegeneration;
	}
	
	public MagicDamageTargetPicker(final int amount) {
		
		this(amount,false);
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		// Player
		if (target instanceof MagicPlayer) {
			final MagicPlayer targetPlayer=(MagicPlayer)target;
			final int actualAmount=amount-targetPlayer.getPreventDamage();
			if (actualAmount<=0) {
				return 0;
			}
			final int life=targetPlayer.getLife();
			final int score;
			if (life>actualAmount) {
				score=ArtificialScoringSystem.getLifeScore(life)-ArtificialScoringSystem.getLifeScore(life-actualAmount);
			} else {
				score=ArtificialScoringSystem.WIN_GAME_SCORE;
			}
			return targetPlayer==player?-score:score;
		}		

		// Permanent
		final MagicPermanent permanent=(MagicPermanent)target;
		if (permanent.hasAbility(game,MagicAbility.Indestructible)) {
			return 0;
		}		
		if (permanent.isRegenerated()&&!noRegeration) {
			return 0;
		}		
		final int actualAmount=amount-permanent.getPreventDamage();
		if (actualAmount<=0) {
			return 0;
		}
		final int leftToughness=permanent.getToughness(game)-permanent.getDamage()-actualAmount;
		final int score=leftToughness<=0?permanent.getScore(game):20-leftToughness;
		return permanent.getController()==player?-score:score;
	}
}
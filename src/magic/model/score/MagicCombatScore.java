package magic.model.score;

import magic.model.choice.MagicDeclareBlockersResult;

public interface MagicCombatScore {

	public int getScore(final MagicDeclareBlockersResult result);
}
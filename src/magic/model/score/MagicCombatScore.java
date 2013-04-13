package magic.model.score;

import magic.model.choice.MagicDeclareBlockersResult;

public interface MagicCombatScore {

    int getScore(final MagicDeclareBlockersResult result);
}

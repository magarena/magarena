package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public interface MagicAI {
    int MAX_LEVEL = 8;
    Object[] findNextEventChoiceResults(final MagicGame game, final MagicPlayer player); 
}

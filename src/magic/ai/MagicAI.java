package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public interface MagicAI {
    Object[] findNextEventChoiceResults(MagicGame game, MagicPlayer player); 
}

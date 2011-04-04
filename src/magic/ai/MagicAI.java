package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public interface MagicAI {
	
    public Object[] findNextEventChoiceResults(final MagicGame game, final MagicPlayer player); 
}
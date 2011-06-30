package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public interface MagicAI {
    public static final int MAX_LEVEL = 8;
    public Object[] findNextEventChoiceResults(final MagicGame game, final MagicPlayer player); 
}

package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public abstract class MagicAI {

    // default number of threads to use when running simulated games.
    private static int maxThreads = Runtime.getRuntime().availableProcessors();

    int MAX_LEVEL = 8;
    abstract public Object[] findNextEventChoiceResults(final MagicGame game, final MagicPlayer player);

    public static void setMaxThreads(int i) {
        maxThreads = Math.min(i, Runtime.getRuntime().availableProcessors());
    }

    public static int getMaxThreads() {
        return maxThreads;
    }

}

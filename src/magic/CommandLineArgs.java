package magic;

import java.util.Locale;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.utility.MagicSystem;

class CommandLineArgs {

    private MagicAIImpl ai1 = MagicAIImpl.MMABFast;
    private MagicAIImpl ai2 = MagicAIImpl.MMABFast;
    private int ai1Level = 8;
    private int ai2Level = 8;
    private boolean isAnimationsEnabled = true;
    private int maxAiThreads = MagicAI.getMaxThreads();
    private boolean isDevMode = false;

    CommandLineArgs(final String[] args) {

        for (int i = 0; i < args.length; i++) {

            final String arg = args[i];
            switch (arg.toLowerCase(Locale.ENGLISH)) {

            case "--ai1": // MagicAIImpl class to load as player 1. (eg. --ai1 MMABFast)
                setAi1(MagicAIImpl.valueOf(args[i + 1].trim()));
                break;

            case "--ai2": // MagicAIImpl class to load as player 2. (eg. --ai2 MMABFast)
                setAi2(MagicAIImpl.valueOf(args[i + 1].trim()));
                break;

            case "--str1": // level of AI player #1. (eg. --str1 8)
                setAi1Level(Integer.parseInt(args[i + 1].trim()));
                break;

            case "--str2": // level of AI player #2. (eg. --str2 8)
                setAi2Level(Integer.parseInt(args[i + 1].trim()));
                break;

            case "--nofx": // turns off gameplay animations for session (does not change preferences).
                isAnimationsEnabled = false;
                break;

            case "--threads": // limits the number of threads AI uses. (eg. --threads 2)
                maxAiThreads = Integer.parseInt(args[i + 1].trim());
                break;

            case "--devmode": // enables access to additional functionality.
                isDevMode = true;
                break;

            }
        }
    }

    private void setAi1(MagicAIImpl ai1) {
        this.ai1 = ai1;
        MagicSystem.setAiVersusAi(true);
    }

    MagicAIImpl getAi1() {
        return ai1;
    }

    private void setAi2(MagicAIImpl ai2) {
        this.ai2 = ai2;
        MagicSystem.setAiVersusAi(true);
    }

    MagicAIImpl getAi2() {
        return ai2;
    }

    private void setAi1Level(int ai1Level) {
        this.ai1Level = ai1Level;
    }

    int getAi1Level() {
        return ai1Level;
    }

    private void setAi2Level(int ai2Level) {
        this.ai2Level = ai2Level;
    }

    int getAi2Level() {
        return ai2Level;
    }

    boolean isAnimationsEnabled() {
        return isAnimationsEnabled;
    }

    int getMaxThreads() {
        return maxAiThreads;
    }

    boolean isDevMode() {
        return isDevMode;
    }

}

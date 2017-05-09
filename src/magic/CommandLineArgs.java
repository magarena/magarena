package magic;

import java.util.Locale;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.GeneralConfig;
import magic.ui.widget.duel.animation.MagicAnimations;
import magic.utility.MagicSystem;

class CommandLineArgs {

    private MagicAIImpl ai1 = MagicAIImpl.MMABFast;
    private MagicAIImpl ai2 = MagicAIImpl.MMABFast;
    private int ai1Level = 8;
    private int ai2Level = 8;

    CommandLineArgs(final String[] args) {

        // default setting shown in braces if applicable.
        for (int i = 0; i < args.length; i++) {

            final String arg = args[i];
            switch (arg.toLowerCase(Locale.ENGLISH)) {

            case "disablelogviewer":
                GeneralConfig.getInstance().setLogMessagesVisible(false);
                break;

            case "--ai1": // MagicAIImpl class to load as player 1. [--ai1 MMABFast]
                setAi1(MagicAIImpl.valueOf(args[i + 1].trim()));
                break;

            case "--ai2": // MagicAIImpl class to load as player 2. [--ai2 MMABFast]
                setAi2(MagicAIImpl.valueOf(args[i + 1].trim()));
                break;

            case "--str1": // level of AI player #1 [--str1 8].
                setAi1Level(Integer.parseInt(args[i + 1].trim()));
                break;

            case "--str2": // level of AI player #2 [--str2 8].
                setAi2Level(Integer.parseInt(args[i + 1].trim()));
                break;

            case "--nofx": // turns off gameplay animations for session (does not change preferences).
                MagicAnimations.setEnabled(false);
                break;

            case "--threads": // limits the number of threads AI uses when running simulated games.
                MagicAI.setMaxThreads(Integer.parseInt(args[i + 1].trim()));
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

}

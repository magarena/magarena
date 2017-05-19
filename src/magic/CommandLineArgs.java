package magic;

import java.util.Locale;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.DuelConfig;
import magic.utility.MagicSystem;

class CommandLineArgs {

    private MagicAIImpl ai1 = MagicAIImpl.MMABFast;
    private MagicAIImpl ai2 = MagicAIImpl.MMABFast;
    private int ai1Level = 8;
    private int ai2Level = 8;
    private boolean isAnimationsEnabled = true;
    private int maxAiThreads = MagicAI.getMaxThreads();
    private boolean isDevMode = MagicSystem.isDevMode();
    private int games = DuelConfig.DEFAULT_GAMES;
    private int startLife = DuelConfig.DEFAULT_LIFE;
    private boolean showHelp = false;
    private String deck1 = "";
    private String deck2 = "";
    private boolean showFps = false;
    private int fps;

    CommandLineArgs(final String[] args) {

        for (int i = 0; i < args.length; i++) {

            final String arg = args[i];
            switch (arg.toLowerCase(Locale.ENGLISH)) {

            //
            // setting any of the following arguments will
            // automatically run an AI vs AI game.
            //
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

            case "--games": // "best of..." games total in AI v AI game. (eg. --games 3 = best of 3 games)
                setGames(Integer.parseInt(args[i + 1].trim()));
                break;

            case "--life": // initial life for each AI player. (eg. --life 10)
                setStartLife(Integer.parseInt(args[i + 1].trim()));
                break;

            case "--deck1": // set player 1 deck (see wiki page for value syntax)
                setDeck1(args[i + 1].trim());
                break;

            case "--deck2": // set player 2 deck (see wiki page for value syntax)
                setDeck2(args[i + 1].trim());
                break;

            //
            // other settings.
            //
            case "--fps": // sets the desired frames-per-second for the Trident animation library.
                fps = Integer.parseInt(args[i + 1].trim());
                break;

            case "--showfps": // displays refresh rate in hertz for each screen device.
                showFps = true;
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

            case "--test": // Runs specified test game, equivalent to -DtestGame VM arg. (eg. --test TestAura)
                System.setProperty("testGame", args[i + 1].trim());
                break;

            case "--help": // shows wiki help page in browser.
                showHelp = true;
                break;

            }
        }
    }

    private void setDeck2(String deck) {
        this.deck2 = deck;
        MagicSystem.setAiVersusAi(true);
    }

    private void setDeck1(String deck) {
        this.deck1 = deck;
        MagicSystem.setAiVersusAi(true);
    }

    private void setStartLife(int life) {
        this.startLife = life;
        MagicSystem.setAiVersusAi(true);
    }

    private void setGames(int games) {
        this.games = games;
        MagicSystem.setAiVersusAi(true);
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
        MagicSystem.setAiVersusAi(true);
    }

    int getAi1Level() {
        return ai1Level;
    }

    private void setAi2Level(int ai2Level) {
        this.ai2Level = ai2Level;
        MagicSystem.setAiVersusAi(true);
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

    int getGames() {
        return games;
    }

    int getLife() {
        return startLife;
    }

    boolean showHelp() {
        return showHelp;
    }

    String getDeck1() {
        return deck1;
    }

    String getDeck2() {
        return deck2;
    }

    boolean showFPS() {
        return showFps;
    }

    int getFPS() {
        return fps;
    }

}

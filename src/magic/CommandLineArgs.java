package magic;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.DuelConfig;
import magic.utility.MagicSystem;

class CommandLineArgs {
    private static Map<String, Arg<?>> argMap = new HashMap<>();
    private static Map<String, Flag> flagMap = new HashMap<>();

    // set player 1 deck (see wiki page for value syntax)
    private Arg<String> deck1 = new Arg<>("--deck1", "", x -> {
        MagicSystem.setAiVersusAi(true);
        return x;
    });

    // set player 2 deck (see wiki page for value syntax)
    private Arg<String> deck2 = new Arg<>("--deck2", "", x -> {
        MagicSystem.setAiVersusAi(true);
        return x;
    });

    // HeadlessAiGame-only settings
    private Flag headless = new Flag("--headless");
    private Flag cardTest = new Flag("--cardtest");

    // displays refresh rate in hertz for each screen device.
    private Flag showFps = new Flag("--showfps");

    // turns off gameplay animations for session (does not change preferences).
    private Flag isAnimationsDisabled = new Flag("--nofx");

    // enables access to additional functionality.
    private Flag isDevMode = new Flag("--devmode");

    // shows wiki help page in browser.
    private Flag showHelp = new Flag("--help");

    // sets the desired frames-per-second for the Trident animation library.
    private Arg<Integer> fps = new Arg<>("--fps", 0, x -> Integer.parseInt(x));

    // limits the number of threads AI uses. (eg. --threads 2)
    private Arg<Integer> maxAiThreads = new Arg<>("--threads", MagicAI.getMaxThreads(), x -> Integer.parseInt(x));

    // Runs specified test game, equivalent to -DtestGame VM arg. (eg. --test TestAura)
    private Arg<String> testGame = new Arg<>("--test", "", x -> {
        System.setProperty("testGame", x);
        return x;
    });

    // Setting any of the following arguments will automatically run an AI vs AI game.

    // MagicAIImpl class to load as player 1. (eg. --ai1 MMABFast)
    private Arg<MagicAIImpl> ai1 = new Arg<>("--ai1", MagicAIImpl.MMABFast, x -> {
        MagicSystem.setAiVersusAi(true);
        return MagicAIImpl.valueOf(x);
    });
    // MagicAIImpl class to load as player 2. (eg. --ai2 MMABFast)
    private Arg<MagicAIImpl> ai2 = new Arg<>("--ai2", MagicAIImpl.MMABFast, x -> {
        MagicSystem.setAiVersusAi(true);
        return MagicAIImpl.valueOf(x);
    });
    // level of AI player #1. (eg. --str1 8)
    private Arg<Integer> ai1Level = new Arg<>("--str1", 6, x -> {
        MagicSystem.setAiVersusAi(true);
        return Integer.parseInt(x);
    });
    // level of AI player #2. (eg. --str2 8)
    private Arg<Integer> ai2Level = new Arg<>("--str2", 6, x -> {
        MagicSystem.setAiVersusAi(true);
        return Integer.parseInt(x);
    });
    // "best of..." games total in AI v AI game. (eg. --games 3 = best of 3 games)
    private Arg<Integer> games = new Arg<>("--games", DuelConfig.DEFAULT_GAMES, x -> {
        MagicSystem.setAiVersusAi(true);
        return Integer.parseInt(x);
    });
    // initial life for each AI player. (eg. --life 10)
    private Arg<Integer> startLife = new Arg<>("--life", DuelConfig.DEFAULT_LIFE, x -> {
        MagicSystem.setAiVersusAi(true);
        return Integer.parseInt(x);
    });

    // the number of duels to play [--duels 1].
    private Arg<Integer> duels = new Arg<>("--duels", 1, x -> Integer.parseInt(x));

    // Skiplist for card test
    private Arg<String> skipList = new Arg<>("--skip", null, x -> x);

    /**
     * Commandline argument requiring a value
     *
     * @param <V> type of internally stored value
     */
    private static class Arg<V> {
        private String argName;
        public V value;
        private Function<String, V> setter;

        public Arg(String argName, V defaultValue, Function<String, V> setter) {
            this.argName = argName;
            this.value = defaultValue;
            this.setter = setter;
            argMap.put(argName, this);
        }

        public void set(String arg) {
            value = setter.apply(arg);
        }
    }

    /**
     * Commandline argument that is a flag that enables something.
     */
    private static class Flag {
        public boolean value = false;

        public Flag(String argName) {
            flagMap.put(argName, this);
        }
    }

    CommandLineArgs(final String[] args) {
        isDevMode.value = MagicSystem.isDevMode();
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i].toLowerCase(Locale.ENGLISH);
            Arg<?> argValue = argMap.get(arg);
            if (argValue != null) {
                String arg0 = nextArg(args, i);
                if (arg0 == null) {
                    throw new IllegalArgumentException("Argument " + argValue.argName + " requires a value");
                } else {
                    argValue.set(arg0);
                }
                i++;
            }
            Flag flagValue = flagMap.get(arg);
            if (flagValue != null) {
                flagValue.value = true;
            }
        }
    }

    private String nextArg(String[] args, int i) {
        if (i + 1 >= args.length) {
            return null;
        }
        return args[i + 1].trim();
    }


    MagicAIImpl getAi1() {
        return ai1.value;
    }


    MagicAIImpl getAi2() {
        return ai2.value;
    }

    int getAi1Level() {
        return ai1Level.value;
    }

    int getAi2Level() {
        return ai2Level.value;
    }

    boolean isAnimationsEnabled() {
        return !isAnimationsDisabled.value;
    }

    int getMaxThreads() {
        return maxAiThreads.value;
    }

    boolean isDevMode() {
        return isDevMode.value;
    }

    int getGames() {
        return games.value;
    }

    int getLife() {
        return startLife.value;
    }

    boolean showHelp() {
        return showHelp.value;
    }

    String getDeck1() {
        return deck1.value;
    }

    String getDeck2() {
        return deck2.value;
    }

    boolean showFPS() {
        return showFps.value;
    }

    boolean isHeadless() {
        return headless.value;
    }

    boolean isCardTest() {
        return cardTest.value;
    }

    int getFPS() {
        return fps.value;
    }

    int getDuels() {
        return duels.value;
    }

    String getSkipList() {
        return skipList.value;
    }
}

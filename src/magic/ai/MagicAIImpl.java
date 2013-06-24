package magic.ai;

public enum MagicAIImpl {
    MMAB("minimax", new MMAB(false)),
    MMABC("minimax (cheat)", new MMAB(true)),
    MCTS("monte carlo tree search", new MCTSAI(false)),
    MCTSC("monte carlo tree search (cheat)", new MCTSAI(true)),
    VEGAS("vegas", new VegasAI(false)),
    VEGASC("vegas (cheat)", new VegasAI(true)),
    RND("random", new RandomAI()),
    MMABFast("minimax (deck strength)", magic.ai.MMAB.DeckStrAI()),

    MCTS2("monte carlo tree search", new MCTSAI2(false)),
    MCTSC2("monte carlo tree search (cheat)", new MCTSAI2(true)),
    ;

    private static final MagicAIImpl[] SUPPORTED_AIS = {MMAB, MMABC, MCTS, MCTSC, VEGAS, VEGASC};

    private final String name;
    private final MagicAI ai;

    private MagicAIImpl(final String name, final MagicAI ai) {
        this.name=name;
        this.ai=ai;
    }

    private String getName() {
        return name;
    }

    public MagicAI getAI() {
        return ai;
    }

    public static MagicAIImpl getAI(final String name) {
        for (final MagicAIImpl ai : values()) {
            if (ai.getName().equals(name)) {
                return ai;
            }
        }
        return MMAB;
    }

    public static String[] getNames() {
        final String[] names=new String[SUPPORTED_AIS.length];
        int index=0;
        for (final MagicAIImpl ai : SUPPORTED_AIS) {
            names[index++]=ai.getName();
        }
        return names;
    }
}

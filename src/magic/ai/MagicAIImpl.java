package magic.ai;

public enum MagicAIImpl {
    MMAB("minimax", new MMAB(false)),
    MMABC("minimax (cheat)", new MMAB(true)),
    MCTS("monte carlo tree search", new MCTSAI(false)),
    MCTSC("monte carlo tree search (cheat)", new MCTSAI(true)),
    VEGAS("vegas", new VegasAI(false)),
    VEGASC("vegas (cheat)", new VegasAI(true)),
    MTDF("mtd(f)", new MTDF(false)),
    MTDFC("mtd(f) (cheat)", new MTDF(true)),

    MMABFast("minimax (deck strength)", magic.ai.MMAB.DeckStrAI()),
    ;

    public static final MagicAIImpl[] SUPPORTED_AIS = {MMAB, MMABC, MCTS, MCTSC, VEGAS, VEGASC};
    public static final MagicAIImpl[] DECKSTR_AIS = {MMABFast, MMABFast};

    private final String name;
    private final MagicAI ai;

    private MagicAIImpl(final String name, final MagicAI ai) {
        this.name=name;
        this.ai=ai;
    }

    public MagicAI getAI() {
        return ai;
    }

    @Override
    public String toString() {
        return name;
    }

}

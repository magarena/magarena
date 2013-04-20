package magic.ai;

public enum MagicAIImpl {
    MMAB("minimax", new MMAB()), 
    MMABC("minimax (cheat)", new MMAB(true)), 
    MCTSNC("monte carlo tree search", new MCTSAI(false, false)),
    MCTS("monte carlo tree search (cheat)", new MCTSAI()),
    VEGAS("vegas", new VegasAI()),
    VEGASC("vegas (cheat)", new VegasAI(false, true)),
    RND("random", new RandomAI()),
    MMABFast("minimax (deck strength)", new MMAB(false, true)),
    ;
    
    private static final MagicAIImpl[] SUPPORTED_AIS = {MMAB, MMABC, MCTSNC, MCTS, VEGAS, VEGASC};
    
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

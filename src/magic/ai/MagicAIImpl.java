package magic.ai;

public enum MagicAIImpl {
    MMAB("minimax", new MMAB()), 
    MMABC("minimax (cheat)", new MMAB(false, true)), 
    MCTS("monte carlo (cheat)", new MCTSAI()),
    MCTSNC("monte carlo", new MCTSAI(false, false)),
    VEGAS("vegas", new VegasAI()),
    RND("random", new RandomAI()),
    ;
    
    private static final MagicAIImpl SUPPORTED_AIS[] = new MagicAIImpl[]{MMAB, MMABC, MCTS, VEGAS, RND};
    
    private final String name;
    private final MagicAI ai;

    private MagicAIImpl(final String name, final MagicAI ai) {
    	this.name=name;
        this.ai=ai;
    }
    
    public String getName() {
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
    	final String names[]=new String[SUPPORTED_AIS.length];
    	int index=0;
    	for (final MagicAIImpl ai : SUPPORTED_AIS) {
    		names[index++]=ai.getName();
    	}
    	return names;
    }
}

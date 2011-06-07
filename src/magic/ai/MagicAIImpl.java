package magic.ai;

public enum MagicAIImpl {
    DEFAULT("default", new MiniMaxAlphaBetaAI()), 
    MMAB("minimax", new MiniMaxAlphaBetaAI()), 
    MCTS("monte carlo", new MCTSAI()),
    MCTSNC("monte carlo", new MCTSAI(false, false)),
    MCTSD("monte carlo", new MCTSAI(true, true)),
    VEGAS("vegas", new VegasAI()),
    RND("random", new RandomAI()),
    ;
    
    private static final MagicAIImpl SUPPORTED_AIS[] = new MagicAIImpl[]{MMAB, MCTS, VEGAS};
    
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
    	return DEFAULT;
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

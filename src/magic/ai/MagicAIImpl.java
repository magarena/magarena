package magic.ai;

public enum MagicAIImpl {
	
    DEFAULT(new MiniMaxAlphaBetaAI()),
    MMAB(new MiniMaxAlphaBetaAI()), 
    RND(new RandomAI()),
    MCTS(new MCTSAI());

    private final MagicAI ai;

    private MagicAIImpl(final MagicAI ai) {
    	
        this.ai = ai;
    }

    public MagicAI getAI() {
    	
        return ai;        
    }
}

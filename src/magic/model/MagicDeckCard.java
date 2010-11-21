package magic.model;

public class MagicDeckCard {

	private final MagicCardDefinition cardDefinition;
	private final int draftScore;
	
	public MagicDeckCard(final MagicCardDefinition cardDefinition,final int draftScore) {
		
		this.cardDefinition=cardDefinition;
		this.draftScore=draftScore;
	}

	public MagicDeckCard(final MagicCardDefinition cardDefinition) {
		
		this(cardDefinition,0);
	}
	
	public MagicCardDefinition getCardDefinition() {
		
		return cardDefinition;
	}
		
	public int getDraftScore() {
		
		return draftScore;
	}
}
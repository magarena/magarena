package magic.model;

import java.util.ArrayList;
import java.util.List;

public enum MagicDeckConstructionRule {
	
	MinDeckSize("Decks must have a least 40 cards."),
	FourCopyLimit("With the exception of basic lands, a deck must have no more than 4 copies of a card.")
	;
	
	public static final int MIN_DECK_SIZE = 40;	
	public static final int MAX_COPIES = 4;
	
	private final String text;
	
	private MagicDeckConstructionRule(final String text) {
		this.text = text;
	}
	
	private String getRuleText() {
		return text;
	}
	
	public static List<MagicDeckConstructionRule> checkDeck(MagicDeck deck) {
		ArrayList<MagicDeckConstructionRule> brokenRules = new ArrayList<MagicDeckConstructionRule>();
		
		if(deck.size() < MIN_DECK_SIZE) {
			brokenRules.add(MinDeckSize);
		}
		
		MagicCondensedDeck countedDeck = new MagicCondensedDeck(deck);
		for(MagicCondensedCardDefinition countedCard : countedDeck) {
			if(countedCard.getNumCopies() > 4 && !countedCard.getCard().isBasic() && !countedCard.getCard().getName().equals("Relentless Rats")) {
				brokenRules.add(FourCopyLimit);
				break;
			}
		}
		
		return brokenRules;
	}
	
	public static String getRulesText(List<MagicDeckConstructionRule> rules) {
		StringBuilder sb = new StringBuilder();
		
		for(MagicDeckConstructionRule rule : rules) {
			sb.append(rule.getRuleText());
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
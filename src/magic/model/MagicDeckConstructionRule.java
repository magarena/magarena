package magic.model;

import java.util.ArrayList;
import java.util.List;

public enum MagicDeckConstructionRule {
	
	Min60Cards("Decks must have a least 60 cards."),
	FourCopyLimit("With the exception of basic lands, a deck must have no more than 4 copies of a card.")
	;
	
	private final String text;
	
	private MagicDeckConstructionRule(final String text) {
		this.text = text;
	}
	
	private String getRuleText() {
		return text;
	}
	
	public static List<MagicDeckConstructionRule> checkDeck(MagicDeck deck) {
		ArrayList<MagicDeckConstructionRule> brokenRules = new ArrayList<MagicDeckConstructionRule>();
		
		if(deck.size() < 60) {
			brokenRules.add(Min60Cards);
		}
		
		List<MagicDeckCardDefinition> countedDeck = MagicDeckCardDefinition.condenseCopyCardList(deck);
		for(MagicDeckCardDefinition countedCard : countedDeck) {
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
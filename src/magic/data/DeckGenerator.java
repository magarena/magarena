package magic.data;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicPlayerProfile;

public class DeckGenerator {
	
	private final List<MagicCardDefinition> spellCards;
	private final List<MagicCardDefinition> landCards;
	
	public DeckGenerator(final MagicCubeDefinition cubeDefinition) {
				
		spellCards=new ArrayList<MagicCardDefinition>();
		for (int rarity=1;rarity<=3;rarity++) {
			
			for (final MagicCardDefinition card : CardDefinitions.getInstance().getSpellCards()) {
				
				if (card.getRarity()<=rarity&&cubeDefinition.containsCard(card)) {
					spellCards.add(card);
				}
			}
		}
		
		landCards=new ArrayList<MagicCardDefinition>();
		for (int count=4;count>0;count--) {
			
			for (final MagicCardDefinition card : CardDefinitions.getInstance().getLandCards()) {

				if (cubeDefinition.containsCard(card)) {
					landCards.add(card);
				}
			}			
		}
	}
	
	public MagicDeck buildDeck(final int size,final MagicPlayerProfile profile) {
	
		final MagicDeck deck = new MagicDeck();
		deck.add(CardDefinitions.getInstance().getCard("Elite Vanguard"));
		return deck;
	}
}
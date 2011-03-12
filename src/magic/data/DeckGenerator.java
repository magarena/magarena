package magic.data;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicCardDefinition;
import magic.model.MagicColoredType;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRandom;

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
	
	public MagicDeck generateDeck(final int size,final MagicPlayerProfile profile) {
		
		final MagicDeck deck=new MagicDeck();		
		final int spells=(size*3)/5;
		final int lands=profile.getNrOfNonBasicLands(size-spells);		
		final int maxCreatures=(spells*3)/4;
		final int maxColorless=spells/6;
		final int maxHigh=spells/6;
		int countCreatures=0;
		int countColorless=0;
		int countHigh=0;
		
		// Add spells to deck.
		while (deck.size()<spells) {

			final int index=MagicRandom.nextInt(spellCards.size());
			final MagicCardDefinition cardDefinition=spellCards.get(index);
			if (cardDefinition.isPlayable(profile)) {
				final boolean creature=cardDefinition.isCreature();
				if (creature&&countCreatures>=maxCreatures) {
					continue;
				}
				final boolean colorless=cardDefinition.getColoredType()==MagicColoredType.Colorless;
				if (colorless&&countColorless>=maxColorless) {
					continue;
				}
				final boolean high=cardDefinition.getConvertedCost()>=5;
				if (high&&countHigh>=maxHigh) {
					continue;
				}
				deck.add(cardDefinition);
				spellCards.remove(index);
				if (creature) {
					countCreatures++;
				}
				if (colorless) {
					countColorless++;
				}
				if (high) {
					countHigh++;
				}
			}
		}	
		
		// Add non basic lands to deck.
		while (deck.size()<spells+lands) {
			
			final int index=MagicRandom.nextInt(landCards.size());
			final MagicCardDefinition cardDefinition=landCards.get(index);
			if (cardDefinition.isPlayable(profile)) {
				deck.add(cardDefinition);
				landCards.remove(index);
			}
		}
		
		return deck;
	}
}
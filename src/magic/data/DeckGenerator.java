package magic.data;

import magic.model.MagicCardDefinition;
import magic.model.MagicColoredType;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRandom;

import java.util.ArrayList;
import java.util.List;

public class DeckGenerator {
	
	private final List<MagicCardDefinition> spellCards = new ArrayList<MagicCardDefinition>();
	private final List<MagicCardDefinition> landCards = new ArrayList<MagicCardDefinition>();
    private final MagicCubeDefinition cubeDefinition;
	
	public DeckGenerator(final MagicCubeDefinition cubeDefinition) {
        this.cubeDefinition = cubeDefinition;
	}

    private void genSpells() {
		spellCards.clear();
		for (int rarity=1;rarity<=4;rarity++) {
			for (final MagicCardDefinition card : CardDefinitions.getInstance().getSpellCards()) {
				if (card.getRarity()<=rarity&&cubeDefinition.containsCard(card)) {
					spellCards.add(card);
				}
			}
		}
    }

    private void genLands() {
		landCards.clear();
        for (final MagicCardDefinition card : CardDefinitions.getInstance().getLandCards()) {
            if (cubeDefinition.containsCard(card)) {
                for (int count=4;count>0;count--) {
                    landCards.add(card);
                }
            }
        }			
    }
	
	public MagicDeck generateDeck(final int size,final MagicPlayerProfile profile) {
        genSpells();
        genLands();
		
		final MagicDeck deck=new MagicDeck();		
		final int spells=(size*3)/5;
		final int lands=profile.getNrOfNonBasicLands(size-spells);		
		final int maxCreatures=(spells*2)/3;
		final int maxColorless=spells/6;
		final int maxHigh=spells/6;
		final int maxOther=(spells-maxHigh)/2;
		final int maxCost[]=new int[]{maxOther,maxOther+1,maxHigh};
		int countCreatures=0;
		int countColorless=0;
		int countCost[]=new int[3];
		
		// Add spells to deck.
		while (deck.size() < spells && spellCards.size() > 0) {
			final int index=MagicRandom.nextInt(spellCards.size());
			final MagicCardDefinition cardDefinition=spellCards.get(index);
            spellCards.remove(index);
			
            if (cardDefinition.isPlayable(profile)) {
				final boolean creature=cardDefinition.isCreature();
				if (creature&&countCreatures>=maxCreatures) {
					continue;
				}
				final boolean colorless=cardDefinition.getColoredType()==MagicColoredType.Colorless;
				if (colorless&&countColorless>=maxColorless) {
					continue;
				}
				final int bucket=cardDefinition.getCostBucket();
				if (countCost[bucket]>=maxCost[bucket]) {
					continue;
				}
				deck.add(cardDefinition);
				countCost[bucket]++;
				if (creature) {
					countCreatures++;
				}
				if (colorless) {
					countColorless++;
				}
			} 
            
            if (spellCards.size() == 0) {
                genSpells();
            }
		}	
		
		// Add non basic lands to deck.
		while (deck.size() < spells+lands && landCards.size() > 0) {
			final int index=MagicRandom.nextInt(landCards.size());
			final MagicCardDefinition cardDefinition=landCards.get(index);
            landCards.remove(index);
            
            if (cardDefinition.isPlayable(profile)) {
				deck.add(cardDefinition);
			}
		}
		
		return deck;
	}
}

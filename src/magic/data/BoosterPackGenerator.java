package magic.data;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicBoosterPack;
import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRandom;

public class BoosterPackGenerator {
	
	private final List<MagicCardDefinition> spellCards;
	private final List<MagicCardDefinition> landCards;
	
	public BoosterPackGenerator(final MagicCubeDefinition cubeDefinition) {
				
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
	
	private MagicBoosterPack createBoosterPack(final List<MagicCardDefinition> cards,final int size,final MagicPlayerProfile profile) {
		
		final MagicBoosterPack pack=new MagicBoosterPack();
		for (int s=size;s>0;) {
			
			final int index=MagicRandom.nextInt(cards.size());
			final MagicCardDefinition cardDefinition=cards.get(index);
			if (profile==null||cardDefinition.isPlayable(profile)) {
				pack.add(cardDefinition);
				cards.remove(index);
				s--;
			} 
		}
		return pack;
	}
	
	public MagicBoosterPack createSpellBoosterPack(final int size,final MagicPlayerProfile profile) {
		
		return createBoosterPack(spellCards,size,profile);
	}
		
	public MagicBoosterPack createLandBoosterPack(final int size,final MagicPlayerProfile profile) {

		return createBoosterPack(landCards,size,profile);
	}
}
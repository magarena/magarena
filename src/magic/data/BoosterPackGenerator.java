package magic.data;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicBoosterPack;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;

public class BoosterPackGenerator {
	
	private final List<MagicCardDefinition> spellCards;
	private final List<MagicCardDefinition> landCards;
	
	public BoosterPackGenerator() {
				
		spellCards=new ArrayList<MagicCardDefinition>();
		for (int rarity=1;rarity<=3;rarity++) {
			
			for (final MagicCardDefinition card : CardDefinitions.getInstance().getSpellCards()) {
				
				if (card.getRarity()<=rarity) {
					spellCards.add(card);
				}
			}
		}
		
		landCards=new ArrayList<MagicCardDefinition>();
		for (int count=3;count>0;count--) {
			
			for (final MagicCardDefinition card : CardDefinitions.getInstance().getLandCards()) {
			
				landCards.add(card);
			}			
		}
	}
	
	private MagicBoosterPack createBoosterPack(final List<MagicCardDefinition> cards,final int size) {
		
		final MagicBoosterPack pack=new MagicBoosterPack();
		for (int s=size;s>0;s--) {
			
			final int index=MagicRandom.nextInt(cards.size());
			pack.add(cards.get(index));
			cards.remove(index);
		}
		return pack;
	}
	
	public MagicBoosterPack createSpellBoosterPack(final int size) {
		
		return createBoosterPack(spellCards,size);
	}
		
	public MagicBoosterPack createLandBoosterPack(final int size) {

		return createBoosterPack(landCards,size);
	}
}
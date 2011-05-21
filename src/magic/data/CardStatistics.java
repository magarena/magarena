package magic.data;

import java.io.PrintStream;
import java.util.Collection;

import javax.swing.ImageIcon;

import magic.model.MagicCardDefinition;
import magic.model.MagicColor;

public class CardStatistics {

	public static final String MANA_CURVE_TEXT[]={"X","1","2","3","4","5","6","7","8","9+"};
	public static final ImageIcon MANA_CURVE_ICONS[]={
		IconImages.COST_X,
		IconImages.COST_ONE,
		IconImages.COST_TWO,
		IconImages.COST_THREE,
		IconImages.COST_FOUR,
		IconImages.COST_FIVE,
		IconImages.COST_SIX,
		IconImages.COST_SEVEN,
		IconImages.COST_EIGHT,
		IconImages.COST_NINE,
	};
	public static final int MANA_CURVE_SIZE=MANA_CURVE_TEXT.length;
	
	public static final String TYPE_NAMES[]={"Land","Spell","Creature","Equipment","Aura","Enchantment","Artifact"};
	public static final ImageIcon TYPE_ICONS[]={
		IconImages.LAND,
		IconImages.SPELL,
		IconImages.CREATURE,
		IconImages.EQUIPMENT,
		IconImages.AURA,	
		IconImages.ENCHANTMENT,
		IconImages.ARTIFACT
	};
	public static final int NR_OF_TYPES=TYPE_NAMES.length;
	
	private final Collection<MagicCardDefinition> cards;
	
	public int totalCards=0;
	public int totalTypes[]=new int[NR_OF_TYPES];
	
	public int totalRarity[]=new int[MagicCardDefinition.NR_OF_RARITIES];
	
	public int averageCost=0;
	public int averageValue=0;
	
	public int colorCount[]=new int[MagicColor.NR_COLORS];
	public int colorMono[]=new int[MagicColor.NR_COLORS];
	public int colorLands[]=new int[MagicColor.NR_COLORS];
	public int manaCurve[]=new int[MANA_CURVE_SIZE];
	public int monoColor=0;
	public int multiColor=0;
	public int colorless=0;
	
	public CardStatistics(final Collection<MagicCardDefinition> cards) {

		this.cards=cards;
		createStatistics();
	}
	
	private void createStatistics() {
		
		totalCards=cards.size();

		if (cards.size()==0) {
			return;
		}
		
		for (final MagicCardDefinition card : cards) {
												
			totalRarity[card.getRarity()]++;
						
			if (card.isLand()) {
				totalTypes[0]++;
				for (final MagicColor color : MagicColor.values()) {
					
					if (card.getManaSource(color)>0) {
						colorLands[color.getIndex()]++;
					}
				}				
			} else {
				if (card.hasX()) {
					manaCurve[0]++;
				} else {
					int convertedCost=card.getConvertedCost();
					manaCurve[convertedCost>=MANA_CURVE_SIZE?MANA_CURVE_SIZE-1:convertedCost]++;
				}
				
				averageCost+=card.getConvertedCost();
				averageValue+=card.getValue();
				
				if (card.isCreature()) {
					totalTypes[2]++;
				} else if (card.isEquipment()) {
					totalTypes[3]++;
				} else if (card.isArtifact()) {
					totalTypes[6]++;
				} else if (card.isAura()) {
					totalTypes[4]++;
				} else if (card.isEnchantment()) {
					totalTypes[5]++;
				} else {
					totalTypes[1]++;
				}
				
				int count=0;
				int index=-1;
				for (final MagicColor color : MagicColor.values()) {
				
					if (color.hasColor(card.getColorFlags())) {
						index=color.getIndex();
						colorCount[index]++;
						count++;
					}
				}
				if (count==0) {
					colorless++;
				} else if (count==1) {
					colorMono[index]++;
					monoColor++;
				} else {
					multiColor++;
				}
			}
		}
		
		int total=totalCards-totalTypes[0];
		if (total>0) {
			averageValue=(averageValue*10)/total;
			averageCost=(averageCost*10)/total;
		}
	}
	
	public void printStatictics(final PrintStream stream) {

		stream.print("Cards : "+totalCards);
		for (int index=0;index<NR_OF_TYPES;index++) {
		
			stream.print("  "+TYPE_NAMES[index]+" : "+totalTypes[index]);
		}
		stream.println();
		
		for (int index=0;index<MagicCardDefinition.NR_OF_RARITIES;index++) {
			
			stream.print(MagicCardDefinition.RARITY_NAMES[index]+" : "+totalRarity[index]+"  ");
		}
		stream.println();
		stream.println("Average Cost : "+averageCost+"  Value : "+averageValue);
		stream.println("Monocolor : "+monoColor+"  Multicolor : "+multiColor+"  Colorless : "+colorless);

		for (final MagicColor color : MagicColor.values()) {
			
			final int index=color.getIndex();
			stream.print("Color "+color.getName()+" : "+colorCount[index]);
			stream.print("  Mono : "+colorMono[index]);
			stream.print("  Lands : "+colorLands[index]);
			stream.println();			
		}	
		
		for (int index=0;index<MANA_CURVE_SIZE;index++) {
			
			stream.print(MANA_CURVE_TEXT[index]+" = "+manaCurve[index]+"  ");
		}
		stream.println();
	}
}
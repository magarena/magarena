package magic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import magic.data.BoosterPackGenerator;
import magic.data.CardDefinitions;

public class MagicPlayerDefinition {

	private static final int SPELL_BOOSTER_PACK_SIZE=60;
	
	private static final int DECK_COUNT[]={3,6,6,4,3,2};
	private static final int DECK_COST[]={1,2,3,4,6,10};
	private static final int DECK_SPELLS=24;
	private static final int DECK_LANDS=16;
	private static final int DECK_SIZE=DECK_SPELLS+DECK_LANDS;
	private static final int MIN_CARD_SCORE=25;
	private static final int MIN_SOURCE=16;
	private static final int MAX_NON_BASIC_SOURCE=DECK_LANDS/2;
	
	private static final String NAME="name";
	private static final String ARTIFICIAL="artificial";
	private static final String COLORS="colors";
	private static final String FACE="face";
	
	private String name;
	private boolean artificial;
	private MagicPlayerProfile profile;
	private int face;
	private MagicBoosterPack boosterPack;
	private MagicBoosterPack landBoosterPack;
	private List<MagicDeckCard> draftedDeck=new ArrayList<MagicDeckCard>();

	public MagicPlayerDefinition() {
		
	}
	
	public MagicPlayerDefinition(final String name,final boolean artificial,final MagicPlayerProfile profile,final int face) {
		
		this.name=name;
		this.artificial=artificial;
		this.profile=profile;
		this.face=face;
	}
	
	public String getName() {
		
		return name;
	}

	public void setArtificial(final boolean artificial) {
		
		this.artificial=artificial;
	}
	
	public boolean isArtificial() {
		
		return artificial;
	}
	
	public void setProfile(final MagicPlayerProfile profile) {
		
		this.profile=profile;
	}
	
	public MagicPlayerProfile getProfile() {
		
		return profile;
	}
	
	public int getFace() {
		
		return face;
	}

	public void setBoosterPacks(final BoosterPackGenerator generator) {
		
		final boolean three=profile.getNrOfColors()>2;
		this.boosterPack=generator.createSpellBoosterPack(three?SPELL_BOOSTER_PACK_SIZE:SPELL_BOOSTER_PACK_SIZE+20,null);
		this.landBoosterPack=generator.createLandBoosterPack(three?MAX_NON_BASIC_SOURCE:MAX_NON_BASIC_SOURCE/2,profile);
	}

	private MagicDeckCard createCard(final MagicCardDefinition card) {
		
		final int cost=card.getCost().getCostScore(profile);
		if (cost<=0) {
			return new MagicDeckCard(card,0);
		}
				
		int type=0;
		if (card.isCreature()) {
			type=10;
		} else if (card.isEquipment()) {
			type=8;
		} else if (card.isAura()) {
			type=5;
		} else if (card.hasType(MagicType.Instant)) {
			type=7;
		} else {
			type=6;
		}

		int score=5*card.getValue()/*25*/+3*card.getRemoval()/*15*/+type/*10*/+cost/*50*/;		
		return new MagicDeckCard(card,score);
	}
			
	private void addCardsToDeck(final Collection<MagicDeckCard> remainingCards,final int minCost,final int maxCost,final int count) {

		for (int c=count;c>0;c--) {

			MagicDeckCard bestCard=null;
			int bestScore=-1;
			
			for (final MagicDeckCard draftedCard : remainingCards) {
				
				final int score=draftedCard.getDraftScore();
				final int cost=draftedCard.getCardDefinition().getConvertedCost();
				if (score>bestScore&&!draftedCard.getCardDefinition().isLand()&&cost>=minCost&&cost<=maxCost) {
					bestScore=score;
					bestCard=draftedCard;
				}
			}
			
			if (bestCard==null||bestScore<MIN_CARD_SCORE) {
				break;
			}
			draftedDeck.add(bestCard);
			remainingCards.remove(bestCard);
		}
	}
	
	private void addLandsToDeck() {

		// Calculate statistics per color.
		final int colorCount[]=new int[MagicColor.NR_COLORS];
		for (final MagicDeckCard draftedCard : draftedDeck) {

			final int colorFlags=draftedCard.getCardDefinition().getColorFlags();
			for (final MagicColor color : profile.getColors()) {

				if (color.hasColor(colorFlags)) {
					colorCount[color.getIndex()]++;
				}
			}
		}

		// Add suitable non basic lands to deck in order of pack.
		final int colorSource[]=new int[MagicColor.NR_COLORS];
		for (final MagicCardDefinition card : landBoosterPack) {

			draftedDeck.add(new MagicDeckCard(card));
			for (final MagicColor color : MagicColor.values()) {

				colorSource[color.getIndex()]+=card.getManaSource(color);
			}
		}

		// Add optimal basic lands to deck.
		while (draftedDeck.size()<DECK_SIZE) {

			MagicColor bestColor=null;
			int lowestRatio=Integer.MAX_VALUE;
			for (final MagicColor color : MagicColor.values()) {
				
				final int index=color.getIndex();
				final int count=colorCount[index];
				if (count>0) {
					final int source=colorSource[index];
					final int ratio;
					if (source<MIN_SOURCE) {
						ratio=source-count;
					} else {
						ratio=source*100/count;
					}
					if (ratio<lowestRatio) {
						lowestRatio=ratio;
						bestColor=color;
					}
				}
			}
			final MagicCardDefinition landCard=CardDefinitions.getInstance().getBasicLand(bestColor);
			colorSource[bestColor.getIndex()]+=landCard.getManaSource(bestColor);
			draftedDeck.add(new MagicDeckCard(landCard));
		}
	}

	public void buildDeck() {

		draftedDeck.clear();
		final Collection<MagicDeckCard> remainingCards=new ArrayList<MagicDeckCard>();
		for (final MagicCardDefinition cardDefinition : boosterPack) {
			
			remainingCards.add(createCard(cardDefinition));
		}
		int min=0;
		for (int index=0;index<DECK_COUNT.length;index++) {
			
			final int max=DECK_COST[index];
			addCardsToDeck(remainingCards,min,max,DECK_COUNT[index]);
			min=max+1;
		}
		addCardsToDeck(remainingCards,0,4,DECK_SPELLS-draftedDeck.size());
		addCardsToDeck(remainingCards,5,10,DECK_SPELLS-draftedDeck.size());
		addLandsToDeck();
	}
	
	public List<MagicDeckCard> getDraftedDeck() {

		return draftedDeck;
	}	
	
	public void setDraftedDeck(final List<MagicDeckCard> draftedDeck) {

		this.draftedDeck=draftedDeck;
	}
	
	private String getDeckPrefix(final String prefix,final int index) {
		
		return prefix+"deck"+index;
	}
	
	public void load(final Properties properties,final String prefix) {

		name=properties.getProperty(prefix+NAME,"");
		artificial=Boolean.parseBoolean(properties.getProperty(prefix+ARTIFICIAL,"true"));
		final String colors=properties.getProperty(prefix+COLORS,"");
		profile=new MagicPlayerProfile(colors);
		face=Integer.parseInt(properties.getProperty(prefix+FACE));

		draftedDeck.clear();
		for (int index=1;index<=DECK_SIZE;index++) {

			final String name=properties.getProperty(getDeckPrefix(prefix,index));
			draftedDeck.add(new MagicDeckCard(CardDefinitions.getInstance().getCard(name)));
		}
	}
	
	public void save(final Properties properties,final String prefix) {

		properties.setProperty(prefix+NAME,name);
		properties.setProperty(prefix+ARTIFICIAL,""+artificial);
		properties.setProperty(prefix+COLORS,getProfile().getColorText());
		properties.setProperty(prefix+FACE,""+face);
		
		int index=1;
		for (final MagicDeckCard card : draftedDeck) {

			properties.setProperty(getDeckPrefix(prefix,index++),card.getCardDefinition().getFullName());
		}
	}
}
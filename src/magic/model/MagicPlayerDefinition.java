package magic.model;

import magic.data.CardDefinitions;
import magic.data.DeckGenerator;

import java.util.Properties;

public class MagicPlayerDefinition {

	private static final int DECK_SIZE=60;
	private static final int MIN_SOURCE=16;
	
	private static final String NAME="name";
	private static final String ARTIFICIAL="artificial";
	private static final String COLORS="colors";
	private static final String FACE="face";
	
	private String name;
	private boolean artificial;
	private MagicPlayerProfile profile;
	private int face;
	private final MagicDeck deck = new MagicDeck();

	MagicPlayerDefinition() {}
	
	public MagicPlayerDefinition(final String name,final boolean artificial,final MagicPlayerProfile profile,final int face) {
		this.name=name;
		this.artificial=artificial;
		this.profile=profile;
		this.face=face;
	}
	
	public String getName() {
		return name;
	}

	public void setArtificial(final boolean art) {
		this.artificial=art;
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
				
	private void addBasicLandsToDeck() {
		// Calculate statistics per color.
		final int colorCount[]=new int[MagicColor.NR_COLORS];
		final int colorSource[]=new int[MagicColor.NR_COLORS];
		for (final MagicCardDefinition cardDefinition : deck) {

			if (cardDefinition.isLand()) {
				for (final MagicColor color : MagicColor.values()) {

					colorSource[color.ordinal()]+=cardDefinition.getManaSource(color);
				}
			} else {
				final int colorFlags=cardDefinition.getColorFlags();
				for (final MagicColor color : profile.getColors()) {

					if (color.hasColor(colorFlags)) {
						colorCount[color.ordinal()]++;
					}
				}				
			}
		}
		
		// Add optimal basic lands to deck.
		while (deck.size()<DECK_SIZE) {
			MagicColor bestColor=null;
			int lowestRatio=Integer.MAX_VALUE;
			for (final MagicColor color : MagicColor.values()) {
				
				final int index=color.ordinal();
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
			colorSource[bestColor.ordinal()]+=landCard.getManaSource(bestColor);
			deck.add(landCard);
		}
	}
	
	public MagicDeck getDeck() {
		return deck;
	}	
	
    public void setDeck(final MagicDeck aDeck) {
        deck.setContent(aDeck);
	}	
	
	void generateDeck(final DeckGenerator generator) {
		generator.generateDeck(DECK_SIZE, profile, deck);
		addBasicLandsToDeck();
	}
	
	private static String getDeckPrefix(final String prefix,final int index) {
		return prefix+"deck"+index;
	}
	
	void load(final Properties properties,final String prefix) {
		name=properties.getProperty(prefix+NAME,"");
		artificial=Boolean.parseBoolean(properties.getProperty(prefix+ARTIFICIAL,"true"));
		final String colors=properties.getProperty(prefix+COLORS,"");
		profile=new MagicPlayerProfile(colors);
		face=Integer.parseInt(properties.getProperty(prefix+FACE));

        final MagicDeck unsupported = new MagicDeck();
		deck.clear();
        for (int index=1;index<=properties.size();index++) {
            final String deckPrefix = getDeckPrefix(prefix,index);
            if (properties.containsKey(deckPrefix)) {
                final String tName = properties.getProperty(deckPrefix);
                final MagicCardDefinition cdef = CardDefinitions.getInstance().getCard(tName);
                if (cdef.isValid()){
                    deck.add(cdef);
                } else {
                    unsupported.add(cdef);
                }
            }
        }

        magic.data.DeckUtils.showUnsupportedCards(unsupported);
	}
	
	void save(final Properties properties,final String prefix) {
		properties.setProperty(prefix+NAME,name);
		properties.setProperty(prefix+ARTIFICIAL,Boolean.toString(artificial));
		properties.setProperty(prefix+COLORS,getProfile().getColorText());
		properties.setProperty(prefix+FACE,Integer.toString(face));
		
		int index=1;
		for (final MagicCardDefinition cardDefinition : deck) {
			properties.setProperty(getDeckPrefix(prefix,index++),cardDefinition.getFullName());
		}
	}
}

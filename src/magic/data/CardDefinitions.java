package magic.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeckCard;
import magic.model.MagicManaCost;
import magic.model.MagicStaticType;
import magic.model.MagicType;
import magic.model.event.MagicTiming;

public class CardDefinitions {

	private static final CardDefinitions INSTANCE=new CardDefinitions();
	
	private static final String CARDS_FILENAME="cards.txt";
	
	private final List<MagicCardDefinition> cards;
	private final List<MagicCardDefinition> landCards;
	private final List<MagicCardDefinition> spellCards;
	private final Map<String,MagicCardDefinition> cardsMap;
	
	private CardDefinitions() {
		
		cards=new ArrayList<MagicCardDefinition>();
		landCards=new ArrayList<MagicCardDefinition>();
		spellCards=new ArrayList<MagicCardDefinition>();
		cardsMap=new HashMap<String, MagicCardDefinition>();
	}
	
	private void setProperty(final MagicCardDefinition card,final String property,final String value) {
		
		if ("value".equals(property)) {
			card.setValue(Integer.parseInt(value));
		} else if ("removal".equals(property)) {
			card.setRemoval(Integer.parseInt(value));
		} else if ("rarity".equals(property)) {
			card.setRarity(Integer.parseInt(value));
		} else if ("type".equals(property)) {
			final String names[]=value.split(",");
			for (final String name : names) {
				
				card.addType(MagicType.getType(name));
			}
		} else if ("subtype".equals(property)) {
			card.setSubTypes(value.split(","));
		} else if ("color".equals(property)) {
			card.setColors(value);
			card.setColoredType();
		} else if ("converted".equals(property)) {
			card.setConvertedCost(Integer.parseInt(value));
		} else if ("cost".equals(property)) {
			card.setCost(MagicManaCost.createCost(value));
		} else if ("equip".equals(property)) {
			card.setEquipCost(MagicManaCost.createCost(value));
		} else if ("mana".equals(property)) {
			card.setManaSourceText(value);
		} else if ("basic".equals(property)) {
			card.setBasicManaActivations(value);
		} else if ("power".equals(property)) {
			card.setPower(Integer.parseInt(value));
		} else if ("toughness".equals(property)) {
			card.setToughness(Integer.parseInt(value));
		} else if ("ability".equals(property)) {
			final String names[]=value.split(",");
			for (final String name : names) {

				card.setAbility(MagicAbility.getAbility(name));
			}
		} else if ("static".equals(property)) {
			card.setStaticType(MagicStaticType.getStaticTypeFor(value));
		} else if ("timing".equals(property)) {
			card.setTiming(MagicTiming.getTimingFor(value));
		} else {
			System.err.println(property);
		}
	}
	
	private void filterCards() {
		
		for (final MagicCardDefinition card : cards) {
			
			if (!card.isLand()) {
				spellCards.add(card);
			} else if (!card.isBasic()) {
				landCards.add(card);
			}
		}
	}

	private void checkCard(final MagicCardDefinition card) {
		
		if (card!=null) {		
			if (card.hasType(MagicType.Legendary)&&card.getRarity()<3) {
				card.setRarity(3);
				System.err.println("Rarity for Legendary : "+card.getName());
			}
			if (card.getTiming()==MagicTiming.None) {
				System.err.println("No timing : "+card.getName());
			}
		}
	}
	
	public void addDefinition(final MagicCardDefinition cardDefinition) {

		cardDefinition.setIndex(cards.size());
		cards.add(cardDefinition);
		cardsMap.put(cardDefinition.getFullName(),cardDefinition);
	}
	
	public void addDefinitions(final List<MagicCardDefinition> cardDefinitions) {
		
		for (final MagicCardDefinition cardDefinition : cardDefinitions) {
			
			addDefinition(cardDefinition);
		}
	}
	
	public void loadCardDefinitions() throws IOException {

		// Cards.
		final InputStream stream=this.getClass().getResourceAsStream(CARDS_FILENAME);
		final BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
		MagicCardDefinition cardDefinition=null;
		String line;
		while ((line=reader.readLine())!=null) {

			line=line.trim();
			int pos=line.indexOf(':');
			if (pos>0) {
				checkCard(cardDefinition);
				final String name=line.substring(pos+1);
				cardDefinition=new MagicCardDefinition(name);
				addDefinition(cardDefinition);
			} else {
				pos=line.indexOf('=');
				if (pos>0) {
					final String property=line.substring(0,pos).toLowerCase();
					final String value=line.substring(pos+1);
					setProperty(cardDefinition,property,value);
				}
			}
		}
		checkCard(cardDefinition);
		reader.close();
		filterCards();
		printStatistics();
		
		// Tokens.
		addDefinitions(TokenCardDefinitions.TOKEN_CARDS);
		addDefinition(MagicCardDefinition.EMPTY);
		
		System.out.println(getNumberOfCards()+" card definitions");
	}
	
	public int getNumberOfCards() {
		
		return cards.size();
	}
	
	public MagicCardDefinition getCard(final String name) {
		
		final MagicCardDefinition cardDefinition=cardsMap.get(name);
		if (cardDefinition==null) {
			System.err.println("No card definition found for "+name);
		}
		return cardDefinition;
	}
		
	public MagicCardDefinition getBasicLand(final MagicColor color) {

		if (MagicColor.Black.equals(color)) {
			return getCard("Swamp");
		} else if (MagicColor.Blue.equals(color)) {
			return getCard("Island");
		} else if (MagicColor.Green.equals(color)) {
			return getCard("Forest");
		} else if (MagicColor.Red.equals(color)) {
			return getCard("Mountain");
		} else if (MagicColor.White.equals(color)) {
			return getCard("Plains");
		}
		return null;
	}

	public List<MagicCardDefinition> getCards() {
		
		return cards;
	}
	
	public List<MagicCardDefinition> getLandCards() {

		return landCards;
	}
	
	public List<MagicCardDefinition> getSpellCards() {
		
		return spellCards;
	}
	
	public void printStatistics() {
		
		final List<MagicDeckCard> allCards=new ArrayList<MagicDeckCard>();
		for (final MagicCardDefinition card : cards) {
			
			allCards.add(new MagicDeckCard(card));
		}
		final CardStatistics statistics=new CardStatistics(allCards);
		statistics.printStatictics(System.out);
	}
	
	public static CardDefinitions getInstance() {
		
		return INSTANCE;
	}
}
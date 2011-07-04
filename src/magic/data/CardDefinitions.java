package magic.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicStaticType;
import magic.model.MagicType;
import magic.model.event.MagicTiming;

/**
 * Load card definitions from cards.txt and cards2.txt
 */
public class CardDefinitions {

	private static final CardDefinitions INSTANCE=new CardDefinitions();
	
	private static final String CARDS_FILENAME="cards.txt";
	private static final String EXTRA_CARDS_FILENAME="cards2.txt";
	
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
	
        if ("image".equals(property)) {
            card.setImageURL(value);
        } else if ("cube".equals(property)) {
			CubeDefinitions.getInstance().getCubeDefinition(value).add(card.getName());
		} else if ("value".equals(property)) {
			card.setValue(Integer.parseInt(value));
		} else if ("removal".equals(property)) {
			card.setRemoval(Integer.parseInt(value));
		} else if ("rarity".equals(property)) {
			card.setRarity(value.charAt(0));
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
            //legendaries are at least Rare
			if (card.hasType(MagicType.Legendary) && card.getRarity() < 3) {
				card.setRarity('R');
				System.err.println("Rarity for Legendary : "+card.getName());
                System.exit(1);
			}
            //every card should have a timing hint
			if (card.getTiming()==MagicTiming.None) {
				System.err.println("No timing : "+card.getName());
                System.exit(1);
			}
		}
	}
	
	public void addDefinition(final MagicCardDefinition cardDefinition) {
		cardDefinition.setIndex(cards.size());
		cards.add(cardDefinition);
		cardsMap.put(cardDefinition.getFullName(),cardDefinition);

        //add to default vintage cube
        if (!cardDefinition.isToken()) {
			CubeDefinitions.getInstance().getCubeDefinition("all").add(cardDefinition.getName());
        }
        
        //link to companion object containing static variables
        final String fname = cardDefinition.getFullName();
        final String cname = fname.replace(' ', '_').replace('\'','_');
        try {
            Class c = Class.forName("magic.card." + cname);
            Field[] fields = c.getDeclaredFields();
            for (final Field field : fields) {
                final Object obj = Modifier.isPublic(field.getModifiers()) ? field.get(null) : null;
                if (obj != null) {
                    cardDefinition.add(obj);
                }
            }
        } catch (ClassNotFoundException err) {
            //System.err.println("No companion class for " + fname);
        } catch (IllegalAccessException err) {

        }
	}
	
	public void addDefinitions(final List<MagicCardDefinition> cardDefinitions) {
		for (final MagicCardDefinition cardDefinition : cardDefinitions) {
			addDefinition(cardDefinition);
		}
	}
	
	private void loadCardDefinitions(final String filename) throws IOException {

		// Cards.
		final InputStream stream=this.getClass().getResourceAsStream(filename);
		final BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
		MagicCardDefinition cardDefinition=null;
		String line;
		while ((line=reader.readLine())!=null) {
			line=line.trim();
			int pos=line.indexOf('>');
			if (pos==0) {
				checkCard(cardDefinition);
				final String name=line.substring(1);
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
	}
	
	public void loadCardDefinitions() throws IOException {

		loadCardDefinitions(CARDS_FILENAME);
		loadCardDefinitions(EXTRA_CARDS_FILENAME);
		filterCards();
		printStatistics();
		
		// Tokens.
		addDefinitions(TokenCardDefinitions.TOKEN_CARDS);
		addDefinition(MagicCardDefinition.EMPTY);
		
		System.err.println(getNumberOfCards()+" card definitions");
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
		final CardStatistics statistics=new CardStatistics(cards);
		statistics.printStatictics(System.err);
	}
	
	public static CardDefinitions getInstance() {
		
		return INSTANCE;
	}
}

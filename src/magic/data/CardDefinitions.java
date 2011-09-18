package magic.data;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicStaticType;
import magic.model.MagicType;
import magic.model.event.MagicTiming;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
	
	private static void setProperty(final MagicCardDefinition card,final String property,final String value) {
        if ("image".equals(property)) {
            card.setImageURL(value);
		} else if ("num_images".equals(property)) {
			card.setImageCount(Integer.parseInt(value));
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
		} else if ("given_ability".equals(property)) {
			final String names[]=value.split(",");
			for (final String name : names) {
				card.setGivenAbility(MagicAbility.getAbility(name));
			}
		} else if ("static".equals(property)) {
			card.setStaticType(MagicStaticType.getStaticTypeFor(value));
		} else if ("timing".equals(property)) {
			card.setTiming(MagicTiming.getTimingFor(value));
		} else if ("ignore".equals(property)) {
			final String sizes[]=value.split(",");
			for (final String size : sizes) {
				card.addIgnore(Long.parseLong(size));
			}
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

	private static void checkCard(final MagicCardDefinition card) {
		if (card!=null) {	
            //legendaries are at least Rare
			if (card.hasType(MagicType.Legendary) && card.getRarity() < 3) {
			    System.err.println("ERROR! Wrong rarity for " + card.getName());
				throw new RuntimeException(card.getName() + " is legendary but rarity is only " + card.getRarity());
			}
            //every card should have a timing hint
			if (card.getTiming()==MagicTiming.None) {
			    System.err.println("ERROR! No timing hint for " + card.getName());
				throw new RuntimeException(card.getName() + " does not have a timing hint");
			}
		}
	}
	
	private void addDefinition(final MagicCardDefinition cardDefinition) {
		cardDefinition.setIndex(cards.size());
		cards.add(cardDefinition);
		cardsMap.put(cardDefinition.getFullName(),cardDefinition);

        //add to default vintage cube
        if (!cardDefinition.isToken()) {
			CubeDefinitions.getInstance().getCubeDefinition("all").add(cardDefinition.getName());
        }
        
        //link to companion object containing static variables
        final String fname = cardDefinition.getFullName();
        final String cname = fname.replaceAll("[^A-Za-z]", "_");
        try { //reflection
            final Class c = Class.forName("magic.card." + cname);
            final Field[] fields = c.getDeclaredFields();
            for (final Field field : fields) {
                final Object obj = Modifier.isPublic(field.getModifiers()) ? field.get(null) : null;
                if (obj != null) {
                    cardDefinition.add(obj);
                }
            }
        } catch (final ClassNotFoundException ex) {
            //no companion class found
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
	}
	
	private void addDefinitions(final List<MagicCardDefinition> cardDefinitions) {
		for (final MagicCardDefinition cardDefinition : cardDefinitions) {
			addDefinition(cardDefinition);
		}
	}
	
	private void loadCardDefinitions(final String filename) {

		// Cards.
		final InputStream stream=this.getClass().getResourceAsStream(filename);
        String content = null;
        try { //load card definitions
            content = FileIO.toStr(stream);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load card definitions from " + filename);
            return;
        }

        final Scanner sc = new Scanner(content);
		MagicCardDefinition cardDefinition=null;
		while (sc.hasNextLine()) {
			final String line=sc.nextLine().trim();
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
	}
	
	public void loadCardDefinitions() {
		loadCardDefinitions(CARDS_FILENAME);
		loadCardDefinitions(EXTRA_CARDS_FILENAME);
		filterCards();
		printStatistics();
		
		// add tokens.
		addDefinitions(TokenCardDefinitions.TOKEN_CARDS);
		addDefinition(MagicCardDefinition.UNKNOWN);

		System.err.println(getNumberOfCards()+ " card definitions");
        MagicCardDefinition.printStatistics();
	}
	
	public int getNumberOfCards() {
		return cards.size();
	}
	
    public MagicCardDefinition getCard(final int cindex) {
        return cards.get(cindex);
    }
	
	public MagicCardDefinition getCard(final String name) {
		final MagicCardDefinition cardDefinition=cardsMap.get(name);
		if (cardDefinition == null) {
            return new MagicCardDefinition(name) {
                @Override
                public boolean isValid() {
                    return false;
                }
            };
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
        throw new RuntimeException("No matching basic land for MagicColor " + color);
	}

	public List<MagicCardDefinition> getCards() {
		return cards;
	}
	
	List<MagicCardDefinition> getLandCards() {
		return landCards;
	}
	
	public List<MagicCardDefinition> getSpellCards() {
		return spellCards;
	}
	
	private void printStatistics() {
		final CardStatistics statistics=new CardStatistics(cards);
		statistics.printStatictics(System.err);
	}
	
	public static CardDefinitions getInstance() {
		return INSTANCE;
	}
}

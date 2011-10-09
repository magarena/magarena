package magic.data;

import magic.MagicMain;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicStaticType;
import magic.model.MagicType;
import magic.model.MagicSubType;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicStatic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Load card definitions from cards.txt and cards2.txt
 */
public class CardDefinitions {

	private static final CardDefinitions INSTANCE=new CardDefinitions();
	
	private static final String CARDS_FILENAME="cards.txt";
	private static final String EXTRA_CARDS_FILENAME="cards2.txt";
	
	public static final String CARD_TEXT_FOLDER = "texts";
	public static final String CARD_IMAGE_FOLDER = "cards";
	public static final String TOKEN_IMAGE_FOLDER = "tokens";
	public static final String CARD_IMAGE_EXT = CardImagesProvider.IMAGE_EXTENSION;
	public static final String CARD_TEXT_EXT = ".txt";
	
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
		} else if ("url".equals(property)) {
			card.setCardInfoURL(value);
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
            if (!card.isEquipment()) {
                throw new RuntimeException(card.getFullName() + ": only equipment may have equip cost");
            }
			card.setEquipCost(MagicManaCost.createCost(value));
		} else if ("mana".equals(property)) {
			card.setManaSourceText(value);
		} else if ("basic".equals(property)) {
			card.setBasicManaActivations(value);
		} else if ("pt".equals(property)) {
            if (!card.isCreature()) {
                throw new RuntimeException(card.getFullName() + ": only creatures may have power/toughness");
            }
            final String[] pt = value.split("/");
			card.setPowerToughness(Integer.parseInt(pt[0]),Integer.parseInt(pt[1]));
		} else if ("ability".equals(property)) {
			final String names[]=value.split(",");
			for (final String name : names) {
				card.setAbility(MagicAbility.getAbility(name));
			}
		} else if ("given_pt".equals(property)) {
            if (!card.isEquipment() && !card.isAura()) {
                throw new RuntimeException(card.getFullName() + ": only equipment or aura may have given_pt");
            }
            final String[] pt = value.split("/");
			card.add(MagicStatic.genPTStatic(Integer.parseInt(pt[0]), Integer.parseInt(pt[1])));
		} else if ("given_ability".equals(property)) {
            if (!card.isEquipment() && !card.isAura()) {
                throw new RuntimeException(card.getFullName() + ": only equipment or aura may have given_ability");
            }
            card.add(MagicStatic.genABStatic(MagicAbility.getAbilities(value.split(","))));
		} else if ("given_subtype".equals(property)) {
            if (!card.isEquipment() && !card.isAura()) {
                throw new RuntimeException(card.getFullName() + ": only equipment or aura may have given_subtype");
            }
            card.add(MagicStatic.genSTStatic(MagicSubType.getSubTypes(value.split(","))));
		} else if ("static".equals(property)) {
			card.setStaticType(MagicStaticType.getStaticTypeFor(value));
		} else if ("timing".equals(property)) {
			card.setTiming(MagicTiming.getTimingFor(value));
		} else if ("ignore".equals(property)) {
			final String sizes[]=value.split(",");
			for (final String size : sizes) {
				card.addIgnore(Long.parseLong(size));
			}
		} else if ("mana_or_combat".equals(property)) {
            card.setExcludeManaOrCombat();
		} else {
            throw new RuntimeException("Unknown card property " + property + "=" + value);
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
		MagicCardDefinition cardDefinition = MagicCardDefinition.UNKNOWN;
		while (sc.hasNextLine()) {
			final String line=sc.nextLine().trim();
            if (line.length() == 0) {
                //blank line
            } else if (line.startsWith(">")) {
                //start of a card
				checkCard(cardDefinition);
				final String name=line.substring(1);
				cardDefinition=new MagicCardDefinition(name);
				addDefinition(cardDefinition);
			} else {
                //property of a card
				final String[] tokens = line.split("=");
                if (tokens.length == 1) {
                    setProperty(cardDefinition, tokens[0], "");
                } else if (tokens.length == 2) {
                    setProperty(cardDefinition, tokens[0], tokens[1]);
                } else {
                    throw new RuntimeException("Malformed line: " + line);
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
        
        // set card text
        loadCardTexts();
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
	
	public void loadCardTexts() {
		for(MagicCardDefinition card : getCards()) {
			if(card != MagicCardDefinition.UNKNOWN && card.getText().length() == 0) {
				// try to load text from file
				final StringBuilder buffer = new StringBuilder();
				buffer.append(MagicMain.getGamePath());
				buffer.append(File.separator);
				buffer.append(CARD_TEXT_FOLDER);
				buffer.append(File.separator);				
				buffer.append(card.getCardTextName());
				buffer.append(CARD_TEXT_EXT);
				
				try {
					String text = FileIO.toStr(new File(buffer.toString()));
					if(text != null) {
						card.setText(text);						
					}
				} catch (IOException e) {
					// text not downloaded or missing
				}
			}
		}
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
	
	public List<MagicCardDefinition> getLandCards() {
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

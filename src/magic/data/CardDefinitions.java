package magic.data;

import magic.MagicMain;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicStaticType;
import magic.model.MagicType;
import magic.model.MagicSubType;
import magic.model.event.MagicSacrificeManaActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicStatic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Load card definitions from cards.txt
 */
public class CardDefinitions {

	private static final CardDefinitions INSTANCE=new CardDefinitions();
	
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
		} else if ("token".equals(property)) {
			TokenCardDefinitions.getInstance().addTokenDefinition(card, value);
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
		} else if ("sacrifice_colorless".equals(property)) {
			card.addManaActivation(new MagicSacrificeManaActivation(Arrays.asList(MagicManaType.Colorless)));
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
		if(card == null) {
			return;
		}
		
        //every card should have a timing hint
        if (!card.isToken() && card.getTiming()==MagicTiming.None) {
            System.err.println("ERROR! No timing hint for " + card.getName());
            throw new RuntimeException(card.getName() + " does not have a timing hint");
        }
	}
	
	private void addDefinition(final MagicCardDefinition cardDefinition) {
		if(cardDefinition == null) {
			return;
		}

        if (cardDefinition.getIndex() == -1) {
            cardDefinition.setIndex(cards.size());
            cards.add(cardDefinition);
        }
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

    private MagicCardDefinition string2carddef(final String content) {
        final Scanner sc = new Scanner(content);
		MagicCardDefinition cardDefinition = null;
		while (sc.hasNextLine()) {
			final String line=sc.nextLine().trim();
            if (line.length() == 0) {
                //blank line
            } else if (line.startsWith(">")) {
                //start of a card
				final String name=line.substring(1);
				cardDefinition=new MagicCardDefinition(name);
		        cardDefinition.setIndex(cards.size());
                cards.add(cardDefinition);
			} else {
                //property of a card
                int i = line.indexOf("=");
				if (i < 0) {
                    setProperty(cardDefinition, line, "");
                } else {
                    setProperty(cardDefinition, line.substring(0, i), line.substring(i+1));
                } 
			}
		}
		if (cardDefinition == null) {				
            throw new RuntimeException("Malformed card script");
        } else {
            return cardDefinition;
        }
    }
	
    private void loadCardDefinition(final File file) {
        try { //load card definitions
            final String content = FileIO.toStr(file);
            final MagicCardDefinition cdef = string2carddef(content);
            checkCard(cdef);
            addDefinition(cdef);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load card definitions from " + file);
            return;
        }
    }
	
	private void loadCardDefinitions(final String filename) {
		loadCardDefinitions(filename, false);
	}
	
	private void loadCardDefinitions(final String filename, final boolean addCardDefLast) {
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
		MagicCardDefinition cardDefinition = null;
		while (sc.hasNextLine()) {
			final String line=sc.nextLine().trim();
            if (line.length() == 0) {
                //blank line
            } else if (line.startsWith(">")) {
                //start of a card
				
				// check and add previous card
				checkCard(cardDefinition);
				if(addCardDefLast) {
					// add previous card after setting all properties (for tokens)
					addDefinition(cardDefinition);
				}
				
				final String name=line.substring(1);
				cardDefinition=new MagicCardDefinition(name);
				if(!addCardDefLast) {
					addDefinition(cardDefinition);
				}
			} else {
                //property of a card
				final String[] tokens = line.split("=");
                if (tokens.length == 1) {
                    setProperty(cardDefinition, tokens[0], "");
                } else if (tokens.length == 2) {
                    setProperty(cardDefinition, tokens[0], tokens[1]);
                } else {
					if(tokens.length > 0 && ("image".equals(tokens[0]) || "url".equals(tokens[0]))) {
						// urls may have = signs in it
						int i = line.indexOf("=");
						setProperty(cardDefinition, tokens[0], line.substring(i+1));
					} else {
						throw new RuntimeException("Malformed line: " + line);
					}
                }
			}
		}
		// check and add last card
		checkCard(cardDefinition);
		if(addCardDefLast) {
			// add previous card after setting all properties (for tokens)
			addDefinition(cardDefinition);
		}
	}
	
	public void loadCardDefinitions() {
        //load all files in card directory
        final File cardDir = new File(MagicMain.getScriptsPath());
        final File[] files = cardDir.listFiles();
        for (File file : files) {
            loadCardDefinition(file);
        }

		filterCards();
		printStatistics();
		
		// add tokens.
        // add definiton after setting properties
		loadCardDefinitions(TokenCardDefinitions.TOKEN_FILENAME, true); 
		
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

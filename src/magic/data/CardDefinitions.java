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
import magic.model.event.MagicPlayAuraEvent;
import magic.model.mstatic.MagicStatic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Properties;

/**
 * Load card definitions from cards.txt
 */
public class CardDefinitions {

	public static final String CARD_TEXT_FOLDER = "texts";
	public static final String CARD_IMAGE_FOLDER = "cards";
	public static final String TOKEN_IMAGE_FOLDER = "tokens";
	public static final String CARD_IMAGE_EXT = CardImagesProvider.IMAGE_EXTENSION;
	public static final String CARD_TEXT_EXT = ".txt";
	
	private static final List<MagicCardDefinition> cards = new ArrayList<MagicCardDefinition>();
	private static final List<MagicCardDefinition> landCards = new ArrayList<MagicCardDefinition>();
	private static final List<MagicCardDefinition> spellCards = new ArrayList<MagicCardDefinition>();
	private static final Map<String,MagicCardDefinition> cardsMap = new HashMap<String, MagicCardDefinition>();

	private static void setProperty(final MagicCardDefinition card,final String property,final String value) {
               if ("image".equals(property)) {
            card.setImageURL(value);
		} else if ("url".equals(property)) {
			card.setCardInfoURL(value);
		} else if ("num_images".equals(property)) {
			card.setImageCount(Integer.parseInt(value));
        } else if ("cube".equals(property)) {
			CubeDefinitions.getCubeDefinition(value).add(card.getName());
		} else if ("token".equals(property)) {
			TokenCardDefinitions.add(card, value);
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
		} else if ("pt".equals(property)) {
            if (!card.isCreature()) {
                throw new RuntimeException(card.getFullName() + ": only creatures may have power/toughness");
            }
            final String[] pt = value.split("/");
			card.setPowerToughness(Integer.parseInt(pt[0]),Integer.parseInt(pt[1]));
		} else if ("ability".equals(property)) {
			final String names[]=value.split(",");
			for (final String name : names) {
                final MagicAbility ability = MagicAbility.getAbility(name);
                final String arg = name.substring(ability.toString().length()).trim();
				card.setAbility(ability, arg);
			}
		} else if ("given_pt".equals(property)) {
            if (!card.isEquipment() && !card.isAura()) {
                throw new RuntimeException(card.getFullName() + ": only equipment or aura may have given_pt");
            }
            final String[] pt = value.replace('+','0').split("/");
			card.add(MagicStatic.genPTStatic(Integer.parseInt(pt[0]), Integer.parseInt(pt[1])));
		} else if ("given_ability".equals(property)) {
            if (!card.isEquipment() && !card.isAura() && !card.isEnchantment()) {
                throw new RuntimeException(card.getFullName() + ": only equipment/aura/enchantment may have given_ability");
            }
            card.add(MagicStatic.genABStatic(MagicAbility.getAbilities(value.split(","))));
		} else if ("given_subtype".equals(property)) {
            if (!card.isEquipment() && !card.isAura()) {
                throw new RuntimeException(card.getFullName() + ": only equipment or aura may have given_subtype");
            }
            card.add(MagicStatic.genSTStatic(MagicSubType.getSubTypes(value.split(","))));
		} else if ("given_color".equals(property)) {
            if (!card.isEquipment() && !card.isAura()) {
                throw new RuntimeException(card.getFullName() + ": only equipment or aura may have given_subtype");
            }
            card.add(MagicStatic.genCOStatic(MagicColor.getFlags(value)));
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
		} else if ("enchant".equals(property)) {
            card.add(MagicPlayAuraEvent.create(value));
		} else if ("requires_card_code".equals(property)) {
	        addCardSpecificCode(card);	
        } else if ("name".equals(property)) {
            //ignore
        } else {
            throw new RuntimeException("Unknown card property " + property + "=" + value);
		}
	}
    
	private static void filterCards() {
		for (final MagicCardDefinition card : cards) {
			if (!card.isLand() && !card.isToken()) {
				spellCards.add(card);
			} else if (!card.isBasic() && !card.isToken()) {
				landCards.add(card);
			}
		}
	}

	private static void checkCard(final MagicCardDefinition card) {
		assert card != null : "CardDefinitions.checkCard passed null";
		
        //every card should have a timing hint
        if (!card.isToken() && card.getTiming()==MagicTiming.None) {
            System.err.println("ERROR! No timing hint for " + card.getName());
            throw new RuntimeException(card.getName() + " does not have a timing hint");
        }
	}
	
	private static void addDefinition(final MagicCardDefinition cardDefinition) {
		assert cardDefinition != null : "CardDefinitions.addDefinition passed null";
		assert cardDefinition.getIndex() == -1 : "cardDefinition has been assigned index";

        cardDefinition.setIndex(cards.size());
        cards.add(cardDefinition);
		cardsMap.put(cardDefinition.getFullName(),cardDefinition);

        //add to default all (vintage) cube
        if (!cardDefinition.isToken()) {
			CubeDefinitions.getCubeDefinition("all").add(cardDefinition.getName());
        }
	}
        
    private static void addCardSpecificCode(final MagicCardDefinition cardDefinition) {
        //link to companion object containing static variables
        final String fname = cardDefinition.getFullName();
        final String cname = fname.replaceAll("[^A-Za-z]", "_");
        try { //reflection
            final Class c = Class.forName("magic.card." + cname);
            final Field[] fields = c.getDeclaredFields();
            for (final Field field : fields) {
                if (Modifier.isPublic(field.getModifiers())) {
                    cardDefinition.add(field.get(null));
                }
            }
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static MagicCardDefinition prop2carddef(final Properties content) {
	    final MagicCardDefinition cardDefinition=new MagicCardDefinition(content.getProperty("name"));

        //set type and subtype first
        setProperty(cardDefinition, "type", content.getProperty("type"));
        if (content.getProperty("subtype") != null) {
            setProperty(cardDefinition, "subtype", content.getProperty("subtype"));
        }

        //run through the list of properties
        for (String key : content.stringPropertyNames()) {
            setProperty(cardDefinition, key, content.getProperty(key));
        }

        return cardDefinition;
    }
	
    private static void loadCardDefinition(final File file) {
        System.err.println("Parsing " + file);
        final MagicCardDefinition cdef = prop2carddef(FileIO.toProp(file));
        checkCard(cdef);
        addDefinition(cdef);
    }
	
	public static void loadCardDefinitions() {
        //load all files in card directory
        final File cardDir = new File(MagicMain.getScriptsPath());
        final File[] files = cardDir.listFiles();
        for (File file : files) {
            loadCardDefinition(file);
        }

		filterCards();
		printStatistics();
		
		addDefinition(MagicCardDefinition.UNKNOWN);

		System.err.println(getNumberOfCards()+ " card definitions");
        MagicCardDefinition.printStatistics();
        
        // set card text
        loadCardTexts();
	}
	
	public static int getNumberOfCards() {
		return cards.size();
	}
	
    public static MagicCardDefinition getCard(final int cindex) {
        return cards.get(cindex);
    }
	
	public static MagicCardDefinition getCard(final String name) {
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
	
	public static void loadCardTexts() {
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
		
	public static MagicCardDefinition getBasicLand(final MagicColor color) {
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

	public static List<MagicCardDefinition> getCards() {
		return cards;
	}
	
	public static List<MagicCardDefinition> getLandCards() {
		return landCards;
	}
	
	public static List<MagicCardDefinition> getSpellCards() {
		return spellCards;
	}
	
	private static void printStatistics() {
		final CardStatistics statistics=new CardStatistics(cards);
		statistics.printStatictics(System.err);
	}
}

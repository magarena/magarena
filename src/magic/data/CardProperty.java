package magic.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import magic.exception.ScriptParseException;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicStaticType;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicStatic;

public enum CardProperty {

    IMAGE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setImageURL(value);
        }
    },
    VALUE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setValue(Double.parseDouble(value));
        }
    },
    REMOVAL() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setRemoval(Integer.parseInt(value));
        }
    },
    RARITY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setRarity(value.charAt(0));
        }
    },
    TYPE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] names=value.split(",");
            for (final String name : names) {
                card.addType(MagicType.getType(name));
            }
        }
    },
    SUBTYPE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setSubTypes(value.split(COMMA));
            card.setSubtypeText(value);
        }
    },
    COLOR() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setColors(value);
        }
    },
    COST() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setCost(MagicManaCost.create(value));
        }
    },
    MANA() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setManaSourceText(value);
        }
    },
    PT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] pt = value.split("/");
            final int p = pt[0].contains("*") ? 0 : Integer.parseInt(pt[0]);
            final int t = pt[1].contains("*") ? 0 : Integer.parseInt(pt[1]);
            card.setPowerToughness(p, t);
            card.setPowerToughnessText(value);
        }
    },
    ABILITY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setAbilityProperty(value);
        }
    },
    LOYALTY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setStartingLoyalty(Integer.parseInt(value));
        }
    },
    LOAD_ABILITY_COMMA() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] names=value.split(COMMA);
            for (final String name : names) {
                MagicAbility.getAbility(name).addAbility(card, name);
            }
        }
    },
    LOAD_ABILITY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] names=value.split(SEMI);
            for (final String name : names) {
                try {
                    MagicAbility.getAbility(name).addAbility(card, name);
                } catch (final ScriptParseException origPE) {
                    try {
                        LOAD_ABILITY_COMMA.setProperty(card, name);
                    } catch (final ScriptParseException newPE) {
                        throw origPE;
                    }
                }
            }
        }
    },
    SET_PT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] pt = value.split("/");
            card.add(MagicStatic.genPTSetStatic(Integer.parseInt(pt[0]), Integer.parseInt(pt[1])));
        }
    },
    GIVEN_ABILITY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.linkedABStatic(MagicAbility.getAbilityList(value.split(SEMI))));
        }
    },
    GIVEN_SUBTYPE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.genSTStatic(MagicSubType.getSubTypes(value.split(COMMA))));
        }
    },
    GIVEN_TYPE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.genTypeStatic(MagicType.getTypes(value.split(COMMA))));
        }
    },
    GIVEN_COLOR() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.AddLinkedColor(MagicColor.getFlags(value)));
        }
    },
    OVERWRITE_COLOR() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.SetLinkedColor(MagicColor.getFlags(value)));
        }
    },
    STATIC() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setStaticType(MagicStaticType.getStaticTypeFor(value));
        }
    },
    TIMING() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setTiming(MagicTiming.getTimingFor(value));
        }
    },
    IMAGE_UPDATED() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final SimpleDateFormat format = new SimpleDateFormat(IMAGE_UPDATED_FORMAT);
            try {
                card.setImageUpdated(format.parse(value));
            } catch (final ParseException pe) {
                throw new RuntimeException(pe);
            }
        }
    },
    MANA_OR_COMBAT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setExcludeManaOrCombat();
        }
    },
    ENCHANT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicPlayAuraEvent.create(value));
        }
    },
    TOKEN() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setToken();
            card.setName(value);
        }
    },
    NAME() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            assert card.getName() == null;
            assert card.getDistinctName() == null;
            final String[] names = value.split(SEMI);
            card.setName(names[0]);
            card.setDistinctName(names[names.length - 1]);
        }
    },
    EFFECT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setEffectProperty(value);
        }
    },
    LOAD_EFFECT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicSpellCardEvent.create(card, value));
        }
    },
    REQUIRES_GROOVY_CODE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setRequiresGroovy(value);
        }
    },
    LOAD_GROOVY_CODE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String cardName = !value.isEmpty() ? value : card.getDistinctName();
            final String[] names = cardName.split(SEMI);
            for (final String name : names) {
                CardDefinitions.addCardSpecificGroovyCode(card, name);
            }
        }
    },
    FLIP() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setFlipCardName(value);
        }
    },
    TRANSFORM() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setTransformCardName(value);
        }
    },
    MELD(){
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setMeldCardNames(value.split(SEMI));
        }
    },
    SPLIT() {
      public void setProperty(final MagicCardDefinition card, final String value) {
          card.setSplitCardName(value);
      }
    },
    SECOND_HALF() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setSecondHalf();
        }
    },
    ORACLE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setText(value);
        }
    },
    HIDDEN() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setHidden();
        }
    },
    OVERLAY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setOverlay();
        }
    },
    STATUS() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            //not tracked in game
        }
    },
    ;

    public static final String IMAGE_UPDATED_FORMAT = "yyyy-MM-dd";

    private static final String SEMI = "\\s*;\\s*";
    private static final String COMMA = "\\s*,\\s*";
    public void setProperty(final MagicCardDefinition card, final String value) {
        //do nothing
    }
}

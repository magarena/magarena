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
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setImageURL(value);
        }
    },
    VALUE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setValue(Double.parseDouble(value));
        }
    },
    REMOVAL() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setRemoval(Integer.parseInt(value));
        }
    },
    RARITY() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setRarity(value.charAt(0));
        }
    },
    TYPE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] names=value.split(",");
            for (final String name : names) {
                card.addType(MagicType.getType(name));
            }
        }
    },
    SUBTYPE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setSubTypes(value.split(COMMA));
            card.setSubTypeText(value);
        }
    },
    COLOR() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setColors(value);
        }
    },
    COST() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setCost(MagicManaCost.create(value));
        }
    },
    MANA() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setManaSourceText(value);
        }
    },
    PT() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] pt = value.split("/");
            final int p = pt[0].contains("*") ? 0 : Integer.parseInt(pt[0]);
            final int t = pt[1].contains("*") ? 0 : Integer.parseInt(pt[1]);
            card.setPowerToughness(p, t);
            card.setPowerToughnessText(value);
        }
    },
    ABILITY() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setAbilityProperty(value);
        }
    },
    LOYALTY() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setStartingLoyalty(Integer.parseInt(value));
        }
    },
    LOAD_ABILITY_COMMA() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] names=value.split(COMMA);
            for (final String name : names) {
                MagicAbility.getAbility(name).addAbility(card, name);
            }
        }
    },
    LOAD_ABILITY() {
        @Override
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
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] pt = value.split("/");
            card.add(MagicStatic.genPTSetStatic(Integer.parseInt(pt[0]), Integer.parseInt(pt[1])));
        }
    },
    GIVEN_ABILITY() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.linkedABStatic(MagicAbility.getAbilityList(value.split(SEMI))));
        }
    },
    GIVEN_SUBTYPE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.genSTStatic(MagicSubType.getSubTypes(value.split(COMMA))));
        }
    },
    GIVEN_TYPE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.genTypeStatic(MagicType.getTypes(value.split(COMMA))));
        }
    },
    GIVEN_COLOR() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.AddLinkedColor(MagicColor.getFlags(value)));
        }
    },
    OVERWRITE_COLOR() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicStatic.SetLinkedColor(MagicColor.getFlags(value)));
        }
    },
    STATIC() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setStaticType(MagicStaticType.getStaticTypeFor(value));
        }
    },
    TIMING() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setTiming(MagicTiming.getTimingFor(value));
        }
    },
    IMAGE_UPDATED() {
        @Override
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
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setExcludeManaOrCombat();
        }
    },
    ENCHANT() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicPlayAuraEvent.create(value));
        }
    },
    TOKEN() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setToken();
            card.setName(value);
        }
    },
    NAME() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            assert card.getName() == null;
            assert card.getDistinctName() == null;
            final String[] names = value.split(SEMI);
            card.setName(names[0]);
            card.setDistinctName(names[names.length - 1]);
        }
    },
    EFFECT() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setEffectProperty(value);
        }
    },
    LOAD_EFFECT() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicSpellCardEvent.create(card, value));
        }
    },
    REQUIRES_GROOVY_CODE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setRequiresGroovy(value);
        }
    },
    LOAD_GROOVY_CODE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String cardName = !value.isEmpty() ? value : card.getDistinctName();
            final String[] names = cardName.split(SEMI);
            for (final String name : names) {
                CardDefinitions.addCardSpecificGroovyCode(card, name);
            }
        }
    },
    FLIP() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setFlipCardName(value);
        }
    },
    TRANSFORM() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setTransformCardName(value);
        }
    },
    MELD(){
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setMeldCardNames(value.split(SEMI));
        }
    },
    SPLIT() {
      @Override
      public void setProperty(final MagicCardDefinition card, final String value) {
          card.setSplitCardName(value);
      }
    },
    SECOND_HALF() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setSecondHalf();
        }
    },
    ORACLE() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setText(value);
        }
    },
    HIDDEN() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setHidden();
        }
    },
    OVERLAY() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setOverlay();
        }
    },
    STATUS() {
        @Override
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setStatus(value);
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

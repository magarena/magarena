package magic.data;

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
            card.setPowerToughness(Integer.parseInt(pt[0]),Integer.parseInt(pt[1]));
        }
    },
    ABILITY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setAbilityProperty(value);
        }
    },
    LOAD_ABILITY() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] names=value.split(SEMI);
            for (final String name : names) {
                MagicAbility.getAbility(name).addAbility(card, name);
            }
        }
    },
    GIVEN_PT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] pt = value.replace('+','0').split("/");
            card.add(MagicStatic.genPTStatic(Integer.parseInt(pt[0]), Integer.parseInt(pt[1])));
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
            card.add(MagicStatic.genABStatic(MagicAbility.getAbilityList(value.split(SEMI))));
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
    IGNORE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String[] sizes=value.split(COMMA);
            for (final String size : sizes) {
                card.addIgnore(Long.parseLong(size));
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
            assert card.getFullName() == null;
            card.setName(value);
            card.setFullName(value);
        }
    },
    EFFECT() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.add(MagicSpellCardEvent.create(value));
        }
    },
    REQUIRES_GROOVY_CODE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setRequiresGroovy(value);
        }
    },
    LOAD_GROOVY_CODE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            final String cardName = !value.isEmpty() ? value : card.getFullName();
            final String[] names = cardName.split(SEMI);
            for (final String name : names) {
                CardDefinitions.addCardSpecificGroovyCode(card, name);
            }
        }
    },
    ORACLE() {
        public void setProperty(final MagicCardDefinition card, final String value) {
            card.setText(value);
        }
    }
    ;

    private static final String SEMI = "\\s*;\\s*";
    private static final String COMMA = "\\s*,\\s*";
    public void setProperty(final MagicCardDefinition card, final String value) {
        //do nothing
    }
}

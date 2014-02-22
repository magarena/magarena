package magic.model.event;

import magic.model.MagicPermanent;
import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.choice.MagicTargetChoice;
import java.util.List;
import java.util.LinkedList;

public enum MagicCostEvent {
            
    SacrificeSelf() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice SN");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificeEvent(source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    SacrificeArtifact() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice an artifact");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_ARTIFACT);
        }
    },
    SacrificeEnchantment() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice an enchantment");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_ENCHANTMENT);
        }
    },
    SacrificeAura() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice an aura");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_AURA);
        }
    },
    SacrificeCreature() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a creature");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_CREATURE);
        }
    },
    SacrificeGoblin() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Goblin");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_GOBLIN);
        }
    },
    SacrificeSaproling() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Saproling");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_SAPROLING);
        }
    },
    SacrificeBeast() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Beast");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_BEAST);
        }
    },
    SacrificeLand() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a land");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_LAND);
        }
    },
    SacrificeSwamp() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Swamp");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_SWAMP);
        }
    },
    SacrificeElf() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice an Elf");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_ELF);
        }
    },
    SacrificeBat() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Bat");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_BAT);
        }
    },
    SacrificeSamurai() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Samurai");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_SAMURAI);
        }
    },
    SacrificeSoldier() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Soldier");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_SOLDIER);
        }
    },
    SacrificeCleric() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Cleric");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_CLERIC);
        }
    },
    SacrificeHuman() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Human");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_HUMAN);
        }
    },
    SacrificeElemental() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice an Elemental");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_ELEMENTAL);
        }
    },
    SacrificeWall() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a Wall");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_WALL);
        }
    },
    SacrificePermanent() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice a permanent");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_PERMANENT);
        }
    },
    DiscardCard1() {
        public boolean accept(final String cost) {
            return cost.equals("Discard a card");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicDiscardEvent(source);
        }
    },
    DiscardCard2() {
        public boolean accept(final String cost) {
            return cost.equals("Discard two cards");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicDiscardEvent(source, 2);
        }
    },
    ExileSelf() {
        public boolean accept(final String cost) {
            return cost.equals("{E}");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicExileEvent(source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    TapSelf() {
        public boolean accept(final String cost) {
            return cost.equals("{T}");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicTapEvent(source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    UntapSelf() {
        public boolean accept(final String cost) {
            return cost.equals("{Q}");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicUntapEvent(source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    PayLife() {
        public boolean accept(final String cost) {
            return cost.contains("Pay ") && cost.contains(" life");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            final String costText=cost.replace("Pay ","").replace(" life","");
            return new MagicPayLifeEvent(source, Integer.parseInt(costText));
        }
    },
    Once() {
        public boolean accept(final String cost) {
            return cost.equals("{Once}");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicPlayAbilityEvent(source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    Sorcery() {
        public boolean accept(final String cost) {
            return cost.equals("{Sorcery}");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicSorceryConditionEvent(source);
        }
    },
    BounceSelf() {
        public boolean accept(final String cost) {
            return cost.equals("Return SN to its owner's hand");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicBouncePermanentEvent(source, source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    BounceLand() {
        public boolean accept(final String cost) {
            return cost.equals("Return a land you control to its owner's hand");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicBounceChosenPermanentEvent(source, MagicTargetChoice.LAND_YOU_CONTROL);
        }
    },
    RemoveCounter() {
        public boolean accept(final String cost) {
            return cost.contains("Remove ") && cost.contains(" counter") && cost.contains(" from SN");
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            final String[] costText = cost.replace("Remove ","").replace("\\scounter\\s|\\scounters\\s","").replace("from SN","").split(" ");
            final int amount = englishToInt(costText[0]);
            final String counterType = costText[1];
            return new MagicRemoveCounterEvent(source,MagicCounterType.getCounterRaw(counterType),amount);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    PayMana() {
        public boolean accept(final String cost) {
            return true;
        }
        public MagicEvent toEvent(final String cost, final MagicPermanent source) {
            return new MagicPayManaCostEvent(source, MagicManaCost.create(cost));
        }
    };

    public boolean isIndependent() {
        return true;
    }

    public abstract boolean accept(final String cost);
    
    public abstract MagicEvent toEvent(final String cost, final MagicPermanent source);
    
    public static int englishToInt(String num) {
        switch (num) {
            case "a": return 1;
            case "an": return 1;
            case "two": return 2;
            case "three" : return 3;
            case "four" : return 4;
            case "five" : return 5;
            case "six" : return 6;
            case "seven" : return 7;
            case "eight" : return 8;
            case "nine" : return 9;
            case "ten" : return 10;
            default: throw new RuntimeException("Unknown count: " + num);
        }
    }
    
    public static final MagicCostEvent build(final String cost) {
        for (final MagicCostEvent rule : values()) {
            if (rule.accept(cost)) {
                return rule;
            }
        }
        throw new RuntimeException("Unable to match " + cost + " to a rule");
    }
}

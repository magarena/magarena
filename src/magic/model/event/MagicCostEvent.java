package magic.model.event;


import magic.data.EnglishToInt;
import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicCostEvent {
            
    SacrificeSelf() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice SN");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicSacrificeEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    SacrificeAnotherCreature() {
        public boolean accept(final String cost) {
            return cost.equals("Sacrifice another creature");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicSacrificePermanentEvent(
                source,
                new MagicTargetChoice(
                    new MagicOtherPermanentTargetFilter(
                        MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                        (MagicPermanent)source
                    ),
                    "another creature to sacrifice"
                )
            );
        }
    },
    SacrificeChosen() {
        public boolean accept(final String cost) {
            return cost.startsWith("Sacrifice ") &&
                   toEvent(cost, MagicEvent.NO_SOURCE).isValid();
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            final String chosen = cost.replace("Sacrifice ", "") + " to sacrifice";
            return new MagicSacrificePermanentEvent(source, new MagicTargetChoice(chosen));
        }
    },
    DiscardSelf() {
        public boolean accept(final String cost) {
            return cost.equals("Discard SN");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicDiscardSelfEvent((MagicCard)source);
        }
    },
    DiscardCard1() {
        public boolean accept(final String cost) {
            return cost.equals("Discard a card");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicDiscardEvent(source);
        }
    },
    DiscardCard2() {
        public boolean accept(final String cost) {
            return cost.equals("Discard two cards");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicDiscardEvent(source, 2);
        }
    },
    DiscardCardRandom() {
        public boolean accept(final String cost) {
            return cost.equals("Discard a card at random");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return MagicDiscardEvent.Random(source);
        }
    },
    DiscardChosen() {
        public boolean accept(final String cost) {
            return cost.startsWith("Discard a") &&
                   toEvent(cost, MagicEvent.NO_SOURCE).isValid();
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            final String chosen = cost.replace("Discard ", "") + " from your hand";
            return new MagicDiscardChosenEvent(source, new MagicTargetChoice(chosen));
        }
    },
    ExileSelf() {
        public boolean accept(final String cost) {
            return cost.equals("Exile SN");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicExileEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    ExileGraveyardCreature() {
        public boolean accept(final String cost) {
            return cost.equals("Exile a creature card from your graveyard");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicExileCardEvent(source, MagicTargetChoice.A_CREATURE_CARD_FROM_GRAVEYARD);
        }
    },
    TapSelf() {
        public boolean accept(final String cost) {
            return cost.equals("{T}");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicTapEvent((MagicPermanent)source);
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
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicUntapEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    TapOther() {
        public boolean accept(final String cost) {
            return cost.startsWith("Tap an untapped ") &&
                   toEvent(cost, MagicEvent.NO_SOURCE).isValid();
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            final String chosen = cost.replace("Tap an untapped ", "");
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.singlePermanent(chosen);
            final MagicTargetChoice choice = new MagicTargetChoice(MagicTargetFilterFactory.UNTAPPED(filter), "an untapped " + chosen);
            return new MagicTapPermanentEvent(source, choice);
        }
    },
    PayLife() {
        public boolean accept(final String cost) {
            return cost.contains("Pay ") && cost.contains(" life");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            final String costText=cost.replace("Pay ","").replace(" life","");
            return new MagicPayLifeEvent(source, Integer.parseInt(costText));
        }
    },
    Once() {
        public boolean accept(final String cost) {
            return cost.equals("{Once}");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicPlayAbilityEvent((MagicPermanent)source);
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
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicConditionEvent(source, MagicCondition.SORCERY_CONDITION);
        }
    },
    Threshold() {
        public boolean accept(final String cost) {
            return cost.equals("{Threshold}");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicConditionEvent(source, MagicCondition.THRESHOLD_CONDITION);
        }
    },
    Hellbent() {
        public boolean accept(final String cost) {
            return cost.equals("{Hellbent}");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicConditionEvent(source, MagicCondition.HELLBENT);
        }
    },
    YourUpkeep() {
        public boolean accept(final String cost) {
            return cost.equals("{YourUpkeep}");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicConditionEvent(source, MagicCondition.YOUR_UPKEEP_CONDITION);
        }
    },
    YourTurn() {
        public boolean accept(final String cost) {
            return cost.equals("{YourTurn}");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicConditionEvent(source, MagicCondition.YOUR_TURN_CONDITION);
        }
    },
    BounceSelf() {
        public boolean accept(final String cost) {
            return cost.equals("Return SN to its owner's hand");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicBouncePermanentEvent(source, (MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    BounceChosen() {
        public boolean accept(final String cost) {
            return cost.startsWith("Return ") && cost.endsWith(" to its owner's hand") &&
                   toEvent(cost, MagicEvent.NO_SOURCE).isValid();
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            final String chosen = cost.replace("Return ", "").replace(" to its owner's hand", "");
            return new MagicBounceChosenPermanentEvent(source, new MagicTargetChoice(chosen));
        }
    },
    RemoveCounter() {
        public boolean accept(final String cost) {
            return cost.contains("Remove ") && cost.contains(" counter") && cost.contains(" from SN");
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            final String[] costText = cost.replace("Remove ","").replace("\\scounter\\s|\\scounters\\s","").replace("from SN","").split(" ");
            final int amount = EnglishToInt.convert(costText[0]);
            final String counterType = costText[1];
            return new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.getCounterRaw(counterType),amount);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    PayMana() {
        private final Pattern PATTERN=Pattern.compile("(\\{[A-Z\\d/]+\\})+");
        public boolean accept(final String cost) {
            final Matcher m = PATTERN.matcher(cost);
            return m.matches() && toEvent(cost, MagicEvent.NO_SOURCE).isValid();
        }
        public MagicEvent toEvent(final String cost, final MagicSource source) {
            return new MagicPayManaCostEvent(source, MagicManaCost.create(cost));
        }
    };

    public boolean isIndependent() {
        return true;
    }

    public abstract boolean accept(final String cost);
    
    public abstract MagicEvent toEvent(final String cost, final MagicSource source);
    
    public static final MagicCostEvent build(final String cost) {
        for (final MagicCostEvent rule : values()) {
            if (rule.accept(cost)) {
                return rule;
            }
        }
        throw new RuntimeException("unknown cost \"" + cost + "\"");
    }
}

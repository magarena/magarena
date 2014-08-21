package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicSource;
import magic.model.ARG;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicCostEvent {
            
    SacrificeSelf("Sacrifice SN") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicSacrificeEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    SacrificeAnotherCreature("Sacrifice another creature") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
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
    SacrificeChosen("Sacrifice " + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final String chosen = ARG.any(arg) + " to sacrifice";
            return new MagicSacrificePermanentEvent(source, new MagicTargetChoice(chosen));
        }
    },
    DiscardSelf("Discard SN") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicDiscardSelfEvent((MagicCard)source);
        }
    },
    DiscardCard1("Discard a card") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicDiscardEvent(source);
        }
    },
    DiscardCard2("Discard two cards") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicDiscardEvent(source, 2);
        }
    },
    DiscardCardRandom("Discard a card at random") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return MagicDiscardEvent.Random(source);
        }
    },
    DiscardChosen("Discard " + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final String chosen = ARG.any(arg) + " from your hand";
            return new MagicDiscardChosenEvent(source, new MagicTargetChoice(chosen));
        }
    },
    ExileSelf("Exile SN") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicExileEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    ExileCard("Exile " + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicExileCardEvent(source, new MagicTargetChoice(ARG.any(arg)));
        }
    },
    TapSelf("\\{T\\}") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicTapEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    UntapSelf("\\{Q\\}") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicUntapEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    TapOther("Tap an untapped " + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final String chosen = ARG.any(arg);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.singlePermanent(chosen);
            final MagicTargetChoice choice = new MagicTargetChoice(MagicTargetFilterFactory.untapped(filter), "an untapped " + chosen);
            return new MagicTapPermanentEvent(source, choice);
        }
    },
    PayLife("Pay " + ARG.NUMBER + " life") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicPayLifeEvent(source, ARG.number(arg));
        }
    },
    BounceSelf("Return SN to its owner's hand") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicBouncePermanentEvent(source, (MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    BounceChosen("Return " + ARG.ANY + " to its owner's hand") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicBounceChosenPermanentEvent(source, new MagicTargetChoice(ARG.any(arg)));
        }
    },
    RemoveCounterSelf("Remove " + ARG.AMOUNT + " " + ARG.WORD1 + " counter(s)? from SN") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amount = ARG.amount(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return new MagicRemoveCounterEvent((MagicPermanent)source, counterType, amount);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    RemoveCounterChosen("Remove a " + ARG.WORD1 + " counter from a creature you control") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return new MagicRemoveCounterChosenEvent(source, counterType);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    AddCounterSelf("Put " + ARG.AMOUNT + " " + ARG.WORD1 + " counter(s)? on SN") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amount = ARG.amount(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return new MagicAddCounterEvent((MagicPermanent)source, counterType, amount);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    PayMana("(\\{[A-Z\\d/]+\\})+") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicPayManaCostEvent(source, MagicManaCost.create(arg.group()));
        }
    };

    public boolean isIndependent() {
        return true;
    }
    
    private final Pattern pattern;
    
    private MagicCostEvent(final String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    
    public Matcher matcher(final String rule) {
        return pattern.matcher(rule);
    }
    
    public abstract MagicEvent toEvent(final Matcher arg, final MagicSource source);
    
    public static final MagicCostEvent build(final String cost) {
        for (final MagicCostEvent rule : values()) {
            final Matcher arg = rule.matcher(cost);
            if (arg.matches() && (arg.groupCount() == 0 || rule.toEvent(arg, MagicPermanent.NONE).isValid())) {
                return rule;
            }
        }
        throw new RuntimeException("unknown cost \"" + cost + "\"");
    }
}

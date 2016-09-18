package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.ARG;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicOtherCardTargetFilter;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicCostEvent {

    SacrificeSelf("Sacrifice (SN|this permanent|this land)") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicSacrificeEvent((MagicPermanent)source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    SacrificeMultiple("Sacrifice (?<another>another )?(" + ARG.AMOUNT + " )?" + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amt = ARG.amount(arg);
            final String chosen = MagicTargetFilterFactory.toSingular(ARG.any(arg)) + " you control";
            final MagicTargetFilter<MagicPermanent> regular = MagicTargetFilterFactory.Permanent(chosen);
            final MagicTargetFilter<MagicPermanent> filter = arg.group("another") != null ?
                new MagicOtherPermanentTargetFilter(regular, (MagicPermanent)source) :
                regular;
            final MagicTargetChoice choice = new MagicTargetChoice(
                filter,
                ("aeiou".indexOf(chosen.charAt(0)) >= 0 ? "an " : "a ") + chosen
            );
            return new MagicRepeatedPermanentsEvent(source, choice, amt, MagicChainEventFactory.Sac);
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
    BounceMultiple("Return ((?<another>another )|" + ARG.AMOUNT + " )?" + ARG.ANY + " to (their|its) owner's hand") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amt = ARG.amount(arg);
            final String chosen = MagicTargetFilterFactory.toSingular(ARG.any(arg));
            final MagicTargetFilter<MagicPermanent> regular = MagicTargetFilterFactory.Permanent(chosen);
            final MagicTargetFilter<MagicPermanent> filter = arg.group("another") != null ?
                new MagicOtherPermanentTargetFilter(regular, (MagicPermanent)source) :
                regular;
            final MagicTargetChoice choice = new MagicTargetChoice(
                filter,
                ("aeiou".indexOf(chosen.charAt(0)) >= 0 ? "an " : "a ") + chosen
            );
            return new MagicRepeatedPermanentsEvent(source, choice, amt, MagicChainEventFactory.Bounce);
        }
    },
    DiscardAll("Discard( all the cards in)? your hand") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicDiscardHandEvent(source);
        }
    },
    DiscardSelf("Discard SN") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicDiscardSelfEvent((MagicCard)source);
        }
    },
    DiscardCards("Discard " + ARG.AMOUNT + " card(s)?") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amount = ARG.amount(arg);
            return new MagicDiscardEvent(source, amount);
        }
    },
    DiscardCardsRandom("Discard " + ARG.AMOUNT + " card(s)? at random") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amount = ARG.amount(arg);
            return MagicDiscardEvent.Random(source, amount);
        }
    },
    DiscardChosen("Discard " + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final String chosen = ARG.any(arg) + " from your hand";
            final MagicTargetFilter<MagicCard> regular = MagicTargetFilterFactory.Card(chosen);
            final MagicTargetFilter<MagicCard> filter = (source instanceof MagicCard) ? new MagicOtherCardTargetFilter(regular, (MagicCard)source) : regular;
            return new MagicDiscardChosenEvent(source, new MagicTargetChoice(filter, chosen));
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
    ExileCardSelf("Exile SN from your graveyard") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicExileSelfEvent((MagicCard)source, MagicLocationType.Graveyard);
        }
    },
    ExileTopNCardsLibrary("Exile the top( " + ARG.AMOUNT + ")? card(s)? of your library") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicExileTopLibraryEvent(source, ARG.amount(arg));
        }
    },
    ExileCards("Exile " + ARG.AMOUNT + " (?<any>.*card.*)") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amt = ARG.amount(arg);
            final String chosen = MagicTargetFilterFactory.toSingular(ARG.any(arg));
            final MagicTargetFilter<MagicCard> regular = MagicTargetFilterFactory.Card(chosen);
            final MagicTargetFilter<MagicCard> filter = (source instanceof MagicCard) ? new MagicOtherCardTargetFilter(regular, (MagicCard)source) : regular;
            final MagicTargetChoice choice = new MagicTargetChoice(
                filter,
                ("aeiou".indexOf(chosen.charAt(0)) >= 0 ? "an " : "a ") + chosen
            );
            return new MagicRepeatedCardsEvent(
                source,
                choice,
                amt,
                MagicChainEventFactory.ExileCard
            );
        }
    },
    ExileMultiple("Exile ((?<another>another )|" + ARG.AMOUNT + " )?" + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amt = ARG.amount(arg);
            final String chosen = MagicTargetFilterFactory.toSingular(ARG.any(arg));
            final MagicTargetFilter<MagicPermanent> regular = MagicTargetFilterFactory.Permanent(chosen);
            final MagicTargetFilter<MagicPermanent> filter = arg.group("another") != null ?
                new MagicOtherPermanentTargetFilter(regular, (MagicPermanent)source) :
                regular;
            final MagicTargetChoice choice = new MagicTargetChoice(
                filter,
                ("aeiou".indexOf(chosen.charAt(0)) >= 0 ? "an " : "a ") + chosen
            );
            return new MagicRepeatedPermanentsEvent(source, choice, amt, MagicChainEventFactory.ExilePerm);
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
    TapMultiple("Tap (?<another>another )?(" + ARG.AMOUNT + " )?untapped " + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amt = ARG.amount(arg);
            final String chosen = MagicTargetFilterFactory.toSingular(ARG.any(arg));
            final MagicTargetFilter<MagicPermanent> untapped = MagicTargetFilterFactory.untapped(MagicTargetFilterFactory.Permanent(chosen));
            final MagicTargetFilter<MagicPermanent> filter = arg.group("another") != null ?
                new MagicOtherPermanentTargetFilter(untapped, (MagicPermanent)source) :
                untapped;
            final MagicTargetChoice choice = new MagicTargetChoice(filter, "an untapped " + chosen);
            return new MagicRepeatedPermanentsEvent(source, choice, amt, MagicChainEventFactory.Tap);
        }
    },
    UntapMultiple("Untap (?<another>another )?(" + ARG.AMOUNT + " )?tapped " + ARG.ANY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final int amt = ARG.amount(arg);
            final String chosen = MagicTargetFilterFactory.toSingular(ARG.any(arg));
            final MagicTargetFilter<MagicPermanent> tapped = MagicTargetFilterFactory.tapped(MagicTargetFilterFactory.Permanent(chosen));
            final MagicTargetFilter<MagicPermanent> filter = arg.group("another") != null ?
                new MagicOtherPermanentTargetFilter(tapped, (MagicPermanent)source) :
                tapped;
            final MagicTargetChoice choice = new MagicTargetChoice(filter, "a tapped " + chosen);
            return new MagicRepeatedPermanentsEvent(source, choice, amt, MagicChainEventFactory.Untap);
        }
    },
    PayLife("Pay " + ARG.NUMBER + " life") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicPayLifeEvent(source, ARG.number(arg));
        }
    },
    PayEnergy("Pay " + ARG.ENERGY) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicPayEnergyEvent(source, ARG.energy(arg));
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
    AddCounterChosen("Put a " + ARG.WORD1 + " counter on a creature you control") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return new MagicAddCounterChosenEvent(source, counterType);
        }
    },
    PayMana("(pay )?" + ARG.MANACOST) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicPayManaCostEvent(source, MagicManaCost.create(ARG.manacost(arg)));
        }
    },
    AltManaCost("(pay )?alt mana cost " + ARG.MANACOST) {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            final MagicManaCost origCost = MagicManaCost.create(ARG.manacost(arg));
            final MagicManaCost cost = origCost == MagicManaCost.ZERO ? MagicManaCost.NONE : origCost;
            return (source instanceof MagicCard) ?
                MagicPayManaCostEvent.Cast((MagicCard)source, cost) :
                new MagicPayManaCostEvent(source, cost);
        }
    },
    DamageYou("SN deals " + ARG.NUMBER + " damage to you") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return MagicRuleEventAction.create(arg.group()).getEvent(source);
        }
    },
    DoesntUntap("SN doesn't untap during your next untap step") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return MagicRuleEventAction.create(arg.group()).getEvent(source);
        }
        @Override
        public boolean isIndependent() {
            return false;
        }
    },
    MillSelf("Put the top( " + ARG.AMOUNT + ")? card(s)? of your library into your graveyard") {
        public MagicEvent toEvent(final Matcher arg, final MagicSource source) {
            return new MagicMillEvent(source, ARG.amount(arg));
        }
    }
    ;

    public boolean isIndependent() {
        return true;
    }

    private final Pattern pattern;

    private MagicCostEvent(final String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public Matcher matched(final String rule) {
        final Matcher matcher = pattern.matcher(rule);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new RuntimeException("unknown cost: \"" + rule + "\"");
        }
        return matcher;
    }

    private Matcher matcher(final String rule) {
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

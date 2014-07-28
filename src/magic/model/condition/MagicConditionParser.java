package magic.model.condition;

import magic.model.ARG;
import magic.model.MagicCounterType;
import magic.model.target.MagicTargetFilterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicConditionParser {
            
    YouControl("you control a(n)? " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.YouControl(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    OpponentControl("an opponent controls a(n)? " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.OpponentControl(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    DefenderControl("defending player controls a(n)? " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.OpponentControl(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    YouControlAnother("you control another " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.YouControlAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    Threshold("seven or more cards are in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.THRESHOLD_CONDITION;
        }
    },
    HandSize("you have " + ARG.AMOUNT + " or more cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.amount(arg);
            return MagicConditionFactory.HandAtLeast(amount);
        }
    },
    QuestCounters("SN has " + ARG.AMOUNT + " or more quest counters on it") {
      public MagicCondition toCondition(final Matcher arg) {
          final int amount = ARG.amount(arg);
          return MagicConditionFactory.CounterAtLeast(MagicCounterType.Quest, amount);
      }
    },
    Metalcraft("you control three or more artifacts") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.METALCRAFT_CONDITION;
        }
    },
    NoUntappedLands("you control no untapped lands") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.NO_UNTAPPED_LANDS_CONDITION;
        }
    },
    EightOrMoreLands("you control eight or more lands") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.EIGHT_OR_MORE_LANDS;
        }
    },
    Hellbent("you have no cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HELLBENT;
        }
    },
    OpponentHellbent("an opponent has no cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPPONENT_HELLBENT;
        }
    },
    IsEquipped("SN is equipped") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_EQUIPPED;
        }
    },
    IsEnchanted("(SN is|it's) enchanted") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_ENCHANTED;
        }
    },
    IsUntapped("(SN is|it's) untapped") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.UNTAPPED_CONDITION;
        }
    },
    IsMonstrous("SN is monstrous") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_MONSTROUS_CONDITION;
        }
    },
    IsAttacking("it's attacking") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_ATTACKING_CONDITION;
        }
    },
    AttackingAlone("it's attacking alone") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_ATTACKING_ALONE_CONDITION;
        }
    },
    NoCardsInGraveyard("there are no cards in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.EMPTY_GRAVEYARD_CONDITION;
        }
    },
    LibraryWithLEQ20Cards("a library has twenty or fewer cards in it") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.LIBRARY_HAS_20_OR_LESS_CARDS_CONDITION;
        }
    },
    OpponentGraveyardWithGEQ10Cards("an opponent has ten or more cards in his or her graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPP_GRAVEYARD_WITH_10_OR_MORE_CARDS_CONDTITION;
        }
    },
    OpponentNotControlWhiteOrBlueCreature("no opponent controls a white or blue creature") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPP_NOT_CONTROL_WHITE_OR_BLUE_CREATURE_CONDITION;
        }
    },
    MostCardsInHand("you have more cards in hand than each opponent") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.MOST_CARDS_IN_HAND_CONDITION;
        }
    },
    NoShellCounters("SN has no shell counters on it") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.NO_SHELL_COUNTERS_CONDITION;
        }
    },
    HasMinusOneCounter("it has a -1/-1 counter on it") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HAS_MINUSONE_COUNTER_CONDITION;
        }
    },
    HasPlusOneCounter("it has a \\+1/\\+1 counter on it") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HAS_PLUSONE_COUNTER_CONDITION;
        }
    },
    WarriorCardInGraveyard("a Warrior card is in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HAS_WARRIOR_IN_GRAVEYARD;
        }
    },
    ArtifactCardInGraveyard("an artifact card is in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HAS_ARTIFACT_IN_GRAVEYARD;
        }
    },
    NoOpponentCreatures("no opponent controls a creature") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPP_NOT_CONTROL_CREATURE_CONDITION;
        }
    },
    IsYourTurn("it's your turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOUR_TURN_CONDITION;
        }
    },
    IsNotYourTurn("it's not your turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.NOT_YOUR_TURN_CONDITION;
        }
    },
    You30LifeOrMore("you have 30 or more life") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOU_30_OR_MORE_LIFE;
        }
    },
    You30LifeOrMoreOpponent10LifeOrLess("you have 30 or more life and an opponent has 10 or less life") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOU_30_OR_MORE_OPPPONENT_10_OR_LESS_LIFE;
        }
    },
    HasTenPlusOneCounter("it has ten or more \\+1/\\+1 counters on it") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HAS_TEN_PLUSONE_COUNTER_CONDITION;
        }
    },
    OpponentTenLifeOrLess("an opponent has 10 or less life") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPPONENT_TEN_OR_LESS_LIFE;
        }
    },
    You25LifeOrMore("you have 25 or more life") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOU_25_OR_MORE_LIFE;
        }
    },
    NotContolNonartifactNonwhiteCreature("you control no nonartifact, nonwhite creatures") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.NOT_CONTROL_NONARTIFACT_NONWHITE_CREATURE_CONDITION;
        }
    }
    ;

    private final Pattern pattern;
    
    private MagicConditionParser(final String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    
    public Matcher matcher(final String rule) {
        return pattern.matcher(rule);
    }

    public abstract MagicCondition toCondition(final Matcher arg);
    
    public static final MagicCondition build(final String cost) {
        for (final MagicConditionParser rule : values()) {
            final Matcher matcher = rule.matcher(cost);
            if (matcher.matches()) {
                return rule.toCondition(matcher);
            }
        }
        throw new RuntimeException("unknown condition \"" + cost + "\"");
    }
}

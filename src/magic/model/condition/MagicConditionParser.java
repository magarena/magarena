package magic.model.condition;

import magic.model.ARG;
import magic.model.MagicSource;
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
    Metalcraft("you control three or more artifacts") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.METALCRAFT_CONDITION;
        }
    },
    Hellbent("you have no cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HELLBENT;
        }
    },
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
        throw new RuntimeException("Unable to match " + cost + " to a condition");
    }
}

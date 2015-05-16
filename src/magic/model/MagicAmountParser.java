package magic.model;

import magic.model.ARG;
import magic.model.MagicCounterType;
import magic.model.MagicAbility;
import magic.model.target.MagicTargetFilterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicAmountParser {
            
    YourLife("your life total") {
        public MagicAmount toAmount(final Matcher arg) {
            return new MagicAmount() {
                @Override
                public int getAmount(final MagicSource source) {
                    return source.getController().getLife();
                }
            };
        }
    },
    Domain("basic land type(s)? among lands you control") {
        public MagicAmount toAmount(final Matcher arg) {
            return MagicAmountFactory.Domain;
        }
    },
    Equipment("Equipment attached to (it|SN)") {
        public MagicAmount toAmount(final Matcher arg) {
            return MagicAmountFactory.Equipment;
        }
    },
    Aura("Aura attached to (it|SN)") {
        public MagicAmount toAmount(final Matcher arg) {
            return MagicAmountFactory.Aura;
        }
    },
    ItsPower("its power") {
        public MagicAmount toAmount(final Matcher arg) {
            return MagicAmountFactory.SN_Power;
        }
    },
    CounterOnSN(ARG.WORD1 + " counter(s)? on (it|SN)") {
        public MagicAmount toAmount(final Matcher arg) {
            return MagicAmountFactory.CounterOnSource(
                MagicCounterType.getCounterRaw(ARG.word1(arg))
            );
        }
    },
    FromFilter(ARG.ANY) {
        public MagicAmount toAmount(final Matcher arg) {
            return MagicAmountFactory.FromFilter(
                MagicTargetFilterFactory.Target(ARG.any(arg))
            );
        }
    };
    
    private final Pattern pattern;
    
    private MagicAmountParser(final String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    
    public Matcher matcher(final String rule) {
        return pattern.matcher(rule);
    }

    public abstract MagicAmount toAmount(final Matcher arg);
    
    public static final MagicAmount build(final String text) {
        if (text == null || text.isEmpty()) {
            return MagicAmountFactory.One;
        }
        final String cleaned = text.replaceAll("^the number of ", "");
        for (final MagicAmountParser rule : values()) {
            final Matcher matcher = rule.matcher(cleaned);
            if (matcher.matches()) {
                return rule.toAmount(matcher);
            }
        }
        throw new RuntimeException("unknown amount \"" + text + "\"");
    }
} 

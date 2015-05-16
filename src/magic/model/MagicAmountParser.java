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
    FromFilter("(the number of )?" + ARG.ANY) {
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
        for (final MagicAmountParser rule : values()) {
            final Matcher matcher = rule.matcher(text);
            if (matcher.matches()) {
                return rule.toAmount(matcher);
            }
        }
        throw new RuntimeException("unknown amount \"" + text + "\"");
    }
} 

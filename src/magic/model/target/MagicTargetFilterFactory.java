package magic.model.target;

import java.util.Map;
import java.util.HashMap;

import magic.model.MagicPermanent;
import magic.model.MagicColor;
import magic.model.MagicSubType;

import static magic.model.target.MagicTargetFilter.*;

public class MagicTargetFilterFactory {
    
    private static final Map<String, MagicTargetFilter<MagicPermanent>> factory =
        new HashMap<String, MagicTargetFilter<MagicPermanent>>();

    static {
        factory.put("creatures you control", TARGET_CREATURE_YOU_CONTROL);
        factory.put("creatures your opponents control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        factory.put("creatures with flying your opponents control", TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        factory.put("creatures you control with flying", TARGET_CREATURE_WITH_FLYING_YOU_CONTROL);
        factory.put("artifact creatures you control", TARGET_ARTIFACT_CREATURE_YOU_CONTROL);
        factory.put("non-human creatures you control", TARGET_NONHUMAN_CREATURE_YOU_CONTROL);
        factory.put("attacking creatures you control", TARGET_ATTACKING_CREATURE_YOU_CONTROL);
        factory.put("creature tokens you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        
        factory.put("faeries you control", TARGET_FAERIE_YOU_CONTROL);
        factory.put("each wolf you control", TARGET_WOLF_YOU_CONTROL);

        factory.put("all sliver creatures", TARGET_SLIVER);
        factory.put("all creatures", TARGET_CREATURE);

        factory.put("nonblack creatures", TARGET_NONBLACK_CREATURE);
        
        factory.put("permanents you control", TARGET_PERMANENT_YOU_CONTROL);
        factory.put("artifacts you control", TARGET_ARTIFACT_YOU_CONTROL);
    }

    public static MagicTargetFilter<MagicPermanent> build(final String arg) {
        if (factory.containsKey(arg)) {
            return factory.get(arg);
        } else if (arg.endsWith(" creatures you control")) {
            return matchPrefix(arg, " creatures you control", Control.You);
        } else if (arg.endsWith(" creatures your opponents control")) {
            return matchPrefix(arg, " creatures your opponents control", Control.Opp);
        } else if (arg.endsWith(" creatures")) {
            return matchPrefix(arg, " creatures", Control.Any);
        } else {
            throw new RuntimeException("unknown target filter \"" + arg + "\"");
        }
    }

    private static MagicTargetFilter<MagicPermanent> matchPrefix(final String arg, final String suffix, final Control control) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return Factory.creature(c, control);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return Factory.creature(st, control);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
}

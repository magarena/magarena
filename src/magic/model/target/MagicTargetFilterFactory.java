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
    
    private static final Map<String, MagicTargetFilter<?>> single =
        new HashMap<String, MagicTargetFilter<?>>();

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
       
        single.put("creature", TARGET_CREATURE);
        single.put("land", TARGET_LAND);
        single.put("land you control", TARGET_LAND_YOU_CONTROL);
        single.put("creature you control", TARGET_CREATURE_YOU_CONTROL);
        single.put("red or green creature you control", TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL);
        single.put("creature your opponents control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        single.put("creature without flying you don't control", MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("creature you don't control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        single.put("artifact", TARGET_ARTIFACT);
        single.put("artifact your opponents control", TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS);
        single.put("artifact you don't control", TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS);
        single.put("artifact you control", TARGET_ARTIFACT_YOU_CONTROL);
        single.put("creature token you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        single.put("planeswalker your opponents control", TARGET_PLANESWALKER_YOUR_OPPONENT_CONTROLS);
        single.put("unpaired Soulbond creature", TARGET_UNPAIRED_SOULBOND_CREATURE);
        single.put("nonland permanent", TARGET_NONLAND_PERMANENT);
        single.put("nonland permanent your opponents control", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent you don't control", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("noncreature artifact", TARGET_NONCREATURE_ARTIFACT);
        single.put("artifact or enchantment", TARGET_ARTIFACT_OR_ENCHANTMENT);
        single.put("enchantment you control", TARGET_ENCHANTMENT_YOU_CONTROL);
        single.put("artifact or creature", TARGET_ARTIFACT_OR_CREATURE);
        single.put("permanent you control", TARGET_PERMANENT_YOU_CONTROL);
        single.put("multicolored permanent you control", TARGET_MULTICOLORED_PERMANENT_YOU_CONTROL);
        single.put("Mountain you control", TARGET_MOUNTAIN_YOU_CONTROL);
        single.put("Forest you control", TARGET_FOREST_YOU_CONTROL);
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
    
    public static MagicTargetFilter<?> single(final String arg) {
        final String filter = arg.replaceFirst("^target |^a |^an ", "").replaceFirst(" to sacrifice$", " you control");
        if (single.containsKey(filter)) {
            return single.get(filter);
        } else if (filter.endsWith(" creature you control")) {
            return matchPrefix(filter, " creature you control", Control.You);
        } else if (filter.endsWith(" creature your opponents control")) {
            return matchPrefix(filter, " creature your opponents control", Control.Opp);
        } else if (filter.endsWith(" creature")) {
            return matchPrefix(filter, " creature", Control.Any);
        } else {
            throw new RuntimeException("unknown target filter \"" + filter + "\"");
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

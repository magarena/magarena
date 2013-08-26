package magic.model.target;

import java.util.Map;
import java.util.HashMap;

import magic.model.MagicPermanent;

import static magic.model.target.MagicTargetFilter.*;

public class MagicTargetFilterFactory {
    
    private static final Map<String, MagicTargetFilter<MagicPermanent>> factory =
        new HashMap<String, MagicTargetFilter<MagicPermanent>>();

    static {
        factory.put("creatures you control", TARGET_CREATURE_YOU_CONTROL);
        factory.put("creatures your opponents control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        factory.put("sliver creatures you control", TARGET_SLIVER_YOU_CONTROL);
        factory.put("dragon creatures you control", TARGET_DRAGON_YOU_CONTROL);
        factory.put("all sliver creatures", TARGET_SLIVER);
        factory.put("all creatures", TARGET_CREATURE);
        factory.put("goblin creatures", TARGET_GOBLIN_CREATURE);
        factory.put("black creatures", TARGET_BLACK_CREATURE);
        factory.put("white creatures you control", TARGET_WHITE_CREATURE_YOU_CONTROL);
    }

    public static MagicTargetFilter<MagicPermanent> build(final String arg) {
        if (factory.containsKey(arg)) {
            return factory.get(arg);
        } else {
            throw new RuntimeException("unknown target filter \"" + arg + "\"");
        }
    }
}

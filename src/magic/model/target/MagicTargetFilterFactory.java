package magic.model.target;

import java.util.Map;
import java.util.HashMap;

import magic.model.MagicPermanent;

import static magic.model.target.MagicTargetFilter.*;

public class MagicTargetFilterFactory {
    
    private static final Map<String, MagicTargetFilter<MagicPermanent>> factory =
        new HashMap<String, MagicTargetFilter<MagicPermanent>>();

    static {
        factory.put("sliver creatures you control", TARGET_SLIVER_YOU_CONTROL);
        factory.put("all sliver creatures", TARGET_SLIVER);
        factory.put("goblin creatures", TARGET_GOBLIN_CREATURE);
    }

    public static MagicTargetFilter<MagicPermanent> build(final String arg) {
        if (factory.containsKey(arg)) {
            return factory.get(arg);
        } else {
            throw new RuntimeException("unknown target filter \"" + arg + "\"");
        }
    }
}

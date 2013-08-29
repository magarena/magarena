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
        factory.put("creatures with flying your opponents control", TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        factory.put("creatures you control with flying", TARGET_CREATURE_WITH_FLYING_YOU_CONTROL);
        factory.put("sliver creatures you control", TARGET_SLIVER_YOU_CONTROL);
        factory.put("golem creatures you control", TARGET_GOLEM_YOU_CONTROL);
        factory.put("dragon creatures you control", TARGET_DRAGON_YOU_CONTROL);
        factory.put("knight creatures you control", TARGET_KNIGHT_YOU_CONTROL);
        factory.put("soldier creatures you control", TARGET_SOLDIER_YOU_CONTROL);
        factory.put("illusion creatures you control", TARGET_ILLUSION_YOU_CONTROL);
        factory.put("faerie creatures you control", TARGET_FAERIE_CREATURE_YOU_CONTROL);
        factory.put("merfolk creatures you control", TARGET_MERFOLK_YOU_CONTROL);
        factory.put("snake creatures you control", TARGET_SNAKE_YOU_CONTROL);
        factory.put("elf creatures you control", TARGET_ELF_YOU_CONTROL);
        factory.put("vampire creatures you control", TARGET_VAMPIRE_YOU_CONTROL);
        factory.put("zombie creatures you control", TARGET_ZOMBIE_YOU_CONTROL);
        factory.put("spirit creatures you control", TARGET_SPIRIT_YOU_CONTROL);
        factory.put("goblin creatures you control", TARGET_GOBLIN_YOU_CONTROL);
        factory.put("myr creatures you control", TARGET_MYR_YOU_CONTROL);
        factory.put("kithkin creatures you control", TARGET_KITHKIN_YOU_CONTROL);
        factory.put("artifact creatures you control", TARGET_ARTIFACT_CREATURE_YOU_CONTROL);
        factory.put("non-human creatures you control", TARGET_NONHUMAN_CREATURE_YOU_CONTROL);
        factory.put("attacking creatures you control", TARGET_ATTACKING_CREATURE_YOU_CONTROL);
        factory.put("creature tokens you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        
        factory.put("faeries you control", TARGET_FAERIE_YOU_CONTROL);
        factory.put("each wolf you control", TARGET_WOLF_YOU_CONTROL);

        factory.put("all sliver creatures", TARGET_SLIVER);
        factory.put("all creatures", TARGET_CREATURE);
        factory.put("goblin creatures", TARGET_GOBLIN_CREATURE);
        factory.put("merfolk creatures", TARGET_MERFOLK_CREATURE);
        factory.put("zombie creatures", TARGET_ZOMBIE);
        factory.put("myr creatures", TARGET_MYR_CREATURE);
        factory.put("squirrel creatures", TARGET_SQUIRREL_CREATURE);
        factory.put("griffin creatures", TARGET_GRIFFIN);
        factory.put("bird creatures", TARGET_BIRD);
        factory.put("soldier creatures", TARGET_SOLDIER);
        factory.put("elf creatures", TARGET_ELF);

        factory.put("white creatures", TARGET_WHITE_CREATURE);
        factory.put("blue creatures", TARGET_BLUE_CREATURE);
        factory.put("black creatures", TARGET_BLACK_CREATURE);
        factory.put("nonblack creatures", TARGET_NONBLACK_CREATURE);
        factory.put("red creatures", TARGET_RED_CREATURE);
        factory.put("green creatures", TARGET_GREEN_CREATURE);
        
        factory.put("white creatures you control", TARGET_WHITE_CREATURE_YOU_CONTROL);
        factory.put("blue creatures you control", TARGET_BLUE_CREATURE_YOU_CONTROL);
        factory.put("black creatures you control", TARGET_BLACK_CREATURE_YOU_CONTROL);
        factory.put("red creatures you control", TARGET_RED_CREATURE_YOU_CONTROL);
        factory.put("green creatures you control", TARGET_GREEN_CREATURE_YOU_CONTROL);
        
        factory.put("permanents you control", TARGET_PERMANENT_YOU_CONTROL);
        factory.put("artifacts you control", TARGET_ARTIFACT_YOU_CONTROL);
    }

    public static MagicTargetFilter<MagicPermanent> build(final String arg) {
        if (factory.containsKey(arg)) {
            return factory.get(arg);
        } else {
            throw new RuntimeException("unknown target filter \"" + arg + "\"");
        }
    }
}

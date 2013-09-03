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
       
        single.put("opponent", TARGET_OPPONENT);
        single.put("player", TARGET_PLAYER);
        single.put("1/1 creature", TARGET_1_1_CREATURE);
        single.put("creature", TARGET_CREATURE);
        single.put("nonblack creature", TARGET_NONBLACK_CREATURE);
        single.put("non-Angel creature you control", TARGET_NON_ANGEL_CREATURE_YOU_CONTROL);
        single.put("noncreature", TARGET_NONCREATURE);
        single.put("land", TARGET_LAND);
        single.put("land or nonblack creature", TARGET_LAND_OR_NONBLACK_CREATURE);
        single.put("non basic land", TARGET_NONBASIC_LAND);
        single.put("spell", TARGET_SPELL);
        single.put("spell or permanent", TARGET_SPELL_OR_PERMANENT);
        single.put("spell you don't control", TARGET_SPELL_YOU_DONT_CONTROL);
        single.put("spell with converted mana cost 2", TARGET_SPELL_WITH_CMC_EQ_2);
        single.put("spell that targets a player", TARGET_SPELL_THAT_TARGETS_PLAYER);
        single.put("spell with {X} in its mana cost", TARGET_SPELL_WITH_X_COST);
        single.put("creature spell", TARGET_CREATURE_SPELL);
        single.put("noncreature spell", TARGET_NONCREATURE_SPELL);
        single.put("artifact spell", TARGET_ARTIFACT_SPELL);
        single.put("red or green spell", TARGET_RED_GREEN_SPELL);
        single.put("instant or sorcery spell", TARGET_INSTANT_OR_SORCERY_SPELL);
        single.put("instant spell", TARGET_INSTANT_SPELL);
        single.put("land you control", TARGET_LAND_YOU_CONTROL);
        single.put("creature you control", TARGET_CREATURE_YOU_CONTROL);
        single.put("red or green creature you control", TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL);
        single.put("creature your opponents control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        single.put("creature without flying you don't control", MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("creature you don't control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        single.put("Equipment", TARGET_EQUIPMENT);
        single.put("artifact", TARGET_ARTIFACT);
        single.put("artifact creature", TARGET_ARTIFACT_CREATURE);
        single.put("nonartifact creature", TARGET_NONARTIFACT_CREATURE);
        single.put("nonartifact, nonblack creature", TARGET_NONARTIFACT_NONBLACK_CREATURE);
        single.put("tapped creature", TARGET_TAPPED_CREATURE);
        single.put("untapped creature", TARGET_UNTAPPED_CREATURE);
        single.put("artifact or land", TARGET_ARTIFACT_OR_LAND);
        single.put("artifact or enchantment", TARGET_ARTIFACT_OR_ENCHANTMENT);
        single.put("artifact, enchantment, or land", TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND);
        single.put("artifact or enchantment your opponents control", TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS);
        single.put("artifact or creature", TARGET_ARTIFACT_OR_CREATURE);
        single.put("artifact, creature, or land", TARGET_ARTIFACT_OR_CREATURE_OR_LAND);
        single.put("artifact your opponents control", TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS);
        single.put("artifact you don't control", TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS);
        single.put("artifact you control", TARGET_ARTIFACT_YOU_CONTROL);
        single.put("creature token you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        single.put("planeswalker your opponents control", TARGET_PLANESWALKER_YOUR_OPPONENT_CONTROLS);
        single.put("unpaired Soulbond creature", TARGET_UNPAIRED_SOULBOND_CREATURE);
        single.put("nonland permanent", TARGET_NONLAND_PERMANENT);
        single.put("nonland permanent your opponents control", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent you don't control", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent with converted mana cost 3 or less", TARGET_NONLAND_PERMANENT_CMC_LEQ_3);
        single.put("noncreature artifact", TARGET_NONCREATURE_ARTIFACT);
        single.put("Spirit or enchantment", TARGET_SPIRIT_OR_ENCHANTMENT);
        single.put("enchantment", TARGET_ENCHANTMENT);
        single.put("enchantment you control", TARGET_ENCHANTMENT_YOU_CONTROL);
        single.put("enchantment your opponents control", TARGET_ENCHANTMENT_YOUR_OPPONENT_CONTROLS);
        single.put("permanent you control", TARGET_PERMANENT_YOU_CONTROL);
        single.put("permanent you own", TARGET_PERMANENT_YOU_OWN);
        single.put("permanent an opponent controls", TARGET_PERMANENT_AN_OPPONENT_CONTROLS);
        single.put("permanent", TARGET_PERMANENT);
        single.put("black permanent", TARGET_BLACK_PERMANENT);
        single.put("black or red permanent", TARGET_BLACK_RED_PERMANENT);
        single.put("multicolored permanent you control", TARGET_MULTICOLORED_PERMANENT_YOU_CONTROL);
        single.put("Mountain you control", TARGET_MOUNTAIN_YOU_CONTROL);
        single.put("Forest you control", TARGET_FOREST_YOU_CONTROL);

        single.put("artifact card from your hand", TARGET_ARTIFACT_CARD_FROM_HAND);
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

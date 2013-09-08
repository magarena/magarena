package magic.model.target;

import java.util.Map;
import java.util.TreeMap;

import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicSubType;
import magic.model.MagicType;

import static magic.model.target.MagicTargetFilter.*;

public class MagicTargetFilterFactory {
    
    private static final Map<String, MagicTargetFilter<MagicPermanent>> factory =
        new TreeMap<String, MagicTargetFilter<MagicPermanent>>(String.CASE_INSENSITIVE_ORDER);
    
    private static final Map<String, MagicTargetFilter<?>> single =
        new TreeMap<String, MagicTargetFilter<?>>(String.CASE_INSENSITIVE_ORDER);

    static {
        // used by lord ability
        factory.put("lands you control", TARGET_LAND_YOU_CONTROL);
        factory.put("permanents you control", TARGET_PERMANENT_YOU_CONTROL);
        factory.put("artifacts you control", TARGET_ARTIFACT_YOU_CONTROL);
        factory.put("creatures you control", TARGET_CREATURE_YOU_CONTROL);
        factory.put("creatures your opponents control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        factory.put("creatures with flying your opponents control", TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        factory.put("creatures you control with flying", TARGET_CREATURE_WITH_FLYING_YOU_CONTROL);
        factory.put("non-human creatures you control", TARGET_NONHUMAN_CREATURE_YOU_CONTROL);
        factory.put("attacking creatures you control", TARGET_ATTACKING_CREATURE_YOU_CONTROL);
        factory.put("creature tokens you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        
        factory.put("faeries you control", TARGET_FAERIE_YOU_CONTROL);
        factory.put("each wolf you control", TARGET_WOLF_YOU_CONTROL);

        factory.put("all sliver creatures", TARGET_SLIVER);
        factory.put("all creatures", TARGET_CREATURE);
        factory.put("nonblack creatures", TARGET_NONBLACK_CREATURE);
        factory.put("artifacts", TARGET_ARTIFACT);
       
        // used by MagicTargetChoice
        single.put("opponent", TARGET_OPPONENT);
        single.put("player", TARGET_PLAYER);
        single.put("1/1 creature", TARGET_1_1_CREATURE);
        single.put("nonblack creature", TARGET_NONBLACK_CREATURE);
        single.put("non-Angel creature you control", TARGET_NON_ANGEL_CREATURE_YOU_CONTROL);
        single.put("noncreature", TARGET_NONCREATURE);
        single.put("noncreature permanent", TARGET_NONCREATURE);
        single.put("land or nonblack creature", TARGET_LAND_OR_NONBLACK_CREATURE);
        single.put("non basic land", TARGET_NONBASIC_LAND);
        single.put("nonbasic land", TARGET_NONBASIC_LAND);
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
        single.put("red or green creature you control", TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL);
        single.put("creature without flying you don't control", MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
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
        single.put("creature token you control", TARGET_CREATURE_TOKEN_YOU_CONTROL);
        single.put("unpaired Soulbond creature", TARGET_UNPAIRED_SOULBOND_CREATURE);
        single.put("nonland permanent", TARGET_NONLAND_PERMANENT);
        single.put("nonland permanent your opponents control", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent you don't control", TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent with converted mana cost 3 or less", TARGET_NONLAND_PERMANENT_CMC_LEQ_3);
        single.put("noncreature artifact", TARGET_NONCREATURE_ARTIFACT);
        single.put("Spirit or enchantment", TARGET_SPIRIT_OR_ENCHANTMENT);
        single.put("permanent", TARGET_PERMANENT);
        single.put("permanent you control", TARGET_PERMANENT_YOU_CONTROL);
        single.put("permanent you own", TARGET_PERMANENT_YOU_OWN);
        single.put("permanent an opponent controls", TARGET_PERMANENT_AN_OPPONENT_CONTROLS);
        single.put("black permanent", TARGET_BLACK_PERMANENT);
        single.put("black or red permanent", TARGET_BLACK_RED_PERMANENT);
        single.put("multicolored permanent you control", TARGET_MULTICOLORED_PERMANENT_YOU_CONTROL);

        single.put("card from your graveyard", TARGET_CARD_FROM_GRAVEYARD);
        single.put("card from your hand", TARGET_CARD_FROM_HAND);
        single.put("artifact or creature card from a graveyard", TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("attacking creature", TARGET_ATTACKING_CREATURE);
        single.put("attacking creature with flying", TARGET_ATTACKING_CREATURE_WITH_FLYING);
        single.put("attacking or blocking creature", TARGET_ATTACKING_OR_BLOCKING_CREATURE);
        single.put("attacking or blocking Spirit",  TARGET_ATTACKING_OR_BLOCKING_SPIRIT);
        single.put("basic land card from your hand", TARGET_BASIC_LAND_CARD_FROM_HAND);
        single.put("basic land card from your library", TARGET_BASIC_LAND_CARD_FROM_LIBRARY);
        single.put("blocked creature", TARGET_BLOCKED_CREATURE);
        single.put("blocking creature", TARGET_BLOCKING_CREATURE);
        single.put("blue or red creature card from your hand", TARGET_BLUE_OR_RED_CREATURE_CARD_FROM_HAND);
        single.put("card from a graveyard", TARGET_CARD_FROM_ALL_GRAVEYARDS);
        single.put("creature card from a graveyard", TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("creature card with infect from your graveyard", TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD);
        single.put("creature card with scavenge from your graveyard", PAYABLE_CREATURE_CARD_FROM_GRAVEYARD);
        single.put("creature or enchantment", TARGET_CREATURE_OR_ENCHANTMENT);
        single.put("creature or land", TARGET_CREATURE_OR_LAND);
        single.put("creature or planeswalker", TARGET_CREATURE_OR_PLANESWALKER);
        single.put("creature or player", TARGET_CREATURE_OR_PLAYER); 
        single.put("creature with converted mana cost 3 or less", TARGET_CREATURE_CONVERTED_3_OR_LESS);
        single.put("creature with flying", TARGET_CREATURE_WITH_FLYING);
        single.put("creature without flying", TARGET_CREATURE_WITHOUT_FLYING);
        single.put("creature with power 2 or less", TARGET_CREATURE_POWER_2_OR_LESS); 
        single.put("creature with power 4 or greater", TARGET_CREATURE_POWER_4_OR_MORE);
        single.put("creature with shadow", TARGET_CREATURE_WITH_SHADOW);
        single.put("creature with +1/+1 counter", TARGET_CREATURE_PLUSONE_COUNTER);
        single.put("Forest or Island card from your library", TARGET_FOREST_OR_ISLAND_CARD_FROM_LIBRARY);
        single.put("Forest or Plains card from your library", TARGET_FOREST_OR_PLAINS_CARD_FROM_LIBRARY);
        single.put("Goblin permanent card from your hand", TARGET_GOBLIN_CARD_FROM_HAND);
        single.put("Goblin permanent card from your library", TARGET_GOBLIN_CARD_FROM_LIBRARY);
        single.put("green creature card from your hand", TARGET_GREEN_CREATURE_CARD_FROM_HAND);
        single.put("green or white creature", TARGET_GREEN_OR_WHITE_CREATURE);
        single.put("Insect, Rat, Spider, or Squirrel", TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL);
        single.put("instant or sorcery card from your graveyard", TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD);
        single.put("instant or sorcery card from your opponent's graveyard", TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD);
        single.put("Island or Mountain card from your library", TARGET_ISLAND_OR_MOUNTAIN_CARD_FROM_LIBRARY);
        single.put("Island or Swamp card from your library", TARGET_ISLAND_OR_SWAMP_CARD_FROM_LIBRARY); 
        single.put("land card with a basic land type from your library", TARGET_LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        single.put("Mountain or Plains card from your library", TARGET_MOUNTAIN_OR_PLAINS_CARD_FROM_LIBRARY); 
        single.put("multicolored creature card from your hand", TARGET_MULTICOLORED_CREATURE_CARD_FROM_HAND);
        single.put("non-Demon creature", TARGET_NON_DEMON);
        single.put("non-Human creature", TARGET_NONHUMAN_CREATURE);
        single.put("nonlegendary creature you control", TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL);
        single.put("non-Vampire, non-Werewolf, non-Zombie creature", TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE);
        single.put("non-Zombie creature you control", TARGET_NON_ZOMBIE_YOU_CONTROL);
        single.put("non-Vampire creature you control", TARGET_NON_VAMPIRE_YOU_CONTROL);
        single.put("permanent card from your graveyard", TARGET_PERMANENT_CARD_FROM_GRAVEYARD); 
        single.put("permanent card with converted mana cost 3 or less from your graveyard", TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD);
        single.put("Plains, Island, Swamp, or Mountain card from your library", TARGET_PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY);
        single.put("Plains or Island card from your library", TARGET_PLAINS_OR_ISLAND_CARD_FROM_LIBRARY);
        single.put("Plains or Swamp card from your library", TARGET_PLAINS_OR_SWAMP_CARD_FROM_LIBRARY);
        single.put("Swamp or Forest card from your library", TARGET_SWAMP_OR_FOREST_CARD_FROM_LIBRARY);
        single.put("Swamp or Mountain card from your library", TARGET_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY); 
        single.put("unblocked attacking creature you control", UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL);
        single.put("Vampire, Werewolf, or Zombie", TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE);
        single.put("white or blue creature", TARGET_WHITE_OR_BLUE_CREATURE);
    }

    public static MagicTargetFilter<MagicPermanent> build(final String arg) {
        if (factory.containsKey(arg)) {
            return factory.get(arg);
        } else if (arg.endsWith(" creatures you control")) {
            return matchCreaturePrefix(arg, " creatures you control", Control.You);
        } else if (arg.endsWith(" creatures your opponents control")) {
            return matchCreaturePrefix(arg, " creatures your opponents control", Control.Opp);
        } else if (arg.endsWith(" creatures")) {
            return matchCreaturePrefix(arg, " creatures", Control.Any);
        } else {
            throw new RuntimeException("unknown target filter \"" + arg + "\"");
        }
    }
    
    public static MagicTargetFilter<?> single(final String arg) {
        final String filter = arg.replaceFirst("^target |^a |^an ", "").replaceFirst(" to sacrifice$", " you control");
        if (single.containsKey(filter)) {
            return single.get(filter);
        } else if (filter.endsWith(" card from your graveyard")) {
            return matchCardPrefix(filter, " card from your graveyard", MagicTargetType.Graveyard);
        } else if (filter.endsWith(" card from your opponent's graveyard")) {
            return matchCardPrefix(filter, " card from your opponent's graveyard", MagicTargetType.OpponentsGraveyard);
        } else if (filter.endsWith(" card from your hand")) {
            return matchCardPrefix(filter, " card from your hand", MagicTargetType.Hand);
        } else if (filter.endsWith(" card from your library")) {
            return matchCardPrefix(filter, " card from your library", MagicTargetType.Library);
        } else if (filter.endsWith(" creature you control")) {
            return matchCreaturePrefix(filter, " creature you control", Control.You);
        } else if (filter.endsWith(" creature your opponents control")) {
            return matchCreaturePrefix(filter, " creature your opponents control", Control.Opp);
        } else if (filter.endsWith(" creature")) {
            return matchCreaturePrefix(filter, " creature", Control.Any);
        } else if (filter.endsWith(" you control")) {
            return matchPermanentPrefix(filter, " you control", Control.You);
        } else if (filter.endsWith(" your opponents control")) {
            return matchPermanentPrefix(filter, " your opponents control", Control.Opp);
        } else if (filter.endsWith(" you don't control")) {
            return matchPermanentPrefix(filter, " you don't control", Control.Opp);
        } else {
            return matchPermanentPrefix(filter, "", Control.Any);
        }
    }
    
    private static MagicTargetFilter<MagicCard> matchCardPrefix(final String arg, final String suffix, final MagicTargetType location) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return Factory.card(location, c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return Factory.card(location, t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return Factory.card(location, st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    private static MagicTargetFilter<MagicPermanent> matchPermanentPrefix(final String arg, final String suffix, final Control control) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return Factory.permanent(c, control);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return Factory.permanent(t, control);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return Factory.permanent(st, control);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    private static MagicTargetFilter<MagicPermanent> matchCreaturePrefix(final String arg, final String suffix, final Control control) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return Factory.creature(c, control);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return Factory.creature(t, control);
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

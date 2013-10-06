package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicCardList;
import magic.model.MagicCard;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetNone;
import magic.model.target.MagicTargetType;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetPicker;
import magic.ui.GameController;
import magic.ui.UndoClickedException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class MagicTargetChoice extends MagicChoice {
    public static final MagicTargetChoice NONE =
        new MagicTargetChoice(MagicTargetFilter.NONE,MagicTargetHint.None,"nothing") {
            @Override
            public boolean isValid() {
                return false;
            }
        };
    
    public static final MagicTargetChoice A_PAYABLE_CREATURE_CARD_FROM_YOUR_GRAVEYARD = 
        new MagicTargetChoice("a creature card with scavenge from your graveyard");

    public static final MagicTargetChoice TARGET_SPELL = 
        new MagicTargetChoice("target spell");
    
    public static final MagicTargetChoice TARGET_SPELL_YOU_DONT_CONTROL = 
        new MagicTargetChoice("target spell you don't control");
    
    public static final MagicTargetChoice TARGET_SPELL_WITH_CMC_EQ_2 = 
        new MagicTargetChoice("target spell with converted mana cost 2");
    
    public static final MagicTargetChoice NEG_TARGET_SPELL = 
        MagicTargetChoice.Negative("target spell");

    public static final MagicTargetChoice NEG_TARGET_SPELL_THAT_TARGETS_PLAYER = 
        MagicTargetChoice.Negative("target spell that targets a player");

    public static final MagicTargetChoice NEG_TARGET_SPELL_WITH_X_COST = 
        MagicTargetChoice.Negative("target spell with {X} in its mana cost");

    public static final MagicTargetChoice NEG_TARGET_RED_GREEN_SPELL = 
        MagicTargetChoice.Negative("target red or green spell");

    public static final MagicTargetChoice NEG_TARGET_CREATURE_SPELL = 
        MagicTargetChoice.Negative("target creature spell");

    public static final MagicTargetChoice NEG_TARGET_NONCREATURE_SPELL = 
        MagicTargetChoice.Negative("target noncreature spell");

    public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_SPELL = 
        new MagicTargetChoice("target instant or sorcery spell");

    public static final MagicTargetChoice NEG_TARGET_INSTANT_OR_SORCERY_SPELL = 
        MagicTargetChoice.Negative("target instant or sorcery spell");

    public static final MagicTargetChoice TARGET_INSTANT_SPELL = 
        new MagicTargetChoice("target instant spell");

    public static final MagicTargetChoice NEG_TARGET_INSTANT_SPELL = 
        MagicTargetChoice.Negative("target instant spell");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_SPELL = 
        MagicTargetChoice.Negative("target artifact spell");

    public static final MagicTargetChoice TARGET_PLAYER = 
        new MagicTargetChoice("target player");
    
    public static final MagicTargetChoice TARGET_OPPONENT = 
        new MagicTargetChoice("target opponent");
    
    public static final MagicTargetChoice POS_TARGET_PLAYER = 
        MagicTargetChoice.Positive("target player");
    
    public static final MagicTargetChoice NEG_TARGET_PLAYER = 
        MagicTargetChoice.Negative("target player");
    
    public static final MagicTargetChoice NEG_TARGET_SPELL_OR_PERMANENT = 
        MagicTargetChoice.Negative("target spell or permanent");
    
    public static final MagicTargetChoice TARGET_PERMANENT = 
        new MagicTargetChoice("target permanent");
    
    public static final MagicTargetChoice PERMANENT_YOU_CONTROL = 
        new MagicTargetChoice("a permanent you control");

    public static final MagicTargetChoice TARGET_PERMANENT_YOU_CONTROL = 
        new MagicTargetChoice("target permanent you control");
    
    public static final MagicTargetChoice TARGET_PERMANENT_AN_OPPONENT_CONTROLS = 
        new MagicTargetChoice("target permanent an opponent controls");
    
    public static final MagicTargetChoice TARGET_PERMANENT_YOU_OWN = 
        new MagicTargetChoice("target permanent you own");

    public static final MagicTargetChoice NEG_TARGET_PERMANENT = 
        MagicTargetChoice.Negative("target permanent");
    
    public static final MagicTargetChoice POS_TARGET_PERMANENT =  
        MagicTargetChoice.Positive("target permanent");
    
    public static final MagicTargetChoice TARGET_BLACK_PERMANENT = 
        new MagicTargetChoice("target black permanent");
    
    public static final MagicTargetChoice NEG_TARGET_BLACK_RED_PERMANENT = 
        MagicTargetChoice.Negative("target black or red permanent");

    public static final MagicTargetChoice TARGET_NONBASIC_LAND = 
        new MagicTargetChoice("target non basic land");
    
    public static final MagicTargetChoice NEG_TARGET_NONBASIC_LAND = 
        MagicTargetChoice.Negative("target non basic land");
    
    public static final MagicTargetChoice TARGET_LAND = 
        new MagicTargetChoice("target land");
    
    public static final MagicTargetChoice NEG_TARGET_LAND = 
        MagicTargetChoice.Negative("target land");
    
    public static final MagicTargetChoice NEG_TARGET_LAND_OR_NONBLACK_CREATURE = 
        MagicTargetChoice.Negative("target land or nonblack creature");
    
    public static final MagicTargetChoice POS_TARGET_LAND = 
        MagicTargetChoice.Positive("target land");

    public static final MagicTargetChoice TARGET_NONLAND_PERMANENT = 
        new MagicTargetChoice("target nonland permanent");

    public static final MagicTargetChoice TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS = 
        new MagicTargetChoice("target nonland permanent your opponents control");

    public static final MagicTargetChoice TARGET_NONLAND_PERMANENT_YOU_DONT_CONTROL =
        new MagicTargetChoice("target nonland permanent you don't control");

    public static final MagicTargetChoice NEG_TARGET_NONLAND_PERMANENT = 
        MagicTargetChoice.Negative("target nonland permanent");
    
    public static final MagicTargetChoice NEG_TARGET_NONLAND_PERMANENT_CMC_LEQ_3 = 
        MagicTargetChoice.Negative("target nonland permanent with converted mana cost 3 or less");

    public static final MagicTargetChoice ARTIFACT_YOU_CONTROL = 
        new MagicTargetChoice("an artifact you control");

    public static final MagicTargetChoice TARGET_ARTIFACT_YOU_CONTROL=
        new MagicTargetChoice("target artifact you control");

    public static final MagicTargetChoice TARGET_ARTIFACT =
        new MagicTargetChoice("target artifact");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT = 
        MagicTargetChoice.Negative("target artifact");

    public static final MagicTargetChoice POS_TARGET_ARTIFACT = 
        MagicTargetChoice.Positive("target artifact");

    public static final MagicTargetChoice TARGET_NONCREATURE_ARTIFACT = 
        new MagicTargetChoice("target noncreature artifact");

    public static final MagicTargetChoice POS_TARGET_NONCREATURE_ARTIFACT = 
        MagicTargetChoice.Positive("target noncreature artifact");

    public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT = 
        new MagicTargetChoice("target artifact or enchantment");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_ENCHANTMENT = 
        MagicTargetChoice.Negative("target artifact or enchantment");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_LAND = 
        MagicTargetChoice.Negative("target artifact or land");
    
    public static final MagicTargetChoice TARGET_ARTIFACT_OR_CREATURE = 
        new MagicTargetChoice("target artifact or creature");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_CREATURE = 
        MagicTargetChoice.Negative("target artifact or creature");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_CREATURE_OR_LAND = 
        MagicTargetChoice.Negative("target artifact, creature, or land");
    
    public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS = 
        new MagicTargetChoice("target artifact or enchantment your opponents control");
    
    public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND = 
        new MagicTargetChoice("target artifact, enchantment, or land");
    
    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND = 
        MagicTargetChoice.Negative("target artifact, enchantment, or land");
    
    public static final MagicTargetChoice POS_TARGET_ARTIFACT_CREATURE = 
        MagicTargetChoice.Positive("target artifact creature");
    
    public static final MagicTargetChoice TARGET_ENCHANTMENT = 
        new MagicTargetChoice("target enchantment");
    
    public static final MagicTargetChoice TARGET_ENCHANTMENT_YOU_CONTROL = 
        new MagicTargetChoice("target enchantment you control");
    
    public static final MagicTargetChoice TARGET_ENCHANTMENT_YOUR_OPPONENT_CONTROLS = 
        new MagicTargetChoice("target enchantment your opponents control");
    
    public static final MagicTargetChoice NEG_TARGET_ENCHANTMENT = 
        MagicTargetChoice.Negative("target enchantment");
    
    public static final MagicTargetChoice POS_TARGET_ENCHANTMENT = 
        MagicTargetChoice.Positive("target enchantment");
    
    public static final MagicTargetChoice NEG_TARGET_SPIRIT_OR_ENCHANTMENT = 
        MagicTargetChoice.Negative("target Spirit or enchantment");
    
    public static final MagicTargetChoice NEG_TARGET_EQUIPMENT = 
        MagicTargetChoice.Negative("target Equipment");
    
    public static final MagicTargetChoice TARGET_CREATURE = 
        new MagicTargetChoice("target creature");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE = 
        MagicTargetChoice.Negative("target creature");
    
    public static final MagicTargetChoice POS_TARGET_CREATURE = 
        MagicTargetChoice.Positive("target creature");
    
    public static final MagicTargetChoice POS_TARGET_1_1_CREATURE = 
        MagicTargetChoice.Positive("target 1/1 creature");
    
    public static final MagicTargetChoice POS_TARGET_BLINKMOTH_CREATURE = 
        MagicTargetChoice.Positive("target Blinkmoth creature");
    
    public static final MagicTargetChoice TARGET_NONCREATURE = 
        new MagicTargetChoice("target noncreature");

    public static final MagicTargetChoice NEG_TARGET_NONCREATURE = 
        MagicTargetChoice.Negative("target noncreature");

    public static final MagicTargetChoice NEG_TARGET_NONBLACK_CREATURE = 
        MagicTargetChoice.Negative("target nonblack creature");

    public static final MagicTargetChoice POS_TARGET_NONBLACK_CREATURE = 
        MagicTargetChoice.Positive("target nonblack creature");
    
    public static final MagicTargetChoice NEG_TARGET_NONARTIFACT_CREATURE = 
        MagicTargetChoice.Negative("target nonartifact creature");
    
    public static final MagicTargetChoice TARGET_NONARTIFACT_NONBLACK_CREATURE = 
        new MagicTargetChoice("target nonartifact, nonblack creature");
    
    public static final MagicTargetChoice NEG_TARGET_NONARTIFACT_NONBLACK_CREATURE = 
        MagicTargetChoice.Negative("target nonartifact, nonblack creature");
    
    public static final MagicTargetChoice NEG_TARGET_TAPPED_CREATURE = 
        MagicTargetChoice.Negative("target tapped creature");
    
    public static final MagicTargetChoice NEG_TARGET_UNTAPPED_CREATURE = 
        MagicTargetChoice.Negative("target untapped creature");
    
    public static final MagicTargetChoice CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("a creature you control");
    
    public static final MagicTargetChoice CREATURE = 
        new MagicTargetChoice("a creature");

    public static final MagicTargetChoice RED_OR_GREEN_CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("a red or green creature you control");

    public static final MagicTargetChoice NEG_TARGET_CREATURE_CONVERTED_3_OR_LESS = 
        MagicTargetChoice.Negative("target creature with converted mana cost 3 or less");
    
    public static final MagicTargetChoice TARGET_CREATURE_POWER_2_OR_LESS = 
        MagicTargetChoice.Positive("target creature with power 2 or less");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_POWER_4_OR_MORE = 
        MagicTargetChoice.Negative("target creature with power 4 or greater");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_PLUSONE_COUNTER = 
        MagicTargetChoice.Negative("target creature");
    
    public static final MagicTargetChoice TARGET_CREATURE_PLUSONE_COUNTER = 
        new MagicTargetChoice("target creature with +1/+1 counter");
    
    public static final MagicTargetChoice TARGET_CREATURE_WITH_FLYING = 
        new MagicTargetChoice("target creature with flying");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_WITH_FLYING = 
        MagicTargetChoice.Negative("target creature with flying");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_WITHOUT_FLYING =
        MagicTargetChoice.Negative("target creature without flying");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_WITH_SHADOW = 
        MagicTargetChoice.Negative("target creature with shadow");
    
    public static final MagicTargetChoice NEG_TARGET_ATTACKING_CREATURE = 
        MagicTargetChoice.Negative("target attacking creature");
    
    public static final MagicTargetChoice POS_TARGET_ATTACKING_CREATURE = 
        MagicTargetChoice.Positive("target attacking creature");
    
    public static final MagicTargetChoice POS_TARGET_BLOCKING_CREATURE = 
        MagicTargetChoice.Positive("target blocking creature");
    
    public static final MagicTargetChoice NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE = 
        MagicTargetChoice.Negative("target attacking or blocking creature");
    
    public static final MagicTargetChoice TARGET_ATTACKING_OR_BLOCKING_CREATURE = 
        new MagicTargetChoice("target attacking or blocking creature");
    
    public static final MagicTargetChoice NEG_TARGET_ATTACKING_OR_BLOCKING_SPIRIT = 
        MagicTargetChoice.Negative("target attacking or blocking Spirit");

    public static final MagicTargetChoice NEG_TARGET_ATTACKING_CREATURE_WITH_FLYING = 
        MagicTargetChoice.Negative("target attacking creature with flying");

    public static final MagicTargetChoice NEG_TARGET_BLOCKED_CREATURE = 
        MagicTargetChoice.Negative("target blocked creature");

    public static final MagicTargetChoice NEG_TARGET_GREEN_OR_WHITE_CREATURE = 
        MagicTargetChoice.Negative("target green or white creature");

    public static final MagicTargetChoice NEG_WHITE_OR_BLUE_CREATURE = 
        MagicTargetChoice.Negative("target white or blue creature");

    public static final MagicTargetChoice TARGET_WHITE_CREATURE = 
        new MagicTargetChoice("target white creature");

    public static final MagicTargetChoice TARGET_CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("target creature you control");

    public static final MagicTargetChoice TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("target nonlegendary creature you control");

    public static final MagicTargetChoice POS_TARGET_LEGENDARY_CREATURE = 
        MagicTargetChoice.Positive("target legendary creature");

    public static final MagicTargetChoice TARGET_NON_DEMON = 
        new MagicTargetChoice("target non-Demon creature");
    
    public static final MagicTargetChoice TARGET_CREATURE_OR_PLAYER = 
        new MagicTargetChoice("target creature or player");
   
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_PLAYER = 
        MagicTargetChoice.Negative("target creature or player");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_LAND = 
        MagicTargetChoice.Negative("target creature or land");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_PLANESWALKER = 
        MagicTargetChoice.Negative("target creature or planeswalker");
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_ENCHANTMENT = 
        MagicTargetChoice.Negative("target creature or enchantment");

    public static final MagicTargetChoice POS_TARGET_CREATURE_OR_PLAYER = 
        MagicTargetChoice.Positive("target creature or player");
    
    public static final MagicTargetChoice POS_TARGET_MERFOLK_CREATURE = 
        MagicTargetChoice.Positive("target Merfolk creature");
    
    public static final MagicTargetChoice POS_TARGET_ASSEMBLY_WORKER_CREATURE = 
        MagicTargetChoice.Positive("target Assembly-Worker creature");
    
    public static final MagicTargetChoice NEG_TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE = 
        MagicTargetChoice.Negative("target Vampire, Werewolf, or Zombie");

    public static final MagicTargetChoice NEG_TARGET_VAMPIRE = 
        MagicTargetChoice.Negative("target Vampire");

    public static final MagicTargetChoice NEG_TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE = 
        MagicTargetChoice.Negative("target non-Vampire, non-Werewolf, non-Zombie creature");

    public static final MagicTargetChoice NEG_TARGET_NONHUMAN_CREATURE = 
        MagicTargetChoice.Negative("target non-Human creature");
    
    public static final MagicTargetChoice POS_TARGET_GOLEM_YOU_CONTROL = 
        new MagicTargetChoice("target Golem you control");
    
    public static final MagicTargetChoice POS_TARGET_KNIGHT_CREATURE =
        MagicTargetChoice.Positive("target Knight creature");
    
    public static final MagicTargetChoice POS_TARGET_GOBLIN_CREATURE = 
        MagicTargetChoice.Positive("target Goblin creature");
    
    public static final MagicTargetChoice POS_TARGET_FUNGUS_CREATURE = 
        MagicTargetChoice.Positive("target Fungus creature");
    
    public static final MagicTargetChoice POS_TARGET_ELF = 
        MagicTargetChoice.Positive("target Elf");
    
    public static final MagicTargetChoice POS_TARGET_FOREST = 
        MagicTargetChoice.Positive("target Forest");

    public static final MagicTargetChoice POS_TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL = 
        MagicTargetChoice.Positive("target Insect, Rat, Spider, or Squirrel");
    
    public static final MagicTargetChoice POS_TARGET_ZOMBIE = 
        MagicTargetChoice.Positive("target Zombie");

    public static final MagicTargetChoice POS_TARGET_SAMURAI = 
        MagicTargetChoice.Positive("target Samurai");

    public static final MagicTargetChoice NEG_TARGET_HUMAN_CREATURE = 
        MagicTargetChoice.Negative("target Human creature");

    public static final MagicTargetChoice TARGET_NON_ANGEL_CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("target non-Angel creature you control");

    public static final MagicTargetChoice SACRIFICE_PERMANENT = 
        new MagicTargetChoice("a permanent to sacrifice");

    public static final MagicTargetChoice SACRIFICE_MULTICOLORED_PERMANENT = 
        new MagicTargetChoice("a multicolored permanent to sacrifice");

    public static final MagicTargetChoice SACRIFICE_CREATURE = 
        new MagicTargetChoice("a creature to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_ARTIFACT = 
        new MagicTargetChoice("an artifact to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_LAND = 
        new MagicTargetChoice("a land to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_ENCHANTMENT = 
        new MagicTargetChoice("an enchantment to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_MOUNTAIN = 
        new MagicTargetChoice("a Mountain to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_FOREST = 
        new MagicTargetChoice("a Forest to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_BAT = 
        new MagicTargetChoice("a Bat to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_BEAST = 
        new MagicTargetChoice("a Beast to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_GOBLIN = 
        new MagicTargetChoice("a Goblin to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_NON_ZOMBIE = 
        new MagicTargetChoice("a non-Zombie creature to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_NON_VAMPIRE = 
        new MagicTargetChoice("a non-Vampire creature to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_SAMURAI = 
        new MagicTargetChoice("a Samurai to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_SAPROLING = 
        new MagicTargetChoice("a Saproling to sacrifice");
    
    public static final MagicTargetChoice SACRIFICE_MERFOLK = 
        new MagicTargetChoice("a Merfolk to sacrifice");

    public static final MagicTargetChoice SACRIFICE_HUMAN = 
        new MagicTargetChoice("a Human to sacrifice");

    public static final MagicTargetChoice SACRIFICE_CLERIC = 
        new MagicTargetChoice("a Cleric creature to sacrifice");

    public static final MagicTargetChoice TARGET_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target card from your graveyard");

    public static final MagicTargetChoice NEG_TARGET_CARD_FROM_ALL_GRAVEYARDS = 
        MagicTargetChoice.Negative("target card from a graveyard");

    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target creature card from your graveyard");
    
    public static final MagicTargetChoice AN_UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("an unblocked attacking creature you control");

    public static final MagicTargetChoice TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = 
        new MagicTargetChoice("target creature card with infect from your graveyard");

    public static final MagicTargetChoice TARGET_PERMANENT_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target permanent card from your graveyard");

    public static final MagicTargetChoice TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD = 
        new MagicTargetChoice("target permanent card with converted mana cost 3 or less from your graveyard");

    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD = 
        new MagicTargetChoice("target creature card from your opponent's graveyard");

    public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target instant or sorcery card from your graveyard");

    public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD = 
        new MagicTargetChoice("target instant or sorcery card from your opponent's graveyard");

    public static final MagicTargetChoice TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target enchantment card from your graveyard");
    
    public static final MagicTargetChoice TARGET_ARTIFACT_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target artifact card from your graveyard");
    
    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS = 
        new MagicTargetChoice("target creature card from a graveyard");

    public static final MagicTargetChoice TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS = 
        new MagicTargetChoice("target artifact or creature card from a graveyard");
    
    public static final MagicTargetChoice TARGET_GOBLIN_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target Goblin card from your graveyard");

    public static final MagicTargetChoice TARGET_ZOMBIE_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target Zombie card from your graveyard");

    public static final MagicTargetChoice TARGET_SPIRIT_CARD_FROM_GRAVEYARD = 
        new MagicTargetChoice("target Spirit card from your graveyard");
    
    public static final MagicTargetChoice TARGET_CARD_FROM_HAND = 
        new MagicTargetChoice("a card from your hand");
    
    public static final MagicTargetChoice CREATURE_CARD_FROM_HAND = 
        new MagicTargetChoice("a creature card from your hand");

    public static final MagicTargetChoice TARGET_GREEN_CREATURE_CARD_FROM_HAND = 
        new MagicTargetChoice("a green creature card from your hand");

    public static final MagicTargetChoice ARTIFACT_CARD_FROM_HAND = 
        new MagicTargetChoice("an artifact card from your hand");

    public static final MagicTargetChoice BLUE_OR_RED_CREATURE_CARD_FROM_HAND = 
        new MagicTargetChoice("a blue or red creature card from your hand");

    public static final MagicTargetChoice MULTICOLORED_CREATURE_CARD_FROM_HAND = 
        new MagicTargetChoice("a multicolored creature card from your hand");

    public static final MagicTargetChoice BASIC_LAND_CARD_FROM_HAND = 
        new MagicTargetChoice("a basic land card from your hand");
    
    public static final MagicTargetChoice LAND_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a land card from your library");
    
    public static final MagicTargetChoice BASIC_LAND_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a basic land card from your library");
    
    public static final MagicTargetChoice FOREST_OR_ISLAND_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Forest or Island card from your library");
    
    public static final MagicTargetChoice FOREST_OR_PLAINS_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Forest or Plains card from your library");
    
    public static final MagicTargetChoice ISLAND_OR_MOUNTAIN_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Island or Mountain card from your library");
    
    public static final MagicTargetChoice SWAMP_OR_FOREST_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Swamp or Forest card from your library");
    
    public static final MagicTargetChoice SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Swamp or Mountain card from your library");
    
    public static final MagicTargetChoice PLAINS_OR_SWAMP_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Plains or Swamp card from your library");
    
    public static final MagicTargetChoice MOUNTAIN_OR_PLAINS_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Mountain or Plains card from your library");
    
    public static final MagicTargetChoice MOUNTAIN_OR_FOREST_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Mountain or Plains card from your library");
    
    public static final MagicTargetChoice ISLAND_OR_SWAMP_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Island or Swamp card from your library");
    
    public static final MagicTargetChoice PLAINS_OR_ISLAND_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Plains or Island card from your library");
    
    public static final MagicTargetChoice PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Plains, Island, Swamp, or Mountain card from your library");
    
    public static final MagicTargetChoice LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY = 
        new MagicTargetChoice("a land card with a basic land type from your library");

    public static final MagicTargetChoice LAND_CARD_FROM_HAND = 
        new MagicTargetChoice("a land card from your hand");

    public static final MagicTargetChoice GOBLIN_CARD_FROM_HAND = 
        new MagicTargetChoice("a Goblin permanent card from your hand");
    
    public static final MagicTargetChoice GOBLIN_CARD_FROM_LIBRARY = 
        new MagicTargetChoice("a Goblin permanent card from your library");

    public static final MagicTargetChoice TARGET_UNPAIRED_SOULBOND_CREATURE = 
        new MagicTargetChoice("an unpaired Soulbond creature");

    public static final MagicTargetChoice CREATURE_TOKEN_YOU_CONTROL = 
        new MagicTargetChoice("a creature token you control");

    public static final MagicTargetChoice PLANESWALKER_YOUR_OPPONENT_CONTROLS = 
        new MagicTargetChoice("a planeswalker your opponents control");

    public static final MagicTargetChoice TARGET_CREATURE_YOUR_OPPONENT_CONTROLS = 
        new MagicTargetChoice("target creature your opponents control");

    public static final MagicTargetChoice TARGET_CREATURE_YOU_DONT_CONTROL = 
        new MagicTargetChoice("target creature you don't control");

    public static final MagicTargetChoice TARGET_CREATURE_WITHOUT_FLYING_YOU_DONT_CONTROL = 
        new MagicTargetChoice("target creature without flying you don't control");

    public static final MagicTargetChoice TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS = 
        new MagicTargetChoice("target artifact your opponents control");

    public static final MagicTargetChoice TARGET_ARTIFACT_YOU_DONT_CONTROL = 
        new MagicTargetChoice("target artifact you don't control");

    private final String targetDescription;
    private final MagicTargetFilter<? extends MagicTarget> targetFilter;
    private final boolean targeted;
    private final MagicTargetHint targetHint;
    
    public static MagicTargetChoice Positive(final String aTargetDescription) {
        return new MagicTargetChoice(MagicTargetHint.Positive, aTargetDescription);
    }
    
    public static MagicTargetChoice Negative(final String aTargetDescription) {
        return new MagicTargetChoice(MagicTargetHint.Negative, aTargetDescription);
    }
    
    public MagicTargetChoice(final String aTargetDescription) {
        this(MagicTargetHint.None, aTargetDescription);
    }
    
    public MagicTargetChoice(final MagicTargetHint aTargetHint, final String aTargetDescription) {
        super("Choose " + aTargetDescription + '.');
        targetFilter = MagicTargetFilterFactory.single(aTargetDescription);
        targeted     = aTargetDescription.contains("target");
        targetHint   = aTargetHint;
        targetDescription = aTargetDescription;
    }
    
    public MagicTargetChoice(
        final MagicTargetFilter<? extends MagicTarget> aTargetFilter,
        final String aTargetDescription
    ) {
        super("Choose " + aTargetDescription + '.');
        targetFilter = aTargetFilter;
        targeted     = aTargetDescription.contains("target");
        targetHint   = MagicTargetHint.None;
        targetDescription = aTargetDescription;
    }

    public MagicTargetChoice(
        final MagicTargetFilter<? extends MagicTarget> aTargetFilter,
        final MagicTargetHint aTargetHint,
        final String aTargetDescription
    ) {
        super("Choose " + aTargetDescription + '.');
        targetFilter = aTargetFilter;
        targeted     = aTargetDescription.contains("target");
        targetHint   = aTargetHint;
        targetDescription = aTargetDescription;
    }
    
    public MagicTargetChoice(
        final MagicTargetChoice copy,
        final boolean targeted
    ) {
        super("Choose "+copy.targetDescription+'.');
        this.targetFilter=copy.targetFilter;
        this.targeted=targeted;
        this.targetHint=copy.targetHint;
        this.targetDescription=copy.targetDescription;
    }

    public final String getTargetDescription() {
        return targetDescription;
    }

    public final MagicTargetFilter<? extends MagicTarget> getTargetFilter() {
        return targetFilter;
    }

    public final boolean isTargeted() {
        return targeted;
    }

    public final MagicTargetHint getTargetHint(final boolean hints) {
        return hints?targetHint:MagicTargetHint.None;
    }

    @Override
    public final MagicTargetChoice getTargetChoice() {
        return this;
    }

    @Override
    public final int getTargetChoiceResultIndex() {
        return 0;
    }

    @Override
    public final boolean hasOptions(
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source,
            final boolean hints) {
        return game.hasLegalTargets(player,source,this,hints);
    }

    @Override
    final Collection<?> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        Collection<MagicTarget> targets = game.getLegalTargets(player,source,this,targetHint);
        if (game.getFastChoices()) {
            final MagicTargetPicker<MagicTarget> targetPicker = (MagicTargetPicker<MagicTarget>)event.getTargetPicker();
            targets = targetPicker.pickTargets(game,player,targets);
        }
        return targets;
    }

    @Override
    public final Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) throws UndoClickedException {

        controller.disableActionButton(false);
        controller.showMessage(source,getDescription());
        if (targetFilter.acceptType(MagicTargetType.Graveyard)) {
            controller.focusViewers(1,-1);
        } else if (targetFilter.acceptType(MagicTargetType.OpponentsGraveyard)) {
            controller.focusViewers(2,-1);
        } 
        final MagicTargetHint usedTargetHint=getTargetHint(GeneralConfig.getInstance().getSmartTarget());
        final Set<Object> validChoices=new HashSet<Object>(game.getLegalTargets(player,source,this,usedTargetHint));
        if (validChoices.size()==1) {
            // There are no valid choices.
            if (validChoices.contains(MagicTargetNone.getInstance())) {
                return new Object[]{MagicTargetNone.getInstance()};
            }
            // Only valid choice is player.
            if (validChoices.contains(player)) {
                return new Object[]{player};
            }
            // Only valid choice is opponent.
            final MagicPlayer opponent=player.getOpponent();
            if (validChoices.contains(opponent)) {
                return new Object[]{opponent};
            }
        }
        if (targetFilter.acceptType(MagicTargetType.Library)) {
            final MagicCardList cards = new MagicCardList();
            for (final Object card : validChoices) {
                cards.add((MagicCard)card);
            }
            controller.showCards(cards);
        }
        controller.setValidChoices(validChoices,false);
        controller.waitForInput();
        if (targetFilter.acceptType(MagicTargetType.Library)) {
            controller.showCards(new MagicCardList());
        }
        return new Object[]{controller.getChoiceClicked()};
    }
}

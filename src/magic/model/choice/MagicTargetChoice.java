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
        new MagicTargetChoice(MagicTargetFilter.NONE,false,MagicTargetHint.None,"nothing") {
            @Override
            public boolean isValid() {
                return false;
            }
        };

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
    
    public static final MagicTargetChoice NEG_TARGET_NONARTIFACT_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NONARTIFACT_CREATURE,
        MagicTargetHint.Negative,
        "target nonartifact creature"
    );
    
    public static final MagicTargetChoice TARGET_NONARTIFACT_NONBLACK_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NONARTIFACT_NONBLACK_CREATURE,
        "target nonartifact, nonblack creature"
    );
    
    public static final MagicTargetChoice NEG_TARGET_NONARTIFACT_NONBLACK_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NONARTIFACT_NONBLACK_CREATURE,
        MagicTargetHint.Negative,
        "target nonartifact, nonblack creature"
    );
    
    public static final MagicTargetChoice NEG_TARGET_TAPPED_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_TAPPED_CREATURE,
        MagicTargetHint.Negative,
        "target tapped creature"
    );
    
    public static final MagicTargetChoice NEG_TARGET_UNTAPPED_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_UNTAPPED_CREATURE,
        MagicTargetHint.Negative,
        "target untapped creature"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_CONVERTED_3_OR_LESS = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_CONVERTED_3_OR_LESS,
        MagicTargetHint.Negative,
        "target creature with converted mana cost 3 or less"
    );
    
    public static final MagicTargetChoice TARGET_CREATURE_POWER_2_OR_LESS = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_POWER_2_OR_LESS,
        MagicTargetHint.Positive,
        "target creature with power 2 or less"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_POWER_4_OR_MORE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_POWER_4_OR_MORE,
        MagicTargetHint.Negative,
        "target creature with power 4 or greater"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_PLUSONE_COUNTER = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_PLUSONE_COUNTER,
        MagicTargetHint.Negative,
        "target creature"
    );
    
    public static final MagicTargetChoice TARGET_CREATURE_PLUSONE_COUNTER = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_PLUSONE_COUNTER,
        "target creature"
    );
    
    public static final MagicTargetChoice TARGET_CREATURE_WITH_FLYING = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_WITH_FLYING,
        "target creature with flying"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_WITH_FLYING = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_WITH_FLYING,
        MagicTargetHint.Negative,
        "target creature with flying"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_WITHOUT_FLYING = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING,
        MagicTargetHint.Negative,
        "target creature without flying"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_WITH_SHADOW = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_WITH_SHADOW,
        MagicTargetHint.Negative,
        "target creature with shadow"
    );
    
    public static final MagicTargetChoice NEG_TARGET_ATTACKING_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ATTACKING_CREATURE,
        MagicTargetHint.Negative,
        "target attacking creature"
    );
    
    public static final MagicTargetChoice POS_TARGET_ATTACKING_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ATTACKING_CREATURE,
        MagicTargetHint.Positive,
        "target attacking creature"
    );
    
    public static final MagicTargetChoice POS_TARGET_BLOCKING_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_BLOCKING_CREATURE,
        MagicTargetHint.Positive,
        "target blocking creature"
    );
    
    public static final MagicTargetChoice NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ATTACKING_OR_BLOCKING_CREATURE,
        MagicTargetHint.Negative,
        "target attacking or blocking creature"
    );
    
    public static final MagicTargetChoice TARGET_ATTACKING_OR_BLOCKING_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ATTACKING_OR_BLOCKING_CREATURE,
        "target attacking or blocking creature"
    );
    
    public static final MagicTargetChoice NEG_TARGET_ATTACKING_OR_BLOCKING_SPIRIT = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ATTACKING_OR_BLOCKING_SPIRIT,
        MagicTargetHint.Negative,
        "target attacking or blocking Spirit"
    );

    public static final MagicTargetChoice NEG_TARGET_ATTACKING_CREATURE_WITH_FLYING = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ATTACKING_CREATURE_WITH_FLYING,
        MagicTargetHint.Negative,
        "target attacking creature with flying"
    );

    public static final MagicTargetChoice NEG_TARGET_BLOCKED_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_BLOCKED_CREATURE,
        MagicTargetHint.Negative,
        "target blocked creature"
    );

    public static final MagicTargetChoice CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("a creature you control");
    
    public static final MagicTargetChoice CREATURE = 
        new MagicTargetChoice("a creature");

    public static final MagicTargetChoice RED_OR_GREEN_CREATURE_YOU_CONTROL = 
        new MagicTargetChoice("a red or green creature you control");

    public static final MagicTargetChoice NEG_TARGET_GREEN_OR_WHITE_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GREEN_OR_WHITE_CREATURE,
        MagicTargetHint.Negative,
        "target green or white creature"
    );

    public static final MagicTargetChoice NEG_WHITE_OR_BLUE_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_WHITE_OR_BLUE_CREATURE,
        MagicTargetHint.Negative,
        "target white or blue creature"
    );

    public static final MagicTargetChoice TARGET_WHITE_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_WHITE_CREATURE,
        "target white creature"
    );

    public static final MagicTargetChoice TARGET_CREATURE_YOU_CONTROL = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
        "target creature you control"
    );

    public static final MagicTargetChoice TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL,
        "target nonlegendary creature you control"
    );

    public static final MagicTargetChoice POS_TARGET_LEGENDARY_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_LEGENDARY_CREATURE,
        MagicTargetHint.Positive,
        "target legendary creature"
    );

    public static final MagicTargetChoice TARGET_NON_DEMON = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NON_DEMON,
        "target non-Demon creature"
    );
    
    public static final MagicTargetChoice TARGET_CREATURE_OR_PLAYER = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_OR_PLAYER,
        "target creature or player"
    );
   
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_PLAYER = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_OR_PLAYER,
        MagicTargetHint.Negative,
        "target creature or player"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_LAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_OR_LAND,
        MagicTargetHint.Negative,
        "target creature or land"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_PLANESWALKER = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_OR_PLANESWALKER,
        MagicTargetHint.Negative,
        "target creature or planeswalker"
    );
    
    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_ENCHANTMENT = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_OR_ENCHANTMENT,
        MagicTargetHint.Negative,
        "target creature or enchantment"
    );

    public static final MagicTargetChoice POS_TARGET_CREATURE_OR_PLAYER = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_OR_PLAYER,
        MagicTargetHint.Positive,
        "target creature or player"
    );
    
    public static final MagicTargetChoice POS_TARGET_MERFOLK_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_MERFOLK_CREATURE,
        MagicTargetHint.Positive,
        "target Merfolk creature"
    );
    
    public static final MagicTargetChoice POS_TARGET_ASSEMBLY_WORKER_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ASSEMBLY_WORKER_CREATURE,
        MagicTargetHint.Positive,
        "target Assembly-Worker creature"
    );
    
    public static final MagicTargetChoice NEG_TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE,
        MagicTargetHint.Negative,
        "target Vampire, Werewolf, or Zombie"
    );

    public static final MagicTargetChoice NEG_TARGET_VAMPIRE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_VAMPIRE,
        MagicTargetHint.Negative,
        "target Vampire"
    );

    public static final MagicTargetChoice NEG_TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE,
        MagicTargetHint.Negative,
        "target non-Vampire, non-Werewolf, non-Zombie creature"
    );

    public static final MagicTargetChoice NEG_TARGET_NONHUMAN_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NONHUMAN_CREATURE,
        MagicTargetHint.Negative,
        "target non-Human creature"
    );
    
    public static final MagicTargetChoice POS_TARGET_GOLEM_YOU_CONTROL = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GOLEM_YOU_CONTROL,
        "target Golem you control"
    );
    
    public static final MagicTargetChoice POS_TARGET_KNIGHT_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_KNIGHT_CREATURE,
        MagicTargetHint.Positive,
        "target Knight creature"
    );
    
    public static final MagicTargetChoice POS_TARGET_GOBLIN_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GOBLIN_CREATURE,
        MagicTargetHint.Positive,
        "target Goblin creature"
    );
    
    public static final MagicTargetChoice POS_TARGET_FUNGUS_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_FUNGUS_CREATURE,
        MagicTargetHint.Positive,
        "target Fungus creature"
    );
    
    public static final MagicTargetChoice POS_TARGET_ELF = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ELF,
        MagicTargetHint.Positive,
        "target Elf"
    );
    
    public static final MagicTargetChoice POS_TARGET_FOREST = new MagicTargetChoice(
        MagicTargetFilter.TARGET_FOREST,
        MagicTargetHint.Positive,
        "target Forest"
    );

    public static final MagicTargetChoice POS_TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL = new MagicTargetChoice(
        MagicTargetFilter.TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL,
        MagicTargetHint.Positive,
        "target Insect, Rat, Spider, or Squirrel"
    );
    
    public static final MagicTargetChoice POS_TARGET_ZOMBIE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ZOMBIE,
        MagicTargetHint.Positive,
        "target Zombie"
    );

    public static final MagicTargetChoice POS_TARGET_SAMURAI = new MagicTargetChoice(
        MagicTargetFilter.TARGET_SAMURAI,
        MagicTargetHint.Positive,
        "target Samurai"
    );

    public static final MagicTargetChoice NEG_TARGET_HUMAN_CREATURE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_HUMAN,
        MagicTargetHint.Negative,
        "target Human creature"
    );

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
    
    public static final MagicTargetChoice SACRIFICE_BAT = new MagicTargetChoice(
        MagicTargetFilter.TARGET_BAT_YOU_CONTROL,
        "a Bat to sacrifice"
    );
    
    public static final MagicTargetChoice SACRIFICE_BEAST = new MagicTargetChoice(
        MagicTargetFilter.TARGET_BEAST_YOU_CONTROL,
        "a Beast to sacrifice"
    );
    
    public static final MagicTargetChoice SACRIFICE_GOBLIN = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GOBLIN_YOU_CONTROL,
        "a Goblin to sacrifice"
    );
    
    public static final MagicTargetChoice SACRIFICE_NON_ZOMBIE = new MagicTargetChoice(
        MagicTargetFilter.TARGET_NON_ZOMBIE_YOU_CONTROL,
        "a non-Zombie creature to sacrifice"
    );
    
    public static final MagicTargetChoice SACRIFICE_SAMURAI = new MagicTargetChoice(
        MagicTargetFilter.TARGET_SAMURAI_YOU_CONTROL,
        "a Samurai to sacrifice"
    );
    
    public static final MagicTargetChoice SACRIFICE_SAPROLING = new MagicTargetChoice(
        MagicTargetFilter.TARGET_SAPROLING_YOU_CONTROL,
        "a Saproling to sacrifice"
    );
    
    public static final MagicTargetChoice SACRIFICE_MERFOLK = new MagicTargetChoice(
        MagicTargetFilter.TARGET_MERFOLK_YOU_CONTROL,
        "a Merfolk to sacrifice"
    );

    public static final MagicTargetChoice SACRIFICE_HUMAN = new MagicTargetChoice(
        MagicTargetFilter.TARGET_HUMAN_YOU_CONTROL,
        "a Human to sacrifice"
    );

    public static final MagicTargetChoice SACRIFICE_CLERIC = 
        new MagicTargetChoice("a Cleric creature to sacrifice");

    public static final MagicTargetChoice TARGET_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CARD_FROM_GRAVEYARD,
        "target card from your graveyard"
    );

    public static final MagicTargetChoice NEG_TARGET_CARD_FROM_ALL_GRAVEYARDS = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CARD_FROM_ALL_GRAVEYARDs,
        MagicTargetHint.Negative,
        "target card from a graveyard"
    );

    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_GRAVEYARD=new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
        "target creature card from your graveyard"
    );
    
    public static final MagicTargetChoice A_PAYABLE_CREATURE_CARD_FROM_YOUR_GRAVEYARD=new MagicTargetChoice(
        MagicTargetFilter.PAYABLE_CREATURE_CARD_FROM_GRAVEYARD,
        "a creature card from your graveyard"
    );
    
    public static final MagicTargetChoice AN_UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL=new MagicTargetChoice(
        MagicTargetFilter.UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL,
        "an unblocked attacking creature you control"
    );

    public static final MagicTargetChoice TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD,
        "target creature card with infect from your graveyard"
    );

    public static final MagicTargetChoice TARGET_PERMANENT_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_PERMANENT_CARD_FROM_GRAVEYARD,
        "target permanent card from your graveyard"
    );

    public static final MagicTargetChoice TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD,
        "target permanent card with converted mana cost 3 or less from your graveyard"
    );

    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD,
        "target creature card from your opponent's graveyard"
    );

    public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD,
        "target instant or sorcery card from your graveyard"
    );

    public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD,
        "target instant or sorcery card from your opponent's graveyard"
    );

    public static final MagicTargetChoice TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD,
        "target enchantment card from your graveyard"
    );
    
    public static final MagicTargetChoice TARGET_ARTIFACT_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ARTIFACT_CARD_FROM_GRAVEYARD,
        "target artifact card from your graveyard"
    );
    
    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
        "target creature card from a graveyard"
    );

    public static final MagicTargetChoice TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
        "target artifact or creature card from a graveyard"
    );
    
    public static final MagicTargetChoice TARGET_GOBLIN_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GOBLIN_CARD_FROM_GRAVEYARD,
        "target Goblin card from your graveyard");

    public static final MagicTargetChoice TARGET_ZOMBIE_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD,
        "target Zombie card from your graveyard"
    );

    public static final MagicTargetChoice TARGET_SPIRIT_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
        MagicTargetFilter.TARGET_SPIRIT_CARD_FROM_GRAVEYARD,
        "target Spirit card from your graveyard"
    );
    
    public static final MagicTargetChoice TARGET_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CARD_FROM_HAND,
        "a card from your hand"
    );
    
    public static final MagicTargetChoice CREATURE_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_CREATURE_CARD_FROM_HAND,
        "a creature card from your hand"
    );

    public static final MagicTargetChoice TARGET_GREEN_CREATURE_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GREEN_CREATURE_CARD_FROM_HAND,
        "a green creature card from your hand"
    );

    public static final MagicTargetChoice ARTIFACT_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ARTIFACT_CARD_FROM_HAND,
        "an artifact card from your hand"
    );

    public static final MagicTargetChoice BLUE_OR_RED_CREATURE_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_BLUE_OR_RED_CREATURE_CARD_FROM_HAND,
        "a blue or red creature card from your hand"
    );

    public static final MagicTargetChoice MULTICOLORED_CREATURE_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_MULTICOLORED_CREATURE_CARD_FROM_HAND,
        "a multicolored creature card from your hand"
    );

    public static final MagicTargetChoice BASIC_LAND_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_BASIC_LAND_CARD_FROM_HAND,
        "a basic land card from your hand"
    );
    
    public static final MagicTargetChoice LAND_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_LAND_CARD_FROM_LIBRARY,
        "a land card from your library"
    );
    
    public static final MagicTargetChoice BASIC_LAND_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_BASIC_LAND_CARD_FROM_LIBRARY,
        "a basic land card from your library"
    );
    
    public static final MagicTargetChoice FOREST_OR_ISLAND_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_FOREST_OR_ISLAND_CARD_FROM_LIBRARY,
        "a Forest or Island card from your library"
    );
    
    public static final MagicTargetChoice FOREST_OR_PLAINS_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_FOREST_OR_PLAINS_CARD_FROM_LIBRARY,
        "a Forest or Plains card from your library"
    );
    
    public static final MagicTargetChoice ISLAND_OR_MOUNTAIN_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ISLAND_OR_MOUNTAIN_CARD_FROM_LIBRARY,
        "a Island or Mountain card from your library"
    );
    
    public static final MagicTargetChoice SWAMP_OR_FOREST_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_SWAMP_OR_FOREST_CARD_FROM_LIBRARY,
        "a Swamp or Forest card from your library"
    );
    
    public static final MagicTargetChoice SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY,
        "a Swamp or Mountain card from your library"
    );
    
    public static final MagicTargetChoice PLAINS_OR_SWAMP_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_PLAINS_OR_SWAMP_CARD_FROM_LIBRARY,
        "a Plains or Swamp card from your library"
    );
    
    public static final MagicTargetChoice MOUNTAIN_OR_PLAINS_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_MOUNTAIN_OR_PLAINS_CARD_FROM_LIBRARY,
        "a Mountain or Plains card from your library"
    );
    
    public static final MagicTargetChoice MOUNTAIN_OR_FOREST_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_MOUNTAIN_OR_FOREST_CARD_FROM_LIBRARY,
        "a Mountain or Plains card from your library"
    );
    
    public static final MagicTargetChoice ISLAND_OR_SWAMP_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_ISLAND_OR_SWAMP_CARD_FROM_LIBRARY,
        "a Island or Swamp card from your library"
    );
    
    public static final MagicTargetChoice PLAINS_OR_ISLAND_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_PLAINS_OR_ISLAND_CARD_FROM_LIBRARY,
        "a Plains or Island card from your library"
    );
    
    public static final MagicTargetChoice PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY,
        "a Plains, Island, Swamp, or Mountain card from your library"
    );
    
    public static final MagicTargetChoice LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY,
        "a land card with a basic land type from your library"
    );

    public static final MagicTargetChoice LAND_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_LAND_CARD_FROM_HAND,
        "a land card from your hand"
    );

    public static final MagicTargetChoice GOBLIN_CARD_FROM_HAND = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GOBLIN_CARD_FROM_HAND,
        "a Goblin permanent card from your hand"
    );
    
    public static final MagicTargetChoice GOBLIN_CARD_FROM_LIBRARY = new MagicTargetChoice(
        MagicTargetFilter.TARGET_GOBLIN_CARD_FROM_LIBRARY,
        "a Goblin permanent card from your library"
    );

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
        final MagicTargetFilter<? extends MagicTarget> aTargetFilter,
        final boolean aTargeted,
        final MagicTargetHint aTargetHint,
        final String aTargetDescription
    ) {
        super("Choose " + aTargetDescription + '.');
        targetFilter = aTargetFilter;
        targeted     = aTargeted;
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


    private static final Map<String, MagicTargetChoice> factory =
        new HashMap<String, MagicTargetChoice>();

    static {
        // used by enchant property
        factory.put("creature", TARGET_CREATURE);
        factory.put("creature you control", TARGET_CREATURE_YOU_CONTROL);
        factory.put("creature opponent control", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
        factory.put("pos creature", POS_TARGET_CREATURE);
        factory.put("neg creature", NEG_TARGET_CREATURE);
        factory.put("neg tapped creature", NEG_TARGET_TAPPED_CREATURE);
        factory.put("pos nonblack creature", POS_TARGET_NONBLACK_CREATURE);

        factory.put("permanent", TARGET_PERMANENT);
        factory.put("pos permanent", POS_TARGET_PERMANENT);
        factory.put("neg permanent", NEG_TARGET_PERMANENT);

        factory.put("land", TARGET_LAND);
        factory.put("pos land", POS_TARGET_LAND);
        factory.put("neg land", NEG_TARGET_LAND);
        factory.put("neg nonbasic land", NEG_TARGET_NONBASIC_LAND);

        factory.put("artifact", TARGET_ARTIFACT);
        factory.put("pos artifact", POS_TARGET_ARTIFACT);
        factory.put("neg artifact", NEG_TARGET_ARTIFACT);

        factory.put("enchantment", TARGET_ENCHANTMENT);
        factory.put("pos enchantment", POS_TARGET_ENCHANTMENT);
        factory.put("neg enchantment", NEG_TARGET_ENCHANTMENT);

        factory.put("neg creature or land", NEG_TARGET_CREATURE_OR_LAND);
        factory.put("neg artifact or creature", NEG_TARGET_ARTIFACT_OR_CREATURE);

        // used by effect property
        factory.put("neg target equipment", NEG_TARGET_EQUIPMENT);
        factory.put("neg target artifact", NEG_TARGET_ARTIFACT);
        factory.put("neg target artifact you don't control", TARGET_ARTIFACT_YOU_DONT_CONTROL);
        factory.put("neg target artifact or enchantment", NEG_TARGET_ARTIFACT_OR_ENCHANTMENT);
        factory.put("neg target artifact or land", NEG_TARGET_ARTIFACT_OR_LAND);
        factory.put("neg target artifact, enchantment, or land", NEG_TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND);
        factory.put("neg target attacking creature", NEG_TARGET_ATTACKING_CREATURE);
        factory.put("neg target blocked creature", NEG_TARGET_BLOCKED_CREATURE);
        factory.put("neg target tapped creature", NEG_TARGET_TAPPED_CREATURE);
        factory.put("neg target creature", NEG_TARGET_CREATURE);
        factory.put("neg target creature or enchantment", NEG_TARGET_CREATURE_OR_ENCHANTMENT);
        factory.put("neg target creature or land", NEG_TARGET_CREATURE_OR_LAND);
        factory.put("neg target creature or planeswalker", NEG_TARGET_CREATURE_OR_PLANESWALKER);
        factory.put("neg target creature with power 4 or greater", NEG_TARGET_CREATURE_POWER_4_OR_MORE);
        factory.put("neg target creature with flying", NEG_TARGET_CREATURE_WITH_FLYING);
        factory.put("neg target nonartifact creature", NEG_TARGET_NONARTIFACT_CREATURE);
        factory.put("neg target nonblack creature", NEG_TARGET_NONBLACK_CREATURE);
        factory.put("neg target nonartifact, nonblack creature", NEG_TARGET_NONARTIFACT_NONBLACK_CREATURE);
        factory.put("neg target noncreature permanent", NEG_TARGET_NONCREATURE);
        factory.put("neg target human creature", NEG_TARGET_HUMAN_CREATURE);
        factory.put("neg target green or white creature", NEG_TARGET_GREEN_OR_WHITE_CREATURE);
        factory.put("neg target black or red permanent", NEG_TARGET_BLACK_RED_PERMANENT);
        factory.put("neg target land", NEG_TARGET_LAND);
        factory.put("neg target spirit or enchantment", NEG_TARGET_SPIRIT_OR_ENCHANTMENT);
        factory.put("neg target permanent", NEG_TARGET_PERMANENT);
        factory.put("neg target enchantment", NEG_TARGET_ENCHANTMENT);
        factory.put("neg target non-vampire, non-werewolf, non-zombie creature", NEG_TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE);
        factory.put("neg target vampire, werewolf, or zombie", NEG_TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE);
        factory.put("neg target creature with converted mana cost 3 or less", NEG_TARGET_CREATURE_CONVERTED_3_OR_LESS);
        factory.put("neg target artifact or creature", NEG_TARGET_ARTIFACT_OR_CREATURE);
        factory.put("neg target nonland permanent with converted mana cost 3 or less", NEG_TARGET_NONLAND_PERMANENT_CMC_LEQ_3);
        factory.put("neg target land or nonblack creature", NEG_TARGET_LAND_OR_NONBLACK_CREATURE);

        factory.put("neg target spell", NEG_TARGET_SPELL);
        factory.put("neg target spell you don't control", TARGET_SPELL_YOU_DONT_CONTROL);
        factory.put("neg target spell with converted mana cost 2", TARGET_SPELL_WITH_CMC_EQ_2);
        factory.put("neg target red or green spell", NEG_TARGET_RED_GREEN_SPELL);
        factory.put("neg target noncreature spell", NEG_TARGET_NONCREATURE_SPELL);
        factory.put("neg target creature spell", NEG_TARGET_CREATURE_SPELL);
        factory.put("neg target instant spell", NEG_TARGET_INSTANT_SPELL);
    }

    public static MagicTargetChoice build(final String arg) {
        if (factory.containsKey(arg)) {
            return factory.get(arg);
        } else {
            throw new RuntimeException("unknown target choice \"" + arg + "\"");
        }
    }
}

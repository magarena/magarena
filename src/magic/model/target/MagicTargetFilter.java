package magic.model.target;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.EnumSet;
import java.util.HashSet;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetFilterFactory.Control;

public interface MagicTargetFilter<T extends MagicTarget> {

    boolean acceptType(final MagicTargetType targetType);

    boolean accept(final MagicGame game,final MagicPlayer player,final T target);

    List<T> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint);

    MagicPermanentFilterImpl NONE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return false;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return false;
        }
    };

    MagicPermanentFilterImpl ANY = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return true;
        }
    };
    
    MagicStackFilterImpl SPELL_OR_ABILITY_THAT_TARGETS_PERMANENTS =new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
            return tchoice.isValid() && 
                   tchoice.isTargeted() &&
                   tchoice.getTargetFilter().acceptType(MagicTargetType.Permanent);
        }
    };

    MagicStackFilterImpl TARGET_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell();
        }
    };

    MagicStackFilterImpl TARGET_SPELL_THAT_TARGETS_PLAYER=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
            return target.isSpell() &&
                   tchoice.isValid() &&
                   tchoice.isTargeted() &&
                   tchoice.getTargetFilter().acceptType(MagicTargetType.Player);
        }
    };

    MagicStackFilterImpl TARGET_SPELL_YOU_DONT_CONTROL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getController() != player;
        }
    };
    
    MagicStackFilterImpl TARGET_SPELL_WITH_CMC_EQ_1 = new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() == 1;
        }
    };
    
    MagicStackFilterImpl TARGET_SPELL_WITH_CMC_EQ_2 = new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() == 2;
        }
    };

    MagicStackFilterImpl TARGET_SPELL_WITH_X_COST=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getCardDefinition().hasX();
        }
    };

    MagicStackFilterImpl TARGET_RED_OR_GREEN_SPELL = MagicTargetFilterFactory.spellOr(MagicColor.Red, MagicColor.Green);
    
    MagicStackFilterImpl TARGET_NONBLUE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Blue);
        }
    };
    
    MagicStackFilterImpl TARGET_CREATURE_OR_AURA_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Creature, MagicSubType.Aura);
    
    MagicStackFilterImpl TARGET_CREATURE_OR_SORCERY_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Creature, MagicType.Sorcery);

    MagicStackFilterImpl TARGET_NONCREATURE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   !itemOnStack.isSpell(MagicType.Creature);
        }
    };

    MagicStackFilterImpl TARGET_INSTANT_OR_SORCERY_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Instant, MagicType.Sorcery);

    MagicStackFilterImpl TARGET_SPIRIT_OR_ARCANE_SPELL= MagicTargetFilterFactory.spellOr(MagicSubType.Spirit, MagicSubType.Arcane);

    MagicStackFilterImpl TARGET_ARTIFACT_OR_ENCHANTMENT_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Artifact, MagicType.Enchantment);
    
    MagicPlayerFilterImpl TARGET_PLAYER=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return true;
        }
    };

    MagicPlayerFilterImpl TARGET_OPPONENT=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return target!=player;
        }
    };

    MagicTargetFilterImpl TARGET_SPELL_OR_PERMANENT=new MagicTargetFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isSpell()||target.isPermanent();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack ||
                   targetType==MagicTargetType.Permanent;
        }
    };

    MagicPermanentFilterImpl TARGET_BLACK_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Black, Control.Any);
    
    MagicPermanentFilterImpl TARGET_WHITE_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.White, Control.Any);

    MagicPermanentFilterImpl TARGET_RED_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Red, Control.Any);
    
    MagicPermanentFilterImpl TARGET_GREEN_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Green, Control.Any);
    
    MagicPermanentFilterImpl TARGET_BLUE_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Blue, Control.Any);
    
    MagicPermanentFilterImpl TARGET_WHITE_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.White, Control.You);

    MagicPermanentFilterImpl TARGET_BLUE_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.Blue, Control.You);

    MagicPermanentFilterImpl TARGET_BLACK_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.Black, Control.You);

    MagicPermanentFilterImpl TARGET_GREEN_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.Green, Control.You);

    MagicPermanentFilterImpl TARGET_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
    };

    MagicPermanentFilterImpl TARGET_BLACK_RED_PERMANENT = MagicTargetFilterFactory.permanentOr(MagicColor.Black, MagicColor.Red, Control.Any);

    MagicPermanentFilterImpl TARGET_NONBASIC_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic);
        }
    };
    
    MagicPermanentFilterImpl TARGET_NONBASIC_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic) && target.isController(player);
        }
    };
    
    MagicPermanentFilterImpl TARGET_BASIC_LAND = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Basic, Control.Any);
    
    MagicPermanentFilterImpl TARGET_SNOW_LAND = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Snow, Control.Any);
    
    MagicPermanentFilterImpl TARGET_BASIC_LAND_YOU_CONTROL = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Basic, Control.You);
    
    MagicPermanentFilterImpl TARGET_SNOW_LAND_YOU_CONTROL = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Snow, Control.You);

    MagicPermanentFilterImpl TARGET_LAND = MagicTargetFilterFactory.permanent(MagicType.Land, Control.Any);

    MagicPermanentFilterImpl TARGET_LAND_OR_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() || (!target.hasColor(MagicColor.Black) && target.isCreature());
        }
    };

    MagicPermanentFilterImpl TARGET_NONLAND_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand();
        }
    };
    
    MagicPermanentFilterImpl TARGET_NONLAND_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && target.isController(player);
        }
    };
    

    MagicPermanentFilterImpl TARGET_NONLAND_NONTOKEN_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && !target.isToken();
        }
    };
    
    MagicPermanentFilterImpl TARGET_NONTOKEN_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && !target.isToken();
        }
    };

    MagicPermanentFilterImpl TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && target.isOpponent(player);
        }
    };

    MagicPermanentFilterImpl TARGET_NONCREATURE_ARTIFACT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() && !target.isCreature();
        }
    };

    MagicPermanentFilterImpl TARGET_ARTIFACT = MagicTargetFilterFactory.permanent(MagicType.Artifact, Control.Any);

    MagicPermanentFilterImpl TARGET_ARTIFACT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Artifact, Control.You);

    MagicPermanentFilterImpl TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.permanent(MagicType.Artifact, Control.Opp);

    MagicPermanentFilterImpl TARGET_ARTIFACT_CREATURE = MagicTargetFilterFactory.permanentAnd(MagicType.Artifact, MagicType.Creature, Control.Any);

    MagicPermanentFilterImpl TARGET_ARTIFACT_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.permanentAnd(MagicType.Artifact, MagicType.Creature, Control.You);

    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_CREATURE = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Creature, Control.Any);
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Creature, Control.You);

    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_CREATURE_OR_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() ||
                   target.isCreature() ||
                   target.isLand();
        }
    };

    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_ENCHANTMENT = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Enchantment, Control.Any);

    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_LAND = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Land, Control.Any);

    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() ||
                   target.isArtifact() ||
                   target.isEnchantment();
        }
    };

    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_CREATURE_OR_ENCHANTMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() ||
                   target.isArtifact() ||
                   target.isEnchantment();
        }
    };
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Enchantment, Control.Opp);
    
    MagicPermanentFilterImpl TARGET_1_1_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.getPower() == 1 && target.getToughness() == 1;
        }
    };

    MagicPermanentFilterImpl TARGET_NONCREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isCreature();
        }
    };

    MagicTargetFilterImpl TARGET_CREATURE_OR_PLAYER=new MagicTargetFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isPlayer() ||
                   target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent ||
                   targetType==MagicTargetType.Player;
        }
    };
    
    MagicTargetFilterImpl TARGET_SLIVER_CREATURE_OR_PLAYER=new MagicTargetFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isPlayer() ||
                   (target.isCreature() && target.hasSubType(MagicSubType.Sliver));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent ||
                   targetType==MagicTargetType.Player;
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_OR_LAND = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Land, Control.Any);
    
    MagicPermanentFilterImpl TARGET_CREATURE_OR_PLANESWALKER = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Planeswalker, Control.Any);

    MagicPermanentFilterImpl TARGET_CREATURE_OR_ENCHANTMENT = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Enchantment, Control.Any);
    
    MagicPermanentFilterImpl TARGET_CREATURE_OR_ENCHANTMENT_YOU_CONTROL = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Enchantment, Control.You);

    MagicPermanentFilterImpl TARGET_ENCHANTMENT = MagicTargetFilterFactory.permanent(MagicType.Enchantment, Control.Any);

    MagicPermanentFilterImpl TARGET_ENCHANTMENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Enchantment, Control.You);

    MagicPermanentFilterImpl TARGET_SPIRIT_OR_ENCHANTMENT = MagicTargetFilterFactory.permanentOr(MagicType.Enchantment, MagicSubType.Spirit, Control.Any);

    MagicPermanentFilterImpl TARGET_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player);
        }
    };
    
    MagicPermanentFilterImpl TARGET_PERMANENT_AN_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isOpponent(player);
        }
    };

    MagicPermanentFilterImpl TARGET_PERMANENT_YOU_OWN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isOwner(player);
        }
    };

    MagicPermanentFilterImpl TARGET_LAND_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Land, Control.You);

    MagicPermanentFilterImpl TARGET_FOREST = MagicTargetFilterFactory.permanent(MagicSubType.Forest, Control.Any);

    MagicPermanentFilterImpl TARGET_FOREST_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Forest, Control.You);

    MagicPermanentFilterImpl TARGET_ISLAND_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Island, Control.You);
    
    MagicPermanentFilterImpl TARGET_ISLAND = MagicTargetFilterFactory.permanent(MagicSubType.Island, Control.Any);

    MagicPermanentFilterImpl TARGET_MOUNTAIN_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Mountain, Control.You);

    MagicPermanentFilterImpl TARGET_PLAINS = MagicTargetFilterFactory.permanent(MagicSubType.Plains, Control.Any);
    
    MagicPermanentFilterImpl TARGET_AURA = MagicTargetFilterFactory.permanent(MagicSubType.Aura, Control.Any);

    MagicPermanentFilterImpl TARGET_SWAMP_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Swamp, Control.You);

    MagicPermanentFilterImpl TARGET_CREATURE_TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isToken();
        }
    };

    MagicPermanentFilterImpl TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasType(MagicType.Legendary) &&
                   target.isCreature();
        }
    };

    MagicPermanentFilterImpl TARGET_NON_DEMON_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return !target.hasSubType(MagicSubType.Demon) &&
                        target.isCreature();
        }
    };

    MagicPermanentFilterImpl TARGET_BLUE_OR_BLACK_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creatureOr(MagicColor.Blue, MagicColor.Black, Control.You);
    
    MagicPermanentFilterImpl TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.Green, Control.You);
    
    MagicPermanentFilterImpl TARGET_RED_OR_GREEN_CREATURE_AN_OPPONENT_CONTROLS = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.Green, Control.Opp);
    
    MagicPermanentFilterImpl TARGET_FOREST_OR_PLAINS = MagicTargetFilterFactory.permanentOr(MagicSubType.Forest, MagicSubType.Plains, Control.Any);
    
    MagicPermanentFilterImpl TARGET_FOREST_OR_PLAINS_YOU_CONTROL = MagicTargetFilterFactory.permanentOr(MagicSubType.Forest, MagicSubType.Plains, Control.You);
    
    MagicPermanentFilterImpl TARGET_RED_OR_GREEN_CREATURE = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.Green, Control.Any);

    MagicPermanentFilterImpl TARGET_GREEN_OR_WHITE_CREATURE = MagicTargetFilterFactory.creatureOr(MagicColor.Green, MagicColor.White, Control.Any);

    MagicPermanentFilterImpl TARGET_WHITE_OR_BLUE_CREATURE = MagicTargetFilterFactory.creatureOr(MagicColor.White, MagicColor.Blue, Control.Any);;
    
    MagicPermanentFilterImpl TARGET_RED_OR_WHITE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.White, Control.You);

    MagicPermanentFilterImpl TARGET_BLACK_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Black, Control.Any);

    MagicPermanentFilterImpl TARGET_WHITE_CREATURE = MagicTargetFilterFactory.creature(MagicColor.White, Control.Any);

    MagicPermanentFilterImpl TARGET_BLUE_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Blue, Control.Any);

    MagicPermanentFilterImpl TARGET_BLACK_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Black, Control.You);

    MagicPermanentFilterImpl TARGET_BLUE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Blue, Control.You);

    MagicPermanentFilterImpl TARGET_GREEN_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Green, Control.You);

    MagicPermanentFilterImpl TARGET_GREEN_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Green, Control.Any);

    MagicPermanentFilterImpl TARGET_RED_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Red, Control.You);

    MagicPermanentFilterImpl TARGET_RED_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Red, Control.Any);

    MagicPermanentFilterImpl TARGET_WHITE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.White, Control.You);

    MagicPermanentFilterImpl TARGET_DRAGON_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Dragon, Control.You);
    
    MagicPermanentFilterImpl TARGET_GOBLIN_PERMANENT = MagicTargetFilterFactory.permanent(MagicSubType.Goblin, Control.Any);

    MagicPermanentFilterImpl TARGET_DJINN_OR_EFREET = MagicTargetFilterFactory.permanentOr(MagicSubType.Djinn, MagicSubType.Efreet, Control.Any);

    MagicPermanentFilterImpl TARGET_SQUIRREL_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Squirrel, Control.Any);

    MagicPermanentFilterImpl TARGET_CAT_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Cat, Control.You);

    MagicPermanentFilterImpl TARGET_MYR_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Myr, Control.You);
    
    MagicPermanentFilterImpl TARGET_LEGENDARY_SAMURAI_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.hasSubType(MagicSubType.Samurai) &&
                   target.hasType(MagicType.Legendary);
        }
    };

    MagicPermanentFilterImpl TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Insect) ||
                   target.hasSubType(MagicSubType.Rat) ||
                   target.hasSubType(MagicSubType.Spider) ||
                   target.hasSubType(MagicSubType.Squirrel);
        }
    };

    MagicPermanentFilterImpl TARGET_VAMPIRE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Vampire, Control.You);

    MagicPermanentFilterImpl TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Werewolf) ||
                    target.hasSubType(MagicSubType.Zombie);
        }
    };

    MagicPermanentFilterImpl TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Vampire) &&
                   !target.hasSubType(MagicSubType.Werewolf) &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    MagicPermanentFilterImpl TARGET_NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    MagicPermanentFilterImpl TARGET_HUMAN = MagicTargetFilterFactory.permanent(MagicSubType.Human, Control.Any);

    MagicPermanentFilterImpl TARGET_HUMAN_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Human, Control.Any);

    MagicPermanentFilterImpl TARGET_HUMAN_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Human, Control.You);

    MagicPermanentFilterImpl TARGET_NONHUMAN_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
    };
    
    MagicPermanentFilterImpl TARGET_ENCHANTED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isEnchanted(); 
        }
    };

    MagicPermanentFilterImpl TARGET_NONHUMAN_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
    };

    MagicPermanentFilterImpl TARGET_NON_ZOMBIE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    MagicPermanentFilterImpl TARGET_NON_VAMPIRE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasSubType(MagicSubType.Vampire);
        }
    };

    MagicPermanentFilterImpl TARGET_ZOMBIE_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Zombie, Control.You);

    MagicPermanentFilterImpl TARGET_ZOMBIE = MagicTargetFilterFactory.permanent(MagicSubType.Zombie, Control.Any);

    MagicPermanentFilterImpl TARGET_KOR_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Kor, Control.You);

    MagicPermanentFilterImpl TARGET_WOLF_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Wolf, Control.You);

    MagicPermanentFilterImpl TARGET_SLIVER = MagicTargetFilterFactory.permanent(MagicSubType.Sliver, Control.Any);
    
    MagicPermanentFilterImpl TARGET_SLIVER_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Sliver, Control.Any);
    
    MagicPermanentFilterImpl TARGET_SLIVER_PERMANENT = MagicTargetFilterFactory.permanent(MagicSubType.Sliver, Control.Any);

    MagicPermanentFilterImpl TARGET_ELF = MagicTargetFilterFactory.permanent(MagicSubType.Elf, Control.Any);

    MagicPermanentFilterImpl TARGET_ELF_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Elf, Control.You);
    
    MagicPermanentFilterImpl TARGET_BARBARIAN_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Barbarian, Control.Any);

    MagicPermanentFilterImpl TARGET_ALLY_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Ally, Control.You);
    
    MagicPermanentFilterImpl TARGET_ALLY_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Ally, Control.You);

    MagicPermanentFilterImpl TARGET_FAERIE_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Faerie, Control.You);

    MagicPermanentFilterImpl TARGET_SPIRIT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Spirit, Control.You);

    MagicPermanentFilterImpl TARGET_MODULAR_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicAbility.Modular, Control.You);

    MagicPermanentFilterImpl TARGET_PLANT_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Plant, Control.You);

    MagicPermanentFilterImpl TARGET_CREATURE = MagicTargetFilterFactory.permanent(MagicType.Creature, Control.Any);
    
    MagicPermanentFilterImpl WORLD = MagicTargetFilterFactory.permanent(MagicType.World, Control.Any);

    MagicPermanentFilterImpl TARGET_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Creature, Control.You);

    MagicPermanentFilterImpl TARGET_CREATURE_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.permanent(MagicType.Creature, Control.Opp);

    MagicPermanentFilterImpl TARGET_TAPPED_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Tapped, Control.Any);

    MagicPermanentFilterImpl TARGET_TAPPED_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicPermanentState.Tapped, Control.You);

    MagicPermanentFilterImpl TARGET_UNTAPPED_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isTapped();
        }
    };
    
    MagicPermanentFilterImpl TARGET_UNTAPPED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isUntapped() &&
                   target.isController(player);
        }
    };

    MagicPermanentFilterImpl TARGET_NONWHITE_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.White);
        }
    };

    MagicPermanentFilterImpl TARGET_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Black);
        }
    };

    MagicPermanentFilterImpl TARGET_NONARTIFACT_CREATURE=new MagicPermanentFilterImpl () {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact();
        }
    };

    MagicPermanentFilterImpl TARGET_NONARTIFACT_NONBLACK_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact() &&
                   !target.hasColor(MagicColor.Black);
        }
    };

    MagicPermanentFilterImpl TARGET_NON_ANGEL_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Angel);
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_WITHOUT_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying);
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS =new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying) &&
                   target.isOpponent(player);
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_WITH_FLYING = MagicTargetFilterFactory.creature(MagicAbility.Flying, Control.Any);

    MagicPermanentFilterImpl TARGET_CREATURE_WITH_FLYING_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicAbility.Flying, Control.You);

    MagicPermanentFilterImpl TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.creature(MagicAbility.Flying, Control.Opp);

    MagicPermanentFilterImpl TARGET_CREATURE_WITH_SHADOW = MagicTargetFilterFactory.creature(MagicAbility.Shadow, Control.Any);

    MagicPermanentFilterImpl TARGET_CREATURE_WITHOUT_SHADOW = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Shadow);
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_CONVERTED_3_OR_LESS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getConvertedCost() <= 3;
        }
    };
    
    MagicCardFilterImpl TARGET_CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 3 && target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    MagicPermanentFilterImpl TARGET_CREATURE_CONVERTED_2_OR_LESS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getConvertedCost() <= 2;
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_POWER_2_OR_LESS = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getPower() <= 2;
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_POWER_4_OR_MORE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getPower() >= 4;
        }
    };

    MagicPermanentFilterImpl TARGET_CREATURE_PLUSONE_COUNTER = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getCounters(MagicCounterType.PlusOne) > 0;
        }
    };

    MagicPermanentFilterImpl TARGET_ATTACKING_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Attacking, Control.Any);

    MagicPermanentFilterImpl TARGET_BLOCKING_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Blocking, Control.Any);

    MagicPermanentFilterImpl TARGET_ATTACKING_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicPermanentState.Attacking, Control.You);

    MagicPermanentFilterImpl TARGET_ATTACKING_CREATURE_WITH_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasAbility(MagicAbility.Flying);
        }
    };
    
    MagicPermanentFilterImpl TARGET_ATTACKING_CREATURE_WITHOUT_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   !target.hasAbility(MagicAbility.Flying);
        }
    };

    MagicPermanentFilterImpl TARGET_ATTACKING_GOBLIN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasSubType(MagicSubType.Goblin);
        }
    };
    
    MagicPermanentFilterImpl TARGET_ATTACKING_AUROCHS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isAttacking() &&
                   target.hasSubType(MagicSubType.Aurochs);
        }
    };
    
    MagicPermanentFilterImpl TARGET_ATTACKING_OR_BLOCKING_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.isAttacking() ||
                    target.isBlocking());
        }
    };

    MagicPermanentFilterImpl TARGET_ATTACKING_OR_BLOCKING_SPIRIT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasSubType(MagicSubType.Spirit) &&
                   (target.isAttacking() ||
                    target.isBlocking());
        }
    };

    MagicPermanentFilterImpl TARGET_ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   (target.isAttacking() || target.isBlocking());
        }
    };
    
    MagicPermanentFilterImpl UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&  
                   target.isAttacking() &&
                   target.hasState(MagicPermanentState.Blocked) == false;
        }
    };
    
    MagicPermanentFilterImpl TARGET_KALDRA_EQUIPMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isEquipment() &&
                    (target.getName().equals("Sword of Kaldra") || 
                     target.getName().equals("Shield of Kaldra") || 
                     target.getName().equals("Helm of Kaldra"));
        }
    };

    MagicPermanentFilterImpl TARGET_BLOCKED_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Blocked, Control.Any);

    MagicCardFilterImpl TARGET_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    MagicCardFilterImpl TARGET_CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                   targetType == MagicTargetType.OpponentsGraveyard;
        }
    };

    MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicType.Creature);
    
    MagicCardFilterImpl PAYABLE_CREATURE_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && target.getCost().getCondition().accept(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    MagicCardFilterImpl PAYABLE_INSTANT_OR_SORCERY_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return (target.hasType(MagicType.Instant) || target.hasType(MagicType.Sorcery)) && target.getCost().getCondition().accept(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && target.hasAbility(MagicAbility.Infect);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    MagicCardFilterImpl TARGET_PERMANENT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return !target.getCardDefinition().isSpell();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.getConvertedCost() <= 3 && !cardDefinition.isSpell();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 2 && target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    MagicPermanentFilterImpl TARGET_NONLAND_PERMANENT_CMC_LEQ_3 = new MagicTargetFilter.MagicCMCPermanentFilter(
        MagicTargetFilter.TARGET_NONLAND_PERMANENT,
        MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL,
        3
    );

    MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.OpponentsGraveyard, MagicType.Creature);

    MagicCardFilterImpl TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.cardOr(MagicTargetType.Graveyard, MagicType.Instant, MagicType.Sorcery);
    
    MagicCardFilterImpl TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD = MagicTargetFilterFactory.cardOr(MagicTargetType.OpponentsGraveyard, MagicType.Instant, MagicType.Sorcery);
    
    MagicCardFilterImpl TARGET_ARTIFACT_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicType.Artifact);

    MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    MagicCardFilterImpl TARGET_ENCHANTMENT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Enchantment);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    MagicCardFilterImpl TARGET_INSTANT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Instant);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    MagicCardFilterImpl TARGET_SORCERY_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
   

    MagicCardFilterImpl LAND_CARD_FROM_YOUR_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicType.Land);

    MagicCardFilterImpl TARGET_LAND_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Land);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    MagicCardFilterImpl TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) ||
                   target.hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_ZOMBIE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicSubType.Zombie);

    MagicCardFilterImpl TARGET_ZOMBIE_CREATURE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.creatureCard(MagicTargetType.Graveyard, MagicSubType.Zombie);

    MagicCardFilterImpl TARGET_SPIRIT_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicSubType.Spirit);
    
    MagicCardFilterImpl TARGET_HUMAN_CREATURE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.creatureCard(MagicTargetType.Graveyard, MagicSubType.Human);
    
    MagicCardFilterImpl TARGET_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    MagicCardFilterImpl TARGET_BLUE_CARD_FROM_HAND = MagicTargetFilterFactory.card(MagicTargetType.Hand, MagicColor.Blue);

      MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_HAND = MagicTargetFilterFactory.card(MagicTargetType.Hand, MagicType.Creature);

    MagicCardFilterImpl TARGET_BLUE_OR_RED_CREATURE_CARD_FROM_HAND = MagicTargetFilterFactory.creatureCardOr(MagicTargetType.Hand, MagicColor.Blue, MagicColor.Red);

    MagicPermanentFilterImpl TARGET_MULTICOLORED_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent);
        }
    };
    
    MagicPermanentFilterImpl TARGET_MONOCOLORED_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMono(permanent) && permanent.isCreature();
        }
    };
    
    MagicPermanentFilterImpl TARGET_MULTICOLORED_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent) && permanent.isController(player);
        }
    };

    MagicCardFilterImpl TARGET_MULTICOLORED_CREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && MagicColor.isMulti(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    MagicCardFilterImpl TARGET_BASIC_LAND_CARD_FROM_HAND = MagicTargetFilterFactory.cardAnd(MagicTargetType.Hand, MagicType.Land, MagicType.Basic);
    
    MagicCardFilterImpl TARGET_BASIC_LAND_CARD_FROM_LIBRARY = MagicTargetFilterFactory.cardAnd(MagicTargetType.Library, MagicType.Land, MagicType.Basic);
    
    MagicCardFilterImpl TARGET_PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Plains) ||
                   target.hasSubType(MagicSubType.Island) ||
                   target.hasSubType(MagicSubType.Swamp)  ||
                   target.hasSubType(MagicSubType.Mountain);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };
    
    MagicCardFilterImpl TARGET_LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Plains) ||
                   target.hasSubType(MagicSubType.Island) ||
                   target.hasSubType(MagicSubType.Swamp)  ||
                   target.hasSubType(MagicSubType.Forest) ||
                   target.hasSubType(MagicSubType.Mountain);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };
    
    MagicPermanentFilterImpl TARGET_UNPAIRED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.isPaired();
        }
    };

    MagicPermanentFilterImpl TARGET_UNPAIRED_SOULBOND_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.hasAbility(MagicAbility.Soulbond) &&
                   !target.isPaired();
        }
    };

    // Permanent reference can not be used because game is copied.
    public static final class MagicOtherCardTargetFilter extends MagicCardFilterImpl {

        private final MagicCardFilterImpl targetFilter;
        private final long id;

        public MagicOtherCardTargetFilter(final MagicCardFilterImpl aTargetFilter,final MagicCard invalidCard) {
            targetFilter = aTargetFilter;
            id = invalidCard.getId();
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return targetFilter.accept(game,player,target) &&
                   target.getId() != id;
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };
    
    // Permanent reference can not be used because game is copied.
    public static final class MagicPermanentTargetFilter extends MagicPermanentFilterImpl {

        private final long id;

        public MagicPermanentTargetFilter(final MagicPermanent validPermanent) {
            id = validPermanent.getId();
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isPermanent() &&
                   target.getId() == id;
        }
    };

    public static final class MagicPowerTargetFilter extends MagicPermanentFilterImpl {

        private final MagicPermanentFilterImpl targetFilter;
        private final int power;

        public MagicPowerTargetFilter(final MagicPermanentFilterImpl targetFilter,final int power) {
            this.targetFilter = targetFilter;
            this.power = power;
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return targetFilter.accept(game,player,target) &&
                   target.getPower() <= power;
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };

    enum Operator {
        LESS_THAN() {
            public boolean cmp(final int v1, final int v2) {
                return v1 < v2;
            }
        },
        LESS_THAN_OR_EQUAL() {
            public boolean cmp(final int v1, final int v2) {
                return v1 <= v2;
            }
        },
        EQUAL() {
            public boolean cmp(final int v1, final int v2) {
                return v1 == v2;
            }
        };
        public abstract boolean cmp(final int v1, final int v2);
    }

    public static final class MagicCMCCardFilter extends MagicCardFilterImpl {

        private final MagicTargetFilter<MagicCard> targetFilter;
        private final Operator operator;
        private final int cmc;

        public MagicCMCCardFilter(final MagicTargetFilter<MagicCard> targetFilter,final Operator operator,final int cmc) {
            this.targetFilter = targetFilter;
            this.operator = operator;
            this.cmc = cmc;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return targetFilter.accept(game,player,target) &&
                   operator.cmp(target.getCardDefinition().getConvertedCost(), cmc) ;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };

    public static final class MagicCMCPermanentFilter extends MagicPermanentFilterImpl {

        private final MagicTargetFilter<MagicPermanent> targetFilter;
        private final Operator operator;
        private final int cmc;

        public MagicCMCPermanentFilter(final MagicTargetFilter<MagicPermanent> targetFilter,final Operator operator,final int cmc) {
            this.targetFilter = targetFilter;
            this.operator = operator;
            this.cmc = cmc;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return targetFilter.accept(game,player,target) &&
                   operator.cmp(target.getConvertedCost(), cmc) ;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };

    public static final class NameTargetFilter extends MagicPermanentFilterImpl {

        private final String name;
        private final MagicTargetFilter<MagicPermanent> targetFilter;
        
        public NameTargetFilter(final String aName) {
            this(ANY, aName);
        }

        public NameTargetFilter(final MagicTargetFilter<MagicPermanent> aTargetFilter, final String aName) {
            name = aName;
            targetFilter = aTargetFilter;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return name.equals(target.getName()) && targetFilter.accept(game, player, target);
        }
    };

    public static final class LegendaryCopiesFilter extends MagicPermanentFilterImpl {

        private final String name;

        public LegendaryCopiesFilter(final String name) {
            this.name=name;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return name.equals(target.getName()) &&
                   target.hasType(MagicType.Legendary) &&
                   target.isController(player);
        }
    };

    public static final class PlaneswalkerCopiesFilter extends MagicPermanentFilterImpl {

        private final Set<MagicSubType> pwTypes = EnumSet.noneOf(MagicSubType.class);

        public PlaneswalkerCopiesFilter(final MagicPermanent permanent) {
            for (final MagicSubType st : MagicSubType.ALL_PLANESWALKERS) {
                if (permanent.hasSubType(st)) {
                    pwTypes.add(st);
                }
            }
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            if (target.hasType(MagicType.Planeswalker) == false) {
                return false;
            }
            if (target.isController(player) == false) {
                return false;
            }
            for (final MagicSubType st : pwTypes) {
                if (target.hasSubType(st)) {
                    return true;
                }
            }
            return false;
        }
    };
}

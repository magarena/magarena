package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicCard;
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

public interface MagicTargetFilter<T extends MagicTarget> {
    
    MagicTargetFilter<MagicPermanent> NONE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return false;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return false;
        }
    };
    
    MagicTargetFilter<MagicItemOnStack> TARGET_SPELL=new MagicTargetFilter<MagicItemOnStack>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
    
    MagicTargetFilter<MagicItemOnStack> TARGET_RED_GREEN_SPELL=new MagicTargetFilter<MagicItemOnStack>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   (itemOnStack.hasColor(MagicColor.Red) ||
                    itemOnStack.hasColor(MagicColor.Green));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };

    MagicTargetFilter<MagicItemOnStack> TARGET_CREATURE_SPELL=new MagicTargetFilter<MagicItemOnStack>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };

    MagicTargetFilter<MagicItemOnStack> TARGET_NONCREATURE_SPELL=new MagicTargetFilter<MagicItemOnStack>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   !itemOnStack.isSpell(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
        
    MagicTargetFilter<MagicItemOnStack> TARGET_INSTANT_OR_SORCERY_SPELL=new MagicTargetFilter<MagicItemOnStack>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Instant) ||
                   itemOnStack.isSpell(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
    
    MagicTargetFilter<MagicItemOnStack> TARGET_ARTIFACT_SPELL = new MagicTargetFilter<MagicItemOnStack>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Stack;
        }
    };
    
    MagicTargetFilter<MagicPlayer> TARGET_PLAYER=new MagicTargetFilter<MagicPlayer>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Player;
        }        
    };

    MagicTargetFilter<MagicPlayer> TARGET_OPPONENT=new MagicTargetFilter<MagicPlayer>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return target!=player;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Player;
        }
    };
    
    MagicTargetFilter<MagicTarget> TARGET_SPELL_OR_PERMANENT=new MagicTargetFilter<MagicTarget>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isSpell()||target.isPermanent();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack ||
                   targetType==MagicTargetType.Permanent;
        }
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_BLACK_PERMANENT = Factory.permanent(MagicColor.Black, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_BLACK_PERMANENT_YOU_CONTROL = Factory.permanent(MagicColor.Black, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_GREEN_PERMANENT_YOU_CONTROL = Factory.permanent(MagicColor.Green, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_PERMANENT = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_BLACK_RED_PERMANENT=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasColor(MagicColor.Black) ||
                   target.hasColor(MagicColor.Red);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };

    MagicTargetFilter<MagicPermanent> TARGET_NONBASIC_LAND=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.getCardDefinition().isBasic();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_LAND = Factory.permanent(MagicType.Land, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_NONLAND_PERMANENT=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NONCREATURE_ARTIFACT=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() && !target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT = Factory.permanent(MagicType.Artifact, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_YOU_CONTROL = Factory.permanent(MagicType.Artifact, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS = Factory.permanent(MagicType.Artifact, Control.Opp);
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_CREATURE = new MagicTargetFilter<MagicPermanent> () {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() &&
                   target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_CREATURE_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isArtifact() &&
                   target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_OR_CREATURE=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact()||target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_OR_CREATURE_OR_LAND = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() ||
                   target.isCreature() ||
                   target.isLand();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_OR_ENCHANTMENT=new MagicTargetFilter<MagicPermanent> () {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() || 
                   target.isEnchantment();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_OR_LAND = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {    
            return target.isArtifact() || 
                   target.isLand();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() || 
                   target.isArtifact() || 
                   target.isEnchantment();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isOpponent(player) &&
                   (target.isArtifact() || target.isEnchantment());
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_1_1_CREATURE=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.getPower() == 1 && target.getToughness() == 1;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NONCREATURE=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicTarget> TARGET_CREATURE_OR_PLAYER=new MagicTargetFilter<MagicTarget>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isPlayer() || 
                   target.isCreature();
        }    
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent || 
                   targetType==MagicTargetType.Player;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_OR_LAND=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() || 
                   target.isLand();
        }        
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };

    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_OR_ENCHANTMENT=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() ||
                   target.isEnchantment();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_ENCHANTMENT = Factory.permanent(MagicType.Enchantment, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_ENCHANTMENT_YOU_CONTROL = Factory.permanent(MagicType.Enchantment, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ENCHANTMENT_YOUR_OPPONENT_CONTROLS = Factory.permanent(MagicType.Enchantment, Control.Opp);
    
    MagicTargetFilter<MagicPermanent> TARGET_SPIRIT_OR_ENCHANTMENT = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {    
            return target.hasSubType(MagicSubType.Spirit) ||
                   target.isEnchantment();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }
    };

    MagicTargetFilter<MagicPermanent> TARGET_EQUIPMENT = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isEquipment();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }
    };

    MagicTargetFilter<MagicPermanent> TARGET_PERMANENT_YOU_CONTROL=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_LAND_YOU_CONTROL = Factory.permanent(MagicType.Land, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_FOREST = Factory.permanent(MagicSubType.Forest, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_FOREST_YOU_CONTROL = Factory.permanent(MagicSubType.Forest, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ISLAND_YOU_CONTROL = Factory.permanent(MagicSubType.Island, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_MOUNTAIN_YOU_CONTROL = Factory.permanent(MagicSubType.Mountain, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_PLAINS_YOU_CONTROL = Factory.permanent(MagicSubType.Plains, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_SWAMP_YOU_CONTROL = Factory.permanent(MagicSubType.Swamp, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_TOKEN_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isToken();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasType(MagicType.Legendary) && 
                   target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NON_DEMON = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return !target.hasSubType(MagicSubType.Demon) &&
                        target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   (target.hasColor(MagicColor.Red) ||
                    target.hasColor(MagicColor.Green));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_GREEN_OR_WHITE_CREATURE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasColor(MagicColor.Green) || 
                    target.hasColor(MagicColor.White));
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_WHITE_OR_BLUE_CREATURE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasColor(MagicColor.White) || 
                    target.hasColor(MagicColor.Blue));
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_BLACK_CREATURE = Factory.creature(MagicColor.Black, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_WHITE_CREATURE = Factory.creature(MagicColor.White, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_BLACK_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Black, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_BLUE_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Blue, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_GREEN_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Green, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_RED_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Red, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_WHITE_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.White, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_BAT_YOU_CONTROL = Factory.creature(MagicSubType.Bat, Control.You);
   
    MagicTargetFilter<MagicPermanent> TARGET_BEAST_YOU_CONTROL = Factory.creature(MagicSubType.Beast, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_DRAGON_YOU_CONTROL = Factory.creature(MagicSubType.Dragon, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ELEMENTAL_YOU_CONTROL = Factory.creature(MagicSubType.Elemental, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_GOBLIN_CREATURE = Factory.creature(MagicSubType.Goblin, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_GOBLIN_YOU_CONTROL = Factory.creature(MagicSubType.Goblin, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_GOBLIN_OR_SHAMAN_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() && 
                   (target.hasSubType(MagicSubType.Goblin) ||
                    target.hasSubType(MagicSubType.Shaman));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_GOLEM_YOU_CONTROL = Factory.creature(MagicSubType.Golem, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_SQUIRREL_CREATURE = Factory.creature(MagicSubType.Squirrel, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_CAT_YOU_CONTROL = Factory.creature(MagicSubType.Cat, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_KNIGHT_CREATURE = Factory.creature(MagicSubType.Knight, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_KNIGHT_YOU_CONTROL = Factory.creature(MagicSubType.Knight, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ILLUSION_YOU_CONTROL = Factory.creature(MagicSubType.Illusion, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_MERFOLK_CREATURE = Factory.creature(MagicSubType.Merfolk, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_MERFOLK_YOU_CONTROL = Factory.creature(MagicSubType.Merfolk, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_MYR_YOU_CONTROL = Factory.creature(MagicSubType.Myr, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_SAMURAI = Factory.creature(MagicSubType.Samurai, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_SAMURAI_YOU_CONTROL = Factory.creature(MagicSubType.Samurai, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_SNAKE_YOU_CONTROL = Factory.creature(MagicSubType.Snake, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_TREEFOLK_OR_WARRIOR_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && 
                    target.isCreature() && 
                    (target.hasSubType(MagicSubType.Treefolk) ||
                     target.hasSubType(MagicSubType.Warrior));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_LEGENDARY_SAMURAI_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && 
                   target.isCreature() &&
                   target.hasSubType(MagicSubType.Samurai) &&
                   target.hasType(MagicType.Legendary);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_INSECT_RAT_SPIDER_OR_SQUIRREL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Insect) ||
                   target.hasSubType(MagicSubType.Rat) ||
                   target.hasSubType(MagicSubType.Spider) ||
                   target.hasSubType(MagicSubType.Squirrel);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_VAMPIRE_YOU_CONTROL = Factory.creature(MagicSubType.Vampire, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Werewolf) ||
                    target.hasSubType(MagicSubType.Zombie));
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Vampire) &&
                   !target.hasSubType(MagicSubType.Werewolf) &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_HUMAN = Factory.creature(MagicSubType.Human, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_HUMAN_YOU_CONTROL = Factory.creature(MagicSubType.Human, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_NONHUMAN_CREATURE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {        
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NONHUMAN_CREATURE_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {        
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_SOLDIER = Factory.creature(MagicSubType.Soldier, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_SOLDIER_YOU_CONTROL = Factory.creature(MagicSubType.Soldier, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_NON_ZOMBIE_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ZOMBIE_YOU_CONTROL = Factory.creature(MagicSubType.Zombie, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ZOMBIE = Factory.creature(MagicSubType.Zombie, Control.Any);

    MagicTargetFilter<MagicPermanent> TARGET_KOR_YOU_CONTROL = Factory.creature(MagicSubType.Kor, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_WOLF_YOU_CONTROL = Factory.creature(MagicSubType.Wolf, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_SLIVER = Factory.creature(MagicSubType.Sliver, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_ELF = Factory.creature(MagicSubType.Elf, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_ELF_YOU_CONTROL = Factory.creature(MagicSubType.Elf, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ALLY_YOU_CONTROL = Factory.creature(MagicSubType.Ally, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_FAERIE_YOU_CONTROL = Factory.creature(MagicSubType.Faerie, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_SPIRIT_YOU_CONTROL = Factory.creature(MagicSubType.Spirit, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_MODULAR_CREATURE_YOU_CONTROL = Factory.creature(MagicAbility.Modular, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_PLANT_YOU_CONTROL = Factory.creature(MagicSubType.Plant, Control.You);
        
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE = Factory.permanent(MagicType.Creature, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_YOU_CONTROL = Factory.permanent(MagicType.Creature, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_YOUR_OPPONENT_CONTROLS = Factory.permanent(MagicType.Creature, Control.Opp);
    
    MagicTargetFilter<MagicPermanent> TARGET_TAPPED_CREATURE = Factory.creature(MagicPermanentState.Tapped, Control.Any);

    MagicTargetFilter<MagicPermanent> TARGET_UNTAPPED_CREATURE=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isTapped();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NONWHITE_CREATURE=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.White);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NONBLACK_CREATURE=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Black);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_NONARTIFACT_CREATURE=new MagicTargetFilter<MagicPermanent> () {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   !target.isArtifact();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NONARTIFACT_NONBLACK_CREATURE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact() &&
                   !target.hasColor(MagicColor.Black);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_NON_ANGEL_CREATURE_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Angel);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_WITHOUT_FLYING=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_WITH_FLYING = Factory.creature(MagicAbility.Flying, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_WITH_FLYING_YOU_CONTROL = Factory.creature(MagicAbility.Flying, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS = Factory.creature(MagicAbility.Flying, Control.Opp);

    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_WITH_SHADOW = Factory.creature(MagicAbility.Shadow, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_WITHOUT_SHADOW = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Shadow);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_CONVERTED_3_OR_LESS=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   target.getCardDefinition().getCost().getConvertedCost() <= 3;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_POWER_2_OR_LESS = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getPower() <= 2;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_POWER_4_OR_MORE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   target.getPower() >= 4;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_CREATURE_PLUSONE_COUNTER = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getCounters(MagicCounterType.PlusOne) > 0;
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ATTACKING_CREATURE = Factory.creature(MagicPermanentState.Attacking, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_BLOCKING_CREATURE = Factory.creature(MagicPermanentState.Blocking, Control.Any);
    
    MagicTargetFilter<MagicPermanent> TARGET_ATTACKING_CREATURE_YOU_CONTROL = Factory.creature(MagicPermanentState.Attacking, Control.You);
    
    MagicTargetFilter<MagicPermanent> TARGET_ATTACKING_CREATURE_WITH_FLYING=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   target.isAttacking() && 
                   target.hasAbility(MagicAbility.Flying);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ATTACKING_GOBLIN=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   target.isAttacking() && 
                   target.hasSubType(MagicSubType.Goblin);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_ATTACKING_OR_BLOCKING_CREATURE=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   (target.isAttacking() || 
                    target.isBlocking());
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_ATTACKING_OR_BLOCKING_SPIRIT = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasSubType(MagicSubType.Spirit) &&
                   (target.isAttacking() ||
                    target.isBlocking());
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };

    MagicTargetFilter<MagicPermanent> TARGET_ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL=new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && 
                   target.isCreature() && 
                   (target.isAttacking() || target.isBlocking());
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_BLOCKED_CREATURE = Factory.creature(MagicPermanentState.Blocked, Control.Any);
    
    MagicTargetFilter<MagicCard> TARGET_CARD_FROM_GRAVEYARD=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_CARD_FROM_ALL_GRAVEYARDs = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }        
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                   targetType == MagicTargetType.OpponentsGraveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_CREATURE_CARD_FROM_GRAVEYARD=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isCreature() &&
                   cardDefinition.hasAbility(MagicAbility.Infect);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_PERMANENT_CARD_FROM_GRAVEYARD = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return !target.getCardDefinition().isSpell();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.getConvertedCost() <= 3 && !cardDefinition.isSpell();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }                        
    };

    MagicTargetFilter<MagicCard> TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.OpponentsGraveyard;
        }                        
    };

    MagicTargetFilter<MagicCard> TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition= target.getCardDefinition();
            return cardDefinition.hasType(MagicType.Instant) ||
                   cardDefinition.hasType(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition= target.getCardDefinition();
            return cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasType(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.OpponentsGraveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasType(MagicType.Enchantment);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_ARTIFACT_CARD_FROM_GRAVEYARD = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicTargetFilter<MagicCard> TARGET_ENCHANTMENT_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isEnchantment();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicTargetFilter<MagicCard> TARGET_INSTANT_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isInstant();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicTargetFilter<MagicCard> TARGET_SORCERY_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isSorcery();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicTargetFilter<MagicCard> TARGET_LAND_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isLand();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    MagicTargetFilter<MagicCard> TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition= target.getCardDefinition();
            return cardDefinition.isCreature()||cardDefinition.isArtifact();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicTargetFilter<MagicCard> TARGET_GOBLIN_CARD_FROM_GRAVEYARD=new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Goblin);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_ZOMBIE_CARD_FROM_GRAVEYARD = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Zombie);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_SPIRIT_CARD_FROM_GRAVEYARD = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Spirit);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_HUMAN_CARD_FROM_GRAVEYARD = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Human);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_CARD_FROM_HAND = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_CREATURE_CARD_FROM_HAND = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_GREEN_CREATURE_CARD_FROM_HAND = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isCreature() && MagicColor.Green.hasColor(cardDefinition.getColorFlags());
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_MULTICOLOR_CREATURE_CARD_FROM_HAND = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isCreature() && MagicColor.isMulti(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_BASIC_LAND_CARD_FROM_HAND = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isLand()&&cardDefinition.isBasic();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_LAND_CARD_FROM_HAND = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isLand();
        }    
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicTargetFilter<MagicCard> TARGET_GOBLIN_CARD_FROM_HAND = new MagicTargetFilter<MagicCard>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.hasSubType(MagicSubType.Goblin);
        }    
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_UNPAIRED_CREATURE_YOU_CONTROL = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && 
                   target.isCreature() &&
                   !target.isPaired();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    MagicTargetFilter<MagicPermanent> TARGET_UNPAIRED_SOULBOND_CREATURE = new MagicTargetFilter<MagicPermanent>() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && 
                   target.isCreature() &&
                   target.hasAbility(MagicAbility.Soulbond) &&
                   !target.isPaired();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };

    enum Control {
        Any,
        You,
        Opp
    }
    
    public static final class Factory {
        static final MagicTargetFilter<MagicPermanent> permanent(final MagicType type, final Control control) {
            return new MagicTargetFilter<MagicPermanent>() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.hasType(type) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
                public boolean acceptType(final MagicTargetType targetType) {    
                    return targetType == MagicTargetType.Permanent;
                }        
            };
        }
        static final MagicTargetFilter<MagicPermanent> permanent(final MagicColor color, final Control control) {
            return new MagicTargetFilter<MagicPermanent>() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.hasColor(color) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
                public boolean acceptType(final MagicTargetType targetType) {    
                    return targetType == MagicTargetType.Permanent;
                }        
            };
        }
        static final MagicTargetFilter<MagicPermanent> permanent(final MagicSubType subtype, final Control control) {
            return new MagicTargetFilter<MagicPermanent>() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.hasSubType(subtype) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
                public boolean acceptType(final MagicTargetType targetType) {    
                    return targetType == MagicTargetType.Permanent;
                }        
            };
        }
        static final MagicTargetFilter<MagicPermanent> creature(final MagicColor color, final Control control) {
            return new MagicTargetFilter<MagicPermanent>() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasColor(color) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
                public boolean acceptType(final MagicTargetType targetType) {    
                    return targetType == MagicTargetType.Permanent;
                }        
            };
        }
        static final MagicTargetFilter<MagicPermanent> creature(final MagicSubType subtype, final Control control) {
            return new MagicTargetFilter<MagicPermanent>() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasSubType(subtype) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
                public boolean acceptType(final MagicTargetType targetType) {    
                    return targetType == MagicTargetType.Permanent;
                }        
            };
        }
        static final MagicTargetFilter<MagicPermanent> creature(final MagicAbility ability, final Control control) {
            return new MagicTargetFilter<MagicPermanent>() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasAbility(ability) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
                public boolean acceptType(final MagicTargetType targetType) {    
                    return targetType == MagicTargetType.Permanent;
                }        
            };
        }
        static final MagicTargetFilter<MagicPermanent> creature(final MagicPermanentState state, final Control control) {
            return new MagicTargetFilter<MagicPermanent>() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasState(state) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
                public boolean acceptType(final MagicTargetType targetType) {    
                    return targetType == MagicTargetType.Permanent;
                }        
            };
        }
    }
    
    // Permanent reference can not be used because game is copied.
    public static final class MagicOtherPermanentTargetFilter implements MagicTargetFilter<MagicPermanent> {

        private final MagicTargetFilter<MagicPermanent> targetFilter;
        private final long id;        

        public MagicOtherPermanentTargetFilter(final MagicTargetFilter<MagicPermanent> targetFilter,final MagicPermanent invalidPermanent) {
            this.targetFilter=targetFilter;
            this.id=invalidPermanent.getId();
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return targetFilter.accept(game,player,target) && 
                   target.getId() != id;
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }        
    };
    
    // Permanent reference can not be used because game is copied.
    public static final class MagicPermanentTargetFilter implements MagicTargetFilter<MagicPermanent> {

        private final long id;        

        public MagicPermanentTargetFilter(final MagicPermanent validPermanent) {
            id = validPermanent.getId();
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isPermanent() && 
                   target.getId() == id;
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent;
        }        
    };
    
    public static final class MagicPowerTargetFilter implements MagicTargetFilter<MagicPermanent> {

        private final MagicTargetFilter<MagicPermanent> targetFilter;
        private final int power;        

        public MagicPowerTargetFilter(final MagicTargetFilter<MagicPermanent> targetFilter,final int power) {    
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

    public static final class MagicCMCTargetFilter<T extends MagicTarget> implements MagicTargetFilter<T> {
        
        private final MagicTargetFilter<T> targetFilter;
        private final Operator operator;
        private final int cmc;

        public MagicCMCTargetFilter(final MagicTargetFilter<T> targetFilter,final Operator operator,final int cmc) {    
            this.targetFilter = targetFilter;
            this.operator = operator;
            this.cmc = cmc;
        }
        
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final T target) {
            return targetFilter.accept(game,player,target) && 
                   operator.cmp(target.getCardDefinition().getConvertedCost(), cmc) ;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }        
    };

    public static final class CardTargetFilter implements MagicTargetFilter<MagicPermanent> {
     
        private final MagicCardDefinition cardDefinition;
          
        public CardTargetFilter(final MagicCardDefinition cardDefinition) {
            this.cardDefinition=cardDefinition;
        }
         
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
           return target.getCardDefinition()==cardDefinition;
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
           return targetType==MagicTargetType.Permanent;
        }
    };


    public static final class NameTargetFilter implements MagicTargetFilter<MagicPermanent> {
        
        private final String name;
        
        public NameTargetFilter(final String name) {
            this.name=name;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return name.equals(target.getName());
        }
        
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent;
        }
    };
    
    boolean accept(final MagicGame game,final MagicPlayer player,final T target);
    
    boolean acceptType(final MagicTargetType targetType);
}

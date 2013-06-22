package magic.model.target;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.EnumSet;

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
import magic.model.choice.MagicTargetChoice;

abstract class MagicTargetFilterImpl implements MagicTargetFilter<MagicTarget> {
    public List<MagicTarget> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicTarget> targets=new ArrayList<MagicTarget>();
        
        // Players
        if (acceptType(MagicTargetType.Player)) {
            for (final MagicPlayer targetPlayer : game.getPlayers()) {
                if (accept(game,player,targetPlayer) &&
                    targetHint.accept(player,targetPlayer)) {
                    targets.add(targetPlayer);
                }                
            }
        }
        
        // Permanents
        if (acceptType(MagicTargetType.Permanent)) {
            for (final MagicPlayer controller : game.getPlayers()) {
                for (final MagicPermanent targetPermanent : controller.getPermanents()) {
                    if (accept(game,player,targetPermanent) && 
                        targetHint.accept(player,targetPermanent)) {
                        targets.add(targetPermanent);
                    }
                }
            }
        }        

        // Items on stack
        if (acceptType(MagicTargetType.Stack)) {
            for (final MagicItemOnStack targetItemOnStack : game.getStack()) {
                if (accept(game,player,targetItemOnStack) && 
                    targetHint.accept(player,targetItemOnStack)) {
                    targets.add(targetItemOnStack);
                }
            }
        }
            
        // Cards in graveyard
        if (acceptType(MagicTargetType.Graveyard)) {
            for (final MagicCard targetCard : player.getGraveyard()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }                
            }
        }

        // Cards in opponent's graveyard
        if (acceptType(MagicTargetType.OpponentsGraveyard)) {
            for (final MagicCard targetCard : player.getOpponent().getGraveyard()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }                
            }
        }
        
        // Cards in hand
        if (acceptType(MagicTargetType.Hand)) {
            for (final MagicCard targetCard : player.getHand()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }                
            }
        }
                
        return targets;
    }
}

abstract class MagicCardFilterImpl implements MagicTargetFilter<MagicCard> {
    public List<MagicCard> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicCard> targets=new ArrayList<MagicCard>();
            
        // Cards in graveyard
        if (acceptType(MagicTargetType.Graveyard)) {
            for (final MagicCard targetCard : player.getGraveyard()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }                
            }
        }

        // Cards in opponent's graveyard
        if (acceptType(MagicTargetType.OpponentsGraveyard)) {
            for (final MagicCard targetCard : player.getOpponent().getGraveyard()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }                
            }
        }
        
        // Cards in hand
        if (acceptType(MagicTargetType.Hand)) {
            for (final MagicCard targetCard : player.getHand()) {
                if (accept(game,player,targetCard)) {
                    targets.add(targetCard);
                }                
            }
        }
        
        return targets;
    }
}

abstract class MagicStackFilterImpl implements MagicTargetFilter<MagicItemOnStack> {
    public List<MagicItemOnStack> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicItemOnStack> targets=new ArrayList<MagicItemOnStack>();
        
        // Items on stack
        if (acceptType(MagicTargetType.Stack)) {
            for (final MagicItemOnStack targetItemOnStack : game.getStack()) {
                if (accept(game,player,targetItemOnStack) && 
                    targetHint.accept(player,targetItemOnStack)) {
                    targets.add(targetItemOnStack);
                }
            }
        }
                
        return targets;
    }
}

abstract class MagicPlayerFilterImpl implements MagicTargetFilter<MagicPlayer> {
    public List<MagicPlayer> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicPlayer> targets=new ArrayList<MagicPlayer>();
        
        // Players
        if (acceptType(MagicTargetType.Player)) {
            for (final MagicPlayer targetPlayer : game.getPlayers()) {
                if (accept(game,player,targetPlayer) &&
                    targetHint.accept(player,targetPlayer)) {
                    targets.add(targetPlayer);
                }                
            }
        }
                
        return targets;
    }
}

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
    
    MagicStackFilterImpl TARGET_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
    
    MagicStackFilterImpl TARGET_SPELL_THAT_TARGETS_PLAYER=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            if (target.isSpell() == false) {
                return false;
            }
            final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
            return tchoice != null && tchoice.isTargeted() && tchoice.getTargetFilter().acceptType(MagicTargetType.Player);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
    
    MagicStackFilterImpl TARGET_SPELL_YOU_DONT_CONTROL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getController() != player;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
    
    MagicStackFilterImpl TARGET_SPELL_WITH_X_COST=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getCardDefinition().hasX();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
    
    MagicStackFilterImpl TARGET_RED_GREEN_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   (itemOnStack.hasColor(MagicColor.Red) ||
                    itemOnStack.hasColor(MagicColor.Green));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };

    MagicStackFilterImpl TARGET_CREATURE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };

    MagicStackFilterImpl TARGET_NONCREATURE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   !itemOnStack.isSpell(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
        
    MagicStackFilterImpl TARGET_INSTANT_OR_SORCERY_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Instant) ||
                   itemOnStack.isSpell(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };
    
    MagicStackFilterImpl TARGET_INSTANT_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Instant);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack;
        }
    };

     MagicStackFilterImpl TARGET_ARTIFACT_SPELL = new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Stack;
        }
    };
    
    MagicPlayerFilterImpl TARGET_PLAYER=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Player;
        }        
    };

    MagicPlayerFilterImpl TARGET_OPPONENT=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return target!=player;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Player;
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
    
    MagicPermanentFilterImpl TARGET_BLACK_PERMANENT = Factory.permanent(MagicColor.Black, Control.Any);
    
    MagicPermanentFilterImpl TARGET_BLACK_PERMANENT_YOU_CONTROL = Factory.permanent(MagicColor.Black, Control.You);
    
    MagicPermanentFilterImpl TARGET_GREEN_PERMANENT_YOU_CONTROL = Factory.permanent(MagicColor.Green, Control.You);
    
    MagicPermanentFilterImpl TARGET_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
    };
    
    MagicPermanentFilterImpl TARGET_BLACK_RED_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasColor(MagicColor.Black) ||
                   target.hasColor(MagicColor.Red);
        }
    };

    MagicPermanentFilterImpl TARGET_NONBASIC_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic);
        }
    };
    
    MagicPermanentFilterImpl TARGET_LAND = Factory.permanent(MagicType.Land, Control.Any);
    
    MagicPermanentFilterImpl TARGET_PLANESWALKER = Factory.permanent(MagicType.Planeswalker, Control.Any);
    
    MagicPermanentFilterImpl TARGET_PLANESWALKER_YOU_CONTROL = Factory.permanent(MagicType.Planeswalker, Control.You);
    
    MagicPermanentFilterImpl TARGET_PLANESWALKER_YOUR_OPPONENT_CONTROLS = Factory.permanent(MagicType.Planeswalker, Control.Opp);

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
    
    MagicPermanentFilterImpl TARGET_NONLAND_NONTOKEN_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && !target.isToken();
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
    
    MagicPermanentFilterImpl TARGET_ARTIFACT = Factory.permanent(MagicType.Artifact, Control.Any);
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_YOU_CONTROL = Factory.permanent(MagicType.Artifact, Control.You);
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS = Factory.permanent(MagicType.Artifact, Control.Opp);
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_CREATURE = new MagicPermanentFilterImpl () {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() &&
                   target.isCreature();
        }
    };
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isArtifact() &&
                   target.isCreature();
        }
    };
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact()||target.isCreature();
        }
    };
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_CREATURE_OR_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() ||
                   target.isCreature() ||
                   target.isLand();
        }
    };
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_ENCHANTMENT=new MagicPermanentFilterImpl () {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() || 
                   target.isEnchantment();
        }
    };
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {    
            return target.isArtifact() || 
                   target.isLand();
        }
    };

    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() || 
                   target.isArtifact() || 
                   target.isEnchantment();
        }
    };
    
    MagicPermanentFilterImpl TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isOpponent(player) &&
                   (target.isArtifact() || target.isEnchantment());
        }
    };
    
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

    MagicPermanentFilterImpl TARGET_CREATURE_OR_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() || 
                   target.isLand();
        }        
    };
    
    MagicPermanentFilterImpl TARGET_CREATURE_OR_PLANESWALKER=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() || 
                   target.isPlaneswalker();
        }        
    };

    MagicPermanentFilterImpl TARGET_CREATURE_OR_ENCHANTMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() ||
                   target.isEnchantment();
        }
    };
    
    MagicPermanentFilterImpl TARGET_CREATURE_OR_ENCHANTMENT_OR_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && (
               target.isCreature() ||
               target.isEnchantment() ||
               target.isLand()
           );
        }
    };

    MagicPermanentFilterImpl TARGET_ENCHANTMENT = Factory.permanent(MagicType.Enchantment, Control.Any);
    
    MagicPermanentFilterImpl TARGET_ENCHANTMENT_YOU_CONTROL = Factory.permanent(MagicType.Enchantment, Control.You);
    
    MagicPermanentFilterImpl TARGET_ENCHANTMENT_YOUR_OPPONENT_CONTROLS = Factory.permanent(MagicType.Enchantment, Control.Opp);
    
    MagicPermanentFilterImpl TARGET_SPIRIT_OR_ENCHANTMENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {    
            return target.hasSubType(MagicSubType.Spirit) ||
                   target.isEnchantment();
        }
    };

    MagicPermanentFilterImpl TARGET_EQUIPMENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isEquipment();
        }
    };

    MagicPermanentFilterImpl TARGET_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player);
        }
    };
    
    MagicPermanentFilterImpl TARGET_PERMANENT_YOU_OWN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isOwner(player);
        }
    };

    MagicPermanentFilterImpl TARGET_LAND_YOU_CONTROL = Factory.permanent(MagicType.Land, Control.You);
    
    MagicPermanentFilterImpl TARGET_FOREST = Factory.permanent(MagicSubType.Forest, Control.Any);
    
    MagicPermanentFilterImpl TARGET_FOREST_YOU_CONTROL = Factory.permanent(MagicSubType.Forest, Control.You);
    
    MagicPermanentFilterImpl TARGET_ISLAND_YOU_CONTROL = Factory.permanent(MagicSubType.Island, Control.You);
    
    MagicPermanentFilterImpl TARGET_MOUNTAIN_YOU_CONTROL = Factory.permanent(MagicSubType.Mountain, Control.You);
    
    MagicPermanentFilterImpl TARGET_PLAINS_YOU_CONTROL = Factory.permanent(MagicSubType.Plains, Control.You);
    
    MagicPermanentFilterImpl TARGET_SWAMP_YOU_CONTROL = Factory.permanent(MagicSubType.Swamp, Control.You);
    
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
    
    MagicPermanentFilterImpl TARGET_NON_DEMON = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return !target.hasSubType(MagicSubType.Demon) &&
                        target.isCreature();
        }
    };
    
    MagicPermanentFilterImpl TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   (target.hasColor(MagicColor.Red) ||
                    target.hasColor(MagicColor.Green));
        }
    };

    MagicPermanentFilterImpl TARGET_GREEN_OR_WHITE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasColor(MagicColor.Green) || 
                    target.hasColor(MagicColor.White));
        }
    };
    
    MagicPermanentFilterImpl TARGET_WHITE_OR_BLUE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasColor(MagicColor.White) || 
                    target.hasColor(MagicColor.Blue));
        }
    };

    MagicPermanentFilterImpl TARGET_BLACK_CREATURE = Factory.creature(MagicColor.Black, Control.Any);
    
    MagicPermanentFilterImpl TARGET_WHITE_CREATURE = Factory.creature(MagicColor.White, Control.Any);
    
    MagicPermanentFilterImpl TARGET_BLUE_CREATURE = Factory.creature(MagicColor.Blue, Control.Any);
    
    MagicPermanentFilterImpl TARGET_BLACK_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Black, Control.You);
    
    MagicPermanentFilterImpl TARGET_BLUE_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Blue, Control.You);
    
    MagicPermanentFilterImpl TARGET_GREEN_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Green, Control.You);
    
    MagicPermanentFilterImpl TARGET_GREEN_CREATURE = Factory.creature(MagicColor.Green, Control.Any);
    
    MagicPermanentFilterImpl TARGET_RED_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.Red, Control.You);
    
    MagicPermanentFilterImpl TARGET_RED_CREATURE = Factory.creature(MagicColor.Red, Control.Any);
    
    MagicPermanentFilterImpl TARGET_WHITE_CREATURE_YOU_CONTROL = Factory.creature(MagicColor.White, Control.You);
    
    MagicPermanentFilterImpl TARGET_BAT_YOU_CONTROL = Factory.creature(MagicSubType.Bat, Control.You);
   
    MagicPermanentFilterImpl TARGET_BEAST_YOU_CONTROL = Factory.creature(MagicSubType.Beast, Control.You);
    
    MagicPermanentFilterImpl TARGET_DRAGON_YOU_CONTROL = Factory.creature(MagicSubType.Dragon, Control.You);
    
    MagicPermanentFilterImpl TARGET_ELEMENTAL_YOU_CONTROL = Factory.creature(MagicSubType.Elemental, Control.You);
    
    MagicPermanentFilterImpl TARGET_KITHKIN_YOU_CONTROL = Factory.creature(MagicSubType.Kithkin, Control.You);
    
    MagicPermanentFilterImpl TARGET_GRIFFIN = Factory.creature(MagicSubType.Griffin, Control.Any);
    
    MagicPermanentFilterImpl TARGET_BIRD = Factory.creature(MagicSubType.Bird, Control.Any);
    
    MagicPermanentFilterImpl TARGET_GOBLIN_CREATURE = Factory.creature(MagicSubType.Goblin, Control.Any);
    
    MagicPermanentFilterImpl TARGET_GOBLIN_YOU_CONTROL = Factory.creature(MagicSubType.Goblin, Control.You);
    
    MagicPermanentFilterImpl TARGET_GOBLIN_OR_SHAMAN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() && 
                   (target.hasSubType(MagicSubType.Goblin) ||
                    target.hasSubType(MagicSubType.Shaman));
        }
    };
    
    MagicPermanentFilterImpl TARGET_GOLEM_YOU_CONTROL = Factory.creature(MagicSubType.Golem, Control.You);
    
    MagicPermanentFilterImpl TARGET_SQUIRREL_CREATURE = Factory.creature(MagicSubType.Squirrel, Control.Any);
    
    MagicPermanentFilterImpl TARGET_CAT_YOU_CONTROL = Factory.creature(MagicSubType.Cat, Control.You);
    
    MagicPermanentFilterImpl TARGET_KNIGHT_CREATURE = Factory.creature(MagicSubType.Knight, Control.Any);
    
    MagicPermanentFilterImpl TARGET_KNIGHT_YOU_CONTROL = Factory.creature(MagicSubType.Knight, Control.You);
    
    MagicPermanentFilterImpl TARGET_ILLUSION_YOU_CONTROL = Factory.creature(MagicSubType.Illusion, Control.You);
    
    MagicPermanentFilterImpl TARGET_MERFOLK_CREATURE = Factory.creature(MagicSubType.Merfolk, Control.Any);
    
    MagicPermanentFilterImpl TARGET_FUNGUS_CREATURE = Factory.creature(MagicSubType.Fungus, Control.Any);
    
    MagicPermanentFilterImpl TARGET_MERFOLK_YOU_CONTROL = Factory.creature(MagicSubType.Merfolk, Control.You);
    
    MagicPermanentFilterImpl TARGET_SAPROLING_YOU_CONTROL = Factory.creature(MagicSubType.Saproling, Control.You);
    
    MagicPermanentFilterImpl TARGET_MYR_YOU_CONTROL = Factory.creature(MagicSubType.Myr, Control.You);
    
    MagicPermanentFilterImpl TARGET_MYR_CREATURE = Factory.creature(MagicSubType.Myr, Control.Any);
    
    MagicPermanentFilterImpl TARGET_SAMURAI = Factory.creature(MagicSubType.Samurai, Control.Any);
    
    MagicPermanentFilterImpl TARGET_SAMURAI_YOU_CONTROL = Factory.creature(MagicSubType.Samurai, Control.You);
    
    MagicPermanentFilterImpl TARGET_LEGENDARY_CREATURE = Factory.creature(MagicType.Legendary, Control.You);
    
    MagicPermanentFilterImpl TARGET_SNAKE_YOU_CONTROL = Factory.creature(MagicSubType.Snake, Control.You);
    
    MagicPermanentFilterImpl TARGET_TREEFOLK_OR_WARRIOR_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && 
                    target.isCreature() && 
                    (target.hasSubType(MagicSubType.Treefolk) ||
                     target.hasSubType(MagicSubType.Warrior));
        }
    };
    
    MagicPermanentFilterImpl TARGET_LEGENDARY_SAMURAI_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && 
                   target.isCreature() &&
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
    
    MagicPermanentFilterImpl TARGET_VAMPIRE_YOU_CONTROL = Factory.creature(MagicSubType.Vampire, Control.You);
    
    MagicPermanentFilterImpl TARGET_VAMPIRE_WEREWOLF_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Werewolf) ||
                    target.hasSubType(MagicSubType.Zombie));
        }
    };
    
    MagicPermanentFilterImpl TARGET_NONVAMPIRE_NONWEREWOLF_NONZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Vampire) &&
                   !target.hasSubType(MagicSubType.Werewolf) &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    MagicPermanentFilterImpl TARGET_HUMAN = Factory.creature(MagicSubType.Human, Control.Any);
    
    MagicPermanentFilterImpl TARGET_HUMAN_YOU_CONTROL = Factory.creature(MagicSubType.Human, Control.You);
    
    MagicPermanentFilterImpl TARGET_NONHUMAN_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {        
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
    };
    
    MagicPermanentFilterImpl TARGET_NONHUMAN_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {        
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
    };
    
    MagicPermanentFilterImpl TARGET_SOLDIER = Factory.creature(MagicSubType.Soldier, Control.Any);
    
    MagicPermanentFilterImpl TARGET_SOLDIER_YOU_CONTROL = Factory.creature(MagicSubType.Soldier, Control.You);
    
    MagicPermanentFilterImpl TARGET_NON_ZOMBIE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    MagicPermanentFilterImpl TARGET_ZOMBIE_YOU_CONTROL = Factory.creature(MagicSubType.Zombie, Control.You);
    
    MagicPermanentFilterImpl TARGET_ZOMBIE = Factory.creature(MagicSubType.Zombie, Control.Any);

    MagicPermanentFilterImpl TARGET_KOR_YOU_CONTROL = Factory.creature(MagicSubType.Kor, Control.You);
    
    MagicPermanentFilterImpl TARGET_WOLF_YOU_CONTROL = Factory.creature(MagicSubType.Wolf, Control.You);
    
    MagicPermanentFilterImpl TARGET_SLIVER = Factory.creature(MagicSubType.Sliver, Control.Any);
    
    MagicPermanentFilterImpl TARGET_ELF = Factory.creature(MagicSubType.Elf, Control.Any);
    
    MagicPermanentFilterImpl TARGET_ELF_YOU_CONTROL = Factory.creature(MagicSubType.Elf, Control.You);
    
    MagicPermanentFilterImpl TARGET_ALLY_YOU_CONTROL = Factory.creature(MagicSubType.Ally, Control.You);
    
    MagicPermanentFilterImpl TARGET_FAERIE_YOU_CONTROL = Factory.creature(MagicSubType.Faerie, Control.You);
    
    MagicPermanentFilterImpl TARGET_SPIRIT_YOU_CONTROL = Factory.creature(MagicSubType.Spirit, Control.You);
    
    MagicPermanentFilterImpl TARGET_MODULAR_CREATURE_YOU_CONTROL = Factory.creature(MagicAbility.Modular, Control.You);
    
    MagicPermanentFilterImpl TARGET_PLANT_YOU_CONTROL = Factory.creature(MagicSubType.Plant, Control.You);
        
    MagicPermanentFilterImpl TARGET_CREATURE = Factory.permanent(MagicType.Creature, Control.Any);
    
    MagicPermanentFilterImpl TARGET_CREATURE_YOU_CONTROL = Factory.permanent(MagicType.Creature, Control.You);
    
    MagicPermanentFilterImpl TARGET_CREATURE_YOUR_OPPONENT_CONTROLS = Factory.permanent(MagicType.Creature, Control.Opp);
    
    MagicPermanentFilterImpl TARGET_TAPPED_CREATURE = Factory.creature(MagicPermanentState.Tapped, Control.Any);
    
    MagicPermanentFilterImpl TARGET_TAPPED_CREATURE_YOU_CONTROL = Factory.creature(MagicPermanentState.Tapped, Control.You);

    MagicPermanentFilterImpl TARGET_UNTAPPED_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isTapped();
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
    
    MagicPermanentFilterImpl TARGET_CREATURE_WITH_FLYING = Factory.creature(MagicAbility.Flying, Control.Any);
    
    MagicPermanentFilterImpl TARGET_CREATURE_WITH_FLYING_YOU_CONTROL = Factory.creature(MagicAbility.Flying, Control.You);
    
    MagicPermanentFilterImpl TARGET_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS = Factory.creature(MagicAbility.Flying, Control.Opp);

    MagicPermanentFilterImpl TARGET_CREATURE_WITH_SHADOW = Factory.creature(MagicAbility.Shadow, Control.Any);
    
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
    
    MagicPermanentFilterImpl TARGET_ATTACKING_CREATURE = Factory.creature(MagicPermanentState.Attacking, Control.Any);
    
    MagicPermanentFilterImpl TARGET_BLOCKING_CREATURE = Factory.creature(MagicPermanentState.Blocking, Control.Any);
    
    MagicPermanentFilterImpl TARGET_ATTACKING_CREATURE_YOU_CONTROL = Factory.creature(MagicPermanentState.Attacking, Control.You);
    
    MagicPermanentFilterImpl TARGET_ATTACKING_CREATURE_WITH_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   target.isAttacking() && 
                   target.hasAbility(MagicAbility.Flying);
        }
    };
    
    MagicPermanentFilterImpl TARGET_ATTACKING_GOBLIN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   target.isAttacking() && 
                   target.hasSubType(MagicSubType.Goblin);
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
    
    MagicPermanentFilterImpl TARGET_BLOCKED_CREATURE = Factory.creature(MagicPermanentState.Blocked, Control.Any);
    
    MagicCardFilterImpl TARGET_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_CARD_FROM_ALL_GRAVEYARDs = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }        
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                   targetType == MagicTargetType.OpponentsGraveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isCreature() &&
                   cardDefinition.hasAbility(MagicAbility.Infect);
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
    
    MagicPermanentFilterImpl TARGET_NONLAND_PERMANENT_CMC_LEQ_3 = new MagicTargetFilter.MagicCMCPermanentFilter(
        MagicTargetFilter.TARGET_NONLAND_PERMANENT, 
        MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL, 
        3
    );

    MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.OpponentsGraveyard;
        }                        
    };

    MagicCardFilterImpl TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition= target.getCardDefinition();
            return cardDefinition.hasType(MagicType.Instant) ||
                   cardDefinition.hasType(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition= target.getCardDefinition();
            return cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasType(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.OpponentsGraveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasType(MagicType.Enchantment);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_ARTIFACT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {    
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_ENCHANTMENT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isEnchantment();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_INSTANT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isInstant();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_SORCERY_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isSorcery();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_LAND_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isLand();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    MagicCardFilterImpl TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition= target.getCardDefinition();
            return cardDefinition.isCreature()||cardDefinition.isArtifact();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    MagicCardFilterImpl TARGET_GOBLIN_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Goblin);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_ZOMBIE_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Zombie);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_SPIRIT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Spirit);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_HUMAN_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().hasSubType(MagicSubType.Human);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }                        
    };
    
    MagicCardFilterImpl TARGET_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicCardFilterImpl TARGET_BLUE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasColor(MagicColor.Blue);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicCardFilterImpl TARGET_CREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicCardFilterImpl TARGET_GREEN_CREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isCreature() && MagicColor.Green.hasColor(cardDefinition.getColorFlags());
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicCardFilterImpl TARGET_BLUE_OR_RED_CREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isCreature() && (
                MagicColor.Blue.hasColor(cardDefinition.getColorFlags()) ||
                MagicColor.Red.hasColor(cardDefinition.getColorFlags())
            );
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicPermanentFilterImpl TARGET_MULTICOLOR_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent);
        }
    };
    
    MagicCardFilterImpl TARGET_MULTICOLOR_CREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isCreature() && MagicColor.isMulti(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicCardFilterImpl TARGET_BASIC_LAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isLand()&&cardDefinition.isBasic();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicCardFilterImpl TARGET_LAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.isLand();
        }    
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }                        
    };
    
    MagicCardFilterImpl TARGET_GOBLIN_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.hasSubType(MagicSubType.Goblin);
        }    
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
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

    enum Control {
        Any,
        You,
        Opp
    }
    
    public static final class Factory {
        static final MagicPermanentFilterImpl permanent(final MagicType type, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.hasType(type) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
        static final MagicPermanentFilterImpl permanent(final MagicColor color, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.hasColor(color) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
        static final MagicPermanentFilterImpl permanent(final MagicSubType subtype, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.hasSubType(subtype) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
        static final MagicPermanentFilterImpl creature(final MagicColor color, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasColor(color) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
        static final MagicPermanentFilterImpl creature(final MagicType type, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasType(type) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
        static final MagicPermanentFilterImpl creature(final MagicSubType subtype, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasSubType(subtype) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
        static final MagicPermanentFilterImpl creature(final MagicAbility ability, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasAbility(ability) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
        static final MagicPermanentFilterImpl creature(final MagicPermanentState state, final Control control) {
            return new MagicPermanentFilterImpl() {
                public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                    return target.isCreature() &&
                           target.hasState(state) &&
                           ((control == Control.You && target.isController(player)) ||
                            (control == Control.Opp && target.isOpponent(player)) ||
                            (control == Control.Any));
                }
            };
        }
    }
    
    // Permanent reference can not be used because game is copied.
    public static final class MagicOtherCardTargetFilter extends MagicCardFilterImpl {

        private final MagicCardFilterImpl targetFilter;
        private final long id;        

        public MagicOtherCardTargetFilter(final MagicCardFilterImpl targetFilter,final MagicCard invalidCard) {
            this.targetFilter=targetFilter;
            this.id=invalidCard.getId();
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
        
        public NameTargetFilter(final String name) {
            this.name=name;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return name.equals(target.getName());
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

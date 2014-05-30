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
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicItemOnStack;

import java.util.Map;
import java.util.TreeMap;

public class MagicTargetFilterFactory {

    public static final MagicPermanentFilterImpl NONE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return false;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return false;
        }
    };

    public static final MagicPermanentFilterImpl ANY = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return true;
        }
    };
    
    public static final MagicStackFilterImpl SPELL_OR_ABILITY_THAT_TARGETS_PERMANENTS =new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
            return tchoice.isValid() && 
                   tchoice.isTargeted() &&
                   tchoice.getTargetFilter().acceptType(MagicTargetType.Permanent);
        }
    };
    
    public static final MagicStackFilterImpl SPELL_OR_ABILITY = new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return true;
        }
    };

    public static final MagicStackFilterImpl SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell();
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_PLAYER=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
            return target.isSpell() &&
                   tchoice.isValid() &&
                   tchoice.isTargeted() &&
                   tchoice.getTargetFilter().acceptType(MagicTargetType.Player);
        }
    };

    public static final MagicStackFilterImpl SPELL_YOU_DONT_CONTROL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getController() != player;
        }
    };
    
    public static final MagicStackFilterImpl SPELL_WITH_CMC_EQ_1 = new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() == 1;
        }
    };
    
    public static final MagicStackFilterImpl SPELL_WITH_CMC_EQ_2 = new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() == 2;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_X_COST=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getCardDefinition().hasX();
        }
    };

    public static final MagicStackFilterImpl RED_OR_GREEN_SPELL = MagicTargetFilterFactory.spellOr(MagicColor.Red, MagicColor.Green);
    
    public static final MagicStackFilterImpl BLUE_OR_BLACK_SPELL = MagicTargetFilterFactory.spellOr(MagicColor.Blue, MagicColor.Black);
    
    public static final MagicStackFilterImpl NONBLUE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicStackFilterImpl BLUE_SPELL_YOUR_TURN=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && itemOnStack.hasColor(MagicColor.Blue) && game.getTurnPlayer() == player;
        }
    };
    
    public static final MagicStackFilterImpl BLUE_OR_BLACK_SPELL_YOUR_TURN=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && 
                   (itemOnStack.hasColor(MagicColor.Blue) || itemOnStack.hasColor(MagicColor.Black)) && 
                   game.getTurnPlayer() == player;
        }
    };
    
    public static final MagicStackFilterImpl NONRED_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Red);
        }
    };
    
    public static final MagicStackFilterImpl BLUE_OR_BLACK_OR_RED_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && (
                itemOnStack.hasColor(MagicColor.Blue) ||
                itemOnStack.hasColor(MagicColor.Black) ||
                itemOnStack.hasColor(MagicColor.Red));
        }
    };
    
    public static final MagicStackFilterImpl WHITE_OR_BLUE_OR_BLACK_OR_RED_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && (
                itemOnStack.hasColor(MagicColor.White) ||
                itemOnStack.hasColor(MagicColor.Blue) ||
                itemOnStack.hasColor(MagicColor.Black) ||
                itemOnStack.hasColor(MagicColor.Red));
        }
    };
    
    public static final MagicStackFilterImpl CREATURE_OR_AURA_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Creature, MagicSubType.Aura);
    
    public static final MagicStackFilterImpl CREATURE_OR_SORCERY_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Creature, MagicType.Sorcery);

    public static final MagicStackFilterImpl NONCREATURE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   !itemOnStack.isSpell(MagicType.Creature);
        }
    };
    
    public static final MagicStackFilterImpl NONARTIFACT_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   !itemOnStack.isSpell(MagicType.Artifact);
        }
    };
    
    public static final MagicStackFilterImpl CREATURE_SPELL_CMC_6_OR_MORE=new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.getConvertedCost() >= 6;
        }
    };

    public static final MagicStackFilterImpl INSTANT_OR_SORCERY_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Instant, MagicType.Sorcery);

    public static final MagicStackFilterImpl SPIRIT_OR_ARCANE_SPELL= MagicTargetFilterFactory.spellOr(MagicSubType.Spirit, MagicSubType.Arcane);

    public static final MagicStackFilterImpl ARTIFACT_OR_ENCHANTMENT_SPELL = MagicTargetFilterFactory.spellOr(MagicType.Artifact, MagicType.Enchantment);
    
    public static final MagicPlayerFilterImpl PLAYER=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return true;
        }
    };

    public static final MagicPlayerFilterImpl OPPONENT=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPlayer target) {
            return target!=player;
        }
    };

    public static final MagicTargetFilterImpl SPELL_OR_PERMANENT=new MagicTargetFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isSpell()||target.isPermanent();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack ||
                   targetType==MagicTargetType.Permanent;
        }
    };

    public static final MagicPermanentFilterImpl BLACK_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Black, Control.Any);
    
    public static final MagicPermanentFilterImpl WHITE_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl RED_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Red, Control.Any);
    
    public static final MagicPermanentFilterImpl GREEN_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Green, Control.Any);
    
    public static final MagicPermanentFilterImpl BLUE_PERMANENT = MagicTargetFilterFactory.permanent(MagicColor.Blue, Control.Any);
    
    public static final MagicPermanentFilterImpl WHITE_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl BLUE_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.Blue, Control.You);

    public static final MagicPermanentFilterImpl BLACK_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.Black, Control.You);

    public static final MagicPermanentFilterImpl GREEN_PERMANENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicColor.Green, Control.You);

    public static final MagicPermanentFilterImpl PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl BLACK_RED_PERMANENT = MagicTargetFilterFactory.permanentOr(MagicColor.Black, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl NONBASIC_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic);
        }
    };
    
    public static final MagicPermanentFilterImpl NONBASIC_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl TRAPPED_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && 
                   target.getCounters(MagicCounterType.Trap) >= 1 && 
                   target.isController(player);
        }
    };
    
    public static final MagicPermanentFilterImpl BASIC_LAND = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Basic, Control.Any);
    
    public static final MagicPermanentFilterImpl SNOW_LAND = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Snow, Control.Any);
    
    public static final MagicPermanentFilterImpl BASIC_LAND_YOU_CONTROL = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Basic, Control.You);
    
    public static final MagicPermanentFilterImpl SNOW_LAND_YOU_CONTROL = MagicTargetFilterFactory.permanentAnd(MagicType.Land, MagicType.Snow, Control.You);

    public static final MagicPermanentFilterImpl LAND = MagicTargetFilterFactory.permanent(MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl LAND_OR_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() || (!target.hasColor(MagicColor.Black) && target.isCreature());
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand();
        }
    };
    
    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && target.isController(player);
        }
    };
    

    public static final MagicPermanentFilterImpl NONLAND_NONTOKEN_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && !target.isToken();
        }
    };
    
    public static final MagicPermanentFilterImpl NONTOKEN_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && !target.isToken();
        }
    };
    
    public static final MagicPermanentFilterImpl NONTOKEN_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && !target.isToken() && target.isCreature();
        }
    };
    
    public static final MagicPermanentFilterImpl NONTOKEN_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isToken() && target.isCreature();
        }
    };
    
    public static final MagicPermanentFilterImpl NONTOKEN_ELF=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isToken() && target.hasSubType(MagicSubType.Elf);
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl NONCREATURE_ARTIFACT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() && !target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT = MagicTargetFilterFactory.permanent(MagicType.Artifact, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Artifact, Control.You);

    public static final MagicPermanentFilterImpl ARTIFACT_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.permanent(MagicType.Artifact, Control.Opp);

    public static final MagicPermanentFilterImpl ARTIFACT_CREATURE = MagicTargetFilterFactory.permanentAnd(MagicType.Artifact, MagicType.Creature, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Creature, Control.Any);
    
    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Creature, Control.You);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() ||
                   target.isCreature() ||
                   target.isLand();
        }
    };
    
    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && (
                       target.isArtifact() ||
                       target.isCreature() ||
                       target.isLand()
                   );
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Enchantment, Control.Any);
    
    public static final MagicPermanentFilterImpl ARTIFACT_OR_LAND = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_OR_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() ||
                   target.isArtifact() ||
                   target.isEnchantment();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_ENCHANTMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() ||
                   target.isArtifact() ||
                   target.isEnchantment();
        }
    };
    
    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.permanentOr(MagicType.Artifact, MagicType.Enchantment, Control.Opp);

    public static final MagicPermanentFilterImpl NONCREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isCreature();
        }
    };

    public static final MagicTargetFilterImpl CREATURE_OR_PLAYER=new MagicTargetFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isPlayer() ||
                   target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent ||
                   targetType==MagicTargetType.Player;
        }
    };
    
    public static final MagicTargetFilterImpl SLIVER_CREATURE_OR_PLAYER=new MagicTargetFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return target.isPlayer() ||
                   (target.isCreature() && target.hasSubType(MagicSubType.Sliver));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent ||
                   targetType==MagicTargetType.Player;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_OR_LAND = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Land, Control.Any);
    
    public static final MagicPermanentFilterImpl CREATURE_OR_PLANESWALKER = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Planeswalker, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_OR_ENCHANTMENT = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Enchantment, Control.Any);
    
    public static final MagicPermanentFilterImpl CREATURE_OR_ENCHANTMENT_YOU_CONTROL = MagicTargetFilterFactory.permanentOr(MagicType.Creature, MagicType.Enchantment, Control.You);
    
    public static final MagicCardFilterImpl CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Enchantment) || target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };


    public static final MagicPermanentFilterImpl EQUIPMENT = MagicTargetFilterFactory.permanent(MagicSubType.Equipment, Control.Any);
    
    public static final MagicPermanentFilterImpl ENCHANTMENT = MagicTargetFilterFactory.permanent(MagicType.Enchantment, Control.Any);

    public static final MagicPermanentFilterImpl ENCHANTMENT_OR_LAND = MagicTargetFilterFactory.permanentOr(MagicType.Enchantment, MagicType.Land, Control.Any);
    
    public static final MagicPermanentFilterImpl ENCHANTMENT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Enchantment, Control.You);

    public static final MagicPermanentFilterImpl SPIRIT_OR_ENCHANTMENT = MagicTargetFilterFactory.permanentOr(MagicType.Enchantment, MagicSubType.Spirit, Control.Any);

    public static final MagicPermanentFilterImpl PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player);
        }
    };
    
    public static final MagicPermanentFilterImpl PERMANENT_AN_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isOwner(player);
        }
    };
    
    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN_AND_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) && target.isController(player);
        }
    };
    
    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN_OR_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) || target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl LAND_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Land, Control.You);

    public static final MagicPermanentFilterImpl FOREST = MagicTargetFilterFactory.permanent(MagicSubType.Forest, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Forest, Control.You);

    public static final MagicPermanentFilterImpl ISLAND_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Island, Control.You);
    
    public static final MagicPermanentFilterImpl ISLAND = MagicTargetFilterFactory.permanent(MagicSubType.Island, Control.Any);

    public static final MagicPermanentFilterImpl MOUNTAIN_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Mountain, Control.You);

    public static final MagicPermanentFilterImpl PLAINS = MagicTargetFilterFactory.permanent(MagicSubType.Plains, Control.Any);
    
    public static final MagicPermanentFilterImpl AURA = MagicTargetFilterFactory.permanent(MagicSubType.Aura, Control.Any);
    
    public static final MagicPermanentFilterImpl SWAMP = MagicTargetFilterFactory.permanent(MagicSubType.Swamp, Control.Any);

    public static final MagicPermanentFilterImpl SWAMP_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Swamp, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isToken();
        }
    };
    
    public static final MagicPermanentFilterImpl CARIBOU_TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isToken() &&
                   target.hasSubType(MagicSubType.Caribou);
        }
    };
    
    public static final MagicPermanentFilterImpl CREATURE_TOKEN = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_THAT_ISNT_ENCHANTED = new MagicPermanentFilterImpl(){
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.isEnchanted() == false;
        }
    };

    public static final MagicPermanentFilterImpl NON_LEGENDARY_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasType(MagicType.Legendary) &&
                   target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NON_DEMON_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return !target.hasSubType(MagicSubType.Demon) &&
                        target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl BLACK_OR_RED_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creatureOr(MagicColor.Black, MagicColor.Red, Control.You);
    
    public static final MagicPermanentFilterImpl BLUE_OR_BLACK_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creatureOr(MagicColor.Blue, MagicColor.Black, Control.You);
    
    public static final MagicPermanentFilterImpl RED_OR_GREEN_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.Green, Control.You);
    
    public static final MagicPermanentFilterImpl RED_OR_GREEN_CREATURE_AN_OPPONENT_CONTROLS = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.Green, Control.Opp);
    
    public static final MagicPermanentFilterImpl FOREST_OR_PLAINS = MagicTargetFilterFactory.permanentOr(MagicSubType.Forest, MagicSubType.Plains, Control.Any);
    
    public static final MagicPermanentFilterImpl FOREST_OR_PLAINS_YOU_CONTROL = MagicTargetFilterFactory.permanentOr(MagicSubType.Forest, MagicSubType.Plains, Control.You);
    
    public static final MagicPermanentFilterImpl RED_OR_GREEN_CREATURE = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_CREATURE = MagicTargetFilterFactory.creatureOr(MagicColor.Green, MagicColor.White, Control.Any);
    
    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_CREATURE_AN_OPPONENT_CONTROLS = MagicTargetFilterFactory.creatureOr(MagicColor.Green, MagicColor.White, Control.Opp);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_CREATURE = MagicTargetFilterFactory.creatureOr(MagicColor.White, MagicColor.Blue, Control.Any);;
    
    public static final MagicPermanentFilterImpl RED_OR_WHITE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creatureOr(MagicColor.Red, MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl BLACK_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_CREATURE = MagicTargetFilterFactory.creature(MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Black, Control.You);

    public static final MagicPermanentFilterImpl BLUE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Blue, Control.You);

    public static final MagicPermanentFilterImpl GREEN_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Green, Control.You);

    public static final MagicPermanentFilterImpl GREEN_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl RED_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.Red, Control.You);

    public static final MagicPermanentFilterImpl RED_CREATURE = MagicTargetFilterFactory.creature(MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl DRAGON_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Dragon, Control.You);
    
    public static final MagicPermanentFilterImpl GOBLIN_PERMANENT = MagicTargetFilterFactory.permanent(MagicSubType.Goblin, Control.Any);

    public static final MagicPermanentFilterImpl DJINN_OR_EFREET = MagicTargetFilterFactory.permanentOr(MagicSubType.Djinn, MagicSubType.Efreet, Control.Any);

    public static final MagicPermanentFilterImpl SQUIRREL_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Squirrel, Control.Any);

    public static final MagicPermanentFilterImpl CAT_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Cat, Control.You);
    
    public static final MagicPermanentFilterImpl CLERIC_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Cleric, Control.You);

    public static final MagicPermanentFilterImpl MYR_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Myr, Control.You);
    
    public static final MagicPermanentFilterImpl LEGENDARY_SAMURAI = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Samurai) &&
                   target.hasType(MagicType.Legendary);
        }
    };

    public static final MagicPermanentFilterImpl INSECT_RAT_SPIDER_OR_SQUIRREL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Insect) ||
                   target.hasSubType(MagicSubType.Rat) ||
                   target.hasSubType(MagicSubType.Spider) ||
                   target.hasSubType(MagicSubType.Squirrel);
        }
    };

    public static final MagicPermanentFilterImpl VAMPIRE_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Vampire, Control.You);

    public static final MagicPermanentFilterImpl SKELETON_VAMPIRE_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Skeleton) ||
                    target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    public static final MagicPermanentFilterImpl VAMPIRE_WEREWOLF_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Werewolf) ||
                    target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Vampire) &&
                   !target.hasSubType(MagicSubType.Werewolf) &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    public static final MagicPermanentFilterImpl NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    public static final MagicPermanentFilterImpl HUMAN = MagicTargetFilterFactory.permanent(MagicSubType.Human, Control.Any);

    public static final MagicPermanentFilterImpl HUMAN_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Human, Control.Any);

    public static final MagicPermanentFilterImpl HUMAN_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Human, Control.You);

    public static final MagicPermanentFilterImpl NONHUMAN_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
    };
    
    public static final MagicPermanentFilterImpl NONELF_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Elf);
        }
    };
    
    public static final MagicPermanentFilterImpl NONENCHANTMENT_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasType(MagicType.Enchantment);
        }
    };
    
    public static final MagicPermanentFilterImpl ENCHANTED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isEnchanted(); 
        }
    };
    
    public static final MagicPermanentFilterImpl ENCHANTED_OR_ENCHANTMENT_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return (target.isCreature() && target.isEnchanted()) ||
                    (target.isCreature() && target.hasType(MagicType.Enchantment)); 
        }
    };

    public static final MagicPermanentFilterImpl NONHUMAN_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Human);
        }
    };

    public static final MagicPermanentFilterImpl NON_ZOMBIE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };
    
    public static final MagicPermanentFilterImpl NON_VAMPIRE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasSubType(MagicSubType.Vampire);
        }
    };

    public static final MagicPermanentFilterImpl ZOMBIE_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Zombie, Control.You);

    public static final MagicPermanentFilterImpl ZOMBIE = MagicTargetFilterFactory.permanent(MagicSubType.Zombie, Control.Any);

    public static final MagicPermanentFilterImpl KOR_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicSubType.Kor, Control.You);

    public static final MagicPermanentFilterImpl WOLF_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Wolf, Control.You);

    public static final MagicPermanentFilterImpl SLIVER = MagicTargetFilterFactory.permanent(MagicSubType.Sliver, Control.Any);
    
    public static final MagicPermanentFilterImpl SLIVER_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Sliver, Control.Any);
    
    public static final MagicPermanentFilterImpl SLIVER_PERMANENT = MagicTargetFilterFactory.permanent(MagicSubType.Sliver, Control.Any);

    public static final MagicPermanentFilterImpl ELF = MagicTargetFilterFactory.permanent(MagicSubType.Elf, Control.Any);

    public static final MagicPermanentFilterImpl ELF_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Elf, Control.You);
    
    public static final MagicPermanentFilterImpl BARBARIAN_CREATURE = MagicTargetFilterFactory.creature(MagicSubType.Barbarian, Control.Any);

    public static final MagicPermanentFilterImpl ALLY_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Ally, Control.You);

    public static final MagicPermanentFilterImpl FAERIE_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Faerie, Control.You);

    public static final MagicPermanentFilterImpl SPIRIT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Spirit, Control.You);
    
    public static final MagicPermanentFilterImpl RAT_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Rat, Control.You);
    
    public static final MagicPermanentFilterImpl TREEFOLK_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicSubType.Treefolk, Control.You);

    public static final MagicPermanentFilterImpl MODULAR_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicAbility.Modular, Control.You);
    
    public static final MagicPermanentFilterImpl LEVELUP_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicAbility.LevelUp, Control.You);

    public static final MagicPermanentFilterImpl CREATURE = MagicTargetFilterFactory.permanent(MagicType.Creature, Control.Any);
    
    public static final MagicPermanentFilterImpl WORLD = MagicTargetFilterFactory.permanent(MagicType.World, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_YOU_CONTROL = MagicTargetFilterFactory.permanent(MagicType.Creature, Control.You);
    
    public static final MagicPermanentFilterImpl CREATURE_YOU_OWN = MagicTargetFilterFactory.permanent(MagicType.Creature, Own.You);

    public static final MagicPermanentFilterImpl CREATURE_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.permanent(MagicType.Creature, Control.Opp);

    public static final MagicPermanentFilterImpl TAPPED_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Tapped, Control.Any);

    public static final MagicPermanentFilterImpl TAPPED_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicPermanentState.Tapped, Control.You);

    public static final MagicPermanentFilterImpl TAPPED_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isTapped() &&
                   !target.hasColor(MagicColor.Black);
        }
    };
    
    public static final MagicPermanentFilterImpl UNTAPPED_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isTapped();
        }
    };
    
    public static final MagicPermanentFilterImpl UNTAPPED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isUntapped() &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl NONWHITE_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_CREATURE=new MagicPermanentFilterImpl () {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact();
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_NONBLACK_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact() &&
                   !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NON_ANGEL_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Angel);
        }
    };
    
    public static final MagicPermanentFilterImpl NON_SPIRIT_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.hasSubType(MagicSubType.Spirit);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS =new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying) &&
                   target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING = MagicTargetFilterFactory.creature(MagicAbility.Flying, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicAbility.Flying, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS = MagicTargetFilterFactory.creature(MagicAbility.Flying, Control.Opp);

    public static final MagicPermanentFilterImpl CREATURE_WITH_DEFENDER = MagicTargetFilterFactory.creature(MagicAbility.Defender, Control.Any);
    
    public static final MagicPermanentFilterImpl CREATURE_WITH_SHADOW = MagicTargetFilterFactory.creature(MagicAbility.Shadow, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_SHADOW = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Shadow);
        }
    };
    
    public static final MagicCardFilterImpl CREATURE_WITH_DEATHTOUCH_HEXPROOF_REACH_OR_TRAMPLE_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(MagicGame game, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Creature) && (
                        target.hasAbility(MagicAbility.Deathtouch) ||
                        target.hasAbility(MagicAbility.Hexproof) ||
                        target.hasAbility(MagicAbility.Reach) ||
                        target.hasAbility(MagicAbility.Trample)
                   );
        }
        public boolean acceptType(MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };
            
    
    public static final MagicPermanentFilterImpl CREATURE_DEFENDING_PLAYER_CONTROLS = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.isController(game.getDefendingPlayer());
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_CONVERTED_3_OR_LESS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getConvertedCost() <= 3;
        }
    };
    
    public static final MagicCardFilterImpl CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 3 && target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicPermanentFilterImpl CREATURE_CONVERTED_2_OR_LESS = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getConvertedCost() <= 2;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_TOUGHNESS_2_OR_LESS = new MagicPTTargetFilter(
        MagicTargetFilterFactory.CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );  

    public static final MagicPermanentFilterImpl CREATURE_TOUGHNESS_3_OR_LESS = new MagicPTTargetFilter(
        MagicTargetFilterFactory.CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );
    
    public static final MagicPermanentFilterImpl CREATURE_POWER_2_OR_LESS = new MagicPTTargetFilter(
        MagicTargetFilterFactory.CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );
    
    public static final MagicPermanentFilterImpl CREATURE_POWER_2_OR_LESS_YOU_CONTROL = new MagicPTTargetFilter(
        MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_3_OR_LESS = new MagicPTTargetFilter(
            MagicTargetFilterFactory.CREATURE,
            Operator.LESS_THAN_OR_EQUAL,
            3
        );
    
    public static final MagicPermanentFilterImpl CREATURE_POWER_4_OR_MORE = new MagicPTTargetFilter(
        MagicTargetFilterFactory.CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        4
    );
    
    public static final MagicPermanentFilterImpl CREATURE_POWER_3_OR_MORE_YOU_CONTROL = new MagicPTTargetFilter(
        MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
        Operator.GREATER_THAN_OR_EQUAL,
        3
    );
    
    public static final MagicPermanentFilterImpl CREATURE_POWER_3_OR_MORE = new MagicPTTargetFilter(
            MagicTargetFilterFactory.CREATURE,
            Operator.GREATER_THAN_OR_EQUAL,
            3
        );

    public static final MagicPermanentFilterImpl CREATURE_POWER_5_OR_MORE = new MagicPTTargetFilter(
            MagicTargetFilterFactory.CREATURE,
            Operator.GREATER_THAN_OR_EQUAL,
            5
        );

    public static final MagicPermanentFilterImpl CREATURE_POWER_5_OR_MORE_YOU_CONTROL = new MagicPTTargetFilter(
        MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
        Operator.GREATER_THAN_OR_EQUAL,
        5
    );

    public static final MagicPermanentFilterImpl CREATURE_PLUSONE_COUNTER = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.hasCounters(MagicCounterType.PlusOne);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_AT_LEAST_3_LEVEL_COUNTERS = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && 
                   target.getCounters(MagicCounterType.Level) >= 3;
        }
    };
    
    public static final MagicPermanentFilterImpl CREATURE_MINSUONE_COUNTER = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.hasCounters(MagicCounterType.MinusOne);
        }
    };
    
    public static final MagicPermanentFilterImpl CREATURE_WITH_COUNTER = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.hasCounters();
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Attacking, Control.Any);

    public static final MagicPermanentFilterImpl BLOCKING_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Blocking, Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.creature(MagicPermanentState.Attacking, Control.You);

    public static final MagicPermanentFilterImpl NONATTACKING_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isAttacking();
        }
    };
    
    public static final MagicPermanentFilterImpl NONATTACKING_NONBLOCKING_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isAttacking() &&
                   !target.isBlocking();
        }
    };
    
    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITH_FLANKING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasAbility(MagicAbility.Flanking);
        }
    };
    
    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITH_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasAbility(MagicAbility.Flying);
        }
    };
    
    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITHOUT_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   !target.hasAbility(MagicAbility.Flying);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_GOBLIN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasSubType(MagicSubType.Goblin);
        }
    };
    
    public static final MagicPermanentFilterImpl ATTACKING_HUMAN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasSubType(MagicSubType.Human);
        }
    };
    
    public static final MagicPermanentFilterImpl ATTACKING_AUROCHS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isAttacking() &&
                   target.hasSubType(MagicSubType.Aurochs);
        }
    };
    
    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.isAttacking() ||
                    target.isBlocking());
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_SPIRIT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasSubType(MagicSubType.Spirit) &&
                   (target.isAttacking() ||
                    target.isBlocking());
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   (target.isAttacking() || target.isBlocking());
        }
    };
    
    public static final MagicPermanentFilterImpl UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&  
                   target.isAttacking() &&
                   target.hasState(MagicPermanentState.Blocked) == false;
        }
    };
    
    public static final MagicPermanentFilterImpl KALDRA_EQUIPMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isEquipment() &&
                    (target.getName().equals("Sword of Kaldra") || 
                     target.getName().equals("Shield of Kaldra") || 
                     target.getName().equals("Helm of Kaldra"));
        }
    };

    public static final MagicPermanentFilterImpl BLOCKED_CREATURE = MagicTargetFilterFactory.creature(MagicPermanentState.Blocked, Control.Any);

    public static final MagicCardFilterImpl CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicCardFilterImpl CARD_FROM_LIBRARY=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };
    
    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Artifact) ||
                   target.hasType(MagicType.Creature) ||
                   target.hasType(MagicType.Enchantment);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                   targetType == MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicType.Creature);
    
    public static final MagicCardFilterImpl PAYABLE_CREATURE_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && target.getCost().getCondition().accept(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicCardFilterImpl PAYABLE_INSTANT_OR_SORCERY_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return (target.hasType(MagicType.Instant) || target.hasType(MagicType.Sorcery)) && target.getCost().getCondition().accept(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicCardFilterImpl CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && target.hasAbility(MagicAbility.Infect);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl PERMANENT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getCardDefinition().isPermanent();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicCardFilterImpl PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.getConvertedCost() <= 3 && cardDefinition.isPermanent();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicCardFilterImpl ARTIFACT_CARD_CMC_LEQ_1_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 1 && target.hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicCardFilterImpl CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 2 && target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_CMC_LEQ_3 = new MagicCMCPermanentFilter(
        MagicTargetFilterFactory.NONLAND_PERMANENT,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.OpponentsGraveyard, MagicType.Creature);

    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.cardOr(MagicTargetType.Graveyard, MagicType.Instant, MagicType.Sorcery);
    
    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD = MagicTargetFilterFactory.cardOr(MagicTargetType.OpponentsGraveyard, MagicType.Instant, MagicType.Sorcery);
    
    public static final MagicCardFilterImpl ARTIFACT_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicType.Artifact);

    public static final MagicCardFilterImpl NONCREATURE_ARTIFACT_CARD_WITH_CMC_LEQ_1_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 1 &&
                   target.hasType(MagicType.Artifact) &&
                   !target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };
    
    public static final MagicCardFilterImpl CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl ENCHANTMENT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Enchantment);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl INSTANT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Instant);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl SORCERY_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
   

    public static final MagicCardFilterImpl LAND_CARD_FROM_YOUR_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicType.Land);

    public static final MagicCardFilterImpl LAND_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Land);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) ||
                   target.hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };
    
    public static final MagicCardFilterImpl ZOMBIE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicSubType.Zombie);

    public static final MagicCardFilterImpl ZOMBIE_CREATURE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.creatureCard(MagicTargetType.Graveyard, MagicSubType.Zombie);

    public static final MagicCardFilterImpl SPIRIT_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.card(MagicTargetType.Graveyard, MagicSubType.Spirit);
    
    public static final MagicCardFilterImpl HUMAN_CREATURE_CARD_FROM_GRAVEYARD = MagicTargetFilterFactory.creatureCard(MagicTargetType.Graveyard, MagicSubType.Human);
    
    public static final MagicCardFilterImpl CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicCardFilterImpl BLUE_CARD_FROM_HAND = MagicTargetFilterFactory.card(MagicTargetType.Hand, MagicColor.Blue);

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_HAND = MagicTargetFilterFactory.card(MagicTargetType.Hand, MagicType.Creature);

    public static final MagicCardFilterImpl BLUE_OR_RED_CREATURE_CARD_FROM_HAND = MagicTargetFilterFactory.creatureCardOr(MagicTargetType.Hand, MagicColor.Blue, MagicColor.Red);

    public static final MagicPermanentFilterImpl MULTICOLORED_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent);
        }
    };
    
    public static final MagicStackFilterImpl MULTICOLORED_SPELL = new MagicStackFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return MagicColor.isMulti(itemOnStack.getSource()) && itemOnStack.isSpell();
        }
    };
    
    public static final MagicPermanentFilterImpl MONOCOLORED_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMono(permanent) && permanent.isCreature();
        }
    };
    
    public static final MagicPermanentFilterImpl MONOCOLORED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMono(permanent) && permanent.isCreature() && permanent.isController(player);
        }
    };
    
    public static final MagicPermanentFilterImpl MULTICOLORED_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent) && permanent.isController(player);
        }
    };

    public static final MagicCardFilterImpl MULTICOLORED_CREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && MagicColor.isMulti(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() &&
                   target.isUntapped() &&
                   target.isController(player);
        }
    };

    public static MagicCardFilterImpl WARRIOR_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
        public boolean accept(final MagicGame game, final MagicPlayer player, final MagicCard target) {
            return target.hasSubType(MagicSubType.Warrior);
        }
    };

    public static MagicPermanentFilterImpl NONARTIFACT_NONWHITE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact() &&
                   !target.hasColor(MagicColor.White);
        }
    };

    public static MagicPermanentFilterImpl UNTAPPED_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() &&
                   target.isUntapped();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_LAND = MagicTargetFilterFactory.permanentAnd(MagicType.Artifact, MagicType.Land, Control.Any);
    
    public static final MagicCardFilterImpl permanentCardMaxCMC(final MagicSubType subtype, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.getCardDefinition().isPermanent() && 
                       target.hasSubType(subtype) && 
                       target.getConvertedCost() <= cmc;
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    };
    
    public static final MagicPermanentFilterImpl NONARTIFACT_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isArtifact();
        }
    };
    
    public static final MagicPermanentFilterImpl NON_AURA_ENCHANTMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return !target.isAura() && target.isEnchantment();
        }
    };
    
    public static final MagicCardFilterImpl permanentCardMaxCMC(final MagicType type, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.getCardDefinition().isPermanent() && 
                       target.hasType(type) && 
                       target.getConvertedCost() <= cmc;
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }
    
    public static final MagicCardFilterImpl permanentCardMinCMC(final MagicType type, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.getCardDefinition().isPermanent() && 
                       target.hasType(type) && 
                       target.getConvertedCost() >= cmc;
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_HAND = MagicTargetFilterFactory.cardAnd(MagicTargetType.Hand, MagicType.Land, MagicType.Basic);
    
    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_LIBRARY = MagicTargetFilterFactory.cardAnd(MagicTargetType.Library, MagicType.Land, MagicType.Basic);
    
    public static final MagicCardFilterImpl BASIC_LAND_CARD_OR_GATE_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean acceptType(MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
        public boolean accept(MagicGame game, MagicPlayer player, MagicCard target) {
            return (target.hasType(MagicType.Basic) && target.hasType(MagicType.Land)) ||
                    target.hasSubType(MagicSubType.Gate);
        }
    };
    
    public static final MagicCardFilterImpl BASIC_FOREST_PLAINS_OR_ISLAND_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(MagicGame game, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                    target.hasSubType(MagicSubType.Forest) ||
                    target.hasSubType(MagicSubType.Plains) ||
                    target.hasSubType(MagicSubType.Island)
                   );
        }
        public boolean acceptType(MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };
    
    public static final MagicCardFilterImpl BASIC_PLAINS_ISLAND_OR_SWAMP_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(MagicGame game, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                    target.hasSubType(MagicSubType.Plains) ||
                    target.hasSubType(MagicSubType.Island) ||
                    target.hasSubType(MagicSubType.Swamp)
                   );
        }
        public boolean acceptType(MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };
    
    public static final MagicCardFilterImpl BASIC_ISLAND_SWAMP_OR_MOUNTAIN_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(MagicGame game, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                    target.hasSubType(MagicSubType.Island) ||
                    target.hasSubType(MagicSubType.Swamp) ||
                    target.hasSubType(MagicSubType.Mountain)
                   );
        }
        public boolean acceptType(MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };
    
    public static final MagicCardFilterImpl BASIC_SWAMP_MOUNTAIN_OR_FOREST_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(MagicGame game, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                    target.hasSubType(MagicSubType.Swamp) ||
                    target.hasSubType(MagicSubType.Mountain) ||
                    target.hasSubType(MagicSubType.Forest)
                   );
        }
        public boolean acceptType(MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };
    
    public static final MagicCardFilterImpl BASIC_MOUNTAIN_FOREST_OR_PLAINS_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(MagicGame game, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                    target.hasSubType(MagicSubType.Mountain) ||
                    target.hasSubType(MagicSubType.Forest) ||
                    target.hasSubType(MagicSubType.Plains)
                   );
        }
        public boolean acceptType(MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };
    
    public static final MagicCardFilterImpl PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
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
    
    public static final MagicCardFilterImpl INSTANT_OR_FLASH_CARD_FROM_LIBRARY = MagicTargetFilterFactory.cardOr(MagicTargetType.Library, MagicType.Instant, MagicAbility.Flash);
    
    public static final MagicCardFilterImpl LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY = new MagicCardFilterImpl() {
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
    
    public static final MagicPermanentFilterImpl UNPAIRED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.isPaired();
        }
    };

    public static final MagicPermanentFilterImpl UNPAIRED_SOULBOND_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.hasAbility(MagicAbility.Soulbond) &&
                   !target.isPaired();
        }
    };
    
    private static final Map<String, MagicTargetFilter<MagicPermanent>> multiple =
        new TreeMap<String, MagicTargetFilter<MagicPermanent>>(String.CASE_INSENSITIVE_ORDER);
    
    private static final Map<String, MagicTargetFilter<?>> single =
        new TreeMap<String, MagicTargetFilter<?>>(String.CASE_INSENSITIVE_ORDER);

    static {
        // used by lord ability/target <group>
        // <color|type|subtype> creatures you controls
        multiple.put("creatures you control", CREATURE_YOU_CONTROL);
        multiple.put("red creatures and white creatures you control", RED_OR_WHITE_CREATURE_YOU_CONTROL);
        multiple.put("creatures you control with flying", CREATURE_WITH_FLYING_YOU_CONTROL);
        multiple.put("enchanted creatures you control", ENCHANTED_CREATURE_YOU_CONTROL);
        multiple.put("non-human creatures you control", NONHUMAN_CREATURE_YOU_CONTROL);
        multiple.put("attacking creatures you control", ATTACKING_CREATURE_YOU_CONTROL);
        multiple.put("untapped creatures you control", UNTAPPED_CREATURE_YOU_CONTROL);

        // <color|type|subtype> creatures your opponents control
        multiple.put("creatures your opponents control", CREATURE_YOUR_OPPONENT_CONTROLS);
        multiple.put("creatures you don't control", CREATURE_YOUR_OPPONENT_CONTROLS);
       
        // <color|type|subtype> creatures
        multiple.put("creatures", CREATURE);
        multiple.put("all creatures", CREATURE);
        multiple.put("nonblack creatures", NONBLACK_CREATURE);
        multiple.put("nonwhite creatures", NONWHITE_CREATURE);
        multiple.put("nonartifact creatures", NONARTIFACT_CREATURE);
        multiple.put("nonenchantment creatures", NONENCHANTMENT_CREATURE);
        multiple.put("creatures without flying", CREATURE_WITHOUT_FLYING);
        multiple.put("creatures with flying", CREATURE_WITH_FLYING);
        multiple.put("all sliver creatures", SLIVER_CREATURE);
        multiple.put("all creatures", CREATURE);
        multiple.put("attacking creatures", ATTACKING_CREATURE);
        multiple.put("attacking creatures with flanking", ATTACKING_CREATURE_WITH_FLANKING);
        multiple.put("attacking Humans", ATTACKING_HUMAN);
        multiple.put("blocking creatures", BLOCKING_CREATURE);
        multiple.put("tapped creatures", TAPPED_CREATURE);
        multiple.put("creatures with power 3 or greater", CREATURE_POWER_3_OR_MORE);
        multiple.put("monocolored creatures", MONOCOLORED_CREATURE);
        multiple.put("creature tokens", CREATURE_TOKEN);
        multiple.put("all non-Zombie creatures", NONZOMBIE_CREATURE);
        multiple.put("tapped creatures you control", TAPPED_CREATURE_YOU_CONTROL);

        // <color|type|subtype> you control
        multiple.put("lands you control", LAND_YOU_CONTROL);
        multiple.put("permanents you control", PERMANENT_YOU_CONTROL);
        multiple.put("artifacts you control", ARTIFACT_YOU_CONTROL);
        multiple.put("enchantments you control", ENCHANTMENT_YOU_CONTROL);
        multiple.put("creature tokens you control", CREATURE_TOKEN_YOU_CONTROL);
        multiple.put("faeries you control", FAERIE_YOU_CONTROL);
        multiple.put("rats you control", RAT_YOU_CONTROL);
        multiple.put("allies you control", ALLY_YOU_CONTROL);
        multiple.put("Wolf permanents you control", WOLF_YOU_CONTROL);
        
        // <color|type|subtype> your opponents control
        multiple.put("creatures with flying your opponents control", CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        
        // <color|type|subtype> 
        multiple.put("lands",LAND);
        multiple.put("nonbasic lands", NONBASIC_LAND);
        multiple.put("islands", ISLAND);
        multiple.put("forests", FOREST);
        multiple.put("nonland permanents", NONLAND_PERMANENT);
        multiple.put("nonartifact permanents", NONARTIFACT_PERMANENT);
        multiple.put("all permanents you own", PERMANENT_YOU_OWN);
        multiple.put("all slivers", SLIVER_PERMANENT);
        multiple.put("all goblins", GOBLIN_PERMANENT);
        multiple.put("goblins", GOBLIN_PERMANENT);
        multiple.put("zombies", ZOMBIE);
        multiple.put("artifacts", ARTIFACT);
        multiple.put("noncreature artifacts", NONCREATURE_ARTIFACT);
        multiple.put("creatures and lands", CREATURE_OR_LAND);
        multiple.put("artifacts, creatures, and lands", ARTIFACT_OR_CREATURE_OR_LAND);
        multiple.put("artifacts and enchantments", ARTIFACT_OR_ENCHANTMENT);
        multiple.put("enchantments", ENCHANTMENT);
        multiple.put("auras", AURA);
        multiple.put("non-Aura enchantments", NON_AURA_ENCHANTMENT);
        multiple.put("artifacts, creatures, and enchantments", ARTIFACT_OR_CREATURE_OR_ENCHANTMENT);
        multiple.put("creatures with converted mana cost 3 or less", CREATURE_CONVERTED_3_OR_LESS);
        multiple.put("Djinns and Efreets", DJINN_OR_EFREET);


        // used by MagicTargetChoice
        // <color|type|subtype> card from your graveyard
        single.put("card from your graveyard", CARD_FROM_GRAVEYARD);
        single.put("instant or sorcery card from your graveyard", INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD);
        single.put("artifact or enchantment card from your graveyard", cardOr(MagicTargetType.Graveyard, MagicType.Artifact, MagicType.Enchantment));
        single.put("artifact, creature, or enchantment card from your graveyard", ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD);
        single.put("artifact card with converted mana cost 1 or less from your graveyard", ARTIFACT_CARD_CMC_LEQ_1_FROM_GRAVEYARD);
        single.put("creature or enchantment card from your graveyard", CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD);
        single.put("noncreature artifact card with converted mana cost 1 or less from your graveyard", NONCREATURE_ARTIFACT_CARD_WITH_CMC_LEQ_1_FROM_GRAVEYARD);
        single.put("Rebel permanent card with converted mana cost 5 or less from your graveyard", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Graveyard, 5));

        // <color|type|subtype> permanent card from your graveyard
        single.put("permanent card from your graveyard", PERMANENT_CARD_FROM_GRAVEYARD); 
        single.put("permanent card with converted mana cost 3 or less from your graveyard", PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD);
        
        // <color|type|subtype> creature card from your graveyard
        single.put("creature card with converted mana cost 3 or less from your graveyard", CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD);
        single.put("creature card with converted mana cost 2 or less from your graveyard", CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD); 
        single.put("creature card with infect from your graveyard", CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD);
        single.put("creature card with scavenge from your graveyard", MagicTargetFilterFactory.PAYABLE_CREATURE_CARD_FROM_GRAVEYARD);
        
        // <color|type|subtype> card from an opponent's graveyard
        single.put("instant or sorcery card from an opponent's graveyard", INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD);
        
        // <color|type|subtype> card from your hand
        single.put("card from your hand", CARD_FROM_HAND);
        single.put("basic land card from your hand", BASIC_LAND_CARD_FROM_HAND);
        
        // <color|type|subtype> permanent card from your hand
        
        // <color|type|subtype> creature card from your hand
        single.put("blue or red creature card from your hand", BLUE_OR_RED_CREATURE_CARD_FROM_HAND);
        single.put("multicolored creature card from your hand", MULTICOLORED_CREATURE_CARD_FROM_HAND);

        // <color|type|subtype> card from your library
        single.put("card from your library", CARD_FROM_LIBRARY);
        single.put("basic land card from your library", BASIC_LAND_CARD_FROM_LIBRARY);
        single.put("basic land card or a Gate card from your library", BASIC_LAND_CARD_OR_GATE_CARD_FROM_LIBRARY);
        single.put("Plains, Island, Swamp, Mountain or Forest card from your library", LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        single.put("Plains or Island card from your library", cardOr(MagicTargetType.Library, MagicSubType.Plains, MagicSubType.Island));
        single.put("Plains or Swamp card from your library", cardOr(MagicTargetType.Library, MagicSubType.Plains, MagicSubType.Swamp));
        single.put("Island or Swamp card from your library", cardOr(MagicTargetType.Library, MagicSubType.Island, MagicSubType.Swamp));
        single.put("Island or Mountain card from your library", cardOr(MagicTargetType.Library, MagicSubType.Island, MagicSubType.Mountain));
        single.put("Swamp or Mountain card from your library", cardOr(MagicTargetType.Library, MagicSubType.Swamp, MagicSubType.Mountain));
        single.put("Swamp or Forest card from your library", cardOr(MagicTargetType.Library, MagicSubType.Swamp, MagicSubType.Forest));
        single.put("Mountain or Forest card from your library", cardOr(MagicTargetType.Library, MagicSubType.Mountain, MagicSubType.Forest));
        single.put("Mountain or Plains card from your library", cardOr(MagicTargetType.Library, MagicSubType.Mountain, MagicSubType.Plains));
        single.put("Forest or Plains card from your library", cardOr(MagicTargetType.Library, MagicSubType.Forest, MagicSubType.Plains));
        single.put("Forest or Island card from your library", cardOr(MagicTargetType.Library, MagicSubType.Forest, MagicSubType.Island));
        single.put("Plains, Island, Swamp, or Mountain card from your library", PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY);
        single.put("land card with a basic land type from your library", LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        single.put("artifact or enchantment card from your library", cardOr(MagicTargetType.Library, MagicType.Artifact, MagicType.Enchantment));
        single.put("instant or sorcery card from your library", cardOr(MagicTargetType.Library, MagicType.Instant, MagicType.Sorcery));
        single.put("Treefolk or Forest card from your library", cardOr(MagicTargetType.Library, MagicSubType.Treefolk, MagicSubType.Forest));
        single.put("instant card or a card with flash from your library", INSTANT_OR_FLASH_CARD_FROM_LIBRARY);
        single.put("basic Forest, Plains, or Island card from your library", BASIC_FOREST_PLAINS_OR_ISLAND_FROM_LIBRARY);
        single.put("basic Plains, Island, or Swamp card from your library", BASIC_PLAINS_ISLAND_OR_SWAMP_FROM_LIBRARY);
        single.put("basic Island, Swamp, or Mountain card from your library", BASIC_ISLAND_SWAMP_OR_MOUNTAIN_FROM_LIBRARY);
        single.put("basic Swamp, Mountain, or Forest card from your library", BASIC_SWAMP_MOUNTAIN_OR_FOREST_FROM_LIBRARY);
        single.put("basic Mountain, Forest, or Plains card from your library", BASIC_MOUNTAIN_FOREST_OR_PLAINS_FROM_LIBRARY);
        single.put("enchantment card with converted mana cost 3 or less from your library", permanentCardMaxCMC(MagicType.Enchantment, MagicTargetType.Library, 3));
        
        // <color|type|subtype> permanent card from your library
        single.put("Rebel permanent card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 1));
        single.put("Rebel permanent card with converted mana cost 2 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 2));
        single.put("Rebel permanent card with converted mana cost 3 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 3));
        single.put("Rebel permanent card with converted mana cost 4 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 4));
        single.put("Rebel permanent card with converted mana cost 5 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 5));
        single.put("Rebel permanent card with converted mana cost 6 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 6));
        single.put("Mercenary permanent card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 1));
        single.put("Mercenary permanent card with converted mana cost 2 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 2));
        single.put("Mercenary permanent card with converted mana cost 3 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 3));
        single.put("Mercenary permanent card with converted mana cost 4 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 4));
        single.put("Mercenary permanent card with converted mana cost 5 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 5));
        single.put("Mercenary permanent card with converted mana cost 6 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 6));

        // <color|type|subtype> creature card from your library
        single.put("creature card with converted mana cost 6 or greater from your library", permanentCardMinCMC(MagicType.Creature, MagicTargetType.Library, 6));
        single.put("creature card with deathtouch, hexproof, reach, or trample from your library", CREATURE_WITH_DEATHTOUCH_HEXPROOF_REACH_OR_TRAMPLE_FROM_LIBRARY);
        
        // <color|type|subtype> creature you control
        single.put("non-Angel creature you control", NON_ANGEL_CREATURE_YOU_CONTROL);
        single.put("non-Spirit creature you control", NON_SPIRIT_CREATURE_YOU_CONTROL);
        single.put("black or red creature you control", BLACK_OR_RED_CREATURE_YOU_CONTROL);
        single.put("blue or black creature you control", BLUE_OR_BLACK_CREATURE_YOU_CONTROL);
        single.put("red or green creature you control", RED_OR_GREEN_CREATURE_YOU_CONTROL);
        single.put("untapped creature you control", UNTAPPED_CREATURE_YOU_CONTROL);
        single.put("artifact or creature you control", ARTIFACT_OR_CREATURE_YOU_CONTROL);
        single.put("attacking or blocking creature you control", ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL);
        single.put("nonlegendary creature you control", NON_LEGENDARY_CREATURE_YOU_CONTROL);
        single.put("non-Zombie creature you control", NON_ZOMBIE_YOU_CONTROL);
        single.put("non-Vampire creature you control", NON_VAMPIRE_YOU_CONTROL);
        single.put("unblocked attacking creature you control", UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL);
        single.put("attacking creature you control", ATTACKING_CREATURE_YOU_CONTROL);
        single.put("nontoken creature you control", NONTOKEN_CREATURE_YOU_CONTROL);
        single.put("creature with power 3 or greater you control", CREATURE_POWER_3_OR_MORE_YOU_CONTROL);
        single.put("creature with power 5 or greater you control", CREATURE_POWER_5_OR_MORE_YOU_CONTROL);
        single.put("creature with power 2 or less you control", CREATURE_POWER_2_OR_LESS_YOU_CONTROL); 
        single.put("creature you control with power 5 or greater", CREATURE_POWER_5_OR_MORE_YOU_CONTROL);
        single.put("creature with modular you control", MODULAR_CREATURE_YOU_CONTROL);
        single.put("creature you control with level up", LEVELUP_CREATURE_YOU_CONTROL);
        single.put("monocolored creature you control", MONOCOLORED_CREATURE_YOU_CONTROL);
        
        // <color|type|subtype> creature an opponent controls
        single.put("creature with flying an opponent controls", CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("red or green creature an opponent controls", RED_OR_GREEN_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("green or white creature an opponent controls", GREEN_OR_WHITE_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("creature an opponent controls", CREATURE_YOUR_OPPONENT_CONTROLS);

        // <color|type|subtype> creature
        single.put("1/1 creature", new MagicPTTargetFilter(MagicTargetFilterFactory.CREATURE, Operator.EQUAL, 1, Operator.EQUAL, 1));
        single.put("nonblack creature", NONBLACK_CREATURE);
        single.put("nonwhite creature", NONWHITE_CREATURE);
        single.put("nonartifact creature", NONARTIFACT_CREATURE);
        single.put("non-Demon creature", NON_DEMON_CREATURE);
        single.put("non-Zombie creature", NONZOMBIE_CREATURE);
        single.put("non-Human creature", NONHUMAN_CREATURE);
        single.put("non-Elf creature", NONELF_CREATURE);
        single.put("non-Vampire, non-Werewolf, non-Zombie creature", NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE);
        single.put("Skeleton, Vampire, or Zombie", SKELETON_VAMPIRE_OR_ZOMBIE);
        single.put("noncreature", NONCREATURE);
        single.put("nonartifact, nonblack creature", NONARTIFACT_NONBLACK_CREATURE);
        single.put("land or nonblack creature", LAND_OR_NONBLACK_CREATURE);
        single.put("red or green creature",RED_OR_GREEN_CREATURE);
        single.put("tapped creature", TAPPED_CREATURE);
        single.put("untapped creature", UNTAPPED_CREATURE);
        single.put("artifact or creature", ARTIFACT_OR_CREATURE);
        single.put("unpaired Soulbond creature", UNPAIRED_SOULBOND_CREATURE);
        single.put("monocolored creature", MONOCOLORED_CREATURE);
        single.put("attacking creature", ATTACKING_CREATURE);
        single.put("nonattacking creature", NONATTACKING_CREATURE);
        single.put("attacking or blocking creature", ATTACKING_OR_BLOCKING_CREATURE);
        single.put("blocked creature", BLOCKED_CREATURE);
        single.put("blocking creature", BLOCKING_CREATURE);
        single.put("green or white creature", GREEN_OR_WHITE_CREATURE);
        single.put("white or blue creature", WHITE_OR_BLUE_CREATURE);
        single.put("creature with converted mana cost 3 or less", CREATURE_CONVERTED_3_OR_LESS);
        single.put("creature with converted mana cost 2 or less", CREATURE_CONVERTED_2_OR_LESS);
        single.put("creature with flying", CREATURE_WITH_FLYING);
        single.put("creature without flying", CREATURE_WITHOUT_FLYING);
        single.put("creature with defender",  CREATURE_WITH_DEFENDER);
        single.put("creature with power 2 or less", CREATURE_POWER_2_OR_LESS);
        single.put("creature with power 3 or less", CREATURE_POWER_3_OR_LESS);
        single.put("creature with power 4 or greater", CREATURE_POWER_4_OR_MORE);
        single.put("creature with power 5 or greater", CREATURE_POWER_5_OR_MORE);
        single.put("creature with toughness 2 or less", CREATURE_TOUGHNESS_2_OR_LESS);
        single.put("creature with toughness 3 or less", CREATURE_TOUGHNESS_3_OR_LESS);
        single.put("creature with shadow", CREATURE_WITH_SHADOW);
        single.put("creature with a +1/+1 counter on it", CREATURE_PLUSONE_COUNTER);
        single.put("creature with a -1/-1 counter on it", CREATURE_MINSUONE_COUNTER);
        single.put("creature with a counter on it", CREATURE_WITH_COUNTER);
        single.put("creature that isn't enchanted", CREATURE_THAT_ISNT_ENCHANTED);
        single.put("attacking creature with flying", ATTACKING_CREATURE_WITH_FLYING);
        single.put("attacking creature without flying", ATTACKING_CREATURE_WITHOUT_FLYING);
        single.put("nontoken creature", NONTOKEN_CREATURE);
        single.put("Djinn or Efreet", DJINN_OR_EFREET);
        single.put("tapped nonblack creature", TAPPED_NONBLACK_CREATURE);
        single.put("nonattacking, nonblocking creature", NONATTACKING_NONBLOCKING_CREATURE);
        single.put("creature defending player controls", CREATURE_DEFENDING_PLAYER_CONTROLS);
        single.put("creature token", CREATURE_TOKEN);

        // <color|type|subtype> you control
        single.put("basic land you control", BASIC_LAND_YOU_CONTROL);
        single.put("nonbasic land you control", NONBASIC_LAND_YOU_CONTROL);
        single.put("land with a trap counter on it you control", TRAPPED_LAND_YOU_CONTROL);
        single.put("Forest or Plains you control", FOREST_OR_PLAINS_YOU_CONTROL);
        single.put("artifact, creature, or land you control",ARTIFACT_OR_CREATURE_OR_LAND_YOU_CONTROL);
        single.put("creature or enchantment you control", CREATURE_OR_ENCHANTMENT_YOU_CONTROL);
        single.put("creature token you control", CREATURE_TOKEN_YOU_CONTROL);
        single.put("Caribou token you control", CARIBOU_TOKEN_YOU_CONTROL);
        single.put("permanent you control", PERMANENT_YOU_CONTROL);
        single.put("multicolored permanent you control", MULTICOLORED_PERMANENT_YOU_CONTROL);
        single.put("blue permanent you control", BLUE_PERMANENT_YOU_CONTROL);
        single.put("nonland permanent you control", NONLAND_PERMANENT_YOU_CONTROL);
        single.put("nontoken permanent you control", NONTOKEN_PERMANENT_YOU_CONTROL);
        
        // <color|type|subtype> an opponent controls
        single.put("artifact or enchantment an opponent controls", ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent an opponent controls", NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("permanent an opponent controls", PERMANENT_AN_OPPONENT_CONTROLS);
        
        // <color|type|subtype> you don't control
        single.put("spell you don't control", SPELL_YOU_DONT_CONTROL);
        single.put("creature without flying you don't control", CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent you don't control", NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        
        // <color|type|subtype> permanent
        single.put("permanent", PERMANENT);
        single.put("permanent you own", PERMANENT_YOU_OWN);
        single.put("permanent you both own and control", PERMANENT_YOU_OWN_AND_CONTROL);
        single.put("noncreature permanent", NONCREATURE);
        single.put("spell or permanent", SPELL_OR_PERMANENT);
        single.put("nonland permanent", NONLAND_PERMANENT);
        single.put("nonland permanent with converted mana cost 3 or less", NONLAND_PERMANENT_CMC_LEQ_3);
        single.put("black or red permanent", BLACK_RED_PERMANENT);
        single.put("multicolored permanent", MULTICOLORED_PERMANENT);
        
        // <color|type|subtype>
        single.put("creature you own", CREATURE_YOU_OWN);
        single.put("permanent you own or control", PERMANENT_YOU_OWN_OR_CONTROL);
        single.put("Insect, Rat, Spider, or Squirrel", INSECT_RAT_SPIDER_OR_SQUIRREL);
        single.put("Vampire, Werewolf, or Zombie", VAMPIRE_WEREWOLF_OR_ZOMBIE);
        single.put("attacking or blocking Spirit",  ATTACKING_OR_BLOCKING_SPIRIT);
        single.put("basic land", BASIC_LAND);
        single.put("nonbasic land", NONBASIC_LAND);
        single.put("snow land", SNOW_LAND);
        single.put("Forest or Plains", FOREST_OR_PLAINS);
        single.put("artifact or land", ARTIFACT_OR_LAND);
        single.put("artifact land", ARTIFACT_LAND);
        single.put("artifact or enchantment", ARTIFACT_OR_ENCHANTMENT);
        single.put("artifact, enchantment, or land", ARTIFACT_OR_ENCHANTMENT_OR_LAND);
        single.put("artifact, creature, or land", ARTIFACT_OR_CREATURE_OR_LAND);
        single.put("artifact, creature, or enchantment",ARTIFACT_OR_CREATURE_OR_ENCHANTMENT);
        single.put("enchantment or land", ENCHANTMENT_OR_LAND);
        single.put("enchanted creature or enchantment creature", ENCHANTED_OR_ENCHANTMENT_CREATURE);
        single.put("noncreature artifact", NONCREATURE_ARTIFACT);
        single.put("Spirit or enchantment", SPIRIT_OR_ENCHANTMENT);
        single.put("creature or enchantment", CREATURE_OR_ENCHANTMENT);
        single.put("creature or land", CREATURE_OR_LAND);
        single.put("creature or planeswalker", CREATURE_OR_PLANESWALKER);
        single.put("creature or player", CREATURE_OR_PLAYER);
        single.put("Sliver creature or player", SLIVER_CREATURE_OR_PLAYER);
        single.put("nontoken Elf", NONTOKEN_ELF);
        single.put("legendary Samurai", LEGENDARY_SAMURAI);
        single.put("creature with three or more level counters on it", CREATURE_AT_LEAST_3_LEVEL_COUNTERS);
        single.put("untapped land", UNTAPPED_LAND );
       
        // <color|type> spell
        single.put("spell", SPELL);
        single.put("spell an opponent controls", SPELL_YOU_DONT_CONTROL);
        single.put("spell or ability", SPELL_OR_ABILITY);
        single.put("spell with converted mana cost 1", SPELL_WITH_CMC_EQ_1);
        single.put("spell with converted mana cost 2", SPELL_WITH_CMC_EQ_2);
        single.put("spell that targets a player", SPELL_THAT_TARGETS_PLAYER);
        single.put("spell with {X} in its mana cost", SPELL_WITH_X_COST);
        single.put("noncreature spell", NONCREATURE_SPELL);
        single.put("nonartifact spell", NONARTIFACT_SPELL);
        single.put("artifact or enchantment spell", ARTIFACT_OR_ENCHANTMENT_SPELL);
        single.put("red or green spell", RED_OR_GREEN_SPELL);
        single.put("blue or black spell", BLUE_OR_BLACK_SPELL);
        single.put("blue, black, or red spell", BLUE_OR_BLACK_OR_RED_SPELL);
        single.put("white, blue, black, or red spell", WHITE_OR_BLUE_OR_BLACK_OR_RED_SPELL);
        single.put("nonblue spell", NONBLUE_SPELL);
        single.put("blue spell during your turn", BLUE_SPELL_YOUR_TURN);
        single.put("blue or black spell during your turn", BLUE_OR_BLACK_SPELL_YOUR_TURN);
        single.put("nonred spell", NONRED_SPELL);
        single.put("instant or sorcery spell", INSTANT_OR_SORCERY_SPELL);
        single.put("creature or Aura spell", CREATURE_OR_AURA_SPELL);
        single.put("creature or sorcery spell", CREATURE_OR_SORCERY_SPELL);
        single.put("creature spell with converted mana cost 6 or greater", CREATURE_SPELL_CMC_6_OR_MORE);
        single.put("Spirit or Arcane spell", SPIRIT_OR_ARCANE_SPELL);
        single.put("multicolored spell", MULTICOLORED_SPELL);

        // player
        single.put("opponent", OPPONENT);
        single.put("other player", OPPONENT);
        single.put("player", PLAYER);

        // from a graveyard
        single.put("card from a graveyard", CARD_FROM_ALL_GRAVEYARDS);
        single.put("artifact or creature card from a graveyard", ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("creature card from a graveyard", CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("land card from a graveyard", LAND_CARD_FROM_ALL_GRAVEYARDS);
    }

    public static MagicTargetFilter<MagicPermanent> multiple(final String arg) {
        if (multiple.containsKey(arg)) {
            return multiple.get(arg);
        } else if (arg.endsWith(" creatures you control")) {
            return matchCreaturePrefix(arg, " creatures you control", Control.You);
        } else if (arg.endsWith(" creatures your opponents control")) {
            return matchCreaturePrefix(arg, " creatures your opponents control", Control.Opp);
        } else if (arg.endsWith(" creatures")) {
            return matchCreaturePrefix(arg, " creatures", Control.Any);
        } else if (arg.endsWith(" you control")) {
            return matchPermanentPrefix(arg, " you control", Control.You);
        } else if (arg.endsWith(" your opponents control")) {
            return matchPermanentPrefix(arg, " your opponents control", Control.Opp);
        } else if (arg.endsWith(" permanents")) {
            return matchPermanentPrefix(arg, " permanents", Control.Any);
        } else {
            return matchPermanentPrefix(arg, "", Control.Any);
        } 
    }
    
    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicTarget> singleTarget(final String arg) {
        return (MagicTargetFilter<MagicTarget>)single(arg);
    }
    
    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicPlayer> singlePlayer(final String arg) {
        return (MagicTargetFilter<MagicPlayer>)single(arg);
    }
    
    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicPermanent> singlePermanent(final String arg) {
        return (MagicTargetFilter<MagicPermanent>)single(arg);
    }
    
    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicItemOnStack> singleItemOnStack(final String arg) {
        return (MagicTargetFilter<MagicItemOnStack>)single(arg);
    }
    
    public static MagicTargetFilter<?> single(final String arg) {
        final String filter = arg.replaceFirst(" to sacrifice$", " you control");
        if (single.containsKey(filter)) {
            assert single.get(filter) != null : "return null for " + filter;
            return single.get(filter);
        } else if (filter.endsWith(" permanent card from your graveyard")) {
            return matchPermanentCardPrefix(filter, " permanent card from your graveyard", MagicTargetType.Graveyard);
        } else if (filter.endsWith(" creature card from your graveyard")) {
            return matchCreatureCardPrefix(filter, " creature card from your graveyard", MagicTargetType.Graveyard);
        } else if (filter.endsWith(" card from your graveyard")) {
            return matchCardPrefix(filter, " card from your graveyard", MagicTargetType.Graveyard);
        } else if (filter.endsWith(" permanent card from an opponent's graveyard")) {
            return matchPermanentCardPrefix(filter, " permanent card from an opponent's graveyard", MagicTargetType.OpponentsGraveyard);
        } else if (filter.endsWith(" creature card from an opponent's graveyard")) {
            return matchCreatureCardPrefix(filter, " creature card from an opponent's graveyard", MagicTargetType.OpponentsGraveyard);
        } else if (filter.endsWith(" card from an opponent's graveyard")) {
            return matchCardPrefix(filter, " card from an opponent's graveyard", MagicTargetType.OpponentsGraveyard);
        } else if (filter.endsWith(" permanent card from your hand")) {
            return matchPermanentCardPrefix(filter, " permanent card from your hand", MagicTargetType.Hand);
        } else if (filter.endsWith(" creature card from your hand")) {
            return matchCreatureCardPrefix(filter, " creature card from your hand", MagicTargetType.Hand);
        } else if (filter.endsWith(" card from your hand")) {
            return matchCardPrefix(filter, " card from your hand", MagicTargetType.Hand);
        } else if (filter.endsWith(" permanent card from your library")) {
            return matchPermanentCardPrefix(filter, " permanent card from your library", MagicTargetType.Library);
        } else if (filter.endsWith(" creature card from your library")) {
            return matchCreatureCardPrefix(filter, " creature card from your library", MagicTargetType.Library);
        } else if (filter.endsWith(" card from your library")) {
            return matchCardPrefix(filter, " card from your library", MagicTargetType.Library);
        } else if (filter.endsWith(" creature you control")) {
            return matchCreaturePrefix(filter, " creature you control", Control.You);
        } else if (filter.endsWith(" creature an opponent controls")) {
            return matchCreaturePrefix(filter, " creature an opponent controls", Control.Opp);
        } else if (filter.endsWith(" creature")) {
            return matchCreaturePrefix(filter, " creature", Control.Any);
        } else if (filter.endsWith(" spell")) {
            return matchSpellPrefix(filter, " spell");
        } else if (filter.endsWith(" you control")) {
            return matchPermanentPrefix(filter, " you control", Control.You);
        } else if (filter.endsWith(" an opponent controls")) {
            return matchPermanentPrefix(filter, " an opponent controls", Control.Opp);
        } else if (filter.endsWith(" you don't control")) {
            return matchPermanentPrefix(filter, " you don't control", Control.Opp);
        } else if (filter.endsWith(" permanent")) {
            return matchPermanentPrefix(filter, " permanent", Control.Any);
        } else {
            return matchPermanentPrefix(filter, "", Control.Any);
        }
    }
    
    private static MagicTargetFilter<MagicCard> matchCardPrefix(final String arg, final String suffix, final MagicTargetType location) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return card(location, c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return card(location, t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return card(location, st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    private static MagicTargetFilter<MagicCard> matchPermanentCardPrefix(final String arg, final String suffix, final MagicTargetType location) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return permanentCard(location, c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return permanentCard(location, t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return permanentCard(location, st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    private static MagicTargetFilter<MagicCard> matchCreatureCardPrefix(final String arg, final String suffix, final MagicTargetType location) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return creatureCard(location, c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return creatureCard(location, t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return creatureCard(location, st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }    
    private static MagicTargetFilter<MagicItemOnStack> matchSpellPrefix(final String arg, final String suffix) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return spell(c);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return spell(t);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return spell(st);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    private static MagicTargetFilter<MagicPermanent> matchPermanentPrefix(final String arg, final String suffix, final Control control) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return permanent(c, control);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return permanent(t, control);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return permanent(st, control);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    private static MagicTargetFilter<MagicPermanent> matchCreaturePrefix(final String arg, final String suffix, final Control control) {
        final String prefix = arg.replace(suffix, "");
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return creature(c, control);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return creature(t, control);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return creature(st, control);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    
    enum Control {
        Any,
        You,
        Opp
    }
    
    enum Own {
        Any,
        You,
        Opp
    }
        
    public static final MagicPermanentFilterImpl permanent(final MagicType type, final Control control) {
        return permanentOr(type, type, control);
    }
    
    public static final MagicPermanentFilterImpl permanent(final MagicType type, final Own own) {
        return permanentOr(type, type, own);
    }

    private static MagicPermanentFilterImpl permanentOr(final MagicType type1, final MagicType type2, final Own own) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.hasType(type1) && target.hasType(type2) &&
                       ((own == Own.You && target.isOwner(player)) ||
                        (own == Own.Opp && target.isOwner(player)) ||
                        (own == Own.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentAnd(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.hasType(type1) && target.hasType(type2) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasType(type1) || target.hasType(type2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicSubType subType1, final MagicSubType subType2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasSubType(subType1) || target.hasSubType(subType2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasType(type) || target.hasSubType(subType)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanentOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasColor(color1) || target.hasColor(color2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    
    public static final MagicPermanentFilterImpl permanent(final MagicColor color, final Control control) {
        return permanentOr(color, color, control);
    }
    public static final MagicPermanentFilterImpl permanent(final MagicSubType subType, final Control control) {
        return permanentOr(subType, subType, control);
    }
    public static final MagicPermanentFilterImpl creature(final MagicColor color, final Control control) {
        return creatureOr(color, color, control);
    }
    public static final MagicPermanentFilterImpl creature(final MagicType type, final Control control) {
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
    public static final MagicPermanentFilterImpl creature(final MagicSubType subtype, final Control control) {
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
    public static final MagicPermanentFilterImpl creatureOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasColor(color1) || target.hasColor(color2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creature(final MagicAbility ability, final Control control) {
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
    public static final MagicPermanentFilterImpl creature(final MagicPermanentState state, final Control control) {
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
    public static final MagicCardFilterImpl card(final MagicTargetType aTargetType, final MagicSubType subType) {
        return cardOr(aTargetType, subType, subType);
    }
    public static final MagicCardFilterImpl cardOr(final MagicTargetType aTargetType, final MagicSubType type1, final MagicSubType type2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasSubType(type1) || target.hasSubType(type2);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicTargetType aTargetType, final MagicType type) {
        return cardOr(aTargetType, type, type);
    }
    public static final MagicCardFilterImpl cardOr(final MagicTargetType aTargetType, final MagicType type1, final MagicType type2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type1) || target.hasType(type2);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl cardOr(final MagicTargetType aTargetType, final MagicType type, final MagicAbility ability) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type) || target.hasAbility(ability);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl cardAnd(final MagicTargetType aTargetType, final MagicType type1, final MagicType type2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type1) && target.hasType(type2);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicTargetType aTargetType, final MagicColor color) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasColor(color);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicStackFilterImpl spell(final MagicColor color) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() && itemOnStack.hasColor(color);
            }
        };
    }
    public static final MagicStackFilterImpl spell(final MagicType type) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type);
            }
        };
    }
    public static final MagicStackFilterImpl spell(final MagicSubType subType) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(subType);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicType type1, final MagicType type2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type1) || itemOnStack.isSpell(type2);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicType type, final MagicSubType subType) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type) || itemOnStack.isSpell(subType);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicSubType subType1, final MagicSubType subType2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(subType1) || itemOnStack.isSpell(subType2);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicColor color1, final MagicColor color2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() &&
                       (itemOnStack.hasColor(color1) || itemOnStack.hasColor(color2));
            }
        };
    }
    public static final MagicCardFilterImpl creatureCard(final MagicTargetType aTargetType, final MagicType type) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type) && target.hasType(MagicType.Creature);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl creatureCard(final MagicTargetType aTargetType, final MagicSubType subType) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasSubType(subType) && target.hasType(MagicType.Creature);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl creatureCard(final MagicTargetType aTargetType, final MagicColor color) {
        return creatureCardOr(aTargetType, color, color);
    }
    public static final MagicCardFilterImpl creatureCardOr(final MagicTargetType aTargetType, final MagicColor color1, final MagicColor color2) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return (target.hasColor(color1) || target.hasColor(color2)) && target.hasType(MagicType.Creature);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl permanentCard(final MagicTargetType aTargetType, final MagicType type) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type) && target.getCardDefinition().isPermanent();
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl permanentCard(final MagicTargetType aTargetType, final MagicColor color) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasColor(color) && target.getCardDefinition().isPermanent();
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
    public static final MagicCardFilterImpl permanentCard(final MagicTargetType aTargetType, final MagicSubType subType) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
                return target.hasSubType(subType) && target.getCardDefinition().isPermanent();
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == aTargetType;
            }
        };
    }
}

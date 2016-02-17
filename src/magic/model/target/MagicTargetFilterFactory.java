package magic.model.target;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.stack.MagicItemOnStack;

public class MagicTargetFilterFactory {

    public static final MagicPermanentFilterImpl NONE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return false;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return false;
        }
    };

    public static final MagicPermanentFilterImpl ANY = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return true;
        }
        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl SN = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return source == target;
        }
    };

    public static final MagicPlayerFilterImpl YOU = new MagicPlayerFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
            return player == target;
        }
    };

    public static final MagicPermanentFilterImpl EQUIPMENT_ATTACHED_TO_SOURCE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.getEquippedCreature() == source;
        }
    };

    public static final MagicStackFilterImpl SPELL_OR_ABILITY_THAT_TARGETS_PERMANENTS =new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
            return tchoice.isValid() &&
                   tchoice.isTargeted() &&
                   tchoice.getTargetFilter().acceptType(MagicTargetType.Permanent);
        }
    };

    public static final MagicStackFilterImpl SPELL_OR_ABILITY = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return true;
        }
    };

    public static final MagicStackFilterImpl SPELL_OR_ABILITY_OPPONENT_CONTROL = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isEnemy(player);
        }
    };

    public static final MagicStackFilterImpl ACTIVATED_ABILITY = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target instanceof MagicAbilityOnStack;
        }
    };

    public static final MagicStackFilterImpl ACTIVATED_OR_TRIGGERED_ABILITY = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() == false;
        }
    };

    public static final MagicStackFilterImpl SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell();
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_PLAYER=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
            return target.isSpell() &&
                   tchoice.isValid() &&
                   tchoice.isTargeted() &&
                   tchoice.getTargetFilter().acceptType(MagicTargetType.Player);
        }
    };

    public static final MagicStackFilterImpl SPELL_YOU_DONT_CONTROL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getController() != player;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_EQ_1 = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() == 1;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_EQ_2 = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() == 2;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_LEQ_3 = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() <= 3;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_4_OR_GREATER = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() >= 4;
        }
    };

    public static final MagicStackFilterImpl INSTANT_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell(MagicType.Instant) &&
                   target.getConvertedCost() <= 2 &&
                   target.getController() == player;
        }
    };

    public static final MagicStackFilterImpl SORCERY_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell(MagicType.Sorcery) &&
                   target.getConvertedCost() <= 2 &&
                   target.getController() == player;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_X_COST=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getCardDefinition().hasX();
        }
    };

    public static final MagicStackFilterImpl RED_OR_GREEN_SPELL = spellOr(MagicColor.Red, MagicColor.Green);

    public static final MagicStackFilterImpl BLUE_OR_BLACK_SPELL = spellOr(MagicColor.Blue, MagicColor.Black);

    public static final MagicStackFilterImpl GREEN_OR_WHITE_SPELL = spellOr(MagicColor.Green, MagicColor.White);

    public static final MagicStackFilterImpl NONBLUE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicStackFilterImpl NONBLACK_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Black);
        }
    };

    public static final MagicStackFilterImpl NONFAERIE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasSubType(MagicSubType.Faerie);
        }
    };

    public static final MagicStackFilterImpl BLUE_INSTANT_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Instant) && itemOnStack.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicCardFilterImpl BLUE_INSTANT_CARD_FROM_LIBRARY = card(MagicColor.Blue).and(MagicType.Instant).from(MagicTargetType.Library);

    public static final MagicStackFilterImpl BLUE_SPELL_YOUR_TURN=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            final MagicGame game = player.getGame();
            return itemOnStack.isSpell() && itemOnStack.hasColor(MagicColor.Blue) && game.getTurnPlayer() == player;
        }
    };

    public static final MagicStackFilterImpl BLUE_OR_BLACK_SPELL_YOUR_TURN=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            final MagicGame game = player.getGame();
            return itemOnStack.isSpell() &&
                   (itemOnStack.hasColor(MagicColor.Blue) || itemOnStack.hasColor(MagicColor.Black)) &&
                   game.getTurnPlayer() == player;
        }
    };

    public static final MagicStackFilterImpl NONRED_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Red);
        }
    };

    public static final MagicStackFilterImpl BLUE_OR_BLACK_OR_RED_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && (
                itemOnStack.hasColor(MagicColor.Blue) ||
                itemOnStack.hasColor(MagicColor.Black) ||
                itemOnStack.hasColor(MagicColor.Red));
        }
    };

    public static final MagicStackFilterImpl WHITE_OR_BLUE_OR_BLACK_OR_RED_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && (
                itemOnStack.hasColor(MagicColor.White) ||
                itemOnStack.hasColor(MagicColor.Blue) ||
                itemOnStack.hasColor(MagicColor.Black) ||
                itemOnStack.hasColor(MagicColor.Red));
        }
    };

    public static final MagicStackFilterImpl CREATURE_OR_AURA_SPELL = spellOr(MagicType.Creature, MagicSubType.Aura);

    public static final MagicStackFilterImpl CREATURE_OR_SORCERY_SPELL = spellOr(MagicType.Creature, MagicType.Sorcery);

    public static final MagicStackFilterImpl NONCREATURE_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   !itemOnStack.isSpell(MagicType.Creature);
        }
    };

    public static final MagicStackFilterImpl NONARTIFACT_SPELL=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                   !itemOnStack.isSpell(MagicType.Artifact);
        }
    };

    public static final MagicStackFilterImpl CREATURE_SPELL_CMC_6_OR_MORE=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.getConvertedCost() >= 6;
        }
    };

    public static final MagicStackFilterImpl CREATURE_SPELL_WITH_INFECT=new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.hasAbility(MagicAbility.Infect);
        }
    };

    public static final MagicStackFilterImpl WHITE_OR_BLUE_INSTANT_OR_SORCERY_SPELL = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return (target.hasColor(MagicColor.White) || target.hasColor(MagicColor.Blue)) &&
                   (target.isSpell(MagicType.Instant) || target.isSpell(MagicType.Sorcery));
        }
    };

    public static final MagicStackFilterImpl INSTANT_OR_SORCERY_SPELL = spellOr(MagicType.Instant, MagicType.Sorcery);

    public static final MagicStackFilterImpl INSTANT_OR_SORCERY_SPELL_YOU_CONTROL = spellOr(MagicType.Instant, MagicType.Sorcery, Control.You);

    public static final MagicStackFilterImpl SPIRIT_OR_ARCANE_SPELL= spellOr(MagicSubType.Spirit, MagicSubType.Arcane);

    public static final MagicStackFilterImpl ARTIFACT_OR_ENCHANTMENT_SPELL = spellOr(MagicType.Artifact, MagicType.Enchantment);

    public static final MagicPlayerFilterImpl PLAYER=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
            return true;
        }
    };

    public static final MagicPlayerFilterImpl DEFENDING_PLAYER=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
            return target.getGame().getDefendingPlayer() == target;
        }
    };

    public static final MagicPlayerFilterImpl PLAYER_LOST_LIFE=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
            return target.getLifeLossThisTurn()>=1;
        }
    };

    public static final MagicPlayerFilterImpl PLAYER_CONTROLS_CREATURE=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
            return target.controlsPermanent(MagicType.Creature);
        }
    };

    public static final MagicPlayerFilterImpl OPPONENT=new MagicPlayerFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPlayer target) {
            return target!=player;
        }
    };

    public static final MagicTargetFilterImpl SPELL_OR_PERMANENT=new MagicTargetFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicTarget target) {
            return target.isSpell()||target.isPermanent();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Stack ||
                   targetType==MagicTargetType.Permanent;
        }
    };

    public static final MagicPermanentFilterImpl BLACK_PERMANENT = permanent(MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_PERMANENT = permanent(MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl RED_PERMANENT = permanent(MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_PERMANENT = permanent(MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_PERMANENT = permanent(MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_PERMANENT_YOU_CONTROL = permanent(MagicColor.Blue, Control.You);

    public static final MagicPermanentFilterImpl BLACK_PERMANENT_YOU_CONTROL = permanent(MagicColor.Black, Control.You);

    public static final MagicPermanentFilterImpl PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl BLACK_OR_RED_PERMANENT = permanentOr(MagicColor.Black, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_OR_GREEN_PERMANENT = permanentOr(MagicColor.Black, MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_OR_RED_PERMANENT = permanentOr(MagicColor.Blue, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_BLUE_PERMANENT = permanentOr(MagicColor.Green, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLACK_PERMANENT = permanentOr(MagicColor.White, MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_PERMANENT_AN_OPPONENT_CONTROLS = permanentOr(MagicColor.White, MagicColor.Blue, Control.Opp);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_PERMANENT_YOU_CONTROL = permanentOr(MagicColor.White, MagicColor.Blue, Control.You);

    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_PERMANENT_YOU_CONTROL = permanentOr(MagicColor.Green, MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_PERMANENT = permanentOr(MagicColor.White, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl RED_OR_WHITE_PERMANENT = permanentOr(MagicColor.Red, MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl NON_SWAMP_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasSubType(MagicSubType.Swamp);
        }
    };

    public static final MagicPermanentFilterImpl NONBASIC_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic);
        }
    };

    public static final MagicPermanentFilterImpl NONBASIC_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl NON_LAIR_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasSubType(MagicSubType.Lair) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl NONBASIC_LAND_AN_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic) && target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl TRAPPED_LAND_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() &&
                   target.getCounters(MagicCounterType.Trap) >= 1 &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl TAPPED_BASIC_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() &&
                    target.hasType(MagicType.Basic) &&
                    target.isTapped();
        }
    };

    public static final MagicPermanentFilterImpl BASIC_LAND = permanentAnd(MagicType.Land, MagicType.Basic, Control.Any);

    public static final MagicPermanentFilterImpl SNOW_LAND = permanentAnd(MagicType.Land, MagicType.Snow, Control.Any);

    public static final MagicCardFilterImpl SNOW_LAND_CARD_FROM_LIBRARY = card(MagicType.Snow).and(MagicType.Land).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl LAND_CARD_FROM_LIBRARY = card(MagicType.Land).from(MagicTargetType.Library);

    public static final MagicPermanentFilterImpl BASIC_LAND_YOU_CONTROL = permanentAnd(MagicType.Land, MagicType.Basic, Control.You);

    public static final MagicPermanentFilterImpl SNOW_LAND_YOU_CONTROL = permanentAnd(MagicType.Land, MagicType.Snow, Control.You);

    public static final MagicPermanentFilterImpl SNOW_MOUNTAIN_YOU_CONTROL = permanentAnd(MagicType.Snow, MagicSubType.Mountain, Control.You);

    public static final MagicPermanentFilterImpl SNOW_SWAMP_YOU_CONTROL = permanentAnd(MagicType.Snow, MagicSubType.Swamp, Control.You);

    public static final MagicPermanentFilterImpl SNOW_ISLAND_YOU_CONTROL = permanentAnd(MagicType.Snow, MagicSubType.Island, Control.You);

    public static final MagicPermanentFilterImpl SNOW_FOREST_YOU_CONTROL = permanentAnd(MagicType.Snow, MagicSubType.Forest, Control.You);

    public static final MagicPermanentFilterImpl SNOW_PLAINS_YOU_CONTROL = permanentAnd(MagicType.Snow, MagicSubType.Plains, Control.You);

    public static final MagicPermanentFilterImpl LAND = permanent(MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl LAND_OR_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() || (!target.hasColor(MagicColor.Black) && target.isCreature());
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand();
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && target.isController(player);
        }
    };


    public static final MagicPermanentFilterImpl NONLAND_NONTOKEN_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && !target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && !target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_ARTIFACT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && !target.isToken() && target.hasType(MagicType.Artifact);
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && !target.isToken() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_CREATURE_AN_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isController(player) && !target.isToken() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl FACEUP_NONTOKEN_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && !target.isToken() && target.isCreature() && target.isFaceDown() == false;
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isToken() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_WHITE_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isToken() && target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_ELF=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isToken() && target.hasSubType(MagicSubType.Elf);
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_RED_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isToken() && target.hasColor(MagicColor.Red);
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isLand() && target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl NONCREATURE_ARTIFACT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() && !target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT = permanent(MagicType.Artifact, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_YOU_CONTROL = permanent(MagicType.Artifact, Control.You);

    public static final MagicPermanentFilterImpl ARTIFACT_YOUR_OPPONENT_CONTROLS = permanent(MagicType.Artifact, Control.Opp);

    public static final MagicPermanentFilterImpl ARTIFACT_CREATURE = permanentAnd(MagicType.Artifact, MagicType.Creature, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE = permanentOr(MagicType.Artifact, MagicType.Creature, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_YOU_CONTROL = permanentOr(MagicType.Artifact, MagicType.Creature, Control.You);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isArtifact() ||
                   target.isCreature() ||
                   target.isLand();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && (
                       target.isArtifact() ||
                       target.isCreature() ||
                       target.isLand()
                   );
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT = permanentOr(MagicType.Artifact, MagicType.Enchantment, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_CMC_3_OR_LESS = new MagicCMCPermanentFilter(
        ARTIFACT_OR_ENCHANTMENT,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl ARTIFACT_OR_LAND = permanentOr(MagicType.Artifact, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_OR_LAND=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() ||
                   target.isArtifact() ||
                   target.isEnchantment();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_ENCHANTMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() ||
                   target.isArtifact() ||
                   target.isEnchantment();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS = permanentOr(MagicType.Artifact, MagicType.Enchantment, Control.Opp);

    public static final MagicPermanentFilterImpl NONCREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isCreature();
        }
    };

    public static final MagicTargetFilterImpl ONE=new MagicTargetFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicTarget target) {
            return target == player;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Player;
        }
    };

    public static final MagicTargetFilterImpl CREATURE_OR_PLAYER=new MagicTargetFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicTarget target) {
            return target.isPlayer() ||
                   target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent ||
                   targetType==MagicTargetType.Player;
        }
    };

    public static final MagicTargetFilterImpl SLIVER_CREATURE_OR_PLAYER=new MagicTargetFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicTarget target) {
            return target.isPlayer() ||
                   (target.isCreature() && target.hasSubType(MagicSubType.Sliver));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Permanent ||
                   targetType==MagicTargetType.Player;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_OR_LAND = permanentOr(MagicType.Creature, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_OR_LAND_YOU_CONTROL = permanentOr(MagicType.Creature, MagicType.Land, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_OR_PLANESWALKER = permanentOr(MagicType.Creature, MagicType.Planeswalker, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_OR_ENCHANTMENT = permanentOr(MagicType.Creature, MagicType.Enchantment, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_OR_ENCHANTMENT_YOU_CONTROL = permanentOr(MagicType.Creature, MagicType.Enchantment, Control.You);

    public static final MagicCardFilterImpl CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD = card(MagicType.Creature).or(MagicType.Enchantment).from(MagicTargetType.Graveyard);

    public static final MagicPermanentFilterImpl EQUIPMENT = permanent(MagicSubType.Equipment, Control.Any);

    public static final MagicPermanentFilterImpl EQUIPMENT_YOU_CONTROL = permanent(MagicSubType.Equipment, Control.You);

    public static final MagicPermanentFilterImpl ENCHANTMENT = permanent(MagicType.Enchantment, Control.Any);

    public static final MagicPermanentFilterImpl ENCHANTMENT_OR_LAND = permanentOr(MagicType.Enchantment, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl ENCHANTMENT_YOU_CONTROL = permanent(MagicType.Enchantment, Control.You);

    public static final MagicPermanentFilterImpl ENCHANTMENT_YOU_OWN_AND_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) && target.isController(player) && target.hasType(MagicType.Enchantment);
        }
    };

    public static final MagicPermanentFilterImpl RED_OR_GREEN_ENCHANTMENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isController(player) && target.isEnchantment() &&
                   (target.hasColor(MagicColor.Red) || target.hasColor(MagicColor.Green));
        }
    };

    public static final MagicPermanentFilterImpl SPIRIT_OR_ENCHANTMENT = permanentOr(MagicType.Enchantment, MagicSubType.Spirit, Control.Any);

    public static final MagicPermanentFilterImpl PERMANENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_AN_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_PERMANENT_AN_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isUntapped() && target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isOwner(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN_AND_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN_OR_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) || target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_FADING_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && target.hasAbility(MagicAbility.Fading);
        }
    };

    public static final MagicPermanentFilterImpl LAND_YOU_CONTROL = permanent(MagicType.Land, Control.You);

    public static final MagicPermanentFilterImpl FOREST = permanent(MagicSubType.Forest, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_YOU_CONTROL = permanent(MagicSubType.Forest, Control.You);

    public static final MagicPermanentFilterImpl ISLAND_YOU_CONTROL = permanent(MagicSubType.Island, Control.You);

    public static final MagicPermanentFilterImpl ISLAND_OR_SWAMP_AN_OPPONENT_CONTROLS = permanentOr(MagicSubType.Island, MagicSubType.Swamp, Control.Opp);

    public static final MagicPermanentFilterImpl ISLAND = permanent(MagicSubType.Island, Control.Any);

    public static final MagicPermanentFilterImpl MOUNTAIN = permanent(MagicSubType.Mountain, Control.Any);

    public static final MagicPermanentFilterImpl MOUNTAIN_YOU_CONTROL = permanent(MagicSubType.Mountain, Control.You);

    public static final MagicPermanentFilterImpl PLAINS = permanent(MagicSubType.Plains, Control.Any);

    public static final MagicPermanentFilterImpl AURA = permanent(MagicSubType.Aura, Control.Any);

    public static final MagicPermanentFilterImpl AURA_YOU_OWN = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) && target.hasSubType(MagicSubType.Aura);
        }
    };

    public static final MagicPermanentFilterImpl AURA_ATTACHED_TO_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Aura) &&
                target.getEnchantedPermanent().hasType(MagicType.Creature);
        }
    };

    public static final MagicPermanentFilterImpl SWAMP = permanent(MagicSubType.Swamp, Control.Any);

    public static final MagicPermanentFilterImpl SWAMP_YOU_CONTROL = permanent(MagicSubType.Swamp, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl CARIBOU_TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isToken() &&
                   target.hasSubType(MagicSubType.Caribou);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_TOKEN = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl SERF_TOKEN = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Serf) &&
                   target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_THAT_ISNT_ENCHANTED = new MagicPermanentFilterImpl(){
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.isEnchanted() == false;
        }
    };

    public static final MagicPermanentFilterImpl NON_LEGENDARY_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.hasType(MagicType.Legendary) &&
                   target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NON_LEGENDARY_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   !target.hasType(MagicType.Legendary) &&
                   target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NON_LEGENDARY_CREATURE_AN_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isController(player) &&
                   !target.hasType(MagicType.Legendary) &&
                   target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl ONE_ONE_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                target.isController(player) &&
                target.getPower() == 1 &&
                target.getToughness() == 1;
        }
    };

    public static final MagicPermanentFilterImpl BLACK_OR_RED_CREATURE_YOU_CONTROL = creatureOr(MagicColor.Black, MagicColor.Red, Control.You);

    public static final MagicPermanentFilterImpl BLUE_OR_BLACK_CREATURE_YOU_CONTROL = creatureOr(MagicColor.Blue, MagicColor.Black, Control.You);

    public static final MagicPermanentFilterImpl RED_OR_GREEN_CREATURE_YOU_CONTROL = creatureOr(MagicColor.Red, MagicColor.Green, Control.You);

    public static final MagicPermanentFilterImpl WHITE_OR_BLACK_CREATURE_YOU_CONTROL = creatureOr(MagicColor.White, MagicColor.Black, Control.You);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_CREATURE_YOU_CONTROL = creatureOr(MagicColor.White, MagicColor.Blue, Control.You);

    public static final MagicPermanentFilterImpl RED_OR_GREEN_CREATURE_AN_OPPONENT_CONTROLS = creatureOr(MagicColor.Red, MagicColor.Green, Control.Opp);

    public static final MagicPermanentFilterImpl FOREST_OR_PLAINS = permanentOr(MagicSubType.Forest, MagicSubType.Plains, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_OR_SAPROLING = permanentOr(MagicSubType.Forest, MagicSubType.Saproling, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_OR_PLAINS_YOU_CONTROL = permanentOr(MagicSubType.Forest, MagicSubType.Plains, Control.You);

    public static final MagicPermanentFilterImpl PLAINS_OR_ISLAND = permanentOr(MagicSubType.Plains, MagicSubType.Island, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_OR_RED_CREATURE = creatureOr(MagicColor.Blue, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_OR_GREEN_CREATURE = creatureOr(MagicColor.Black, MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_OR_RED_CREATURE = creatureOr(MagicColor.Black, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl RED_OR_GREEN_CREATURE = creatureOr(MagicColor.Red, MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl RED_OR_WHITE_CREATURE = creatureOr(MagicColor.Red, MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_CREATURE = creatureOr(MagicColor.Green, MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_BLUE_CREATURE = creatureOr(MagicColor.Green, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_CREATURE_AN_OPPONENT_CONTROLS = creatureOr(MagicColor.Green, MagicColor.White, Control.Opp);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_CREATURE = creatureOr(MagicColor.White, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLACK_CREATURE = creatureOr(MagicColor.White, MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl RED_OR_WHITE_CREATURE_YOU_CONTROL = creatureOr(MagicColor.Red, MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_CREATURE_YOU_CONTROL = creatureOr(MagicColor.Green, MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl BLACK_CREATURE = creature(MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_CREATURE = creature(MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_CREATURE = creature(MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_CREATURE_YOU_CONTROL = creature(MagicColor.Black, Control.You);

    public static final MagicPermanentFilterImpl GREEN_CREATURE_YOU_CONTROL = creature(MagicColor.Green, Control.You);

    public static final MagicPermanentFilterImpl GREEN_CREATURE = creature(MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl RED_CREATURE_YOU_CONTROL = creature(MagicColor.Red, Control.You);

    public static final MagicPermanentFilterImpl RED_CREATURE = creature(MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_CREATURE_YOU_CONTROL = creature(MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl DRAGON_YOU_CONTROL = permanent(MagicSubType.Dragon, Control.You);

    public static final MagicPermanentFilterImpl SOLDIER_OR_WARRIOR_YOU_CONTROL = permanentOr(MagicSubType.Soldier, MagicSubType.Warrior, Control.You);

    public static final MagicPermanentFilterImpl FOREST_OR_TREEFOLK_YOU_CONTROL = permanentOr(MagicSubType.Forest, MagicSubType.Treefolk, Control.You);

    public static final MagicPermanentFilterImpl GOBLIN_CREATURE = permanentAnd(MagicType.Creature, MagicSubType.Goblin, Control.Any);

    public static final MagicPermanentFilterImpl GOBLIN_OR_SHAMAN = permanentOr(MagicSubType.Goblin, MagicSubType.Shaman, Control.Any);

    public static final MagicPermanentFilterImpl DJINN_OR_EFREET = permanentOr(MagicSubType.Djinn, MagicSubType.Efreet, Control.Any);

    public static final MagicPermanentFilterImpl TREEFOLK_OR_WARRIOR = permanentOr(MagicSubType.Treefolk, MagicSubType.Warrior, Control.Any);

    public static final MagicPermanentFilterImpl CLERIC_OR_WIZARD_CREATURE = creatureOr(MagicSubType.Cleric, MagicSubType.Wizard, Control.Any);

    public static final MagicPermanentFilterImpl LEGENDARY_SAMURAI = creatureAnd(MagicType.Legendary, MagicSubType.Samurai, Control.Any);

    public static final MagicPermanentFilterImpl LEGENDARY_SNAKE_YOU_CONTROL = creatureAnd(MagicType.Legendary, MagicSubType.Snake, Control.You);

    public static final MagicPermanentFilterImpl INSECT_RAT_SPIDER_OR_SQUIRREL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Insect) ||
                   target.hasSubType(MagicSubType.Rat) ||
                   target.hasSubType(MagicSubType.Spider) ||
                   target.hasSubType(MagicSubType.Squirrel);
        }
    };

    public static final MagicPermanentFilterImpl SKELETON_VAMPIRE_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Skeleton) ||
                    target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl VAMPIRE_WEREWOLF_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                    target.hasSubType(MagicSubType.Werewolf) ||
                    target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Vampire) &&
                   !target.hasSubType(MagicSubType.Werewolf) &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl BARBARIAN_WARRIOR_BERSERKER_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasSubType(MagicSubType.Barbarian) ||
                    target.hasSubType(MagicSubType.Warrior) ||
                    target.hasSubType(MagicSubType.Berserker));
        }
    };

    public static final MagicPermanentFilterImpl NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl HUMAN = permanent(MagicSubType.Human, Control.Any);

    public static final MagicPermanentFilterImpl HUMAN_CREATURE = creature(MagicSubType.Human, Control.Any);

    public static final MagicPermanentFilterImpl NONENCHANTMENT_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasType(MagicType.Enchantment);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_ENCHANTED = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isEnchanted();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_ENCHANTED = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isEnchanted();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_EQUIPPED = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEquipped() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target;
        }
        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_ARTIFACT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target && target.isArtifact();
        }
        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target && target.isCreature();
        }
        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target && target.isLand();
        }
        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl EQUIPPED_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEquippedCreature() == target && target.isCreature();
        }
        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl EQUIPPED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEquipped() && target.isCreature() && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_ANOTHER_AURA = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            final int amount = source.isPermanent() && source.hasSubType(MagicSubType.Aura) ? 1 : 0;
            return target.isCreature() && target.getAuraPermanents().size() >= 1+amount;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTMENT_OR_ENCHANTED_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isEnchantment() || target.isEnchanted();
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.isEnchanted();
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_OR_ENCHANTMENT_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return (target.isCreature() && target.isEnchanted()) ||
                    (target.isCreature() && target.hasType(MagicType.Enchantment));
        }
    };

    public static final MagicPermanentFilterImpl ZOMBIE = permanent(MagicSubType.Zombie, Control.Any);

    public static final MagicPermanentFilterImpl FUNGUS = permanent(MagicSubType.Fungus, Control.Any);

    public static final MagicPermanentFilterImpl SLIVER = permanent(MagicSubType.Sliver, Control.Any);

    public static final MagicPermanentFilterImpl SLIVER_CREATURE = creature(MagicSubType.Sliver, Control.Any);

    public static final MagicPermanentFilterImpl ELF = permanent(MagicSubType.Elf, Control.Any);

    public static final MagicPermanentFilterImpl ELF_YOU_CONTROL = permanent(MagicSubType.Elf, Control.You);

    public static final MagicPermanentFilterImpl SPIRIT_YOU_CONTROL = permanent(MagicSubType.Spirit, Control.You);

    public static final MagicPermanentFilterImpl TREEFOLK_YOU_CONTROL = permanent(MagicSubType.Treefolk, Control.You);

    public static final MagicPermanentFilterImpl MODULAR_CREATURE_YOU_CONTROL = creature(MagicAbility.Modular, Control.You);

    public static final MagicPermanentFilterImpl LEVELUP_CREATURE_YOU_CONTROL = creature(MagicAbility.LevelUp, Control.You);

    public static final MagicPermanentFilterImpl CREATURE = permanent(MagicType.Creature, Control.Any);

    public static final MagicPermanentFilterImpl WORLD = permanent(MagicType.World, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_YOU_CONTROL = permanent(MagicType.Creature, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_YOU_OWN = permanent(MagicType.Creature, Own.You);

    public static final MagicPermanentFilterImpl CREATURE_YOUR_OPPONENT_CONTROLS = permanent(MagicType.Creature, Control.Opp);

    public static final MagicPermanentFilterImpl FACE_DOWN_CREATURE = creature(MagicPermanentState.FaceDown, Control.Any);

    public static final MagicPermanentFilterImpl FACE_DOWN_CREATURE_YOU_CONTROL = creature(MagicPermanentState.FaceDown, Control.You);

    public static final MagicPermanentFilterImpl FACE_DOWN_CREATURE_AN_OPPONENT_CONTROLS = creature(MagicPermanentState.FaceDown, Control.Opp);

    public static final MagicPermanentFilterImpl TAPPED_CREATURE = creature(MagicPermanentState.Tapped, Control.Any);

    public static final MagicPermanentFilterImpl TAPPED_CREATURE_YOU_CONTROL = creature(MagicPermanentState.Tapped, Control.You);

    public static final MagicPermanentFilterImpl TAPPED_CREATURE_AN_OPPONENT_CONTROLS = creature(MagicPermanentState.Tapped, Control.Opp);

    public static final MagicPermanentFilterImpl TAPPED_ARTIFACT_YOU_CONTROL = permanent(MagicPermanentState.Tapped, MagicType.Artifact, Control.You);

    public static final MagicPermanentFilterImpl TAPPED_LAND = permanent(MagicPermanentState.Tapped, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl TAPPED_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isTapped() &&
                   !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isUntapped();
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isUntapped() &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_ISLAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Island) &&
                   target.isUntapped() &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl TAPPED_ARTIFACT_CREATURE_AND_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return  target.isController(player) &&
                    target.isTapped() &&
                    (target.hasType(MagicType.Artifact) || target.hasType(MagicType.Creature) || target.hasType(MagicType.Land));
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_ARTIFACT_CREATURE_OR_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isUntapped() &&
                   target.isController(player) &&
                   (target.isArtifact() || target.isCreature() || target.isLand());
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_CREATURE_OR_BLACK_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.isArtifact() || target.hasColor(MagicColor.Black));
        }
    };

    public static final MagicPermanentFilterImpl NONWHITE_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl NONWHITE_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.White) && !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NONGREEN_CREATURE_YOU_CONTROL =new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Green) &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl NONWHITE_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NONBLUE_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicPermanentFilterImpl NONGREEN_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.Green);
        }
    };

    public static final MagicPermanentFilterImpl NONBLACK_ATTACKING_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Black) &&
                   target.isAttacking();
        }
    };

    public static final MagicPermanentFilterImpl NONRED_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasColor(MagicColor.Red);
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_CREATURE=new MagicPermanentFilterImpl () {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact();
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_ATTACKING_CREATURE=new MagicPermanentFilterImpl () {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact() &&
                   target.isAttacking();
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_NONBLACK_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact() &&
                   !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NONSNOW_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasType(MagicType.Snow);
        }
    };

    public static final MagicPermanentFilterImpl NONSNOW_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isLand() &&
                   !target.hasType(MagicType.Snow);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS =new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying) &&
                   target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_YOUR_OPPONENT_CONTROLS =new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS =new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasAbility(MagicAbility.Flying) &&
                   target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS =new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   !target.hasAbility(MagicAbility.Flying) &&
                   target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING = creature(MagicAbility.Flying, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_YOU_CONTROL = creature(MagicAbility.Flying, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_WITH_TRAMPLE_YOU_CONTROL = creature(MagicAbility.Trample, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS = creature(MagicAbility.Flying, Control.Opp);

    public static final MagicPermanentFilterImpl CREATURE_WITH_DEFENDER = creature(MagicAbility.Defender, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_DEFENDER_YOU_CONTROL = creature(MagicAbility.Defender, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_WITH_INFECT_YOU_CONTROL = creature(MagicAbility.Infect, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_WITH_HORSEMANSHIP = creature(MagicAbility.Horsemanship, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_ISLANDWALK = creature(MagicAbility.Islandwalk, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_SHADOW = creature(MagicAbility.Shadow, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_SHADOW = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Shadow);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_OR_REACH = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasAbility(MagicAbility.Flying) || target.hasAbility(MagicAbility.Reach));
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_MORPH_ABILITY = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasAbility(MagicAbility.Morph) || target.hasAbility(MagicAbility.Megamorph));
        }
    };


    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_OR_ISLANDWALK = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasAbility(MagicAbility.Flying) == false &&
                   target.hasAbility(MagicAbility.Islandwalk) == false;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_OR_PLANESWALKER = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return (target.isCreature() &&  target.hasAbility(MagicAbility.Flying) == false) | target.isPlaneswalker();
        }
    };

    public static final MagicPermanentFilterImpl BLUE_OR_BLACK_CREATURE_WITH_FLYING = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasAbility(MagicAbility.Flying) &&
                   (target.hasColor(MagicColor.Blue) || target.hasColor(MagicColor.Black));
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_BEEN_DAMAGED = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasState(MagicPermanentState.WasDealtDamage);
        }
    };

    public static final MagicCardFilterImpl CREATURE_WITH_DEATHTOUCH_HEXPROOF_REACH_OR_TRAMPLE_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
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
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            final MagicGame game = player.getGame();
            return target.isCreature() && target.isController(game.getDefendingPlayer());
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_DEFENDING_PLAYER_CONTROLS = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            final MagicGame game = player.getGame();
            return target.isCreature() &&
                   !target.hasAbility(MagicAbility.Flying) &&
                   target.isController(game.getDefendingPlayer());
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_CONVERTED_3_OR_LESS=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getConvertedCost() <= 3;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD = card(MagicType.Creature).cmcLEQ(3).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl CREATURE_CARD_POWER_LEQ_2_FROM_LIBRARY = card(MagicType.Creature).powerLEQ(2).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl GREEN_CREATURE_CARD_FROM_GRAVEYARD = card(MagicColor.Green).and(MagicType.Creature).from(MagicTargetType.Graveyard);

    public static final MagicPermanentFilterImpl CREATURE_CONVERTED_2_OR_LESS = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getConvertedCost() <= 2;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_PLUSONE_COUNTER = creature(MagicCounterType.PlusOne, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_PLUSONE_COUNTER_YOU_CONTROL = creature(MagicCounterType.PlusOne, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_LEVEL_COUNTER_YOU_CONTROL = creature(MagicCounterType.Level, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_AT_LEAST_3_LEVEL_COUNTERS = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.getCounters(MagicCounterType.Level) >= 3;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_MINSUONE_COUNTER = creature(MagicCounterType.MinusOne, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_COUNTER = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.hasCounters();
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE = creature(MagicPermanentState.Attacking, Control.Any);

    public static final MagicPermanentFilterImpl BLOCKING_CREATURE = creature(MagicPermanentState.Blocking, Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_YOU_CONTROL = creature(MagicPermanentState.Attacking, Control.You);

    public static final MagicPermanentFilterImpl BLOCKING_CREATURE_YOU_CONTROL = creature(MagicPermanentState.Blocking, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_BLOCKING_SN = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.getBlockedCreature() == source;
        }
    };

    public static final MagicPermanentFilterImpl NONATTACKING_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isAttacking();
        }
    };

    public static final MagicPermanentFilterImpl NONATTACKING_NONBLOCKING_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isAttacking() &&
                   !target.isBlocking();
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITH_FLANKING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasAbility(MagicAbility.Flanking);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITH_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasAbility(MagicAbility.Flying);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITH_SHADOW=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   target.hasAbility(MagicAbility.Shadow);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE_WITH_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   (target.isAttacking() || target.isBlocking()) &&
                   target.hasAbility(MagicAbility.Flying);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_WITHOUT_FLYING=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.isAttacking() &&
                   !target.hasAbility(MagicAbility.Flying);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_ELF_YOU_CONTROL= creatureAnd(MagicPermanentState.Attacking, MagicSubType.Elf, Control.You);

    public static final MagicPermanentFilterImpl ATTACKING_GOBLIN= creatureAnd(MagicPermanentState.Attacking, MagicSubType.Goblin, Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_HUMAN= creatureAnd(MagicPermanentState.Attacking, MagicSubType.Human, Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_AUROCHS= creatureAnd(MagicPermanentState.Attacking,MagicSubType.Aurochs,Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_KAVU = creatureAnd(MagicPermanentState.Attacking, MagicSubType.Kavu, Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_BEAST= creatureAnd(MagicPermanentState.Attacking,MagicSubType.Beast,Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE= creatureOr(MagicPermanentState.Attacking, MagicPermanentState.Blocking, Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_SPIRIT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasSubType(MagicSubType.Spirit) &&
                   (target.isAttacking() ||
                    target.isBlocking());
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL= creatureOr(MagicPermanentState.Attacking, MagicPermanentState.Blocking, Control.You);

    public static final MagicPermanentFilterImpl WEREWOLF_OR_WOLF_CREATURE_YOU_CONTROL= creatureOr(MagicSubType.Werewolf,MagicSubType.Wolf,Control.You);

    public static final MagicPermanentFilterImpl UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isAttacking() &&
                   target.hasState(MagicPermanentState.Blocked) == false;
        }
    };

    public static final MagicPermanentFilterImpl UNBLOCKED_CREATURE=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isAttacking() &&
                   target.hasState(MagicPermanentState.Blocked) == false;
        }
    };

    public static final MagicPermanentFilterImpl KALDRA_EQUIPMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isEquipment() &&
                    (target.isName("Sword of Kaldra") ||
                     target.isName("Shield of Kaldra") ||
                     target.isName("Helm of Kaldra"));
        }
    };

    public static final MagicPermanentFilterImpl KALDRA_EQUIPMENT_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return KALDRA_EQUIPMENT.accept(source, player, target) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl BLOCKED_CREATURE = creature(MagicPermanentState.Blocked, Control.Any);

    public static final MagicCardFilterImpl CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl COLORLESS_CREATURE_CARD_FROM_LIBRARY=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return MagicColor.isColorless(target) && target.isCreature();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

    public static final MagicCardFilterImpl CARD_FROM_LIBRARY=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

    public static final MagicCardFilterImpl CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                   targetType == MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD =
        card(MagicType.Artifact).or(MagicType.Creature).or(MagicType.Enchantment).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_GRAVEYARD = card(MagicType.Creature).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_LIBRARY = card(MagicType.Creature).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl PAYABLE_CREATURE_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && target.getCost().getCondition().accept(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl PAYABLE_INSTANT_OR_SORCERY_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return (target.hasType(MagicType.Instant) || target.hasType(MagicType.Sorcery)) && target.getCost().getCondition().accept(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = card(MagicType.Creature).and(MagicAbility.Infect).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl PERMANENT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.isPermanentCard();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return cardDefinition.getConvertedCost() <= 3 && cardDefinition.isPermanent();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl ARTIFACT_CARD_CMC_LEQ_1_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 1 && target.hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 2 && target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_POWER_LEQ_2_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.getPower() <= 2 && target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_CMC_LEQ_3 = new MagicCMCPermanentFilter(
        NONLAND_PERMANENT,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicCardFilterImpl CARD_FROM_OPPONENTS_GRAVEYARD =
        card().from(MagicTargetType.OpponentsGraveyard);

    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD =
        card(MagicType.Instant).or(MagicType.Sorcery).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl INSTANT_SORCERY_OR_CREATURE_CARD_FROM_GRAVEYARD =
        card(MagicType.Instant).or(MagicType.Sorcery).or(MagicType.Creature).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl RED_SORCERY_CARD_FROM_GRAVEYARD =
        card(MagicColor.Red).and(MagicType.Sorcery).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl BLUE_INSTANT_CARD_FROM_GRAVEYARD =
        card(MagicColor.Blue).and(MagicType.Instant).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl BLUE_OR_RED_CREATURE_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) &&
                   (target.hasColor(MagicColor.Blue) || target.hasColor(MagicColor.Red));
        }
    };

    public static final MagicCardFilterImpl BLACK_OR_RED_CREATURE_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) &&
                   (target.hasColor(MagicColor.Black) || target.hasColor(MagicColor.Red));
        }
    };

    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD =
        card(MagicType.Instant).or(MagicType.Sorcery).from(MagicTargetType.OpponentsGraveyard);

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD =
        card(MagicType.Creature).from(MagicTargetType.OpponentsGraveyard);

    public static final MagicCardFilterImpl ARTIFACT_CARD_FROM_GRAVEYARD =
        card(MagicType.Artifact).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl ARTIFACT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl NONCREATURE_ARTIFACT_CARD_WITH_CMC_LEQ_1_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 1 &&
                   target.hasType(MagicType.Artifact) &&
                   !target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl NONCREATURE_NONLAND_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return !target.hasType(MagicType.Land) &&
                   !target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl ENCHANTMENT_CARD_FROM_GRAVEYARD=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Enchantment);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl ENCHANTMENT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Enchantment);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl INSTANT_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Instant);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl SORCERY_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Sorcery);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Sorcery) || target.hasType(MagicType.Instant);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl LAND_CARD_FROM_YOUR_GRAVEYARD = card(MagicType.Land).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl LAND_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Land);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) ||
                   target.hasType(MagicType.Artifact);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl ZOMBIE_CARD_FROM_GRAVEYARD = card(MagicSubType.Zombie).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl ZOMBIE_CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicCardFilterImpl ZOMBIE_CREATURE_CARD_FROM_GRAVEYARD = card(MagicSubType.Zombie).and(MagicType.Creature).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl SPIRIT_CARD_FROM_GRAVEYARD = card(MagicSubType.Spirit).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl HUMAN_CREATURE_CARD_FROM_GRAVEYARD = card(MagicSubType.Human).and(MagicType.Creature).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl FUNGUS_CARD_FROM_ALL_GRAVEYARDS=new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Fungus);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return true;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicCardFilterImpl INSTANT_LEQ_CMC_2_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.getConvertedCost() <= 2 && target.hasType(MagicType.Instant);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_HAND = card(MagicType.Creature).from(MagicTargetType.Hand);

    public static final MagicCardFilterImpl BLUE_OR_RED_CREATURE_CARD_FROM_HAND = card(MagicColor.Blue).or(MagicColor.Red).and(MagicType.Creature).from(MagicTargetType.Hand);

    public static final MagicCardFilterImpl RED_OR_GREEN_CARD_FROM_HAND = card(MagicColor.Red).or(MagicColor.Green).from(MagicTargetType.Hand);

    public static final MagicPermanentFilterImpl MULTICOLORED_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent);
        }
    };

    public static final MagicPermanentFilterImpl COLORLESS_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return  target.isCreature() && MagicColor.isColorless(target);
        }
    };

    public static final MagicPermanentFilterImpl COLORLESS_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return  target.isCreature() && MagicColor.isColorless(target) && target.isController(player);
        }
    };

    public static final MagicStackFilterImpl COLORLESS_SPELL = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && MagicColor.isColorless(itemOnStack.getSource());
        }
    };

    public static final MagicCardFilterImpl MULTICOLORED_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return MagicColor.isMulti(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicStackFilterImpl MULTICOLORED_SPELL = new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
            return MagicColor.isMulti(itemOnStack.getSource()) && itemOnStack.isSpell();
        }
    };

    public static final MagicPermanentFilterImpl MONOCOLORED_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMono(permanent) && permanent.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl MONOCOLORED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMono(permanent) && permanent.isCreature() && permanent.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl MULTICOLORED_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent) && permanent.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl MULTICOLORED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent) && permanent.isCreature() && permanent.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl MULTICOLORED_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent) && permanent.isCreature();
        }
    };

    public static final MagicCardFilterImpl MULTICOLORED_CREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) && MagicColor.isMulti(target);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicCardFilterImpl NONCREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) == false;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isLand() &&
                   target.isUntapped() &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_MOUNTAIN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Mountain) &&
                   target.isUntapped() &&
                   target.isController(player);
        }
    };

    public static final MagicCardFilterImpl WARRIOR_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasSubType(MagicSubType.Warrior);
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_NONWHITE_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                   !target.isArtifact() &&
                   !target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_LAND = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() &&
                   target.isUntapped();
        }
    };

    public static final MagicPermanentFilterImpl FAERIE_OR_ELF = permanentOr(MagicSubType.Faerie, MagicSubType.Elf, Control.Any);

    public static final MagicPermanentFilterImpl KNIGHT_OR_SOLDIER = permanentOr(MagicSubType.Knight, MagicSubType.Soldier, Control.Any);

    public static final MagicPermanentFilterImpl ELF_OR_SOLDIER_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasSubType(MagicSubType.Elf) || target.hasSubType(MagicSubType.Soldier));
        }
    };

    public static final MagicPermanentFilterImpl ELDRAZI_SPAWN_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                   target.hasSubType(MagicSubType.Eldrazi) &&
                   target.hasSubType(MagicSubType.Spawn) &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl ELDRAZI_SPAWN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Eldrazi) &&
                   target.hasSubType(MagicSubType.Spawn) &&
                   target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl ELDRAZI_SCION = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Eldrazi) &&
                   target.hasSubType(MagicSubType.Scion);
        }
    };

    public static final MagicPermanentFilterImpl ELDRAZI_SCION_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Eldrazi) &&
                target.hasSubType(MagicSubType.Scion) &&
                target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_LAND = permanentAnd(MagicType.Artifact, MagicType.Land, Control.Any);

    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_OR_LAND_CARD_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Artifact) ||
                   target.hasType(MagicType.Creature) ||
                   target.hasType(MagicType.Land);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicPermanentFilterImpl LEGENDARY_LAND = permanentAnd(MagicType.Legendary, MagicType.Land, Control.Any);

    public static final MagicCardFilterImpl GREEN_CARD_FROM_HAND = card(MagicColor.Green).from(MagicTargetType.Hand);

    public static final MagicCardFilterImpl permanentCardMaxCMC(final MagicSubType subtype, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return target.isPermanentCard() &&
                       target.hasSubType(subtype) &&
                       target.getConvertedCost() <= cmc;
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicPermanentFilterImpl NONARTIFACT_PERMANENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isArtifact();
        }
    };

    public static final MagicPermanentFilterImpl NON_AURA_ENCHANTMENT=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return !target.isAura() && target.isEnchantment();
        }
    };

    public static final MagicCardFilterImpl permanentCardMaxCMC(final MagicType type, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return target.isPermanentCard() &&
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
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return target.isPermanentCard() &&
                       target.hasType(type) &&
                       target.getConvertedCost() >= cmc;
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_HAND = card(MagicType.Basic).and(MagicType.Land).from(MagicTargetType.Hand);

    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_LIBRARY = card(MagicType.Basic).and(MagicType.Land).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Land) && target.hasType(MagicType.Basic);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Graveyard ||
                   targetType==MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_YOUR_GRAVEYARD = card(MagicType.Basic).and(MagicType.Land).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl BASIC_LAND_CARD_OR_GATE_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean acceptType(MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return (target.hasType(MagicType.Basic) && target.hasType(MagicType.Land)) ||
                    target.hasSubType(MagicSubType.Gate);
        }
    };

    public static final MagicCardFilterImpl BASIC_FOREST_PLAINS_OR_ISLAND_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
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
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
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
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
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
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
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
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
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
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Plains) ||
                   target.hasSubType(MagicSubType.Island) ||
                   target.hasSubType(MagicSubType.Swamp)  ||
                   target.hasSubType(MagicSubType.Mountain);
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };

    public static final MagicCardFilterImpl INSTANT_OR_FLASH_CARD_FROM_LIBRARY = card(MagicType.Instant).or(MagicAbility.Flash).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
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

    public static final MagicCardFilterImpl NON_LEGENDARY_GREEN_CREATURE_CARD_WITH_CMC_LEQ_3_FROM_LIBRARY= new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Creature) &&
                target.hasType(MagicType.Legendary) == false &&
                target.hasColor(MagicColor.Green) &&
                target.getConvertedCost() <= 3;
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType==MagicTargetType.Library;
        }
    };

    public static final MagicPermanentFilterImpl UNPAIRED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   !target.isPaired();
        }
    };

    public static final MagicPermanentFilterImpl UNPAIRED_SOULBOND_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.hasAbility(MagicAbility.Soulbond) &&
                   !target.isPaired();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_TOUGHNESS_2_OR_LESS = MagicPTTargetFilter.Toughness(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl CREATURE_TOUGHNESS_3_OR_LESS = MagicPTTargetFilter.Toughness(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl CREATURE_TOUGHNESS_3_OR_GREATER = MagicPTTargetFilter.Toughness(
        CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl CREATURE_TOUGHNESS_4_OR_GREATER = MagicPTTargetFilter.Toughness(
        CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        4
    );

    public static final MagicPermanentFilterImpl CREATURE_TOUGHNESS_4_OR_GREATER_YOU_CONTROL = MagicPTTargetFilter.Toughness(
        CREATURE_YOU_CONTROL,
        Operator.GREATER_THAN_OR_EQUAL,
        4
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_1_OR_LESS = new MagicPTTargetFilter(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        1
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_OR_TOUGHNESS_1_OR_LESS = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                (target.getPower() <= 1 || target.getToughness() <= 1);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_POWER_2_OR_LESS = new MagicPTTargetFilter(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_2_OR_LESS_YOU_CONTROL = new MagicPTTargetFilter(
        CREATURE_YOU_CONTROL,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_2_OR_LESS_YOUR_OPPONENT_CONTROLS = new MagicPTTargetFilter(
        CREATURE_YOUR_OPPONENT_CONTROLS,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_3_OR_LESS = new MagicPTTargetFilter(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_POWER_3_OR_LESS = new MagicPTTargetFilter(
        ATTACKING_CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE_POWER_3_OR_LESS = new MagicPTTargetFilter(
        ATTACKING_OR_BLOCKING_CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE_POWER_2_OR_LESS = new MagicPTTargetFilter(
        ATTACKING_OR_BLOCKING_CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_4_OR_MORE = new MagicPTTargetFilter(
        CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        4
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_4_OR_MORE_YOU_CONTROL = new MagicPTTargetFilter(
        CREATURE_YOU_CONTROL,
        Operator.GREATER_THAN_OR_EQUAL,
        4
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_3_OR_MORE_YOU_CONTROL = new MagicPTTargetFilter(
        CREATURE_YOU_CONTROL,
        Operator.GREATER_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_2_OR_MORE = new MagicPTTargetFilter(
        CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl WHITE_CREATURE_POWER_2_OR_MORE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasColor(MagicColor.White) &&
                    target.isCreature() &&
                    target.getPower() >= 2;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_POWER_3_OR_MORE = new MagicPTTargetFilter(
        CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_5_OR_MORE = new MagicPTTargetFilter(
        CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        5
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_5_OR_MORE_YOU_CONTROL = new MagicPTTargetFilter(
        CREATURE_YOU_CONTROL,
        Operator.GREATER_THAN_OR_EQUAL,
        5
    );

    private static final Map<String, MagicTargetFilter<?>> single =
        new TreeMap<String, MagicTargetFilter<?>>(String.CASE_INSENSITIVE_ORDER);

    static {
        // used by MagicTargetChoice
        // <color|type|subtype> card from your graveyard
        single.put("card from your graveyard", CARD_FROM_GRAVEYARD);
        single.put("instant or sorcery card from your graveyard", INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD);
        single.put("instant, sorcery, or creature card from your graveyard", INSTANT_SORCERY_OR_CREATURE_CARD_FROM_GRAVEYARD);
        single.put("red sorcery card from your graveyard", RED_SORCERY_CARD_FROM_GRAVEYARD);
        single.put("blue instant card from your graveyard", BLUE_INSTANT_CARD_FROM_GRAVEYARD);
        single.put("blue or red creature card from your graveyard", BLUE_OR_RED_CREATURE_CARD_FROM_GRAVEYARD);
        single.put("artifact or enchantment card from your graveyard", card(MagicType.Artifact).or(MagicType.Enchantment).from(MagicTargetType.Graveyard));
        single.put("artifact, creature, or enchantment card from your graveyard", ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD);
        single.put("artifact card with converted mana cost 1 or less from your graveyard", ARTIFACT_CARD_CMC_LEQ_1_FROM_GRAVEYARD);
        single.put("creature or enchantment card from your graveyard", CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD);
        single.put("noncreature artifact card with converted mana cost 1 or less from your graveyard", NONCREATURE_ARTIFACT_CARD_WITH_CMC_LEQ_1_FROM_GRAVEYARD);
        single.put("Rebel permanent card with converted mana cost 5 or less from your graveyard", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Graveyard, 5));
        single.put("multicolored card from your graveyard", MULTICOLORED_CARD_FROM_GRAVEYARD);
        single.put("basic land card from your graveyard", BASIC_LAND_CARD_FROM_YOUR_GRAVEYARD);
        single.put("noncreature, nonland card from your graveyard", NONCREATURE_NONLAND_CARD_FROM_GRAVEYARD);

        // <color|type|subtype> permanent card from your graveyard
        single.put("permanent card from your graveyard", PERMANENT_CARD_FROM_GRAVEYARD);
        single.put("permanent card with converted mana cost 3 or less from your graveyard", PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD);

        // <color|type|subtype> creature card from your graveyard
        single.put("creature card with converted mana cost 3 or less from your graveyard", CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD);
        single.put("creature card with converted mana cost 2 or less from your graveyard", CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD);
        single.put("creature card with power 2 or less from your graveyard", CREATURE_CARD_POWER_LEQ_2_FROM_GRAVEYARD);
        single.put("creature card with infect from your graveyard", CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD);
        single.put("creature card with scavenge from your graveyard", PAYABLE_CREATURE_CARD_FROM_GRAVEYARD);

        // <color|type|subtype> card from an opponent's graveyard
        single.put("instant or sorcery card from an opponent's graveyard", INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD);
        single.put("card from an opponent's graveyard", CARD_FROM_OPPONENTS_GRAVEYARD);
        single.put("creature card in an opponent's graveyard", CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD);

        // <color|type|subtype> card from your hand
        single.put("card from your hand", CARD_FROM_HAND);
        single.put("instant card with converted mana cost 2 or less from your hand", INSTANT_LEQ_CMC_2_FROM_HAND);
        single.put("red or green card from your hand", RED_OR_GREEN_CARD_FROM_HAND);
        single.put("basic land card from your hand", BASIC_LAND_CARD_FROM_HAND);
        single.put("artifact, creature, or land card from your hand", ARTIFACT_OR_CREATURE_OR_LAND_CARD_FROM_HAND );

        // <color|type|subtype> permanent card from your hand

        // <color|type|subtype> creature card from your hand
        single.put("blue or red creature card from your hand", BLUE_OR_RED_CREATURE_CARD_FROM_HAND);
        single.put("multicolored creature card from your hand", MULTICOLORED_CREATURE_CARD_FROM_HAND);
        single.put("noncreature card from your hand", NONCREATURE_CARD_FROM_HAND);

        // <color|type|subtype> card from your library
        single.put("card from your library", CARD_FROM_LIBRARY);
        single.put("basic land card from your library", BASIC_LAND_CARD_FROM_LIBRARY);
        single.put("snow land card from your library", SNOW_LAND_CARD_FROM_LIBRARY);
        single.put("basic land card or a Gate card from your library", BASIC_LAND_CARD_OR_GATE_CARD_FROM_LIBRARY);
        single.put("Plains, Island, Swamp, Mountain or Forest card from your library", LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        single.put("Plains or Island card from your library", card(MagicSubType.Plains).or(MagicSubType.Island).from(MagicTargetType.Library));
        single.put("Plains or Swamp card from your library", card(MagicSubType.Plains).or(MagicSubType.Swamp).from(MagicTargetType.Library));
        single.put("Island or Swamp card from your library", card(MagicSubType.Island).or(MagicSubType.Swamp).from(MagicTargetType.Library));
        single.put("Island or Mountain card from your library", card(MagicSubType.Island).or(MagicSubType.Mountain).from(MagicTargetType.Library));
        single.put("Swamp or Mountain card from your library", card(MagicSubType.Swamp).or(MagicSubType.Mountain).from(MagicTargetType.Library));
        single.put("Swamp or Forest card from your library", card(MagicSubType.Swamp).or(MagicSubType.Forest).from(MagicTargetType.Library));
        single.put("Mountain or Forest card from your library", card(MagicSubType.Mountain).or(MagicSubType.Forest).from(MagicTargetType.Library));
        single.put("Mountain or Plains card from your library", card(MagicSubType.Mountain).or(MagicSubType.Plains).from(MagicTargetType.Library));
        single.put("Forest or Plains card from your library", card(MagicSubType.Forest).or(MagicSubType.Plains).from(MagicTargetType.Library));
        single.put("Forest or Island card from your library", card(MagicSubType.Forest).or(MagicSubType.Island).from(MagicTargetType.Library));
        single.put("Plains, Island, Swamp, or Mountain card from your library", PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY);
        single.put("land card with a basic land type from your library", LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        single.put("artifact or enchantment card from your library", card(MagicType.Artifact).or(MagicType.Enchantment).from(MagicTargetType.Library));
        single.put("instant or sorcery card from your library", card(MagicType.Instant).or(MagicType.Sorcery).from(MagicTargetType.Library));
        single.put("Treefolk or Forest card from your library", card(MagicSubType.Treefolk).or(MagicSubType.Forest).from(MagicTargetType.Library));
        single.put("instant card or a card with flash from your library", INSTANT_OR_FLASH_CARD_FROM_LIBRARY);
        single.put("basic Forest, Plains, or Island card from your library", BASIC_FOREST_PLAINS_OR_ISLAND_FROM_LIBRARY);
        single.put("basic Plains, Island, or Swamp card from your library", BASIC_PLAINS_ISLAND_OR_SWAMP_FROM_LIBRARY);
        single.put("basic Island, Swamp, or Mountain card from your library", BASIC_ISLAND_SWAMP_OR_MOUNTAIN_FROM_LIBRARY);
        single.put("basic Swamp, Mountain, or Forest card from your library", BASIC_SWAMP_MOUNTAIN_OR_FOREST_FROM_LIBRARY);
        single.put("basic Mountain, Forest, or Plains card from your library", BASIC_MOUNTAIN_FOREST_OR_PLAINS_FROM_LIBRARY);
        single.put("enchantment card with converted mana cost 3 or less from your library", permanentCardMaxCMC(MagicType.Enchantment, MagicTargetType.Library, 3));
        single.put("artifact card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicType.Artifact, MagicTargetType.Library, 1));
        single.put("artifact card with converted mana cost 6 or greater from your library", permanentCardMinCMC(MagicType.Artifact, MagicTargetType.Library, 6));
        single.put("blue instant card from your library", BLUE_INSTANT_CARD_FROM_LIBRARY);

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
        single.put("creature card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicType.Creature, MagicTargetType.Library, 1));
        single.put("creature card with converted mana cost 6 or greater from your library", permanentCardMinCMC(MagicType.Creature, MagicTargetType.Library, 6));
        single.put("creature card with power 2 or less from your library", CREATURE_CARD_POWER_LEQ_2_FROM_LIBRARY);
        single.put("colorless creature card from your library", COLORLESS_CREATURE_CARD_FROM_LIBRARY);
        single.put("creature card with deathtouch, hexproof, reach, or trample from your library", CREATURE_WITH_DEATHTOUCH_HEXPROOF_REACH_OR_TRAMPLE_FROM_LIBRARY);
        single.put("nonlegendary green creature card with converted mana cost 3 or less from your library", NON_LEGENDARY_GREEN_CREATURE_CARD_WITH_CMC_LEQ_3_FROM_LIBRARY);

        // <color|type|subtype> creature you control
        single.put("1/1 creature you control", ONE_ONE_CREATURE_YOU_CONTROL);
        single.put("black or red creature you control", BLACK_OR_RED_CREATURE_YOU_CONTROL);
        single.put("blue or black creature you control", BLUE_OR_BLACK_CREATURE_YOU_CONTROL);
        single.put("red or green creature you control", RED_OR_GREEN_CREATURE_YOU_CONTROL);
        single.put("red or white creature you control", RED_OR_WHITE_CREATURE_YOU_CONTROL);
        single.put("white or black creature you control", WHITE_OR_BLACK_CREATURE_YOU_CONTROL);
        single.put("white or blue creature you control", WHITE_OR_BLUE_CREATURE_YOU_CONTROL);
        single.put("untapped creature you control", UNTAPPED_CREATURE_YOU_CONTROL);
        single.put("untapped island you control", UNTAPPED_ISLAND_YOU_CONTROL);
        single.put("tapped creature you control", TAPPED_CREATURE_YOU_CONTROL);
        single.put("artifact or creature you control", ARTIFACT_OR_CREATURE_YOU_CONTROL);
        single.put("attacking or blocking creature you control", ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL);
        single.put("nonlegendary creature you control", NON_LEGENDARY_CREATURE_YOU_CONTROL);
        single.put("unblocked attacking creature you control", UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL);
        single.put("attacking creature you control", ATTACKING_CREATURE_YOU_CONTROL);
        single.put("blocking creature you control", BLOCKING_CREATURE_YOU_CONTROL);
        single.put("nontoken creature you control", NONTOKEN_CREATURE_YOU_CONTROL);
        single.put("creature with power 2 or less you control", CREATURE_POWER_2_OR_LESS_YOU_CONTROL);
        single.put("creature with power 3 or greater you control", CREATURE_POWER_3_OR_MORE_YOU_CONTROL);
        single.put("creature with power 4 or greater you control", CREATURE_POWER_4_OR_MORE_YOU_CONTROL);
        single.put("creature with power 5 or greater you control", CREATURE_POWER_5_OR_MORE_YOU_CONTROL);
        single.put("creature you control with power 2 or less", CREATURE_POWER_2_OR_LESS_YOU_CONTROL);
        single.put("creature you control with power 3 or greater", CREATURE_POWER_3_OR_MORE_YOU_CONTROL);
        single.put("creature you control with power 4 or greater", CREATURE_POWER_4_OR_MORE_YOU_CONTROL);
        single.put("creature you control with power 5 or greater", CREATURE_POWER_5_OR_MORE_YOU_CONTROL);
        single.put("creature you control with toughness 4 or greater", CREATURE_TOUGHNESS_4_OR_GREATER_YOU_CONTROL);
        single.put("creature with modular you control", MODULAR_CREATURE_YOU_CONTROL);
        single.put("creature you control with level up", LEVELUP_CREATURE_YOU_CONTROL);
        single.put("monocolored creature you control", MONOCOLORED_CREATURE_YOU_CONTROL);
        single.put("creature you control with a +1/+1 counter on it", CREATURE_PLUSONE_COUNTER_YOU_CONTROL);
        single.put("creature you control with a level counter on it", CREATURE_LEVEL_COUNTER_YOU_CONTROL);
        single.put("creature you control with flying", CREATURE_WITH_FLYING_YOU_CONTROL);
        single.put("creature you control with trample", CREATURE_WITH_TRAMPLE_YOU_CONTROL);
        single.put("enchanted creature you control", ENCHANTED_CREATURE_YOU_CONTROL);
        single.put("multicolored creature you control", MULTICOLORED_CREATURE_YOU_CONTROL);
        single.put("nongreen creature you control", NONGREEN_CREATURE_YOU_CONTROL);
        single.put("red creature or white creature you control", RED_OR_WHITE_CREATURE_YOU_CONTROL);
        single.put("green or white creature you control", GREEN_OR_WHITE_CREATURE_YOU_CONTROL);
        single.put("werewolf or wolf creature you control", WEREWOLF_OR_WOLF_CREATURE_YOU_CONTROL);
        single.put("Eldrazi Spawn creature you control", ELDRAZI_SPAWN_CREATURE_YOU_CONTROL);
        single.put("Eldrazi Spawn you control", ELDRAZI_SPAWN_YOU_CONTROL);
        single.put("Eldrazi Scion you control", ELDRAZI_SCION_YOU_CONTROL);
        single.put("face-down creature you control", FACE_DOWN_CREATURE_YOU_CONTROL);
        single.put("creature you control with defender", CREATURE_WITH_DEFENDER_YOU_CONTROL);
        single.put("creature with defender you control", CREATURE_WITH_DEFENDER_YOU_CONTROL);
        single.put("face-up nontoken creature you control", FACEUP_NONTOKEN_CREATURE_YOU_CONTROL);
        single.put("creature you control with infect", CREATURE_WITH_INFECT_YOU_CONTROL);
        single.put("colorless creature you control", COLORLESS_CREATURE_YOU_CONTROL);

        // <color|type|subtype> creature an opponent controls
        single.put("creature with flying an opponent controls", CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("creature with power 2 or less an opponent controls", CREATURE_POWER_2_OR_LESS_YOUR_OPPONENT_CONTROLS);
        single.put("attacking creature an opponent controls", ATTACKING_CREATURE_YOUR_OPPONENT_CONTROLS);
        single.put("attacking creature with flying an opponent controls", ATTACKING_CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("attacking creature without flying an opponent controls", ATTACKING_CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("creature without flying an opponent controls", CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS);
        single.put("red or green creature an opponent controls", RED_OR_GREEN_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("green or white creature an opponent controls", GREEN_OR_WHITE_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("creature an opponent controls", CREATURE_YOUR_OPPONENT_CONTROLS);
        single.put("nonlegendary creature an opponent controls", NON_LEGENDARY_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("nontoken creature an opponent controls", NONTOKEN_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("face-down creature an opponent controls", FACE_DOWN_CREATURE_AN_OPPONENT_CONTROLS);
        single.put("tapped creature an opponent controls", TAPPED_CREATURE_AN_OPPONENT_CONTROLS);

        // <color|type|subtype> creature
        single.put("1/1 creature", new MagicPTTargetFilter(CREATURE, Operator.EQUAL, 1, Operator.EQUAL, 1));
        single.put("nongreen creature", NONGREEN_CREATURE);
        single.put("nonblue creature", NONBLUE_CREATURE);
        single.put("nonblack creature", NONBLACK_CREATURE);
        single.put("nonblack attacking creature", NONBLACK_ATTACKING_CREATURE);
        single.put("nonwhite creature", NONWHITE_CREATURE);
        single.put("nonwhite creature with power 3 or greater", new MagicPTTargetFilter(NONWHITE_CREATURE, Operator.GREATER_THAN_OR_EQUAL, 3));
        single.put("nonwhite, nonblack creature", NONWHITE_NONBLACK_CREATURE);
        single.put("nonred creature", NONRED_CREATURE);
        single.put("nonartifact creature", NONARTIFACT_CREATURE);
        single.put("nonland creature", NONLAND_CREATURE);
        single.put("non-Vampire, non-Werewolf, non-Zombie creature", NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE);
        single.put("Skeleton, Vampire, or Zombie", SKELETON_VAMPIRE_OR_ZOMBIE);
        single.put("noncreature", NONCREATURE);
        single.put("nonartifact, nonblack creature", NONARTIFACT_NONBLACK_CREATURE);
        single.put("nonartifact, nonwhite creature", NONARTIFACT_NONWHITE_CREATURE);
        single.put("artifact creature or black creature", ARTIFACT_CREATURE_OR_BLACK_CREATURE);
        single.put("nonartifact attacking creature", NONARTIFACT_ATTACKING_CREATURE);
        single.put("land or nonblack creature", LAND_OR_NONBLACK_CREATURE);
        single.put("red or green creature",RED_OR_GREEN_CREATURE);
        single.put("red or white creature",RED_OR_WHITE_CREATURE);
        single.put("tapped creature", TAPPED_CREATURE);
        single.put("untapped creature", UNTAPPED_CREATURE);
        single.put("face-down creature", FACE_DOWN_CREATURE);
        single.put("artifact or creature", ARTIFACT_OR_CREATURE);
        single.put("unpaired Soulbond creature", UNPAIRED_SOULBOND_CREATURE);
        single.put("monocolored creature", MONOCOLORED_CREATURE);
        single.put("attacking Elf you control", ATTACKING_ELF_YOU_CONTROL);
        single.put("attacking Aurochs", ATTACKING_AUROCHS);
        single.put("attacking Goblin", ATTACKING_GOBLIN);
        single.put("attacking Kavu", ATTACKING_KAVU);
        single.put("attacking Beast", ATTACKING_BEAST);
        single.put("attacking creature", ATTACKING_CREATURE);
        single.put("attacking creature with shadow", ATTACKING_CREATURE_WITH_SHADOW);
        single.put("attacking creature with power 3 or less", ATTACKING_CREATURE_POWER_3_OR_LESS);
        single.put("nonattacking creature", NONATTACKING_CREATURE);
        single.put("attacking or blocking creature", ATTACKING_OR_BLOCKING_CREATURE);
        single.put("attacking creature or blocking creature", ATTACKING_OR_BLOCKING_CREATURE);
        single.put("attacking or blocking creature with flying", ATTACKING_OR_BLOCKING_CREATURE_WITH_FLYING);
        single.put("attacking or blocking creature with power 3 or less", ATTACKING_OR_BLOCKING_CREATURE_POWER_3_OR_LESS);
        single.put("attacking or blocking creature with power 2 or less", ATTACKING_OR_BLOCKING_CREATURE_POWER_2_OR_LESS);
        single.put("blocked creature", BLOCKED_CREATURE);
        single.put("blocking creature", BLOCKING_CREATURE);
        single.put("blue or red creature", BLUE_OR_RED_CREATURE);
        single.put("black or green creature", BLACK_OR_GREEN_CREATURE);
        single.put("black or red creature", BLACK_OR_RED_CREATURE);
        single.put("green or white creature", GREEN_OR_WHITE_CREATURE);
        single.put("green or blue creature", GREEN_OR_BLUE_CREATURE);
        single.put("green creature or white creature", GREEN_OR_WHITE_CREATURE);
        single.put("white or blue creature", WHITE_OR_BLUE_CREATURE);
        single.put("white or black creature", WHITE_OR_BLACK_CREATURE);
        single.put("white creature with power 2 or greater", WHITE_CREATURE_POWER_2_OR_MORE);
        single.put("creature with converted mana cost 3 or less", CREATURE_CONVERTED_3_OR_LESS);
        single.put("creature with converted mana cost 2 or less", CREATURE_CONVERTED_2_OR_LESS);
        single.put("creature with flying", CREATURE_WITH_FLYING);
        single.put("creature with flying or reach", CREATURE_WITH_FLYING_OR_REACH);
        single.put("blue or black creature with flying", BLUE_OR_BLACK_CREATURE_WITH_FLYING);
        single.put("creature without flying", CREATURE_WITHOUT_FLYING);
        single.put("creature with defender",  CREATURE_WITH_DEFENDER);
        single.put("creature with a morph ability", CREATURE_WITH_MORPH_ABILITY);
        single.put("creature with horsemanship", CREATURE_WITH_HORSEMANSHIP);
        single.put("creature with islandwalk", CREATURE_WITH_ISLANDWALK);
        single.put("creature with power 1 or less", CREATURE_POWER_1_OR_LESS);
        single.put("creature with power or toughness 1 or less", CREATURE_POWER_OR_TOUGHNESS_1_OR_LESS);
        single.put("creature with power 2 or less", CREATURE_POWER_2_OR_LESS);
        single.put("creature with power 3 or less", CREATURE_POWER_3_OR_LESS);
        single.put("creature with power 2 or greater", CREATURE_POWER_2_OR_MORE);
        single.put("creature with power 3 or greater", CREATURE_POWER_3_OR_MORE);
        single.put("creature with power 4 or greater", CREATURE_POWER_4_OR_MORE);
        single.put("creature with power 5 or greater", CREATURE_POWER_5_OR_MORE);
        single.put("creature with toughness 2 or less", CREATURE_TOUGHNESS_2_OR_LESS);
        single.put("creature with toughness 3 or less", CREATURE_TOUGHNESS_3_OR_LESS);
        single.put("creature with toughness 3 or greater", CREATURE_TOUGHNESS_3_OR_GREATER);
        single.put("creature with toughness 4 or greater", CREATURE_TOUGHNESS_4_OR_GREATER);
        single.put("creature with shadow", CREATURE_WITH_SHADOW);
        single.put("creature with a +1/+1 counter on it", CREATURE_PLUSONE_COUNTER);
        single.put("creature with a -1/-1 counter on it", CREATURE_MINSUONE_COUNTER);
        single.put("creature with a counter on it", CREATURE_WITH_COUNTER);
        single.put("creature with another Aura attached to it", CREATURE_WITH_ANOTHER_AURA);
        single.put("creature that isn't enchanted", CREATURE_THAT_ISNT_ENCHANTED);
        single.put("attacking creature with flying", ATTACKING_CREATURE_WITH_FLYING);
        single.put("attacking creature with flanking", ATTACKING_CREATURE_WITH_FLANKING);
        single.put("attacking creature without flying", ATTACKING_CREATURE_WITHOUT_FLYING);
        single.put("nontoken creature", NONTOKEN_CREATURE);
        single.put("Djinn or Efreet", DJINN_OR_EFREET);
        single.put("Faerie or Elf", FAERIE_OR_ELF);
        single.put("Knight or Soldier", KNIGHT_OR_SOLDIER);
        single.put("Elf or Soldier creature", ELF_OR_SOLDIER_CREATURE);
        single.put("tapped nonblack creature", TAPPED_NONBLACK_CREATURE);
        single.put("nonattacking, nonblocking creature", NONATTACKING_NONBLOCKING_CREATURE);
        single.put("creature defending player controls", CREATURE_DEFENDING_PLAYER_CONTROLS);
        single.put("creature without flying defending player controls", CREATURE_WITHOUT_FLYING_DEFENDING_PLAYER_CONTROLS);
        single.put("creature without flying or islandwalk", CREATURE_WITHOUT_FLYING_OR_ISLANDWALK);
        single.put("creature without flying or a planeswalker", CREATURE_WITHOUT_FLYING_OR_PLANESWALKER);
        single.put("creature token", CREATURE_TOKEN);
        single.put("serf token", SERF_TOKEN);
        single.put("nonsnow creature", NONSNOW_CREATURE);
        single.put("creature that is enchanted", CREATURE_ENCHANTED);
        single.put("creature that is equipped", CREATURE_EQUIPPED);
        single.put("nonenchantment creature", NONENCHANTMENT_CREATURE);
        single.put("creature that's a Barbarian, a Warrior, or a Berserker", BARBARIAN_WARRIOR_BERSERKER_CREATURE);
        single.put("multicolored creature", MULTICOLORED_CREATURE);
        single.put("unblocked creature", UNBLOCKED_CREATURE);
        single.put("Cleric or Wizard creature", CLERIC_OR_WIZARD_CREATURE);
        single.put("creature that was dealt damage this turn", CREATURE_BEEN_DAMAGED);
        single.put("enchanted creature", ENCHANTED_CREATURE);
        single.put("enchanted land", ENCHANTED_LAND);
        single.put("enchanted permanent", ENCHANTED_PERMANENT);
        single.put("enchanted artifact", ENCHANTED_ARTIFACT);
        single.put("equipped creature", EQUIPPED_CREATURE);
        single.put("nonlegendary creature", NON_LEGENDARY_CREATURE);
        single.put("colorless creature", COLORLESS_CREATURE);
        single.put("Eldrazi Scion", ELDRAZI_SCION);

        // <color|type|subtype> you control
        single.put("basic land you control", BASIC_LAND_YOU_CONTROL);
        single.put("snow land you control", SNOW_LAND_YOU_CONTROL);
        single.put("non-lair land you control", NON_LAIR_LAND_YOU_CONTROL);
        single.put("nonsnow land you control", NONSNOW_LAND_YOU_CONTROL);
        single.put("nonbasic land you control", NONBASIC_LAND_YOU_CONTROL);
        single.put("land with a trap counter on it you control", TRAPPED_LAND_YOU_CONTROL);
        single.put("Forest or Plains you control", FOREST_OR_PLAINS_YOU_CONTROL);
        single.put("artifact, creature, or land you control",ARTIFACT_OR_CREATURE_OR_LAND_YOU_CONTROL);
        single.put("creature or enchantment you control", CREATURE_OR_ENCHANTMENT_YOU_CONTROL);
        single.put("creature or land you control", CREATURE_OR_LAND_YOU_CONTROL);
        single.put("creature token you control", CREATURE_TOKEN_YOU_CONTROL);
        single.put("Caribou token you control", CARIBOU_TOKEN_YOU_CONTROL);
        single.put("permanent you control", PERMANENT_YOU_CONTROL);
        single.put("permanent with fading you control", PERMANENT_FADING_YOU_CONTROL);
        single.put("multicolored permanent you control", MULTICOLORED_PERMANENT_YOU_CONTROL);
        single.put("green or white permanent you control", GREEN_OR_WHITE_PERMANENT_YOU_CONTROL);
        single.put("nonland permanent you control", NONLAND_PERMANENT_YOU_CONTROL);
        single.put("nontoken permanent you control", NONTOKEN_PERMANENT_YOU_CONTROL);
        single.put("nontoken artifact you control", NONTOKEN_ARTIFACT_YOU_CONTROL);
        single.put("soldier or warrior you control", SOLDIER_OR_WARRIOR_YOU_CONTROL);
        single.put("forest or treefolk you control", FOREST_OR_TREEFOLK_YOU_CONTROL);
        single.put("snow Mountain you control", SNOW_MOUNTAIN_YOU_CONTROL);
        single.put("snow Swamp you control", SNOW_SWAMP_YOU_CONTROL);
        single.put("snow Island you control", SNOW_ISLAND_YOU_CONTROL);
        single.put("snow Plains you control", SNOW_PLAINS_YOU_CONTROL);
        single.put("snow Forest you control", SNOW_FOREST_YOU_CONTROL);
        single.put("legendary snake you control", LEGENDARY_SNAKE_YOU_CONTROL);
        single.put("untapped land you control", UNTAPPED_LAND_YOU_CONTROL);
        single.put("red or green enchantment you control", RED_OR_GREEN_ENCHANTMENT_YOU_CONTROL);
        single.put("untapped mountain you control", UNTAPPED_MOUNTAIN_YOU_CONTROL);
        single.put("tapped artifact, creature, or land you control", TAPPED_ARTIFACT_CREATURE_AND_LAND_YOU_CONTROL);
        single.put("untapped artifact, creature, or land you control", UNTAPPED_ARTIFACT_CREATURE_OR_LAND_YOU_CONTROL);
        single.put("equipped creature you control", EQUIPPED_CREATURE_YOU_CONTROL);


        // <color|type|subtype> an opponent controls
        single.put("permanent an opponent controls", PERMANENT_AN_OPPONENT_CONTROLS);
        single.put("untapped permanent an opponent controls", UNTAPPED_PERMANENT_AN_OPPONENT_CONTROLS);
        single.put("artifact or enchantment an opponent controls", ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS);
        single.put("nonland permanent an opponent controls", NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS);
        single.put("Island or Swamp an opponent controls", ISLAND_OR_SWAMP_AN_OPPONENT_CONTROLS);
        single.put("nonbasic land an opponent controls", NONBASIC_LAND_AN_OPPONENT_CONTROLS);
        single.put("white or blue permanent an opponent controls", WHITE_OR_BLUE_PERMANENT_AN_OPPONENT_CONTROLS);

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
        single.put("nontoken permanent", NONTOKEN_PERMANENT);
        single.put("nontoken red permanent", NONTOKEN_RED_PERMANENT);
        single.put("nontoken white permanent", NONTOKEN_WHITE_PERMANENT);
        single.put("nonland permanent with converted mana cost 3 or less", NONLAND_PERMANENT_CMC_LEQ_3);
        single.put("black or red permanent", BLACK_OR_RED_PERMANENT);
        single.put("black or green permanent", BLACK_OR_GREEN_PERMANENT);
        single.put("blue or red permanent", BLUE_OR_RED_PERMANENT);
        single.put("green or blue permanent", GREEN_OR_BLUE_PERMANENT);
        single.put("white or black permanent", WHITE_OR_BLACK_PERMANENT);
        single.put("white or blue permanent", WHITE_OR_BLUE_PERMANENT);
        single.put("red or white permanent", RED_OR_WHITE_PERMANENT);
        single.put("multicolored permanent", MULTICOLORED_PERMANENT);
        single.put("nonwhite permanent", NONWHITE_PERMANENT);
        single.put("permanent that is enchanted", PERMANENT_ENCHANTED);
        single.put("enchantment or enchanted permanent", ENCHANTMENT_OR_ENCHANTED_PERMANENT);
        single.put("nonartifact permanent", NONARTIFACT_PERMANENT);
        single.put("Goblin or Shaman", GOBLIN_OR_SHAMAN);
        single.put("Treefolk or Warrior", TREEFOLK_OR_WARRIOR);

        // <color|type|subtype>
        single.put("creature you own", CREATURE_YOU_OWN);
        single.put("permanent you own or control", PERMANENT_YOU_OWN_OR_CONTROL);
        single.put("Insect, Rat, Spider, or Squirrel", INSECT_RAT_SPIDER_OR_SQUIRREL);
        single.put("Vampire, Werewolf, or Zombie", VAMPIRE_WEREWOLF_OR_ZOMBIE);
        single.put("attacking or blocking Spirit",  ATTACKING_OR_BLOCKING_SPIRIT);
        single.put("basic land", BASIC_LAND);
        single.put("nonbasic land", NONBASIC_LAND);
        single.put("non-Swamp land", NON_SWAMP_LAND);
        single.put("snow land", SNOW_LAND);
        single.put("Forest or Plains", FOREST_OR_PLAINS);
        single.put("Plains or Island", PLAINS_OR_ISLAND);
        single.put("artifact or land", ARTIFACT_OR_LAND);
        single.put("artifact land", ARTIFACT_LAND);
        single.put("artifact or enchantment", ARTIFACT_OR_ENCHANTMENT);
        single.put("artifact or enchantment with converted mana cost 3 or less", ARTIFACT_OR_ENCHANTMENT_CMC_3_OR_LESS);
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
        single.put("tapped basic land", TAPPED_BASIC_LAND);
        single.put("untapped land", UNTAPPED_LAND);
        single.put("non-Aura enchantment", NON_AURA_ENCHANTMENT);
        single.put("attacking Human", ATTACKING_HUMAN);
        single.put("Aura attached to a creature", AURA_ATTACHED_TO_CREATURE);

        // <color|type> spell
        single.put("spell", SPELL);
        single.put("spell an opponent controls", SPELL_YOU_DONT_CONTROL);
        single.put("spell or ability", SPELL_OR_ABILITY);
        single.put("spell or ability an opponent controls", SPELL_OR_ABILITY_OPPONENT_CONTROL);
        single.put("activated ability", ACTIVATED_ABILITY);
        single.put("activated or triggered ability", ACTIVATED_OR_TRIGGERED_ABILITY);
        single.put("spell, activated ability, or triggered ability", SPELL_OR_ABILITY);
        single.put("spell that targets a player", SPELL_THAT_TARGETS_PLAYER);
        single.put("spell with {X} in its mana cost", SPELL_WITH_X_COST);
        single.put("noncreature spell", NONCREATURE_SPELL);
        single.put("nonartifact spell", NONARTIFACT_SPELL);
        single.put("artifact or enchantment spell", ARTIFACT_OR_ENCHANTMENT_SPELL);
        single.put("red or green spell", RED_OR_GREEN_SPELL);
        single.put("blue or black spell", BLUE_OR_BLACK_SPELL);
        single.put("green or white spell", GREEN_OR_WHITE_SPELL);
        single.put("blue, black, or red spell", BLUE_OR_BLACK_OR_RED_SPELL);
        single.put("white, blue, black, or red spell", WHITE_OR_BLUE_OR_BLACK_OR_RED_SPELL);
        single.put("nonblue spell", NONBLUE_SPELL);
        single.put("nonblack spell", NONBLACK_SPELL);
        single.put("non-Faerie spell", NONFAERIE_SPELL);
        single.put("blue spell during your turn", BLUE_SPELL_YOUR_TURN);
        single.put("blue or black spell during your turn", BLUE_OR_BLACK_SPELL_YOUR_TURN);
        single.put("blue instant spell", BLUE_INSTANT_SPELL);
        single.put("nonred spell", NONRED_SPELL);
        single.put("instant or sorcery spell", INSTANT_OR_SORCERY_SPELL);
        single.put("white or blue instant or sorcery spell", WHITE_OR_BLUE_INSTANT_OR_SORCERY_SPELL);
        single.put("instant or sorcery spell you control", INSTANT_OR_SORCERY_SPELL_YOU_CONTROL);
        single.put("spell with converted mana cost 1", SPELL_WITH_CMC_EQ_1);
        single.put("spell with converted mana cost 2", SPELL_WITH_CMC_EQ_2);
        single.put("spell with converted mana cost 3 or less", SPELL_WITH_CMC_LEQ_3);
        single.put("spell with converted mana cost 4 or greater", SPELL_WITH_CMC_4_OR_GREATER);
        single.put("instant spell you control with converted mana cost 2 or less", INSTANT_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2);
        single.put("sorcery spell you control with converted mana cost 2 or less", SORCERY_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2);
        single.put("creature spell with converted mana cost 6 or greater", CREATURE_SPELL_CMC_6_OR_MORE);
        single.put("creature spell with infect", CREATURE_SPELL_WITH_INFECT);
        single.put("creature or Aura spell", CREATURE_OR_AURA_SPELL);
        single.put("creature or sorcery spell", CREATURE_OR_SORCERY_SPELL);
        single.put("Spirit or Arcane spell", SPIRIT_OR_ARCANE_SPELL);
        single.put("multicolored spell", MULTICOLORED_SPELL);
        single.put("colorless spell", COLORLESS_SPELL);

        // player
        single.put("opponent", OPPONENT);
        single.put("your opponents", OPPONENT);
        single.put("other player", OPPONENT);
        single.put("player", PLAYER);
        single.put("player who lost life this turn", PLAYER_LOST_LIFE);
        single.put("player that controls a creature", PLAYER_CONTROLS_CREATURE);
        single.put("defending player", DEFENDING_PLAYER);

        // from a graveyard
        single.put("card from a graveyard", CARD_FROM_ALL_GRAVEYARDS);
        single.put("artifact card from a graveyard", ARTIFACT_CARD_FROM_ALL_GRAVEYARDS);
        single.put("enchantment card from a graveyard", ENCHANTMENT_CARD_FROM_ALL_GRAVEYARDS);
        single.put("instant card from a graveyard", INSTANT_CARD_FROM_ALL_GRAVEYARDS);
        single.put("sorcery card from a graveyard", SORCERY_CARD_FROM_ALL_GRAVEYARDS);
        single.put("instant or sorcery card from a graveyard", INSTANT_OR_SORCERY_CARD_FROM_ALL_GRAVEYARDS);
        single.put("artifact or creature card from a graveyard", ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("creature card from a graveyard", CREATURE_CARD_FROM_ALL_GRAVEYARDS);
        single.put("land card from a graveyard", LAND_CARD_FROM_ALL_GRAVEYARDS);
        single.put("Fungus card from a graveyard", FUNGUS_CARD_FROM_ALL_GRAVEYARDS);

        // using source
        single.put("you", YOU);
        single.put("SN", SN);
        single.put("it", SN);
        single.put("this permanent", SN);
        single.put("this creature", SN);
        single.put("creature blocking it", CREATURE_BLOCKING_SN);
    }

    public static String toSingular(final String arg) {
        return arg.toLowerCase()
            .replaceAll("`", "QUOTE")
            .replaceAll("\\bcards\\b", "card")
            .replaceAll("\\bpermanents\\b", "permanent")
            .replaceAll("\\bcreatures\\b", "creature")
            .replaceAll("\\bartifacts\\b", "artifact")
            .replaceAll("\\benchantments\\b", "enchantment")
            .replaceAll("\\bauras\\b", "aura")
            .replaceAll("\\blands\\b", "land")
            .replaceAll("\\bforests\\b", "forest")
            .replaceAll("\\bislands\\b", "island")
            .replaceAll("\\bmountains\\b", "mountain")
            .replaceAll("\\bswamps\\b", "swamp")
            .replaceAll("\\bgates\\b", "gate")
            .replaceAll("\\bgoblins\\b", "goblin")
            .replaceAll("\\brats\\b", "rat")
            .replaceAll("\\bslivers\\b", "sliver")
            .replaceAll("\\bfaeries\\b", "faerie")
            .replaceAll("\\bzombies\\b", "zombie")
            .replaceAll("\\bwerewolves\\b", "werewolf")
            .replaceAll("\\btokens\\b", "token")
            .replaceAll("\\bhumans\\b", "human")
            .replaceAll("\\bspirits\\b", "spirit")
            .replaceAll("\\belves\\b", "elf")
            .replaceAll("\\ballies\\b", "ally")
            .replaceAll("\\bclerics\\b", "cleric")
            .replaceAll("\\bdruids\\b", "druid")
            .replaceAll("\\billusions\\b", "illusion")
            .replaceAll("\\bsoldiers\\b", "soldier")
            .replaceAll("\\bthrulls\\b", "thrull")
            .replaceAll("\\bwarriors\\b", "warrior")
            .replaceAll("\\bwizards\\b", "wizard")
            .replaceAll("\\bvampires\\b", "vampire")
            .replaceAll("\\bmercenaries\\b", "mercenary")
            .replaceAll("\\bbirds\\b", "bird")
            .replaceAll("\\brogues\\b", "rogue")
            .replaceAll("\\bbeasts\\b", "beast")
            .replaceAll("\\bdemons\\b", "demon")
            .replaceAll("\\bdragons\\b", "dragon")
            .replaceAll("\\bsaprolings\\b", "saproling")
            .replaceAll("\\bnightmares\\b", "nightmare")
            .replaceAll("\\bwalls\\b", "wall")
            .replaceAll("\\bshrines\\b", "shrine")
            .replaceAll("\\bscions\\b", "scion")
            .replaceAll("\\bsnakes\\b", "snake")
            .replaceAll("\\bgorgons\\b", "gorgon")
            .replaceAll("\\band\\b", "or")
            .replaceAll("\\bthem\\b", "it")
            .replaceAll("\\bin your hand\\b", "from your hand")
            .replaceAll("\\bin your graveyard\\b", "from your graveyard")
            .replaceAll("\\bin all graveyards\\b", "from a graveyard")
            .replaceAll("\\byour opponents control\\b", "an opponent controls")
            .replaceAll("\\byour opponents' graveyards\\b", "an opponent's graveyard")
            .replaceAll("QUOTE", "")
            .replaceAll(" on the battlefield\\b", "")
            .replaceAll("^all ", "")
            .replaceAll("^each ", "")
            .replaceAll(" each ", " ");
    }

    private static final Pattern OTHER = Pattern.compile("^(an)?other ", Pattern.CASE_INSENSITIVE);

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicTarget> Target(final String text) {
        final String arg = toSingular(text);
        final Matcher matcher = OTHER.matcher(arg);
        final boolean other = matcher.find();
        final String processed = matcher.replaceFirst("");
        final MagicTargetFilter<MagicTarget> filter = (MagicTargetFilter<MagicTarget>)single(processed);
        return other ? new MagicOtherTargetFilter(filter) : filter;
    }

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicPermanent> Permanent(final String text) {
        final String arg = toSingular(text);
        final Matcher matcher = OTHER.matcher(arg);
        final boolean other = matcher.find();
        final String processed = matcher.replaceFirst("");
        final MagicTargetFilter<MagicPermanent> filter = (MagicTargetFilter<MagicPermanent>)single(processed);
        return other ? new MagicOtherPermanentTargetFilter(filter) : filter;
    }

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicCard> Card(final String text) {
        final String arg = toSingular(text);
        return (MagicTargetFilter<MagicCard>)single(arg);
    }

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicPlayer> Player(final String text) {
        final String arg = toSingular(text);
        return (MagicTargetFilter<MagicPlayer>)single(arg);
    }

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicItemOnStack> ItemOnStack(final String text) {
        final String arg = toSingular(text);
        return (MagicTargetFilter<MagicItemOnStack>)single(arg);
    }

    public static MagicTargetFilter<?> single(final String arg) {
        final String filter = arg
            .replaceFirst("^(a|an) ", "")
            .replaceFirst(" to sacrifice$", " you control");
        if (single.containsKey(filter)) {
            assert single.get(filter) != null : "return null for " + filter;
            return single.get(filter);
        } else {
            return MagicTargetFilterParser.build(filter);
        }
    }

    public static MagicTargetFilter<MagicCard> matchCardPrefix(final String arg, final String prefix, final MagicTargetType location) {
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return card(c).from(location);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return card(t).from(location);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return card(st).from(location);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    public static MagicTargetFilter<MagicCard> matchPermanentCardPrefix(final String arg, final String prefix, final MagicTargetType location) {
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return card(c).permanent().from(location);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return card(t).permanent().from(location);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return card(st).permanent().from(location);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    public static MagicTargetFilter<MagicCard> matchCreatureCardPrefix(final String arg, final String prefix, final MagicTargetType location) {
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return card(c).and(MagicType.Creature).from(location);
            }
        }
        for (final MagicType t : MagicType.values()) {
            if (prefix.equalsIgnoreCase(t.toString())) {
                return card(t).and(MagicType.Creature).from(location);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return card(st).and(MagicType.Creature).from(location);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }
    public static MagicTargetFilter<MagicItemOnStack> matchSpellPrefix(final String arg, final String prefix) {
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

    public static MagicTargetFilter<MagicPermanent> matchPermanentPrefix(final String arg, final String prefix, final Control control) {
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

    public static MagicTargetFilter<MagicPermanent> matchCreaturePrefix(final String arg, final String prefix, final Control control) {
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
            if (prefix.equalsIgnoreCase("non-" + st.toString())) {
                return creatureNon(st, control);
            }
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    public enum Control {
        Any,
        You,
        Opp
    }

    enum Own {
        Any,
        You,
        Opp
    }

    public static final MagicPermanentFilterImpl untapped(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isUntapped() && filter.accept(source, player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl tapped(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isTapped() && filter.accept(source, player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) &&
                   ((control == Control.You && target.isController(player)) ||
                    (control == Control.Opp && target.isOpponent(player)) ||
                    (control == Control.Any)
                    );
            }
        };
    }

    public static final MagicPermanentFilterImpl nonTokenPermanentName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) && !target.isToken() &&
                   ((control == Control.You && target.isController(player)) ||
                    (control == Control.Opp && target.isOpponent(player)) ||
                    (control == Control.Any)
                    );
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentNotName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) == false &&
                    ((control == Control.You && target.isController(player)) ||
                     (control == Control.Opp && target.isOpponent(player)) ||
                     (control == Control.Any)
                    );
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) &&
                       target.isCreature() &&
                   ((control == Control.You && target.isController(player)) ||
                    (control == Control.Opp && target.isOpponent(player)) ||
                    (control == Control.Any)
                    );
            }
        };
    }

    public static final MagicPermanentFilterImpl permanent(final MagicType type, final Control control) {
        return permanentOr(type, type, control);
    }

    public static final MagicPermanentFilterImpl permanent(final MagicType type, final Own own) {
        return permanentOr(type, type, own);
    }

    public static final MagicPermanentFilterImpl permanent(final MagicPermanentState state, final MagicType type, final Control control) {;
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.hasType(type) &&
                       target.hasState(state) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }

    private static MagicPermanentFilterImpl permanentOr(final MagicType type1, final MagicType type2, final Own own) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.hasType(type1) && target.hasType(type2) &&
                       ((own == Own.You && target.isOwner(player)) ||
                        (own == Own.Opp && target.isOwner(player.getOpponent())) ||
                        (own == Own.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentAnd(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.hasType(type1) && target.hasType(type2) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentAnd(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.hasType(type) && target.hasSubType(subType) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasType(type1) || target.hasType(type2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicSubType subType1, final MagicSubType subType2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasSubType(subType1) || target.hasSubType(subType2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return (target.hasType(type) || target.hasSubType(subType)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
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
    public static final MagicPermanentFilterImpl creature(final MagicCounterType counter, final Control control) {
        return  new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       target.hasCounters(counter) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    };
    public static final MagicPermanentFilterImpl creature(final MagicColor color, final Control control) {
        return creatureOr(color, color, control);
    }
    public static final MagicPermanentFilterImpl creature(final MagicType type, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
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
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       target.hasSubType(subtype) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creatureNon(final MagicSubType subtype, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasSubType(subtype) == false) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creatureOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasColor(color1) || target.hasColor(color2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creatureOr(final MagicSubType subType1, final MagicSubType subType2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasSubType(subType1) || target.hasSubType(subType2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creatureOr(final MagicPermanentState state1, final MagicPermanentState state2, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasState(state1) || target.hasState(state2)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creatureAnd(final MagicPermanentState state, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasState(state) && target.hasSubType(subType)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creatureAnd(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       (target.hasType(type) && target.hasSubType(subType)) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creature(final MagicAbility ability, final Control control) {
        return new MagicPermanentFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                return target.isCreature() &&
                       target.hasAbility(ability) &&
                       ((control == Control.You && target.isController(player)) ||
                        (control == Control.Opp && target.isOpponent(player)) ||
                        (control == Control.Any));
            }
        };
    }
    public static final MagicPermanentFilterImpl creature(final MagicPermanentState state, final Control control) {
        return permanent(state, MagicType.Creature, control);
    }

    public static final MagicCardFilterImpl card() {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return true;
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return false;
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicType type) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return target.hasType(type);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return false;
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicSubType subType) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return target.hasSubType(subType);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return false;
            }
        };
    }
    public static final MagicCardFilterImpl card(final MagicColor color) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return target.hasColor(color);
            }
            public boolean acceptType(final MagicTargetType targetType) {
                return false;
            }
        };
    }
    public static final MagicCardFilterImpl cardName(final String name) {
        return new MagicCardFilterImpl() {
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.getName().equalsIgnoreCase(name);
            }
            public boolean acceptType(MagicTargetType targetType) {
                return false;
            }
        };
    }


    public static final MagicStackFilterImpl spell(final MagicColor color) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() && itemOnStack.hasColor(color);
            }
        };
    }
    public static final MagicStackFilterImpl spell(final MagicType type) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type);
            }
        };
    }
    public static final MagicStackFilterImpl spell(final MagicSubType subType) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(subType);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicType type1, final MagicType type2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type1) || itemOnStack.isSpell(type2);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                final MagicPlayer controller = itemOnStack.getController();
                return (itemOnStack.isSpell(type1) || itemOnStack.isSpell(type2)) &&
                        ((control == Control.You && controller == player) ||
                         (control == Control.Opp && controller != player) ||
                         (control == Control.Any));
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicType type, final MagicSubType subType) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type) || itemOnStack.isSpell(subType);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicSubType subType1, final MagicSubType subType2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(subType1) || itemOnStack.isSpell(subType2);
            }
        };
    }
    public static final MagicStackFilterImpl spellOr(final MagicColor color1, final MagicColor color2) {
        return new MagicStackFilterImpl() {
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() &&
                       (itemOnStack.hasColor(color1) || itemOnStack.hasColor(color2));
            }
        };
    }
}

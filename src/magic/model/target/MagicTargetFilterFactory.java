package magic.model.target;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
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
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return false;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return false;
        }
    };

    public static final MagicPermanentFilterImpl ANY = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return true;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return true;
        }

        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return source == target;
        }
    };

    public static final MagicPlayerFilterImpl YOU = new MagicPlayerFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPlayer target) {
            return player == target;
        }
    };

    public static final MagicPermanentFilterImpl EQUIPMENT_ATTACHED_TO_SOURCE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.getEquippedCreature() == source;
        }
    };

    public static final MagicStackFilterImpl SPELL_OR_ABILITY = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return true;
        }
    };

    public static final MagicStackFilterImpl SPELL_OR_ABILITY_YOU_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return source.isFriend(item);
        }
    };

    public static final MagicStackFilterImpl SPELL_OR_ABILITY_OPPONENT_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isEnemy(player);
        }
    };

    public static final MagicStackFilterImpl ACTIVATED_ABILITY = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item instanceof MagicAbilityOnStack;
        }
    };

    public static final MagicStackFilterImpl ACTIVATED_OR_TRIGGERED_ABILITY = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() == false;
        }
    };

    public static final MagicStackFilterImpl ACTIVATED_OR_TRIGGERED_ABILITY_OPP_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() == false && source.isEnemy(item);
        }
    };

    public static final MagicStackFilterImpl SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell();
        }
    };

    public static final MagicStackFilterImpl SPELL_OR_ABILITY_THAT_TARGETS_PERMANENTS = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.getTarget().isPermanent();
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_PLAYER = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getTarget().isPlayer();
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_YOU = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getTarget() == player;
        }
    };

    public static final MagicStackFilterImpl INSTANT_OR_SORCERY_SPELL_THAT_TARGETS_YOU = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isInstantOrSorcerySpell() && item.getTarget() == player;
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_YOU_OR_PERMANENT_YOU_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            final MagicTarget target = item.getTarget();
            return item.isSpell() && (target == player || (target.isPermanent() && target.isFriend(player)));
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_PERMANENT_YOU_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getTarget().isPermanent() && item.getTarget().isFriend(player);
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_CREATURE_YOU_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getTarget().isCreaturePermanent() && item.getTarget().isFriend(player);
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_CREATURE = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getTarget().isCreaturePermanent();
        }
    };

    public static final MagicStackFilterImpl SPELL_THAT_TARGETS_ENCHANTMENT = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getTarget().isPermanent() && item.getTarget().hasType(MagicType.Enchantment);
        }
    };

    public static final MagicStackFilterImpl INSTANT_OR_AURA_THAT_TARGETS_PERMANENT_YOU_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack item) {
            return (item.hasType(MagicType.Instant) || item.hasSubType(MagicSubType.Aura)) &&
                item.isSpell() &&
                item.getTarget().isPermanent() &&
                item.getTarget().isFriend(player);
        }
    };

    public static final MagicStackFilterImpl SPELL_YOU_DONT_CONTROL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.isEnemy(player);
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_EQ_1 = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getConvertedCost() == 1;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_EQ_2 = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getConvertedCost() == 2;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_LEQ_3 = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getConvertedCost() <= 3;
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_CMC_4_OR_GREATER = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getConvertedCost() >= 4;
        }
    };

    public static final MagicStackFilterImpl INSTANT_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell(MagicType.Instant) &&
                item.getConvertedCost() <= 2 &&
                item.isFriend(player);
        }
    };

    public static final MagicStackFilterImpl SORCERY_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2 = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell(MagicType.Sorcery) &&
                item.getConvertedCost() <= 2 &&
                item.isFriend(player);
        }
    };

    public static final MagicStackFilterImpl SPELL_WITH_X_COST = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell() && item.getCardDefinition().hasX();
        }
    };

    public static final MagicStackFilterImpl RED_OR_GREEN_SPELL = spellOr(MagicColor.Red, MagicColor.Green);

    public static final MagicStackFilterImpl BLUE_OR_BLACK_SPELL = spellOr(MagicColor.Blue, MagicColor.Black);

    public static final MagicStackFilterImpl GREEN_OR_WHITE_SPELL = spellOr(MagicColor.Green, MagicColor.White);

    public static final MagicStackFilterImpl NONBLUE_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicStackFilterImpl NONBLACK_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Black);
        }
    };

    public static final MagicStackFilterImpl NONFAERIE_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasSubType(MagicSubType.Faerie);
        }
    };

    public static final MagicStackFilterImpl BLUE_INSTANT_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Instant) && itemOnStack.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicStackFilterImpl BLUE_SPELL_YOUR_TURN = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            final MagicGame game = player.getGame();
            return itemOnStack.isSpell() && itemOnStack.hasColor(MagicColor.Blue) && game.getTurnPlayer() == player;
        }
    };

    public static final MagicStackFilterImpl BLUE_OR_BLACK_SPELL_YOUR_TURN = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            final MagicGame game = player.getGame();
            return itemOnStack.isSpell() &&
                (itemOnStack.hasColor(MagicColor.Blue) || itemOnStack.hasColor(MagicColor.Black)) &&
                game.getTurnPlayer() == player;
        }
    };

    public static final MagicStackFilterImpl NONRED_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && !itemOnStack.hasColor(MagicColor.Red);
        }
    };

    public static final MagicStackFilterImpl BLUE_OR_BLACK_OR_RED_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && (
                itemOnStack.hasColor(MagicColor.Blue) ||
                    itemOnStack.hasColor(MagicColor.Black) ||
                    itemOnStack.hasColor(MagicColor.Red));
        }
    };

    public static final MagicStackFilterImpl WHITE_OR_BLUE_OR_BLACK_OR_RED_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && (
                itemOnStack.hasColor(MagicColor.White) ||
                    itemOnStack.hasColor(MagicColor.Blue) ||
                    itemOnStack.hasColor(MagicColor.Black) ||
                    itemOnStack.hasColor(MagicColor.Red));
        }
    };

    public static final MagicStackFilterImpl CREATURE_OR_AURA_SPELL = spellOr(MagicType.Creature, MagicSubType.Aura);

    public static final MagicStackFilterImpl CREATURE_OR_SORCERY_SPELL = spellOr(MagicType.Creature, MagicType.Sorcery);

    public static final MagicStackFilterImpl NONCREATURE_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                !itemOnStack.isSpell(MagicType.Creature);
        }
    };

    public static final MagicStackFilterImpl NONARTIFACT_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() &&
                !itemOnStack.isSpell(MagicType.Artifact);
        }
    };

    public static final MagicStackFilterImpl CREATURE_SPELL_CMC_6_OR_MORE = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.getConvertedCost() >= 6;
        }
    };

    public static final MagicStackFilterImpl CREATURE_SPELL_CMC_3_OR_LESS = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.getConvertedCost() <= 3;
        }
    };

    public static final MagicStackFilterImpl CREATURE_SPELL_WITH_INFECT = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.hasAbility(MagicAbility.Infect);
        }
    };

    public static final MagicStackFilterImpl GREEN_CREATURE_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.hasColor(MagicColor.Green);
        }
    };

    public static final MagicStackFilterImpl BLUE_CREATURE_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell(MagicType.Creature) && itemOnStack.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicStackFilterImpl WHITE_OR_BLUE_INSTANT_OR_SORCERY_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return (item.hasColor(MagicColor.White) || item.hasColor(MagicColor.Blue)) &&
                (item.isSpell(MagicType.Instant) || item.isSpell(MagicType.Sorcery));
        }
    };

    public static final MagicStackFilterImpl ENCHANTMENT_OR_INSTANT_OR_SORCERY_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
            return item.isSpell(MagicType.Enchantment) ||
                   item.isSpell(MagicType.Instant) ||
                   item.isSpell(MagicType.Sorcery);
        }
    };

    public static final MagicStackFilterImpl AURA_EQUIPMENT_OR_VEHICLE_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(MagicSource source, MagicPlayer player, MagicItemOnStack item) {
            return item.isSpell(MagicSubType.Aura) ||
                item.isSpell(MagicSubType.Equipment) ||
                item.isSpell(MagicSubType.Vehicle);
        }
    };

    public static final MagicStackFilterImpl INSTANT_OR_SORCERY_SPELL = spellOr(MagicType.Instant, MagicType.Sorcery);

    public static final MagicStackFilterImpl INSTANT_OR_SORCERY_SPELL_YOU_CONTROL = spellOr(MagicType.Instant, MagicType.Sorcery, Control.You);

    public static final MagicStackFilterImpl SPIRIT_OR_ARCANE_SPELL = spellOr(MagicSubType.Spirit, MagicSubType.Arcane);

    public static final MagicStackFilterImpl ARTIFACT_OR_ENCHANTMENT_SPELL = spellOr(MagicType.Artifact, MagicType.Enchantment);

    public static final MagicPlayerFilterImpl PLAYER = new MagicPlayerFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPlayer target) {
            return true;
        }
    };

    public static final MagicPlayerFilterImpl DEFENDING_PLAYER = new MagicPlayerFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPlayer target) {
            return target.getGame().getDefendingPlayer() == target;
        }
    };

    public static final MagicPlayerFilterImpl PLAYER_LOST_LIFE = new MagicPlayerFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPlayer target) {
            return target.getLifeLossThisTurn() >= 1;
        }
    };

    public static final MagicPlayerFilterImpl PLAYER_CONTROLS_CREATURE = new MagicPlayerFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPlayer target) {
            return target.controlsPermanent(MagicType.Creature);
        }
    };

    public static final MagicPlayerFilterImpl OPPONENT = new MagicPlayerFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPlayer target) {
            return target != player;
        }
    };

    public static final MagicTargetFilterImpl SPELL_OR_PERMANENT = new MagicTargetFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicTarget target) {
            return target.isSpell() || target.isPermanent();
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Stack ||
                targetType == MagicTargetType.Permanent;
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
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl BLACK_OR_RED_PERMANENT = permanentOr(MagicColor.Black, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_OR_GREEN_PERMANENT = permanentOr(MagicColor.Black, MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_OR_RED_PERMANENT = permanentOr(MagicColor.Blue, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_BLUE_PERMANENT = permanentOr(MagicColor.Green, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLACK_PERMANENT = permanentOr(MagicColor.White, MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_PERMANENT_YOU_CONTROL = permanentOr(MagicColor.White, MagicColor.Blue, Control.You);

    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_PERMANENT = permanentOr(MagicColor.Green, MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_PERMANENT = permanentOr(MagicColor.White, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl RED_OR_WHITE_PERMANENT = permanentOr(MagicColor.Red, MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl NON_SWAMP_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() && !target.hasSubType(MagicSubType.Swamp);
        }
    };

    public static final MagicPermanentFilterImpl NONBASIC_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic);
        }
    };

    public static final MagicPermanentFilterImpl NONBASIC_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Basic) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl NON_LAIR_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() && !target.hasSubType(MagicSubType.Lair);
        }
    };

    public static final MagicPermanentFilterImpl TRAPPED_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() && target.getCounters(MagicCounterType.Trap) >= 1;
        }
    };

    public static final MagicPermanentFilterImpl BASIC_LAND = permanentAnd(MagicType.Land, MagicType.Basic, Control.Any);

    public static final MagicPermanentFilterImpl SNOW_LAND = permanentAnd(MagicType.Land, MagicType.Snow, Control.Any);

    public static final MagicCardFilterImpl SNOW_LAND_CARD = card(MagicType.Snow).and(MagicType.Land);

    public static final MagicCardFilterImpl LAND_CARD_FROM_LIBRARY = card(MagicType.Land).from(MagicTargetType.Library);

    public static final MagicPermanentFilterImpl BASIC_LAND_YOU_CONTROL = permanentAnd(MagicType.Land, MagicType.Basic, Control.You);

    public static final MagicPermanentFilterImpl SNOW_LAND_YOU_CONTROL = permanentAnd(MagicType.Land, MagicType.Snow, Control.You);

    public static final MagicPermanentFilterImpl SNOW_MOUNTAIN = permanentAnd(MagicType.Snow, MagicSubType.Mountain, Control.Any);

    public static final MagicPermanentFilterImpl SNOW_SWAMP = permanentAnd(MagicType.Snow, MagicSubType.Swamp, Control.Any);

    public static final MagicPermanentFilterImpl SNOW_ISLAND = permanentAnd(MagicType.Snow, MagicSubType.Island, Control.Any);

    public static final MagicPermanentFilterImpl SNOW_FOREST = permanentAnd(MagicType.Snow, MagicSubType.Forest, Control.Any);

    public static final MagicPermanentFilterImpl SNOW_PLAINS = permanentAnd(MagicType.Snow, MagicSubType.Plains, Control.Any);

    public static final MagicPermanentFilterImpl LAND = permanent(MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl LAND_OR_NONBLACK_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() || (!target.hasColor(MagicColor.Black) && target.isCreature());
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isLand() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isLand();
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isLand() && target.isController(player);
        }
    };


    public static final MagicPermanentFilterImpl NONLAND_NONTOKEN_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isLand() && !target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_ARTIFACT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isToken() && target.hasType(MagicType.Artifact);
        }
    };

    public static final MagicPermanentFilterImpl TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isController(player) && target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl TOKEN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
            return target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl FACEUP_NONTOKEN_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isToken() && target.isCreature() && target.isFaceDown() == false;
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isToken() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_WHITE_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isToken() && target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_ELF = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isToken() && target.hasSubType(MagicSubType.Elf);
        }
    };

    public static final MagicPermanentFilterImpl NONTOKEN_RED_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isToken() && target.hasColor(MagicColor.Red);
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isLand() && target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl NONCREATURE_ARTIFACT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
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
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isArtifact() ||
                target.isCreature() ||
                target.isLand();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT = permanentOr(MagicType.Artifact, MagicType.Enchantment, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_CMC_3_OR_LESS = new MagicCMCPermanentFilter(
        ARTIFACT_OR_ENCHANTMENT,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_CMC_4_OR_LESS = new MagicCMCPermanentFilter(
        ARTIFACT_OR_ENCHANTMENT,
        Operator.LESS_THAN_OR_EQUAL,
        4
    );

    public static final MagicPermanentFilterImpl ARTIFACT_OR_LAND = permanentOr(MagicType.Artifact, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl ARTIFACT_OR_ENCHANTMENT_OR_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() ||
                target.isArtifact() ||
                target.isEnchantment();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_ENCHANTMENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() ||
                target.isArtifact() ||
                target.isEnchantment();
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_OR_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isArtifact() || target.isCreature() || target.isEnchantment() || target.isLand();
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTMENT_OR_TAPPED_ARTIFACT_OR_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEnchantment() || (target.isTapped() && target.isArtifact()) || (target.isTapped() && target.isCreature());
        }
    };

    public static final MagicPermanentFilterImpl NONCREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isCreature();
        }
    };

    public static final MagicTargetFilterImpl ONE = new MagicTargetFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicTarget target) {
            return target == player;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Player;
        }
    };

    public static final MagicTargetFilterImpl CREATURE_OR_PLAYER = new MagicTargetFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicTarget target) {
            return target.isPlayer() ||
                target.isCreaturePermanent();
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent ||
                targetType == MagicTargetType.Player;
        }
    };

    public static final MagicTargetFilterImpl SLIVER_CREATURE_OR_PLAYER = new MagicTargetFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicTarget target) {
            return target.isPlayer() ||
                (target.isCreaturePermanent() && target.hasSubType(MagicSubType.Sliver));
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Permanent ||
                targetType == MagicTargetType.Player;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_OR_LAND = permanentOr(MagicType.Creature, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_OR_LAND_YOU_CONTROL = permanentOr(MagicType.Creature, MagicType.Land, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_OR_PLANESWALKER = permanentOr(MagicType.Creature, MagicType.Planeswalker, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_OR_ENCHANTMENT = permanentOr(MagicType.Creature, MagicType.Enchantment, Control.Any);

    public static final MagicCardFilterImpl CREATURE_OR_ENCHANTMENT_CARD = card(MagicType.Creature).or(MagicType.Enchantment);

    public static final MagicPermanentFilterImpl EQUIPMENT = permanent(MagicSubType.Equipment, Control.Any);

    public static final MagicPermanentFilterImpl EQUIPMENT_YOU_CONTROL = permanent(MagicSubType.Equipment, Control.You);

    public static final MagicPermanentFilterImpl ENCHANTMENT = permanent(MagicType.Enchantment, Control.Any);

    public static final MagicPermanentFilterImpl ENCHANTMENT_OR_LAND = permanentOr(MagicType.Enchantment, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl ENCHANTMENT_YOU_CONTROL = permanent(MagicType.Enchantment, Control.You);

    public static final MagicPermanentFilterImpl ENCHANTMENT_YOU_OWN_AND_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) && target.isController(player) && target.hasType(MagicType.Enchantment);
        }
    };

    public static final MagicPermanentFilterImpl RED_OR_GREEN_ENCHANTMENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEnchantment() && (target.hasColor(MagicColor.Red) || target.hasColor(MagicColor.Green));
        }
    };

    public static final MagicPermanentFilterImpl SPIRIT_OR_ENCHANTMENT = permanentOr(MagicType.Enchantment, MagicSubType.Spirit, Control.Any);

    public static final MagicPermanentFilterImpl PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isUntapped();
        }
    };

    public static final MagicPermanentFilterImpl TAPPED_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isTapped();
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN_AND_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_YOU_OWN_OR_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) || target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_WITH_FADING = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasAbility(MagicAbility.Fading);
        }
    };

    public static final MagicPermanentFilterImpl LAND_YOU_CONTROL = permanent(MagicType.Land, Control.You);

    public static final MagicPermanentFilterImpl FOREST = permanent(MagicSubType.Forest, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_YOU_CONTROL = permanent(MagicSubType.Forest, Control.You);

    public static final MagicPermanentFilterImpl ISLAND_YOU_CONTROL = permanent(MagicSubType.Island, Control.You);

    public static final MagicPermanentFilterImpl ISLAND_OR_SWAMP = permanentOr(MagicSubType.Island, MagicSubType.Swamp, Control.Any);

    public static final MagicPermanentFilterImpl ISLAND = permanent(MagicSubType.Island, Control.Any);

    public static final MagicPermanentFilterImpl MOUNTAIN = permanent(MagicSubType.Mountain, Control.Any);

    public static final MagicPermanentFilterImpl MOUNTAIN_YOU_CONTROL = permanent(MagicSubType.Mountain, Control.You);

    public static final MagicPermanentFilterImpl PLAINS = permanent(MagicSubType.Plains, Control.Any);

    public static final MagicPermanentFilterImpl AURA = permanent(MagicSubType.Aura, Control.Any);

    public static final MagicPermanentFilterImpl AURA_YOU_OWN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isOwner(player) && target.hasSubType(MagicSubType.Aura);
        }
    };

    public static final MagicPermanentFilterImpl AURA_ATTACHED_TO_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Aura) &&
                target.getEnchantedPermanent().hasType(MagicType.Creature);
        }
    };

    public static final MagicPermanentFilterImpl SWAMP = permanent(MagicSubType.Swamp, Control.Any);

    public static final MagicPermanentFilterImpl SWAMP_YOU_CONTROL = permanent(MagicSubType.Swamp, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isController(player) &&
                target.isCreature() &&
                target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl CARIBOU_TOKEN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.isToken() &&
                target.hasSubType(MagicSubType.Caribou);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_TOKEN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl SERF_TOKEN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Serf) &&
                target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl SKELETON_TOKEN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Skeleton) &&
                target.isToken();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_THAT_ISNT_ENCHANTED = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && target.isEnchanted() == false;
        }
    };

    public static final MagicPermanentFilterImpl NON_LEGENDARY_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.hasType(MagicType.Legendary) &&
                target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl BLACK_OR_RED_CREATURE_YOU_CONTROL = creatureOr(MagicColor.Black, MagicColor.Red, Control.You);

    public static final MagicPermanentFilterImpl BLUE_OR_BLACK_CREATURE = creatureOr(MagicColor.Blue, MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_OR_PLAINS = permanentOr(MagicSubType.Forest, MagicSubType.Plains, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_OR_SAPROLING = permanentOr(MagicSubType.Forest, MagicSubType.Saproling, Control.Any);

    public static final MagicPermanentFilterImpl PLAINS_OR_ISLAND = permanentOr(MagicSubType.Plains, MagicSubType.Island, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_OR_RED_CREATURE = creatureOr(MagicColor.Blue, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_OR_GREEN_CREATURE = creatureOr(MagicColor.Black, MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_OR_RED_CREATURE = creatureOr(MagicColor.Black, MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl RED_OR_GREEN_CREATURE = creatureOr(MagicColor.Red, MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl RED_OR_WHITE_CREATURE = creatureOr(MagicColor.Red, MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_WHITE_CREATURE = creatureOr(MagicColor.Green, MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl GREEN_OR_BLUE_CREATURE = creatureOr(MagicColor.Green, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLUE_CREATURE = creatureOr(MagicColor.White, MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_OR_BLACK_CREATURE = creatureOr(MagicColor.White, MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_CREATURE = creature(MagicColor.Black, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_CREATURE = creature(MagicColor.White, Control.Any);

    public static final MagicPermanentFilterImpl BLUE_CREATURE = creature(MagicColor.Blue, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_CREATURE_YOU_CONTROL = creature(MagicColor.Black, Control.You);

    public static final MagicPermanentFilterImpl GREEN_CREATURE = creature(MagicColor.Green, Control.Any);

    public static final MagicPermanentFilterImpl RED_CREATURE_YOU_CONTROL = creature(MagicColor.Red, Control.You);

    public static final MagicPermanentFilterImpl RED_CREATURE = creature(MagicColor.Red, Control.Any);

    public static final MagicPermanentFilterImpl WHITE_CREATURE_YOU_CONTROL = creature(MagicColor.White, Control.You);

    public static final MagicPermanentFilterImpl DRAGON_YOU_CONTROL = permanent(MagicSubType.Dragon, Control.You);

    public static final MagicPermanentFilterImpl SOLDIER_OR_WARRIOR = permanentOr(MagicSubType.Soldier, MagicSubType.Warrior, Control.Any);

    public static final MagicPermanentFilterImpl SCARECROW_OR_PLAINS = permanentOr(MagicSubType.Scarecrow, MagicSubType.Plains, Control.Any);

    public static final MagicPermanentFilterImpl FOREST_OR_TREEFOLK = permanentOr(MagicSubType.Forest, MagicSubType.Treefolk, Control.Any);

    public static final MagicPermanentFilterImpl GOBLIN_CREATURE = permanentAnd(MagicType.Creature, MagicSubType.Goblin, Control.Any);

    public static final MagicPermanentFilterImpl GOBLIN_OR_SHAMAN = permanentOr(MagicSubType.Goblin, MagicSubType.Shaman, Control.Any);

    public static final MagicPermanentFilterImpl DJINN_OR_EFREET = permanentOr(MagicSubType.Djinn, MagicSubType.Efreet, Control.Any);

    public static final MagicPermanentFilterImpl TREEFOLK_OR_WARRIOR = permanentOr(MagicSubType.Treefolk, MagicSubType.Warrior, Control.Any);

    public static final MagicPermanentFilterImpl CLERIC_OR_WIZARD_CREATURE = creatureOr(MagicSubType.Cleric, MagicSubType.Wizard, Control.Any);

    public static final MagicPermanentFilterImpl LEGENDARY_SAMURAI = creatureAnd(MagicType.Legendary, MagicSubType.Samurai, Control.Any);

    public static final MagicPermanentFilterImpl LEGENDARY_SNAKE = creatureAnd(MagicType.Legendary, MagicSubType.Snake, Control.Any);

    public static final MagicPermanentFilterImpl INSECT_RAT_SPIDER_OR_SQUIRREL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Insect) ||
                target.hasSubType(MagicSubType.Rat) ||
                target.hasSubType(MagicSubType.Spider) ||
                target.hasSubType(MagicSubType.Squirrel);
        }
    };

    public static final MagicPermanentFilterImpl HUMAN_OR_ANGEL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Human) || target.hasSubType(MagicSubType.Angel);
        }
    };

    public static final MagicPermanentFilterImpl SKELETON_VAMPIRE_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                target.hasSubType(MagicSubType.Skeleton) ||
                target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl VAMPIRE_WEREWOLF_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                target.hasSubType(MagicSubType.Werewolf) ||
                target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl VAMPIRE_OR_ZOMBIE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Vampire) ||
                   target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasSubType(MagicSubType.Vampire) &&
                !target.hasSubType(MagicSubType.Werewolf) &&
                !target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl BARBARIAN_WARRIOR_BERSERKER_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                (target.hasSubType(MagicSubType.Barbarian) ||
                    target.hasSubType(MagicSubType.Warrior) ||
                    target.hasSubType(MagicSubType.Berserker));
        }
    };

    public static final MagicPermanentFilterImpl NONZOMBIE_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicPermanentFilterImpl HUMAN = permanent(MagicSubType.Human, Control.Any);

    public static final MagicPermanentFilterImpl NONENCHANTMENT_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasType(MagicType.Enchantment);
        }
    };

    public static final MagicPermanentFilterImpl NONENCHANTMENT_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.hasType(MagicType.Enchantment);
        }
    };

    public static final MagicPermanentFilterImpl PERMANENT_ENCHANTED = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEnchanted();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_ENCHANTED = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.isEnchanted();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_EQUIPPED = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEquipped() && target.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target;
        }

        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_ARTIFACT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target && target.isArtifact();
        }

        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target && target.isCreature();
        }

        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent perm = (MagicPermanent)source;
            return perm.getEnchantedPermanent() == target && target.isLand();
        }

        @Override
        public boolean isStatic() {
            return true;
        }
    };

    public static final MagicPermanentFilterImpl EQUIPPED_CREATURE = new MagicPermanentFilterImpl() {
        @Override
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
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEquipped() && target.isCreature() && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_YOU_CONTROL_SHARE_COLOR = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && target.isController(player) && target.shareColor(source);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_ANOTHER_AURA = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final int amount = source.isPermanent() && source.hasSubType(MagicSubType.Aura) ? 1 : 0;
            return target.isCreature() && target.getAuraPermanents().size() >= 1 + amount;
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTMENT_OR_ENCHANTED_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEnchantment() || target.isEnchanted();
        }
    };

    public static final MagicPermanentFilterImpl ENCHANTED_OR_ENCHANTMENT_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return (target.isCreature() && target.isEnchanted()) ||
                (target.isCreature() && target.hasType(MagicType.Enchantment));
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_OR_VEHICLE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() || target.hasSubType(MagicSubType.Vehicle);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_OR_NONBASIC_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() || (!target.hasType(MagicType.Basic) && target.isLand());
        }
    };

    public static final MagicPermanentFilterImpl ZOMBIE = permanent(MagicSubType.Zombie, Control.Any);

    public static final MagicPermanentFilterImpl FUNGUS = permanent(MagicSubType.Fungus, Control.Any);

    public static final MagicPermanentFilterImpl SLIVER = permanent(MagicSubType.Sliver, Control.Any);

    public static final MagicPermanentFilterImpl SLIVER_CREATURE = creature(MagicSubType.Sliver, Control.Any);

    public static final MagicPermanentFilterImpl ELF = permanent(MagicSubType.Elf, Control.Any);

    public static final MagicPermanentFilterImpl SPIRIT_YOU_CONTROL = permanent(MagicSubType.Spirit, Control.You);

    public static final MagicPermanentFilterImpl MODULAR_CREATURE = creature(MagicAbility.Modular, Control.Any);

    public static final MagicPermanentFilterImpl LEVELUP_CREATURE = creature(MagicAbility.LevelUp, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE = permanent(MagicType.Creature, Control.Any);

    public static final MagicPermanentFilterImpl WORLD = permanent(MagicType.World, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_YOU_CONTROL = permanent(MagicType.Creature, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_YOU_OWN = permanent(MagicType.Creature, Own.You);

    public static final MagicPermanentFilterImpl CREATURE_YOUR_OPPONENT_CONTROLS = permanent(MagicType.Creature, Control.Opp);

    public static final MagicPermanentFilterImpl FACE_DOWN_CREATURE = creature(MagicPermanentState.FaceDown, Control.Any);

    public static final MagicPermanentFilterImpl TAPPED_CREATURE = creature(MagicPermanentState.Tapped, Control.Any);

    public static final MagicPermanentFilterImpl TAPPED_CREATURE_YOU_CONTROL = creature(MagicPermanentState.Tapped, Control.You);

    public static final MagicPermanentFilterImpl TAPPED_ARTIFACT_YOU_CONTROL = permanent(MagicPermanentState.Tapped, MagicType.Artifact, Control.You);

    public static final MagicPermanentFilterImpl TAPPED_LAND = permanent(MagicPermanentState.Tapped, MagicType.Land, Control.Any);

    public static final MagicPermanentFilterImpl UNTAPPED_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.isUntapped();
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_ISLAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Island) &&
                target.isUntapped();
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_ARTIFACT_CREATURE_OR_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isUntapped() &&
                (target.isArtifact() || target.isCreature() || target.isLand());
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_CREATURE_OR_BLACK_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                (target.isArtifact() || target.hasColor(MagicColor.Black));
        }
    };

    public static final MagicPermanentFilterImpl NONWHITE_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl NONWHITE_NONBLACK_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.White) && !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NONWHITE_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl NONBLACK_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NONBLUE_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.Blue);
        }
    };

    public static final MagicPermanentFilterImpl NONGREEN_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.Green);
        }
    };

    public static final MagicPermanentFilterImpl NONBLACK_ATTACKING_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.Black) &&
                target.isAttacking();
        }
    };

    public static final MagicPermanentFilterImpl NONRED_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasColor(MagicColor.Red);
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.isArtifact();
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_ATTACKING_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.isArtifact() &&
                target.isAttacking();
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_NONBLACK_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.isArtifact() &&
                !target.hasColor(MagicColor.Black);
        }
    };

    public static final MagicPermanentFilterImpl NONSNOW_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && !target.hasType(MagicType.Snow);
        }
    };

    public static final MagicPermanentFilterImpl NONSNOW_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() && !target.hasType(MagicType.Snow);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasAbility(MagicAbility.Flying);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_DEFENDER = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasAbility(MagicAbility.Defender);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasAbility(MagicAbility.Flying) &&
                target.isOpponent(player);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING = creature(MagicAbility.Flying, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_YOU_CONTROL = creature(MagicAbility.Flying, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_WITH_TRAMPLE = creature(MagicAbility.Trample, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_YOUR_OPPONENT_CONTROLS = creature(MagicAbility.Flying, Control.Opp);

    public static final MagicPermanentFilterImpl CREATURE_WITH_DEFENDER = creature(MagicAbility.Defender, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_INFECT = creature(MagicAbility.Infect, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_HORSEMANSHIP = creature(MagicAbility.Horsemanship, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_ISLANDWALK = creature(MagicAbility.Islandwalk, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_SHADOW = creature(MagicAbility.Shadow, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_SHADOW = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.hasAbility(MagicAbility.Shadow);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLYING_OR_REACH = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                (target.hasAbility(MagicAbility.Flying) || target.hasAbility(MagicAbility.Reach));
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_MORPH_ABILITY = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                (target.hasAbility(MagicAbility.Morph) || target.hasAbility(MagicAbility.Megamorph));
        }
    };


    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_OR_ISLANDWALK = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.hasAbility(MagicAbility.Flying) == false &&
                target.hasAbility(MagicAbility.Islandwalk) == false;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITHOUT_FLYING_OR_PLANESWALKER = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return (target.isCreature() && target.hasAbility(MagicAbility.Flying) == false) || target.isPlaneswalker();
        }
    };

    public static final MagicPermanentFilterImpl BLUE_OR_BLACK_CREATURE_WITH_FLYING = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.hasAbility(MagicAbility.Flying) &&
                (target.hasColor(MagicColor.Blue) || target.hasColor(MagicColor.Black));
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_BEEN_DAMAGED = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.hasState(MagicPermanentState.WasDealtDamage);
        }
    };

    public static final MagicCardFilterImpl CREATURE_WITH_DEATHTOUCH_HEXPROOF_REACH_OR_TRAMPLE_FROM_LIBRARY = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Creature) && (
                target.hasAbility(MagicAbility.Deathtouch) ||
                    target.hasAbility(MagicAbility.Hexproof) ||
                    target.hasAbility(MagicAbility.Reach) ||
                    target.hasAbility(MagicAbility.Trample)
            );
        }

        @Override
        public boolean acceptType(MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_CONVERTED_3_OR_LESS = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.getConvertedCost() <= 3;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD = card(MagicType.Creature).cmcLEQ(3).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl CREATURE_CARD_POWER_LEQ_2_FROM_LIBRARY = card(MagicType.Creature).powerLEQ(2).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl GREEN_CREATURE_CARD_FROM_GRAVEYARD = card(MagicColor.Green).and(MagicType.Creature).from(MagicTargetType.Graveyard);

    public static final MagicPermanentFilterImpl CREATURE_CONVERTED_2_OR_LESS = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.getConvertedCost() <= 2;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_PLUSONE_COUNTER = creature(MagicCounterType.PlusOne, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_LEVEL_COUNTER = creature(MagicCounterType.Level, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_FATE_COUNTER = creature(MagicCounterType.Fate, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_AT_LEAST_3_LEVEL_COUNTERS = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.getCounters(MagicCounterType.Level) >= 3;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_MINSUONE_COUNTER = creature(MagicCounterType.MinusOne, Control.Any);

    public static final MagicPermanentFilterImpl CREATURE_WITH_COUNTER = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && target.hasCounters();
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE = creature(MagicPermanentState.Attacking, Control.Any);

    public static final MagicPermanentFilterImpl BLOCKING_CREATURE = creature(MagicPermanentState.Blocking, Control.Any);

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_YOU_CONTROL = creature(MagicPermanentState.Attacking, Control.You);

    public static final MagicPermanentFilterImpl ATTACKING_CREATURE_OPP_CONTROL = creature(MagicPermanentState.Attacking, Control.Opp);

    public static final MagicPermanentFilterImpl BLOCKING_CREATURE_YOU_CONTROL = creature(MagicPermanentState.Blocking, Control.You);

    public static final MagicPermanentFilterImpl CREATURE_BLOCKING_SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && target.getBlockedCreature() == source;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_BLOCKED_BY_SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && target.getBlockingCreatures().contains(source);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_BLOCKING_BLOCKED_BY_SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isCreature() && (target.getBlockedCreature() == source || target.getBlockingCreatures().contains(source));
        }
    };

    public static final MagicPermanentFilterImpl NONATTACKING_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.isAttacking();
        }
    };

    public static final MagicPermanentFilterImpl NONATTACKING_NONBLOCKING_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.isAttacking() &&
                !target.isBlocking();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_WITH_FLANKING = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && target.hasAbility(MagicAbility.Flanking);
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE = creatureOr(MagicPermanentState.Attacking, MagicPermanentState.Blocking, Control.Any);

    public static final MagicPermanentFilterImpl BLACK_OR_RED_CREATURE_ATTACKING_OR_BLOCKING = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                   (target.hasColor(MagicColor.Black) || target.hasColor(MagicColor.Red)) &&
                   (target.isAttacking() || target.isBlocking());
        }
    };

    public static final MagicPermanentFilterImpl ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL = creatureOr(MagicPermanentState.Attacking, MagicPermanentState.Blocking, Control.You);

    public static final MagicPermanentFilterImpl WEREWOLF_OR_WOLF_CREATURE = creatureOr(MagicSubType.Werewolf, MagicSubType.Wolf, Control.Any);

    public static final MagicPermanentFilterImpl WEREWOLF_OR_WOLF_CREATURE_YOU_CONTROL = creatureOr(MagicSubType.Werewolf, MagicSubType.Wolf, Control.You);

    public static final MagicPermanentFilterImpl WOLF_OR_WEREWOLF = permanentOr(MagicSubType.Wolf, MagicSubType.Werewolf, Control.Any);

    public static final MagicPermanentFilterImpl SERVO_OR_THOPTER = permanentOr(MagicSubType.Servo, MagicSubType.Thopter, Control.Any);

    public static final MagicPermanentFilterImpl WOLF_OR_WEREWOLF_YOU_CONTROL = permanentOr(MagicSubType.Wolf, MagicSubType.Werewolf, Control.You);

    public static final MagicPermanentFilterImpl UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isController(player) &&
                target.isAttacking() &&
                !target.isBlocked();
        }
    };

    public static final MagicPermanentFilterImpl UNBLOCKED_ATTACKING_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isAttacking() &&
                !target.isBlocked();
        }
    };

    public static final MagicPermanentFilterImpl KALDRA_EQUIPMENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isEquipment() &&
                (target.isName("Sword of Kaldra") ||
                    target.isName("Shield of Kaldra") ||
                    target.isName("Helm of Kaldra"));
        }
    };

    public static final MagicPermanentFilterImpl KALDRA_EQUIPMENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return KALDRA_EQUIPMENT.accept(source, player, target) && target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl BLOCKED_CREATURE = creature(MagicPermanentState.Blocked, Control.Any);

    public static final MagicCardFilterImpl CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return true;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl COLORLESS_CREATURE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return MagicColor.isColorless(target) && target.isCreature();
        }
    };

    public static final MagicCardFilterImpl COLORLESS_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return MagicColor.isColorless(target);
        }
    };

    public static final MagicCardFilterImpl AURA_OR_EQUIPMENT_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(MagicSource source, MagicPlayer player, MagicCard target) {
            return target.hasSubType(MagicSubType.Aura) || target.hasSubType(MagicSubType.Equipment);
        }
    };

    public static final MagicCardFilterImpl CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return true;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

    public static final MagicCardFilterImpl CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return true;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                targetType == MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD =
        card(MagicType.Artifact).or(MagicType.Creature).or(MagicType.Enchantment);

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_GRAVEYARD = card(MagicType.Creature).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_ALL_GRAVEYARDS = card(MagicType.Creature).from(MagicTargetType.Graveyard).from(MagicTargetType.OpponentsGraveyard);

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_LIBRARY = card(MagicType.Creature).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl PAYABLE_CREATURE_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) && target.getCost().getCondition().accept(target);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl PAYABLE_INSTANT_OR_SORCERY_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return (target.hasType(MagicType.Instant) || target.hasType(MagicType.Sorcery)) && target.getGameCost().getCondition().accept(target);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_YOU_CONTROL_FOR_EMERGE(final MagicManaCost manaCost) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent perm) {
                return perm.isCreature() && perm.isController(player) && manaCost.reduce(perm.getConvertedCost()).getCondition().accept(source);
            }
        };
    }

    public static final MagicCardFilterImpl CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD = card(MagicType.Creature).and(MagicAbility.Infect).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl PERMANENT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.isPermanentCard();
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl PERMANENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.isPermanentCard();
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

    public static final MagicCardFilterImpl LEGENDARY_SPIRIT_PERMANENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Spirit) &&
                   target.hasType(MagicType.Legendary) &&
                   target.isPermanentCard();
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

    public static final MagicCardFilterImpl ARTIFACT_CARD_CMC_LEQ_1_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.getConvertedCost() <= 1 && target.hasType(MagicType.Artifact);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.getConvertedCost() <= 2 && target.hasType(MagicType.Creature);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_POWER_LEQ_2_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.getPower() <= 2 && target.hasType(MagicType.Creature);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicPermanentFilterImpl NONLAND_PERMANENT_CMC_LEQ_3 = new MagicCMCPermanentFilter(
        NONLAND_PERMANENT,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicCardFilterImpl CARD_FROM_OPPONENTS_GRAVEYARD =
        card().from(MagicTargetType.OpponentsGraveyard);

    public static final MagicCardFilterImpl CARD_FROM_OPPONENTS_EXILE =
        card().from(MagicTargetType.OpponentsExile);

    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD =
        card(MagicType.Instant).or(MagicType.Sorcery).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD =
        card(MagicType.Instant).or(MagicType.Sorcery);

    public static final MagicCardFilterImpl INSTANT_SORCERY_OR_CREATURE_CARD =
        card(MagicType.Instant).or(MagicType.Sorcery).or(MagicType.Creature);

    public static final MagicCardFilterImpl RED_SORCERY_CARD =
        card(MagicColor.Red).and(MagicType.Sorcery);

    public static final MagicCardFilterImpl BLUE_INSTANT_CARD =
        card(MagicColor.Blue).and(MagicType.Instant);

    public static final MagicCardFilterImpl BLUE_OR_RED_CREATURE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) &&
                (target.hasColor(MagicColor.Blue) || target.hasColor(MagicColor.Red));
        }
    };

    public static final MagicCardFilterImpl VAMPIRE_OR_WIZARD_CREATURE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) &&
                (target.hasSubType(MagicSubType.Vampire) || target.hasSubType(MagicSubType.Wizard));
        }
    };

    public static final MagicCardFilterImpl BLACK_OR_RED_CREATURE_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }

        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) &&
                (target.hasColor(MagicColor.Black) || target.hasColor(MagicColor.Red));
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD =
        card(MagicType.Creature).from(MagicTargetType.OpponentsGraveyard);

    public static final MagicCardFilterImpl ARTIFACT_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Artifact);
        }
    };

    public static final MagicCardFilterImpl NONCREATURE_ARTIFACT_CARD_WITH_CMC_LEQ_1_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.getConvertedCost() <= 1 &&
                target.hasType(MagicType.Artifact) &&
                !target.hasType(MagicType.Creature);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl NONLAND_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return !target.hasType(MagicType.Land);
        }
    };

    public static final MagicCardFilterImpl NONCREATURE_NONLAND_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return !target.hasType(MagicType.Land) &&
                !target.hasType(MagicType.Creature);
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature);
        }
    };

    public static final MagicCardFilterImpl ENCHANTMENT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Enchantment);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };

    public static final MagicCardFilterImpl ENCHANTMENT_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Enchantment);
        }
    };

    public static final MagicCardFilterImpl INSTANT_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Instant);
        }
    };

    public static final MagicCardFilterImpl SORCERY_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Sorcery);
        }
    };

    public static final MagicCardFilterImpl INSTANT_OR_SORCERY_CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Sorcery) || target.hasType(MagicType.Instant);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                targetType == MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl LAND_CARD_FROM_YOUR_GRAVEYARD = card(MagicType.Land).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl LAND_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Land);
        }
    };

    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) ||
                target.hasType(MagicType.Artifact);
        }
    };

    public static final MagicCardFilterImpl ZOMBIE_CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                targetType == MagicTargetType.OpponentsGraveyard;
        }

        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasSubType(MagicSubType.Zombie);
        }
    };

    public static final MagicCardFilterImpl SPIRIT_CARD_FROM_GRAVEYARD = card(MagicSubType.Spirit).from(MagicTargetType.Graveyard);

    public static final MagicCardFilterImpl CARD_FROM_HAND = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return true;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicCardFilterImpl INSTANT_LEQ_CMC_2_FROM_HAND = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.getConvertedCost() <= 2 && target.hasType(MagicType.Instant);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };

    public static final MagicCardFilterImpl CREATURE_CARD_FROM_HAND = card(MagicType.Creature).from(MagicTargetType.Hand);

    public static final MagicCardFilterImpl CREATURE_OR_LAND_CARD = card(MagicType.Creature).or(MagicType.Land);

    public static final MagicCardFilterImpl ACELP_CARD =
        card(MagicType.Artifact).or(MagicType.Creature).or(MagicType.Enchantment).or(MagicType.Land).or(MagicType.Planeswalker);

    public static final MagicCardFilterImpl EQUIPMENT_CARD_FROM_HAND = card(MagicSubType.Equipment).from(MagicTargetType.Hand);

    public static final MagicCardFilterImpl RED_OR_GREEN_CARD = card(MagicColor.Red).or(MagicColor.Green);

    public static final MagicPermanentFilterImpl MULTICOLORED_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent);
        }
    };

    public static final MagicPermanentFilterImpl COLORLESS_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && MagicColor.isColorless(target);
        }
    };

    public static final MagicPermanentFilterImpl COLORLESS_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() && MagicColor.isColorless(target) && target.isController(player);
        }
    };

    public static final MagicStackFilterImpl COLORLESS_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && MagicColor.isColorless(itemOnStack.getSource());
        }
    };

    public static final MagicStackFilterImpl COLORLESS_SPELL_CMC_7_OR_MORE = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return itemOnStack.isSpell() && MagicColor.isColorless(itemOnStack.getSource()) && itemOnStack.getConvertedCost() >= 7;
        }
    };

    public static final MagicCardFilterImpl MULTICOLORED_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return MagicColor.isMulti(target);
        }
    };

    public static final MagicStackFilterImpl MULTICOLORED_SPELL = new MagicStackFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
            return MagicColor.isMulti(itemOnStack.getSource()) && itemOnStack.isSpell();
        }
    };

    public static final MagicPermanentFilterImpl MONOCOLORED_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent permanent) {
            return MagicColor.isMono(permanent) && permanent.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl MULTICOLORED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent) && permanent.isCreature() && permanent.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl MULTICOLORED_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent permanent) {
            return MagicColor.isMulti(permanent) && permanent.isCreature();
        }
    };

    public static final MagicPermanentFilterImpl MONO_OR_MULTICOLORED_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent permanent) {
            return (MagicColor.isMono(permanent) || MagicColor.isMulti(permanent)) && permanent.isCreature();
        }
    };

    public static final MagicCardFilterImpl MULTICOLORED_CREATURE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) && MagicColor.isMulti(target);
        }
    };

    public static final MagicCardFilterImpl NONCREATURE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return !target.hasType(MagicType.Creature);
        }
    };

    public static final MagicCardFilterImpl NONARTIFACT_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return !target.hasType(MagicType.Artifact);
        }
    };

    public static final MagicCardFilterImpl NONARTIFACT_NONLAND_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return !target.hasType(MagicType.Artifact) && !target.hasType(MagicType.Land);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() &&
                target.isUntapped() &&
                target.isController(player);
        }
    };

    public static final MagicPermanentFilterImpl NONARTIFACT_NONWHITE_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                !target.isArtifact() &&
                !target.hasColor(MagicColor.White);
        }
    };

    public static final MagicPermanentFilterImpl UNTAPPED_LAND = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isLand() &&
                target.isUntapped();
        }
    };

    public static final MagicPermanentFilterImpl FAERIE_OR_ELF = permanentOr(MagicSubType.Faerie, MagicSubType.Elf, Control.Any);

    public static final MagicPermanentFilterImpl KNIGHT_OR_SOLDIER = permanentOr(MagicSubType.Knight, MagicSubType.Soldier, Control.Any);

    public static final MagicPermanentFilterImpl ELF_OR_SOLDIER_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                (target.hasSubType(MagicSubType.Elf) || target.hasSubType(MagicSubType.Soldier));
        }
    };

    public static final MagicPermanentFilterImpl ELDRAZI_SPAWN_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isCreature() &&
                target.hasSubType(MagicSubType.Eldrazi) &&
                target.hasSubType(MagicSubType.Spawn);
        }
    };

    public static final MagicPermanentFilterImpl ELDRAZI_SPAWN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Eldrazi) &&
                target.hasSubType(MagicSubType.Spawn);
        }
    };

    public static final MagicPermanentFilterImpl ELDRAZI_SCION = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Eldrazi) &&
                target.hasSubType(MagicSubType.Scion);
        }
    };

    public static final MagicPermanentFilterImpl ARTIFACT_LAND = permanentAnd(MagicType.Artifact, MagicType.Land, Control.Any);

    public static final MagicCardFilterImpl ARTIFACT_OR_CREATURE_OR_LAND_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Artifact) ||
                target.hasType(MagicType.Creature) ||
                target.hasType(MagicType.Land);
        }
    };

    public static final MagicPermanentFilterImpl LEGENDARY_LAND = permanentAnd(MagicType.Legendary, MagicType.Land, Control.Any);

    public static final MagicCardFilterImpl permanentCardMaxCMC(final MagicSubType subtype, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.isPermanentCard() &&
                    target.hasSubType(subtype) &&
                    target.getConvertedCost() <= cmc;
            }

            @Override
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicPermanentFilterImpl NONARTIFACT_PERMANENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isArtifact();
        }
    };

    public static final MagicPermanentFilterImpl NON_AURA_ENCHANTMENT = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return !target.isAura() && target.isEnchantment();
        }
    };

    public static final MagicCardFilterImpl permanentCardMaxCMC(final MagicType type, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.isPermanentCard() &&
                    target.hasType(type) &&
                    target.getConvertedCost() <= cmc;
            }

            @Override
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicCardFilterImpl permanentCardMaxCMC(final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicCard target) {
                return target.isPermanentCard() &&
                    target.getConvertedCost() <= cmc;
            }

            @Override
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicCardFilterImpl permanentCardMinCMC(final MagicType type, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.isPermanentCard() &&
                    target.hasType(type) &&
                    target.getConvertedCost() >= cmc;
            }

            @Override
            public boolean acceptType(final MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicCardFilterImpl permanentCardEqualCMC(final MagicType type, final MagicTargetType from, final int cmc) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicCard target) {
                return target.isPermanentCard() &&
                    target.hasType(type) &&
                    target.getConvertedCost() == cmc;
            }

            @Override
            public boolean acceptType(MagicTargetType targetType) {
                return targetType == from;
            }
        };
    }

    public static final MagicCardFilterImpl BASIC_LAND_CARD = card(MagicType.Basic).and(MagicType.Land);

    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_LIBRARY = card(MagicType.Basic).and(MagicType.Land).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl BASIC_LAND_CARD_FROM_ALL_GRAVEYARDS = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Land) && target.hasType(MagicType.Basic);
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard ||
                targetType == MagicTargetType.OpponentsGraveyard;
        }
    };

    public static final MagicCardFilterImpl BASIC_LAND_CARD_OR_DESERT_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return (target.hasType(MagicType.Basic) && target.hasType(MagicType.Land)) ||
                target.hasSubType(MagicSubType.Desert);
        }
    };

    public static final MagicCardFilterImpl BASIC_LAND_CARD_OR_GATE_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return (target.hasType(MagicType.Basic) && target.hasType(MagicType.Land)) ||
                target.hasSubType(MagicSubType.Gate);
        }
    };

    public static final MagicCardFilterImpl BASIC_FOREST_PLAINS_OR_ISLAND = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                target.hasSubType(MagicSubType.Forest) ||
                    target.hasSubType(MagicSubType.Plains) ||
                    target.hasSubType(MagicSubType.Island)
            );
        }
    };

    public static final MagicCardFilterImpl BASIC_PLAINS_ISLAND_OR_SWAMP = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                target.hasSubType(MagicSubType.Plains) ||
                    target.hasSubType(MagicSubType.Island) ||
                    target.hasSubType(MagicSubType.Swamp)
            );
        }
    };

    public static final MagicCardFilterImpl BASIC_ISLAND_SWAMP_OR_MOUNTAIN = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                target.hasSubType(MagicSubType.Island) ||
                    target.hasSubType(MagicSubType.Swamp) ||
                    target.hasSubType(MagicSubType.Mountain)
            );
        }
    };

    public static final MagicCardFilterImpl BASIC_SWAMP_MOUNTAIN_OR_FOREST = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                target.hasSubType(MagicSubType.Swamp) ||
                    target.hasSubType(MagicSubType.Mountain) ||
                    target.hasSubType(MagicSubType.Forest)
            );
        }
    };

    public static final MagicCardFilterImpl BASIC_MOUNTAIN_FOREST_OR_PLAINS = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, MagicPlayer player, MagicCard target) {
            return target.hasType(MagicType.Basic) && (
                target.hasSubType(MagicSubType.Mountain) ||
                    target.hasSubType(MagicSubType.Forest) ||
                    target.hasSubType(MagicSubType.Plains)
            );
        }
    };

    public static final MagicCardFilterImpl PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasSubType(MagicSubType.Plains) ||
                target.hasSubType(MagicSubType.Island) ||
                target.hasSubType(MagicSubType.Swamp) ||
                target.hasSubType(MagicSubType.Mountain);
        }
    };

    public static final MagicCardFilterImpl INSTANT_OR_FLASH_CARD_FROM_LIBRARY = card(MagicType.Instant).or(MagicAbility.Flash).from(MagicTargetType.Library);

    public static final MagicCardFilterImpl LAND_CARD_WITH_BASIC_LAND_TYPE = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasSubType(MagicSubType.Plains) ||
                target.hasSubType(MagicSubType.Island) ||
                target.hasSubType(MagicSubType.Swamp) ||
                target.hasSubType(MagicSubType.Forest) ||
                target.hasSubType(MagicSubType.Mountain);
        }
    };

    public static final MagicCardFilterImpl LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY = LAND_CARD_WITH_BASIC_LAND_TYPE.from(MagicTargetType.Library);

    public static final MagicCardFilterImpl NON_LEGENDARY_GREEN_CREATURE_CARD_WITH_CMC_LEQ_3_FROM_LIBRARY = new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
            return target.hasType(MagicType.Creature) &&
                target.hasType(MagicType.Legendary) == false &&
                target.hasColor(MagicColor.Green) &&
                target.getConvertedCost() <= 3;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };

    public static final MagicPermanentFilterImpl UNPAIRED_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            return target.isController(player) &&
                target.isCreature() &&
                !target.isPaired();
        }
    };

    public static final MagicPermanentFilterImpl UNPAIRED_SOULBOND_CREATURE = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
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

    public static final MagicPermanentFilterImpl CREATURE_POWER_GREATER_THAN_SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent sn = (MagicPermanent)source;
            return target.isCreature() && target.getPower() > sn.getPower();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_POWER_LESS_THAN_SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent sn = (MagicPermanent)source;
            return target.isCreature() && target.getPower() < sn.getPower();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_POWER_LESS_THAN_EQUAL_SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent sn = (MagicPermanent)source;
            return target.isCreature() && target.getPower() <= sn.getPower();
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_OPP_POWER_LESS_THAN_SN = new MagicPermanentFilterImpl() {
        @Override
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
            final MagicPermanent sn = (MagicPermanent)source;
            return target.isCreature() && target.getPower() < sn.getPower() && source.isEnemy(target);
        }
    };

    public static final MagicPermanentFilterImpl CREATURE_POWER_2_OR_LESS = new MagicPTTargetFilter(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        2
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_3_OR_LESS = new MagicPTTargetFilter(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        3
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_4_OR_MORE = new MagicPTTargetFilter(
        CREATURE,
        Operator.GREATER_THAN_OR_EQUAL,
        4
    );

    public static final MagicPermanentFilterImpl CREATURE_POWER_4_OR_LESS = new MagicPTTargetFilter(
        CREATURE,
        Operator.LESS_THAN_OR_EQUAL,
        4
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

    private static final Map<String, MagicTargetFilter<?>> single =
        new TreeMap<String, MagicTargetFilter<?>>(String.CASE_INSENSITIVE_ORDER);

    private static final Map<String, MagicCardFilterImpl> partial =
        new TreeMap<String, MagicCardFilterImpl>(String.CASE_INSENSITIVE_ORDER);

    private static void add(final String key, final MagicTargetFilter<?> filter) {
        if (single.containsKey(key)) {
            throw new RuntimeException("duplicate key \"" + key + "\"");
        }
        single.put(key, filter);
    }

    private static void addp(final String key, final MagicCardFilterImpl filter) {
        if (partial.containsKey(key)) {
            throw new RuntimeException("duplicate key \"" + key + "\"");
        }
        partial.put(key, filter);
    }

    static {
        // used by MagicTargetChoice

        // ... creature card
        addp("blue or red creature card", BLUE_OR_RED_CREATURE_CARD);
        addp("multicolored creature card", MULTICOLORED_CREATURE_CARD);
        addp("instant, sorcery, or creature card", INSTANT_SORCERY_OR_CREATURE_CARD);
        addp("artifact or creature card", ARTIFACT_OR_CREATURE_CARD);
        addp("colorless creature card", COLORLESS_CREATURE_CARD);
        addp("Vampire or Wizard creature card", VAMPIRE_OR_WIZARD_CREATURE_CARD);
        addp("creature card in a graveyard", CREATURE_CARD_FROM_ALL_GRAVEYARDS);

        // ... card
        addp("instant or sorcery card", INSTANT_OR_SORCERY_CARD);
        addp("red sorcery card", RED_SORCERY_CARD);
        addp("blue instant card", BLUE_INSTANT_CARD);
        addp("basic land card", BASIC_LAND_CARD);
        addp("snow land card", SNOW_LAND_CARD);
        addp("artifact or enchantment card", card(MagicType.Artifact).or(MagicType.Enchantment));
        addp("artifact, creature, or enchantment card", ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD);
        addp("creature or enchantment card", CREATURE_OR_ENCHANTMENT_CARD);
        addp("multicolored card", MULTICOLORED_CARD);
        addp("colorless card", COLORLESS_CARD);
        addp("nonland card", NONLAND_CARD);
        addp("noncreature card", NONCREATURE_CARD);
        addp("nonartifact card", NONARTIFACT_CARD);
        addp("noncreature, nonland card", NONCREATURE_NONLAND_CARD);
        addp("nonartifact, nonland card", NONARTIFACT_NONLAND_CARD);
        addp("red or green card", RED_OR_GREEN_CARD);
        addp("artifact, creature, or land card", ARTIFACT_OR_CREATURE_OR_LAND_CARD);
        addp("basic land card or a Desert card", BASIC_LAND_CARD_OR_DESERT_CARD);
        addp("basic land card or a Gate card", BASIC_LAND_CARD_OR_GATE_CARD);
        addp("Plains, Island, Swamp, Mountain or Forest card", LAND_CARD_WITH_BASIC_LAND_TYPE);
        addp("Plains or Island card", card(MagicSubType.Plains).or(MagicSubType.Island));
        addp("Plains or Swamp card", card(MagicSubType.Plains).or(MagicSubType.Swamp));
        addp("Island or Swamp card", card(MagicSubType.Island).or(MagicSubType.Swamp));
        addp("Island or Mountain card", card(MagicSubType.Island).or(MagicSubType.Mountain));
        addp("Swamp or Mountain card", card(MagicSubType.Swamp).or(MagicSubType.Mountain));
        addp("Swamp or Forest card", card(MagicSubType.Swamp).or(MagicSubType.Forest));
        addp("Mountain or Forest card", card(MagicSubType.Mountain).or(MagicSubType.Forest));
        addp("Mountain or Plains card", card(MagicSubType.Mountain).or(MagicSubType.Plains));
        addp("Forest or Plains card", card(MagicSubType.Forest).or(MagicSubType.Plains));
        addp("Forest or Island card", card(MagicSubType.Forest).or(MagicSubType.Island));
        addp("Plains, Island, Swamp, or Mountain card", PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD);
        addp("Treefolk or Forest card", card(MagicSubType.Treefolk).or(MagicSubType.Forest));
        addp("basic Forest, Plains, or Island card", BASIC_FOREST_PLAINS_OR_ISLAND);
        addp("basic Plains, Island, or Swamp card", BASIC_PLAINS_ISLAND_OR_SWAMP);
        addp("basic Island, Swamp, or Mountain card", BASIC_ISLAND_SWAMP_OR_MOUNTAIN);
        addp("basic Swamp, Mountain, or Forest card", BASIC_SWAMP_MOUNTAIN_OR_FOREST);
        addp("basic Mountain, Forest, or Plains card", BASIC_MOUNTAIN_FOREST_OR_PLAINS);
        addp("aura or equipment card", AURA_OR_EQUIPMENT_CARD);
        addp("creature or land card", CREATURE_OR_LAND_CARD);
        addp("artifact, creature, enchantment, land, or planeswalker card", ACELP_CARD);

        // card from a graveyard
        add("card from a graveyard", CARD_FROM_ALL_GRAVEYARDS);

        // <color|type|subtype> card from your graveyard
        add("card from your graveyard", CARD_FROM_GRAVEYARD);
        add("artifact card with converted mana cost 1 or less from your graveyard", ARTIFACT_CARD_CMC_LEQ_1_FROM_GRAVEYARD);
        add("noncreature artifact card with converted mana cost 1 or less from your graveyard", NONCREATURE_ARTIFACT_CARD_WITH_CMC_LEQ_1_FROM_GRAVEYARD);
        add("Rebel permanent card with converted mana cost 5 or less from your graveyard", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Graveyard, 5));

        // <color|type|subtype> permanent card from your graveyard
        add("permanent card from your graveyard", PERMANENT_CARD_FROM_GRAVEYARD);
        add("permanent card with converted mana cost 3 or less from your graveyard", permanentCardMaxCMC(MagicTargetType.Graveyard, 3));
        add("permanent card with converted mana cost 2 or less from your graveyard", permanentCardMaxCMC(MagicTargetType.Graveyard, 2));

        // <color|type|subtype> creature card from your graveyard
        add("creature card with converted mana cost 3 or less from your graveyard", CREATURE_CARD_CMC_LEQ_3_FROM_GRAVEYARD);
        add("creature card with converted mana cost 2 or less from your graveyard", CREATURE_CARD_CMC_LEQ_2_FROM_GRAVEYARD);
        add("creature card with power 2 or less from your graveyard", CREATURE_CARD_POWER_LEQ_2_FROM_GRAVEYARD);
        add("creature card with infect from your graveyard", CREATURE_CARD_WITH_INFECT_FROM_GRAVEYARD);
        add("creature card with scavenge from your graveyard", PAYABLE_CREATURE_CARD_FROM_GRAVEYARD);

        // <color|type|subtype> card from an opponent's graveyard
        add("card from an opponent's graveyard", CARD_FROM_OPPONENTS_GRAVEYARD);
        add("creature card in an opponent's graveyard", CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD);

        // <color|type|subtype> card from your hand
        add("card from your hand", CARD_FROM_HAND);
        add("instant card with converted mana cost 2 or less from your hand", INSTANT_LEQ_CMC_2_FROM_HAND);

        // <color|type|subtype> card from your library
        add("card from your library", CARD_FROM_LIBRARY);
        add("land card with a basic land type from your library", LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY);
        add("instant card or a card with flash from your library", INSTANT_OR_FLASH_CARD_FROM_LIBRARY);
        add("enchantment card with converted mana cost 3 or less from your library", permanentCardMaxCMC(MagicType.Enchantment, MagicTargetType.Library, 3));
        add("artifact card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicType.Artifact, MagicTargetType.Library, 1));
        add("artifact card with converted mana cost 6 or greater from your library", permanentCardMinCMC(MagicType.Artifact, MagicTargetType.Library, 6));
        add("artifact card with converted mana cost 3 from your library", permanentCardEqualCMC(MagicType.Artifact, MagicTargetType.Library, 3));

        // <color|type|subtype> permanent card from your library
        add("Rebel permanent card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 1));
        add("Rebel permanent card with converted mana cost 2 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 2));
        add("Rebel permanent card with converted mana cost 3 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 3));
        add("Rebel permanent card with converted mana cost 4 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 4));
        add("Rebel permanent card with converted mana cost 5 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 5));
        add("Rebel permanent card with converted mana cost 6 or less from your library", permanentCardMaxCMC(MagicSubType.Rebel, MagicTargetType.Library, 6));
        add("Mercenary permanent card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 1));
        add("Mercenary permanent card with converted mana cost 2 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 2));
        add("Mercenary permanent card with converted mana cost 3 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 3));
        add("Mercenary permanent card with converted mana cost 4 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 4));
        add("Mercenary permanent card with converted mana cost 5 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 5));
        add("Mercenary permanent card with converted mana cost 6 or less from your library", permanentCardMaxCMC(MagicSubType.Mercenary, MagicTargetType.Library, 6));
        add("legendary Spirit permanent card from your library", LEGENDARY_SPIRIT_PERMANENT_CARD_FROM_LIBRARY);
        add("permanent card from your library", PERMANENT_CARD_FROM_LIBRARY);

        // <color|type|subtype> creature card from your library
        add("creature card with converted mana cost 1 or less from your library", permanentCardMaxCMC(MagicType.Creature, MagicTargetType.Library, 1));
        add("creature card with converted mana cost 6 or greater from your library", permanentCardMinCMC(MagicType.Creature, MagicTargetType.Library, 6));
        add("creature card with power 2 or less from your library", CREATURE_CARD_POWER_LEQ_2_FROM_LIBRARY);
        add("creature card with deathtouch, hexproof, reach, or trample from your library", CREATURE_WITH_DEATHTOUCH_HEXPROOF_REACH_OR_TRAMPLE_FROM_LIBRARY);
        add("nonlegendary green creature card with converted mana cost 3 or less from your library", NON_LEGENDARY_GREEN_CREATURE_CARD_WITH_CMC_LEQ_3_FROM_LIBRARY);

        // <color|type|subtype> creature you control that
        add("creature you control that's a wolf or a werewolf", WEREWOLF_OR_WOLF_CREATURE_YOU_CONTROL);

        // <color|type|subtype> creature
        add("1/1 creature", new MagicPTTargetFilter(CREATURE, Operator.EQUAL, 1, Operator.EQUAL, 1));
        add("blue or black creature", BLUE_OR_BLACK_CREATURE);
        add("creature with modular", MODULAR_CREATURE);
        add("creature with trample", CREATURE_WITH_TRAMPLE);
        add("creature with level up", LEVELUP_CREATURE);
        add("creature with infect", CREATURE_WITH_INFECT);
        add("red creature or white creature", RED_OR_WHITE_CREATURE);
        add("werewolf or wolf creature", WEREWOLF_OR_WOLF_CREATURE);
        add("Eldrazi Spawn creature", ELDRAZI_SPAWN_CREATURE);
        add("Eldrazi Spawn", ELDRAZI_SPAWN);
        add("face-up nontoken creature", FACEUP_NONTOKEN_CREATURE);

        add("nongreen creature", NONGREEN_CREATURE);
        add("nonblue creature", NONBLUE_CREATURE);
        add("nonblack creature", NONBLACK_CREATURE);
        add("nonblack attacking creature", NONBLACK_ATTACKING_CREATURE);
        add("nonwhite creature", NONWHITE_CREATURE);
        add("nonwhite creature with power 3 or greater", new MagicPTTargetFilter(NONWHITE_CREATURE, Operator.GREATER_THAN_OR_EQUAL, 3));
        add("nonwhite, nonblack creature", NONWHITE_NONBLACK_CREATURE);
        add("nonred creature", NONRED_CREATURE);
        add("nonartifact creature", NONARTIFACT_CREATURE);
        add("nonland creature", NONLAND_CREATURE);
        add("non-Vampire, non-Werewolf, non-Zombie creature", NONVAMPIRE_NONWEREWOLF_NONZOMBIE_CREATURE);
        add("Skeleton, Vampire, or Zombie", SKELETON_VAMPIRE_OR_ZOMBIE);
        add("noncreature", NONCREATURE);
        add("nonartifact, nonblack creature", NONARTIFACT_NONBLACK_CREATURE);
        add("nonartifact, nonwhite creature", NONARTIFACT_NONWHITE_CREATURE);
        add("artifact creature or black creature", ARTIFACT_CREATURE_OR_BLACK_CREATURE);
        add("nonartifact attacking creature", NONARTIFACT_ATTACKING_CREATURE);
        add("land or nonblack creature", LAND_OR_NONBLACK_CREATURE);
        add("red or green creature", RED_OR_GREEN_CREATURE);
        add("red or white creature", RED_OR_WHITE_CREATURE);
        add("face-down creature", FACE_DOWN_CREATURE);
        add("artifact or creature", ARTIFACT_OR_CREATURE);
        add("unpaired Soulbond creature", UNPAIRED_SOULBOND_CREATURE);
        add("monocolored creature", MONOCOLORED_CREATURE);
        add("nonattacking creature", NONATTACKING_CREATURE);
        add("blocked creature", BLOCKED_CREATURE);
        add("blocking creature", BLOCKING_CREATURE);
        add("blue or red creature", BLUE_OR_RED_CREATURE);
        add("black or green creature", BLACK_OR_GREEN_CREATURE);
        add("black or red creature", BLACK_OR_RED_CREATURE);
        add("black or red creature that's attacking or blocking", BLACK_OR_RED_CREATURE_ATTACKING_OR_BLOCKING);
        add("green or white creature", GREEN_OR_WHITE_CREATURE);
        add("green or blue creature", GREEN_OR_BLUE_CREATURE);
        add("green creature or white creature", GREEN_OR_WHITE_CREATURE);
        add("white or blue creature", WHITE_OR_BLUE_CREATURE);
        add("white or black creature", WHITE_OR_BLACK_CREATURE);
        add("white creature with power 2 or greater", WHITE_CREATURE_POWER_2_OR_MORE);
        add("creature with converted mana cost 3 or less", CREATURE_CONVERTED_3_OR_LESS);
        add("creature with converted mana cost 2 or less", CREATURE_CONVERTED_2_OR_LESS);
        add("creature with flying", CREATURE_WITH_FLYING);
        add("creature with flying or reach", CREATURE_WITH_FLYING_OR_REACH);
        add("blue or black creature with flying", BLUE_OR_BLACK_CREATURE_WITH_FLYING);
        add("creature without flying", CREATURE_WITHOUT_FLYING);
        add("creature without defender", CREATURE_WITHOUT_DEFENDER);
        add("creature without shadow", CREATURE_WITHOUT_SHADOW);
        add("creature with defender", CREATURE_WITH_DEFENDER);
        add("creature with a morph ability", CREATURE_WITH_MORPH_ABILITY);
        add("creature with horsemanship", CREATURE_WITH_HORSEMANSHIP);
        add("creature with islandwalk", CREATURE_WITH_ISLANDWALK);
        add("creature with power 1 or less", CREATURE_POWER_1_OR_LESS);
        add("creature with power or toughness 1 or less", CREATURE_POWER_OR_TOUGHNESS_1_OR_LESS);
        add("creature with power 2 or less", CREATURE_POWER_2_OR_LESS);
        add("creature with power 3 or less", CREATURE_POWER_3_OR_LESS);
        add("creature with power 2 or greater", CREATURE_POWER_2_OR_MORE);
        add("creature with power 3 or greater", CREATURE_POWER_3_OR_MORE);
        add("creature with power 4 or greater", CREATURE_POWER_4_OR_MORE);
        add("creature with power 4 or less", CREATURE_POWER_4_OR_LESS);
        add("creature with power 5 or greater", CREATURE_POWER_5_OR_MORE);
        add("creature with power greater than SN's power", CREATURE_POWER_GREATER_THAN_SN);
        add("creature with power less than SN's power", CREATURE_POWER_LESS_THAN_SN);
        add("creature with power less than or equal to SN's power", CREATURE_POWER_LESS_THAN_EQUAL_SN);
        add("creature an opponent controls with power less than SN's power", CREATURE_OPP_POWER_LESS_THAN_SN);
        add("creature with toughness 2 or less", CREATURE_TOUGHNESS_2_OR_LESS);
        add("creature with toughness 3 or less", CREATURE_TOUGHNESS_3_OR_LESS);
        add("creature with toughness 3 or greater", CREATURE_TOUGHNESS_3_OR_GREATER);
        add("creature with toughness 4 or greater", CREATURE_TOUGHNESS_4_OR_GREATER);
        add("creature with shadow", CREATURE_WITH_SHADOW);
        add("creature with a +1/+1 counter on it", CREATURE_PLUSONE_COUNTER);
        add("creature with a -1/-1 counter on it", CREATURE_MINSUONE_COUNTER);
        add("creature that has a -1/-1 counter on it", CREATURE_MINSUONE_COUNTER);
        add("creature with a level counter on it", CREATURE_LEVEL_COUNTER);
        add("creature that has a fate counter on it", CREATURE_FATE_COUNTER);
        add("creature with a counter on it", CREATURE_WITH_COUNTER);
        add("creature with another Aura attached to it", CREATURE_WITH_ANOTHER_AURA);
        add("creature that isn't enchanted", CREATURE_THAT_ISNT_ENCHANTED);
        add("creature with flanking", CREATURE_WITH_FLANKING);
        add("nontoken creature", NONTOKEN_CREATURE);
        add("Djinn or Efreet", DJINN_OR_EFREET);
        add("Faerie or Elf", FAERIE_OR_ELF);
        add("Knight or Soldier", KNIGHT_OR_SOLDIER);
        add("Elf or Soldier creature", ELF_OR_SOLDIER_CREATURE);
        add("nonattacking, nonblocking creature", NONATTACKING_NONBLOCKING_CREATURE);
        add("creature without flying or islandwalk", CREATURE_WITHOUT_FLYING_OR_ISLANDWALK);
        add("creature without flying or a planeswalker", CREATURE_WITHOUT_FLYING_OR_PLANESWALKER);
        add("creature token", CREATURE_TOKEN);
        add("serf token", SERF_TOKEN);
        add("skeleton token", SKELETON_TOKEN);
        add("nonsnow creature", NONSNOW_CREATURE);
        add("creature that is enchanted", CREATURE_ENCHANTED);
        add("creature that is equipped", CREATURE_EQUIPPED);
        add("nonenchantment creature", NONENCHANTMENT_CREATURE);
        add("nonenchantment permanent", NONENCHANTMENT_PERMANENT);
        add("creature that's a Barbarian, a Warrior, or a Berserker", BARBARIAN_WARRIOR_BERSERKER_CREATURE);
        add("multicolored creature", MULTICOLORED_CREATURE);
        add("creature that's one or more colors", MONO_OR_MULTICOLORED_CREATURE);
        add("unblocked creature", UNBLOCKED_ATTACKING_CREATURE);
        add("Cleric or Wizard creature", CLERIC_OR_WIZARD_CREATURE);
        add("creature that was dealt damage this turn", CREATURE_BEEN_DAMAGED);
        add("enchanted creature", ENCHANTED_CREATURE);
        add("enchanted land", ENCHANTED_LAND);
        add("enchanted permanent", ENCHANTED_PERMANENT);
        add("enchanted artifact", ENCHANTED_ARTIFACT);
        add("equipped creature", EQUIPPED_CREATURE);
        add("nonlegendary creature", NON_LEGENDARY_CREATURE);
        add("colorless creature", COLORLESS_CREATURE);
        add("Eldrazi Scion", ELDRAZI_SCION);
        add("unblocked attacking creature", UNBLOCKED_ATTACKING_CREATURE);
        add("non-lair land", NON_LAIR_LAND);
        add("nonsnow land", NONSNOW_LAND);
        add("land with a trap counter on it", TRAPPED_LAND);
        add("Caribou token", CARIBOU_TOKEN);
        add("permanent with fading", PERMANENT_WITH_FADING);
        add("green or white permanent", GREEN_OR_WHITE_PERMANENT);
        add("nontoken artifact", NONTOKEN_ARTIFACT);
        add("soldier or warrior", SOLDIER_OR_WARRIOR);
        add("forest or treefolk", FOREST_OR_TREEFOLK);
        add("snow Mountain", SNOW_MOUNTAIN);
        add("snow Swamp", SNOW_SWAMP);
        add("snow Island", SNOW_ISLAND);
        add("snow Plains", SNOW_PLAINS);
        add("snow Forest", SNOW_FOREST);
        add("legendary snake", LEGENDARY_SNAKE);
        add("red or green enchantment", RED_OR_GREEN_ENCHANTMENT);

        // <color|type|subtype> you control
        add("equipped creature you control", EQUIPPED_CREATURE_YOU_CONTROL);
        add("creature you control that share a color with it", CREATURE_YOU_CONTROL_SHARE_COLOR);

        // <color|type|subtype> you don't control
        add("spell you don't control", SPELL_YOU_DONT_CONTROL);

        // <color|type|subtype> permanent
        add("permanent", PERMANENT);
        add("untapped permanent", UNTAPPED_PERMANENT);
        add("permanent you own", PERMANENT_YOU_OWN);
        add("permanent you both own and control", PERMANENT_YOU_OWN_AND_CONTROL);
        add("noncreature permanent", NONCREATURE);
        add("spell or permanent", SPELL_OR_PERMANENT);
        add("nonland permanent", NONLAND_PERMANENT);
        add("nontoken permanent", NONTOKEN_PERMANENT);
        add("nontoken red permanent", NONTOKEN_RED_PERMANENT);
        add("nontoken white permanent", NONTOKEN_WHITE_PERMANENT);
        add("nonland permanent with converted mana cost 3 or less", NONLAND_PERMANENT_CMC_LEQ_3);
        add("black or red permanent", BLACK_OR_RED_PERMANENT);
        add("black or green permanent", BLACK_OR_GREEN_PERMANENT);
        add("blue or red permanent", BLUE_OR_RED_PERMANENT);
        add("green or blue permanent", GREEN_OR_BLUE_PERMANENT);
        add("white or black permanent", WHITE_OR_BLACK_PERMANENT);
        add("white or blue permanent", WHITE_OR_BLUE_PERMANENT);
        add("red or white permanent", RED_OR_WHITE_PERMANENT);
        add("multicolored permanent", MULTICOLORED_PERMANENT);
        add("nonwhite permanent", NONWHITE_PERMANENT);
        add("permanent that is enchanted", PERMANENT_ENCHANTED);
        add("enchantment or enchanted permanent", ENCHANTMENT_OR_ENCHANTED_PERMANENT);
        add("nonartifact permanent", NONARTIFACT_PERMANENT);

        // <color|type|subtype>
        add("token", TOKEN);
        add("creature you own", CREATURE_YOU_OWN);
        add("permanent you own or control", PERMANENT_YOU_OWN_OR_CONTROL);
        add("Insect, Rat, Spider, or Squirrel", INSECT_RAT_SPIDER_OR_SQUIRREL);
        add("Vampire, Werewolf, or Zombie", VAMPIRE_WEREWOLF_OR_ZOMBIE);
        add("basic land", BASIC_LAND);
        add("nonbasic land", NONBASIC_LAND);
        add("non-Swamp land", NON_SWAMP_LAND);
        add("snow land", SNOW_LAND);
        add("Forest or Plains", FOREST_OR_PLAINS);
        add("Plains or Island", PLAINS_OR_ISLAND);
        add("artifact or land", ARTIFACT_OR_LAND);
        add("artifact land", ARTIFACT_LAND);
        add("artifact or enchantment", ARTIFACT_OR_ENCHANTMENT);
        add("artifact or enchantment with converted mana cost 3 or less", ARTIFACT_OR_ENCHANTMENT_CMC_3_OR_LESS);
        add("artifact or enchantment with converted mana cost 4 or less", ARTIFACT_OR_ENCHANTMENT_CMC_4_OR_LESS);
        add("artifact, enchantment, or land", ARTIFACT_OR_ENCHANTMENT_OR_LAND);
        add("artifact, creature, or land", ARTIFACT_OR_CREATURE_OR_LAND);
        add("artifact, creature, or enchantment", ARTIFACT_OR_CREATURE_OR_ENCHANTMENT);
        add("artifact, creature, enchantment, or land", ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_OR_LAND);
        add("enchantment, tapped artifact, or tapped creature", ENCHANTMENT_OR_TAPPED_ARTIFACT_OR_CREATURE);
        add("enchantment or land", ENCHANTMENT_OR_LAND);
        add("enchanted creature or enchantment creature", ENCHANTED_OR_ENCHANTMENT_CREATURE);
        add("noncreature artifact", NONCREATURE_ARTIFACT);
        add("Spirit or enchantment", SPIRIT_OR_ENCHANTMENT);
        add("creature or enchantment", CREATURE_OR_ENCHANTMENT);
        add("creature or land", CREATURE_OR_LAND);
        add("creature or nonbasic land", CREATURE_OR_NONBASIC_LAND);
        add("creature or planeswalker", CREATURE_OR_PLANESWALKER);
        add("creature or player", CREATURE_OR_PLAYER);
        add("creature or vehicle", CREATURE_OR_VEHICLE);
        add("Sliver creature or player", SLIVER_CREATURE_OR_PLAYER);
        add("nontoken Elf", NONTOKEN_ELF);
        add("legendary Samurai", LEGENDARY_SAMURAI);
        add("creature with three or more level counters on it", CREATURE_AT_LEAST_3_LEVEL_COUNTERS);
        add("non-Aura enchantment", NON_AURA_ENCHANTMENT);
        add("Aura attached to a creature", AURA_ATTACHED_TO_CREATURE);
        add("wolf or werewolf", WOLF_OR_WEREWOLF);
        add("servo or thopter", SERVO_OR_THOPTER);
        add("human or an angel", HUMAN_OR_ANGEL);
        add("Goblin or Shaman", GOBLIN_OR_SHAMAN);
        add("Treefolk or Warrior", TREEFOLK_OR_WARRIOR);
        add("Vampire or Zombie", VAMPIRE_OR_ZOMBIE);
        add("Island or Swamp", ISLAND_OR_SWAMP);
        add("Scarecrow or Plains", SCARECROW_OR_PLAINS);
        add("Treefolk or Forest", FOREST_OR_TREEFOLK);

        // <color|type> spell
        add("spell", SPELL);
        add("spell an opponent controls", SPELL_YOU_DONT_CONTROL);
        add("spell or ability", SPELL_OR_ABILITY);
        add("spell or ability you control", SPELL_OR_ABILITY_YOU_CONTROL);
        add("spell or ability an opponent controls", SPELL_OR_ABILITY_OPPONENT_CONTROL);
        add("activated ability", ACTIVATED_ABILITY);
        add("activated or triggered ability", ACTIVATED_OR_TRIGGERED_ABILITY);
        add("activated or triggered ability you don't control", ACTIVATED_OR_TRIGGERED_ABILITY_OPP_CONTROL);
        add("spell, activated ability, or triggered ability", SPELL_OR_ABILITY);
        add("spell that targets a player", SPELL_THAT_TARGETS_PLAYER);
        add("spell that targets you", SPELL_THAT_TARGETS_YOU);
        add("instant or sorcery spell that targets you", INSTANT_OR_SORCERY_SPELL_THAT_TARGETS_YOU);
        add("spell that targets you or a permanent you control", SPELL_THAT_TARGETS_YOU_OR_PERMANENT_YOU_CONTROL);
        add("spell that targets a permanent you control", SPELL_THAT_TARGETS_PERMANENT_YOU_CONTROL);
        add("spell that targets a creature you control", SPELL_THAT_TARGETS_CREATURE_YOU_CONTROL);
        add("spell that targets a creature", SPELL_THAT_TARGETS_CREATURE);
        add("spell that targets an enchantment", SPELL_THAT_TARGETS_ENCHANTMENT);
        add("instant or Aura spell that targets a permanent you control", INSTANT_OR_AURA_THAT_TARGETS_PERMANENT_YOU_CONTROL);
        add("spell with {X} in its mana cost", SPELL_WITH_X_COST);
        add("noncreature spell", NONCREATURE_SPELL);
        add("nonartifact spell", NONARTIFACT_SPELL);
        add("artifact or enchantment spell", ARTIFACT_OR_ENCHANTMENT_SPELL);
        add("red or green spell", RED_OR_GREEN_SPELL);
        add("blue or black spell", BLUE_OR_BLACK_SPELL);
        add("green or white spell", GREEN_OR_WHITE_SPELL);
        add("blue, black, or red spell", BLUE_OR_BLACK_OR_RED_SPELL);
        add("white, blue, black, or red spell", WHITE_OR_BLUE_OR_BLACK_OR_RED_SPELL);
        add("nonblue spell", NONBLUE_SPELL);
        add("nonblack spell", NONBLACK_SPELL);
        add("non-Faerie spell", NONFAERIE_SPELL);
        add("blue spell during your turn", BLUE_SPELL_YOUR_TURN);
        add("blue or black spell during your turn", BLUE_OR_BLACK_SPELL_YOUR_TURN);
        add("blue instant spell", BLUE_INSTANT_SPELL);
        add("nonred spell", NONRED_SPELL);
        add("instant or sorcery spell", INSTANT_OR_SORCERY_SPELL);
        add("enchantment, instant, or sorcery spell", ENCHANTMENT_OR_INSTANT_OR_SORCERY_SPELL);
        add("white or blue instant or sorcery spell", WHITE_OR_BLUE_INSTANT_OR_SORCERY_SPELL);
        add("instant or sorcery spell you control", INSTANT_OR_SORCERY_SPELL_YOU_CONTROL);
        add("spell with converted mana cost 1", SPELL_WITH_CMC_EQ_1);
        add("spell with converted mana cost 2", SPELL_WITH_CMC_EQ_2);
        add("spell with converted mana cost 3 or less", SPELL_WITH_CMC_LEQ_3);
        add("spell with converted mana cost 4 or greater", SPELL_WITH_CMC_4_OR_GREATER);
        add("instant spell you control with converted mana cost 2 or less", INSTANT_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2);
        add("sorcery spell you control with converted mana cost 2 or less", SORCERY_SPELL_YOU_CONTROL_WITH_CMC_LEQ_2);
        add("creature spell with converted mana cost 6 or greater", CREATURE_SPELL_CMC_6_OR_MORE);
        add("creature spell with infect", CREATURE_SPELL_WITH_INFECT);
        add("green creature spell", GREEN_CREATURE_SPELL);
        add("blue creature spell", BLUE_CREATURE_SPELL);
        add("creature or Aura spell", CREATURE_OR_AURA_SPELL);
        add("creature or sorcery spell", CREATURE_OR_SORCERY_SPELL);
        add("Spirit or Arcane spell", SPIRIT_OR_ARCANE_SPELL);
        add("multicolored spell", MULTICOLORED_SPELL);
        add("colorless spell", COLORLESS_SPELL);
        add("colorless spell with converted mana cost 7 or greater", COLORLESS_SPELL_CMC_7_OR_MORE);
        add("creature spell with converted mana cost 3 or less", CREATURE_SPELL_CMC_3_OR_LESS);
        add("aura, equipment, or vehicle spell", AURA_EQUIPMENT_OR_VEHICLE_SPELL);

        // player
        add("opponent", OPPONENT);
        add("your opponents", OPPONENT);
        add("other player", OPPONENT);
        add("player", PLAYER);
        add("player who lost life this turn", PLAYER_LOST_LIFE);
        add("player that controls a creature", PLAYER_CONTROLS_CREATURE);
        add("defending player", DEFENDING_PLAYER);

        // using source
        add("you", YOU);
        add("SN", SN);
        add("it", SN);
        add("this permanent", SN);
        add("this creature", SN);
        add("creature blocking it", CREATURE_BLOCKING_SN);
        add("creature blocking SN", CREATURE_BLOCKING_SN);
        add("creature blocked by SN", CREATURE_BLOCKED_BY_SN);
        add("creature blocking or blocked by SN", CREATURE_BLOCKING_BLOCKED_BY_SN);
    }

    private static final String[] ENDING_WITH_S = new String[]{
        "controls",
        "less",
        "plains",
        "opponents",
        "graveyards",
        "colorless",
        "aurochs",
        "pegasus",
        "this",
        "toughness",
        "fungus",
        "homunculus",
        "is",
        "its",
        "has",
        "was",
        "colors",
        "targets",
        "locus",
        "counters",
    };

    private static final String PLURAL = "\\b(?!(" + String.join("|", ENDING_WITH_S) + ")\\b)([a-z]+)s\\b";

    public static String toSingular(final String arg) {
        final String[] parts = arg.toLowerCase(Locale.ENGLISH).split(" named ");
        parts[0] = parts[0]
            .replaceAll("\\bwerewolves\\b", "werewolf")
            .replaceAll("\\belves\\b", "elf")
            .replaceAll("\\ballies\\b", "ally")
            .replaceAll("\\bmercenaries\\b", "mercenary")
            .replaceAll(PLURAL, "$2");
        return String.join(" named ", parts)
            .replaceAll("\\band/or\\b", "or")
            .replaceAll("\\band(?! control)\\b", "or")
            .replaceAll("\\bthem\\b", "it")
            .replaceAll("\\bin your hand\\b", "from your hand")
            .replaceAll("\\bin your graveyard\\b", "from your graveyard")
            .replaceAll("\\bin a graveyard\\b", "from a graveyard")
            .replaceAll("\\bin all graveyards\\b", "from a graveyard")
            .replaceAll("\\bfrom all graveyards\\b", "from a graveyard")
            .replaceAll("\\bplayed by your opponents\\b", "an opponent controls")
            .replaceAll("\\byour opponents control\\b", "an opponent controls")
            .replaceAll("\\byour opponents' graveyards\\b", "an opponent's graveyard")
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
        final MagicTargetFilter<MagicPermanent> filter = (MagicPermanentFilterImpl)single(processed);
        if (filter.acceptType(MagicTargetType.Permanent) == false) {
            throw new RuntimeException("unknown permanent filter \"" + text + "\"");
        }
        return other ? new MagicOtherPermanentTargetFilter(filter) : filter;
    }

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicCard> Card(final String text) {
        final String arg = toSingular(text);
        return (MagicCardFilterImpl)single(arg);
    }

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicPlayer> Player(final String text) {
        final String arg = toSingular(text);
        return (MagicPlayerFilterImpl)single(arg);
    }

    @SuppressWarnings("unchecked")
    public static MagicTargetFilter<MagicItemOnStack> ItemOnStack(final String text) {
        final String arg = toSingular(text);
        return (MagicStackFilterImpl)single(arg);
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

    public static MagicCardFilterImpl matchCardPrefix(final String arg, final String prefix, final MagicTargetType location) {
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
        final String withSuffix = prefix + " card";
        if (partial.containsKey(withSuffix)) {
            return partial.get(withSuffix).from(location);
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
        final String withSuffix = prefix + " creature card";
        if (partial.containsKey(withSuffix)) {
            return partial.get(withSuffix).from(location);
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
        final String withSuffix = prefix + " permanent";
        if (single.containsKey(withSuffix)) {
            @SuppressWarnings("unchecked")
            final MagicTargetFilter<MagicPermanent> filter = (MagicPermanentFilterImpl)single.get(withSuffix);
            return permanent(filter, control);
        }
        if (single.containsKey(prefix)) {
            @SuppressWarnings("unchecked")
            final MagicTargetFilter<MagicPermanent> filter = (MagicPermanentFilterImpl)single.get(prefix);
            return permanent(filter, control);
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
        final String withSuffix = prefix + " creature";
        if (single.containsKey(withSuffix)) {
            @SuppressWarnings("unchecked")
            final MagicTargetFilter<MagicPermanent> filter = (MagicPermanentFilterImpl)single.get(withSuffix);
            return permanent(filter, control);
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    public static MagicTargetFilter<MagicPermanent> matchPlaneswalkerPrefix(final String arg, final String prefix, final Control control) {
        for (final MagicColor c : MagicColor.values()) {
            if (prefix.equalsIgnoreCase(c.getName())) {
                return planeswalker(c, control);
            }
        }
        for (final MagicSubType st : MagicSubType.values()) {
            if (prefix.equalsIgnoreCase(st.toString())) {
                return planeswalker(st, control);
            }
        }
        final String withSuffix = prefix + " planeswalker";
        if (single.containsKey(withSuffix)) {
            @SuppressWarnings("unchecked")
            final MagicTargetFilter<MagicPermanent> filter = (MagicPermanentFilterImpl)single.get(withSuffix);
            return permanent(filter, control);
        }
        throw new RuntimeException("unknown target filter \"" + arg + "\"");
    }

    public enum Control {
        Any,
        You,
        Opp,
        Def;

        boolean matches(final MagicPlayer player, final MagicPermanent target) {
            switch (this) {
                case Any:
                    return true;
                case You:
                    return target.isController(player);
                case Opp:
                    return target.isOpponent(player);
                case Def:
                    return target.isController(target.getGame().getDefendingPlayer());
            }
            return false;
        }
    }

    enum Own {
        Any,
        You,
        Opp
    }

    public static final MagicPermanentFilterImpl untapped(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isUntapped() && filter.accept(source, player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl tapped(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isTapped() && filter.accept(source, player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) && control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl nonTokenPermanentName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) && !target.isToken() &&
                    control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentNotName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) == false &&
                    control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) &&
                    target.isCreature() &&
                    control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl landName(final String name, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isName(name) &&
                    target.isLand() && control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanent(final MagicType type, final Control control) {
        return permanentOr(type, type, control);
    }

    public static final MagicPermanentFilterImpl permanent(final MagicType type, final Own own) {
        return permanentOr(type, type, own);
    }

    public static final MagicPermanentFilterImpl permanent(final MagicPermanentState state, final MagicType type, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.hasType(type) &&
                    target.hasState(state) &&
                    control.matches(player,target);
            }
        };
    }

    private static MagicPermanentFilterImpl permanentOr(final MagicType type1, final MagicType type2, final Own own) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.hasType(type1) && target.hasType(type2) &&
                    ((own == Own.You && target.isOwner(player)) ||
                        (own == Own.Opp && target.isOwner(player.getOpponent())) ||
                        (own == Own.Any));
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentAnd(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.hasType(type1) && target.hasType(type2) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentAnd(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.hasType(type) && target.hasSubType(subType) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return (target.hasType(type1) || target.hasType(type2)) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicSubType subType1, final MagicSubType subType2, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return (target.hasSubType(subType1) || target.hasSubType(subType2)) &&
                   control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return (target.hasType(type) || target.hasSubType(subType)) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return (target.hasColor(color1) || target.hasColor(color2)) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanent(final MagicColor color, final Control control) {
        return permanentOr(color, color, control);
    }

    public static final MagicPermanentFilterImpl permanent(final MagicSubType subType, final Control control) {
        return permanentOr(subType, subType, control);
    }

    public static final MagicPermanentFilterImpl permanent(final MagicTargetFilter<MagicPermanent> filter, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return filter.accept(source, player, target) && control.matches(player, target);
            }
            @Override
            public boolean isStatic() {
                return filter.isStatic();
            }
        };
    }

    public static final MagicPermanentFilterImpl creature(final MagicCounterType counter, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    target.hasCounters(counter) &&
                   control.matches(player,target);
            }
        };
    }

    ;

    public static final MagicPermanentFilterImpl creature(final MagicColor color, final Control control) {
        return creatureOr(color, color, control);
    }

    public static final MagicPermanentFilterImpl creature(final MagicType type, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    target.hasType(type) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creature(final MagicSubType subtype, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    target.hasSubType(subtype) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureNon(final MagicSubType subtype, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    (target.hasSubType(subtype) == false) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureOr(final MagicColor color1, final MagicColor color2, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    (target.hasColor(color1) || target.hasColor(color2)) &&
                   control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureOr(final MagicSubType subType1, final MagicSubType subType2, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    (target.hasSubType(subType1) || target.hasSubType(subType2)) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureOr(final MagicPermanentState state1, final MagicPermanentState state2, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    (target.hasState(state1) || target.hasState(state2)) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureAnd(final MagicPermanentState state, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    (target.hasState(state) && target.hasSubType(subType)) &&
                    control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creatureAnd(final MagicType type, final MagicSubType subType, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    target.hasType(type) && target.hasSubType(subType) &&
                    control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creature(final MagicAbility ability, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isCreature() &&
                    target.hasAbility(ability) &&
                    control.matches(player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl creature(final MagicPermanentState state, final Control control) {
        return permanent(state, MagicType.Creature, control);
    }

    public static final MagicPermanentFilterImpl permanent(final MagicPermanentState state, final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.hasState(state) && filter.accept(source, player, target);
            }
        };
    }

    public static final MagicPermanentFilterImpl planeswalker(final MagicSubType subtype, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isPlaneswalker() &&
                    target.hasSubType(subtype) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl planeswalker(final MagicColor color, final Control control) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return target.isPlaneswalker() &&
                    target.hasColor(color) &&
                    control.matches(player,target);
            }
        };
    }

    public static final MagicPermanentFilterImpl permanentOr(final MagicPermanentState state1, final MagicPermanentState state2, final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
                return (target.hasState(state1) || target.hasState(state2)) && filter.accept(source, player, target);
            }
        };
    }

    public static final MagicCardFilterImpl card() {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return true;
            }
        };
    }

    public static final MagicCardFilterImpl card(final MagicType type) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.hasType(type);
            }
        };
    }

    public static final MagicCardFilterImpl card(final MagicSubType subType) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.hasSubType(subType);
            }
        };
    }

    public static final MagicCardFilterImpl card(final MagicColor color) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.hasColor(color);
            }
        };
    }

    public static final MagicCardFilterImpl cardName(final String name) {
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicCard target) {
                return target.getName().equalsIgnoreCase(name);
            }
        };
    }


    public static final MagicStackFilterImpl spell(final MagicColor color) {
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() && itemOnStack.hasColor(color);
            }
        };
    }

    public static final MagicStackFilterImpl spell(final MagicType type) {
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type);
            }
        };
    }

    public static final MagicStackFilterImpl spell(final MagicSubType subType) {
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(subType);
            }
        };
    }

    public static final MagicStackFilterImpl spellOr(final MagicType type1, final MagicType type2) {
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type1) || itemOnStack.isSpell(type2);
            }
        };
    }

    public static final MagicStackFilterImpl spellOr(final MagicType type1, final MagicType type2, final Control control) {
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
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
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(type) || itemOnStack.isSpell(subType);
            }
        };
    }

    public static final MagicStackFilterImpl spellOr(final MagicSubType subType1, final MagicSubType subType2) {
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell(subType1) || itemOnStack.isSpell(subType2);
            }
        };
    }

    public static final MagicStackFilterImpl spellOr(final MagicColor color1, final MagicColor color2) {
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack itemOnStack) {
                return itemOnStack.isSpell() &&
                    (itemOnStack.hasColor(color1) || itemOnStack.hasColor(color2));
            }
        };
    }

}

package magic.model.condition;

import java.util.LinkedList;
import java.util.List;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.PlayAbilityAction;
import magic.model.event.MagicConditionEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicMatchedCostEvent;
import magic.model.phase.MagicPhaseType;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

public abstract class MagicCondition implements MagicMatchedCostEvent {

    private static final MagicEventAction PLAY_ABILITY_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new PlayAbilityAction(event.getPermanent()));

    public static List<MagicMatchedCostEvent> build(final String costs) {
        final List<MagicMatchedCostEvent> matched = new LinkedList<MagicMatchedCostEvent>();
        final String[] splitCosts = costs.split("(,)? and ");
        for (String cost : splitCosts) {
            matched.add(MagicConditionParser.build(cost));
        }
        return matched;
    }

    public abstract boolean accept(final MagicSource source);

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicConditionEvent(source, this, getEventAction());
    }

    public MagicEventAction getEventAction() {
        return MagicEventAction.NONE;
    }

    @Override
    public boolean isIndependent() {
        return true;
    }

    public static MagicCondition NONE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return true;
        }
    };

    public static MagicCondition CARD_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicCard card = (MagicCard)source;
            final MagicCardDefinition cardDefinition = card.getCardDefinition();
            final MagicGame game = source.getGame();
            if (cardDefinition.hasType(MagicType.Instant) || cardDefinition.hasAbility(MagicAbility.Flash)) {
                return true;
            } else if (cardDefinition.hasType(MagicType.Land)) {
                return game.canPlayLand(card.getOwner());
            } else {
                return game.canPlaySorcery(card.getOwner());
            }
        }
    };

    public static MagicCondition HAND_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicCard card = (MagicCard)source;
            return card.isInHand();
        }
    };

    public static MagicCondition GRAVEYARD_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicCard card = (MagicCard)source;
            return card.isInGraveyard();
        }
    };

    public static MagicCondition NINJUTSU_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.DeclareBlockers) ||
                game.isPhase(MagicPhaseType.CombatDamage) ||
                game.isPhase(MagicPhaseType.EndOfCombat);
        }
    };

    public static MagicCondition NOT_SORCERY_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.canPlaySorcery(source.getController()) == false;
        }
    };

    public static MagicCondition NOT_MONSTROUS_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.Monstrous) == false;
        }
    };

    public static MagicCondition NOT_EXCLUDE_COMBAT_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.ExcludeFromCombat) == false;
        }
    };

    public static MagicCondition SORCERY_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.canPlaySorcery(source.getController());
        }
    };

    public static MagicCondition BEFORE_YOUR_ATTACK_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.isFriend(game.getTurnPlayer()) &&
                   game.getPhase().getType().ordinal() < MagicPhaseType.DeclareAttackers.ordinal();
        }
    };

    public static MagicCondition DECLARE_ATTACKERS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.DeclareAttackers);
        }
    };

    public static MagicCondition YOUR_UPKEEP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.Upkeep) && source.isFriend(game.getTurnPlayer());
        }
    };

    public static MagicCondition ANY_UPKEEP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.Upkeep);
        }
    };

    public static MagicCondition OPPONENTS_UPKEEP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.Upkeep) && source.isEnemy(game.getTurnPlayer());
        }
    };

    public static MagicCondition YOUR_TURN_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.isFriend(game.getTurnPlayer());
        }
    };

    public static MagicCondition BEFORE_ATTACKERS = new MagicCondition() {
        @Override
        public boolean accept(MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getPhase().getType().ordinal() < MagicPhaseType.DeclareAttackers.ordinal();
        }
    };

    public static MagicCondition BEFORE_BLOCKERS = new MagicCondition() {
        @Override
        public boolean accept(MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getPhase().getType().ordinal() < MagicPhaseType.DeclareBlockers.ordinal();
        }
    };

    public static MagicCondition BEFORE_COMBAT_DAMAGE = new MagicCondition() {
        @Override
        public boolean accept(MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getPhase().getType().ordinal() < MagicPhaseType.CombatDamage.ordinal();
        }
    };

    public static MagicCondition BEFORE_END_OF_COMBAT = new MagicCondition() {
        @Override
        public boolean accept(MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getPhase().getType().ordinal() < MagicPhaseType.EndOfCombat.ordinal();
        }
    };

    public static MagicCondition BEEN_ATTACKED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.isEnemy(game.getTurnPlayer()) &&
                game.getTurnPlayer().getNrOfAttackers() > 0;
        }
    };

    public static MagicCondition END_OF_COMBAT_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.EndOfCombat);
        }
    };

    public static MagicCondition DURING_COMBAT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getPhase().getType().ordinal() >= MagicPhaseType.BeginOfCombat.ordinal() &&
                   game.getPhase().getType().ordinal() <= MagicPhaseType.EndOfCombat.ordinal();
        }
    };

    public static MagicCondition DURING_COMBAT_AFTER_BLOCKERS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.DeclareBlockers) ||
                game.isPhase(MagicPhaseType.EndOfCombat);
        }
    };

    public static MagicCondition DURING_BLOCKERS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.DeclareBlockers);
        }
    };

    public static MagicCondition DURING_COMBAT_BEFORE_BLOCKERS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getPhase().getType().ordinal() >= MagicPhaseType.BeginOfCombat.ordinal() &&
                   game.getPhase().getType().ordinal() < MagicPhaseType.DeclareBlockers.ordinal();
        }
    };

    public static MagicCondition AFTER_COMBAT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getPhase().getType().ordinal() > MagicPhaseType.EndOfCombat.ordinal();
        }
    };

    public static MagicCondition CAN_TAP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.canTap();
        }
    };

    public static MagicCondition CAN_UNTAP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.canUntap();
        }
    };

    public static MagicCondition TAPPED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isTapped();
        }
    };

    public static MagicCondition UNTAPPED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isUntapped();
        }
    };

    public static MagicCondition IS_BLOCKED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isBlocked();
        }
    };

    public static MagicCondition IS_ATTACKING_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isAttacking();
        }
    };

    public static MagicCondition IS_ATTACKING_ALONE_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isAttacking() && source.getController().getNrOfAttackers() == 1;
        }
    };

    public static MagicCondition ABILITY_ONCE_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getAbilityPlayedThisTurn() < 1;
        }

        @Override
        public MagicEventAction getEventAction() {
            return PLAY_ABILITY_ACTION;
        }

        @Override
        public boolean isIndependent() {
            return false;
        }
    };

    public static MagicCondition ABILITY_TWICE_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getAbilityPlayedThisTurn() < 2;
        }

        @Override
        public MagicEventAction getEventAction() {
            return PLAY_ABILITY_ACTION;
        }

        @Override
        public boolean isIndependent() {
            return false;
        }
    };

    public static MagicCondition ABILITY_THRICE_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getAbilityPlayedThisTurn() < 3;
        }

        @Override
        public MagicEventAction getEventAction() {
            return PLAY_ABILITY_ACTION;
        }

        @Override
        public boolean isIndependent() {
            return false;
        }
    };

    public static MagicCondition NOT_CREATURE_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return !source.isCreaturePermanent();
        }
    };

    public static MagicCondition METALCRAFT_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicType.Artifact) >= 3;
        }
    };

    public static MagicCondition CAN_REGENERATE_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.canRegenerate();
        }
    };

    public static MagicCondition THREE_ATTACKERS_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfAttackers() >= 3;
        }
    };

    public static MagicCondition PLAINS_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Plains);
        }
    };

    public static MagicCondition ISLAND_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Island);
        }
    };

    public static MagicCondition SWAMP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Swamp);
        }
    };

    public static MagicCondition MOUNTAIN_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Mountain);
        }
    };

    public static MagicCondition FOREST_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Forest);
        }
    };

    public static MagicCondition LEAST_FIVE_OTHER_MOUNTAINS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            final MagicOtherPermanentTargetFilter filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.MOUNTAIN_YOU_CONTROL,
                permanent
            );
            return permanent.getController().getNrOfPermanents(filter) >= 5;
        }
    };

    public static MagicCondition THRESHOLD_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getGraveyard().size() >= 7;
        }
    };

    public static MagicCondition DELIRIUM_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicCardList graveyard = source.getController().getGraveyard();
            int count = 0;
            for (MagicType type : MagicType.ALL_CARD_TYPES) {
                if (graveyard.containsType(type)) {
                    count++;
                }
            }
            return count >= 4;
        }
    };

    public static MagicCondition FATEFUL_HOUR = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getLife() <= 5;
        }
    };

    public static MagicCondition HELLBENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getHandSize() == 0;
        }
    };

    public static MagicCondition OPPONENT_HELLBENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getOpponent().getHandSize() == 0;
        }
    };

    public static MagicCondition ANY_HELLBENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getOpponent().getHandSize() == 0 || source.getController().getHandSize() == 0;
        }
    };

    public static MagicCondition ENCHANTED_IS_UNTAPPED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getEnchantedPermanent().isUntapped();
        }
    };

    public static MagicCondition ENCHANTED_IS_BASIC_MOUNTAIN = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent enchanted = ((MagicPermanent)source).getEnchantedPermanent();
            return enchanted.hasType(MagicType.Basic) && enchanted.hasSubType(MagicSubType.Mountain);
        }
    };

    public static MagicCondition HAS_EXILED_CARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getExiledCard().isValid();
        }
    };

    public static MagicCondition HAS_EXILED_CREATURE_CARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            final MagicCard card = permanent.getExiledCard();
            return card.isValid() && card.hasType(MagicType.Creature);
        }
    };

    public static MagicCondition HAS_EQUIPPED_CREATURE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getEquippedCreature().isCreature();
        }
    };

    public static MagicCondition IS_EQUIPPED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isEquipped();
        }
    };

    public static MagicCondition IS_ENCHANTED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isEnchanted();
        }
    };

    public static MagicCondition IS_ENCHANTMENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isEnchantment();
        }
    };

    public static MagicCondition IS_NOT_ENCHANTMENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return !permanent.isEnchantment();
        }
    };

    public static MagicCondition IS_MONSTROUS_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.Monstrous);
        }
    };

    public static MagicCondition IS_RENOWNED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.Renowned);
        }
    };

    public static MagicCondition EMPTY_GRAVEYARD_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getGraveyard().isEmpty();
        }
    };

    public static MagicCondition LIBRARY_HAS_20_OR_LESS_CARDS_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPlayer player = source.getController();
            return player.getLibrary().size() <= 20 || player.getOpponent().getLibrary().size() <= 20;
        }
    };

    public static MagicCondition OPP_GRAVEYARD_WITH_10_OR_MORE_CARDS_CONDTITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getOpponent().getGraveyard().size() >= 10;
        }
    };

    public static MagicCondition OPP_NOT_CONTROL_WHITE_OR_BLUE_CREATURE_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return !source.getOpponent().controlsPermanent(MagicTargetFilterFactory.WHITE_OR_BLUE_CREATURE);
        }
    };

    public static MagicCondition MOST_CARDS_IN_HAND_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPlayer player = source.getController();
            return player.getHandSize() > player.getOpponent().getHandSize();
        }
    };

    public static MagicCondition EXACTLY_SEVEN_CARDS_IN_HAND_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPlayer player = source.getController();
            return player.getHandSize() == 7;
        }
    };

    public static MagicCondition OPPONENT_TEN_OR_LESS_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getOpponent().getLife() <= 10;
        }
    };

    public static MagicCondition OPPONENT_WAS_DEALT_DAMAGE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getOpponent().hasState(MagicPlayerState.WasDealtDamage);
        }
    };

    public static MagicCondition OPPONENT_HAS_GREATER_OR_EQUAL_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getOpponent().getLife() >= source.getController().getLife();
        }
    };

    public static MagicCondition YOU_30_OR_MORE_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getController().getLife() >= 30;
        }
    };

    public static MagicCondition YOU_30_OR_MORE_OPPPONENT_10_OR_LESS_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return YOU_30_OR_MORE_LIFE.accept(source) &&
                OPPONENT_TEN_OR_LESS_LIFE.accept(source);
        }
    };

    public static MagicCondition HAS_WARRIOR_IN_GRAVEYARD = MagicConditionFactory.YouHaveAtLeast(MagicTargetFilterFactory.WARRIOR_CARD_FROM_GRAVEYARD, 1);

    public static MagicCondition HAS_ARTIFACT_IN_GRAVEYARD = MagicConditionFactory.YouHaveAtLeast(MagicTargetFilterFactory.ARTIFACT_CARD_FROM_GRAVEYARD, 1);

    public static MagicCondition OPP_NOT_CONTROL_CREATURE_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return !source.getOpponent().controlsPermanent(MagicType.Creature);
        }
    };

    public static MagicCondition NOT_YOUR_TURN_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.isEnemy(game.getTurnPlayer());
        }
    };

    public static MagicCondition FACE_DOWN_PERMANENT_CONDITION = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isFaceDown();
        }
    };

    public static MagicCondition NO_SPELLS_CAST_LAST_TURN = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getSpellsCastLastTurn() == 0;
        }
    };

    public static MagicCondition TWO_OR_MORE_SPELLS_CAST_BY_PLAYER_LAST_TURN = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return source.getController().getSpellsCastLastTurn() >= 2 || source.getOpponent().getSpellsCastLastTurn() >= 2;
        }
    };

    public static MagicCondition MORE_CREATURES_THAN_DEFENDING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Creature) >
                game.getDefendingPlayer().getNrOfPermanents(MagicType.Creature);
        }
    };

    public static MagicCondition MORE_CREATURES_THAN_ATTACKING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Creature) >
                game.getAttackingPlayer().getNrOfPermanents(MagicType.Creature);
        }
    };

    public static MagicCondition MORE_LANDS_THAN_DEFENDING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Land) >
                game.getDefendingPlayer().getNrOfPermanents(MagicType.Land);
        }
    };

    public static MagicCondition MORE_LANDS_THAN_ATTACKING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Land) >
                game.getAttackingPlayer().getNrOfPermanents(MagicType.Land);
        }
    };

    public static MagicCondition OPP_MORE_LANDS = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return source.getOpponent().getNrOfPermanents(MagicType.Land) >
                source.getController().getNrOfPermanents(MagicType.Land);
        }
    };

    public static MagicCondition CREATURE_DIED_THIS_TURN = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getCreatureDiedThisTurn();
        }
    };

    public static MagicCondition YOU_ATTACKED_WITH_CREATURE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPlayer player = source.getController();
            return player.getCreaturesAttackedThisTurn() >= 1;
        }
    };

    public static MagicCondition CREATURE_IN_A_GRAVEYARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPlayer player = source.getController();
            return MagicTargetFilterFactory.CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(player).size() > 0;
        }
    };

    public static MagicCondition HAS_CREATURE_IN_GRAVEYARD = MagicConditionFactory.YouHaveAtLeast(MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD, 1);

    public static MagicCondition HAS_CREATURE_IN_HAND = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPlayer player = source.getController();
            return MagicTargetFilterFactory.CREATURE_CARD_FROM_HAND.filter(player).size() > 0;
        }
    };

    public static MagicCondition HAS_EQUIPMENT_IN_HAND = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPlayer player = source.getController();
            return MagicTargetFilterFactory.EQUIPMENT_CARD_FROM_HAND.filter(player).size() > 0;
        }
    };

    public static MagicCondition INSTANT_OR_SORCERY_IN_A_GRAVEYARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPlayer player = source.getController();
            return !MagicTargetFilterFactory.INSTANT_OR_SORCERY_CARD_FROM_ALL_GRAVEYARDS.filter(player).isEmpty();
        }
    };

    public static MagicCondition SPELL_MASTERY_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPlayer player = source.getController();
            return MagicTargetFilterFactory.INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD.filter(player).size() >= 2;
        }
    };

    public static MagicCondition FORMIDABLE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final List<MagicPermanent> creatures = MagicTargetFilterFactory.CREATURE_YOU_CONTROL.filter(source.getController());
            int totalPower = 0;
            for (final MagicPermanent creature : creatures) {
                totalPower += creature.getPowerValue();
            }
            return totalPower >= 8;
        }
    };

    public static MagicCondition CAST_FROM_HAND = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.CastFromHand);
        }
    };

    public static MagicCondition WAS_NONCREATURE_ARTIFACT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getCardDefinition().hasType(MagicType.Artifact) &&
                !permanent.getCardDefinition().hasType(MagicType.Creature);
        }
    };

    public static MagicCondition WAS_KICKED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicCardOnStack spell = (MagicCardOnStack)source;
            return spell.isKicked();
        }
    };

    public static MagicCondition CAST_ANOTHER_SPELL_THIS_TURN = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getSpellsCast() > 0;
        }
    };

    public static MagicCondition DEFENDING_POISONED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getGame().getDefendingPlayer().getPoison() > 0;
        }
    };

    public static MagicCondition CONTROL_SINCE_LAST_TURN = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return !permanent.hasState(MagicPermanentState.Summoned);
        }
    };
}

package magic.model.condition;

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
import magic.model.phase.MagicPhaseType;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

import java.util.List;

public interface MagicCondition {

    boolean accept(final MagicSource source);

    MagicCondition NONE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return true;
        }
    };

    MagicCondition CARD_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicCard card=(MagicCard)source;
            final MagicCardDefinition cardDefinition=card.getCardDefinition();
            final MagicGame game = source.getGame();
            if (cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasAbility(MagicAbility.Flash)) {
                return true;
            } else if (cardDefinition.hasType(MagicType.Land)) {
                return game.canPlayLand(card.getOwner());
            } else {
                return game.canPlaySorcery(card.getOwner());
            }
        }
    };
    
    MagicCondition HAND_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicCard card = (MagicCard)source;
            return card.isInHand();
        }
    };

    MagicCondition GRAVEYARD_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
            final MagicCard card = (MagicCard)source;
            return card.isInGraveyard();
        }
    };
    
    MagicCondition NINJUTSU_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.DeclareBlockers) ||
                   game.isPhase(MagicPhaseType.CombatDamage) ||
                   game.isPhase(MagicPhaseType.EndOfCombat);
        }
    };
    
    MagicCondition NOT_SORCERY_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.canPlaySorcery(source.getController()) == false;
        }
    };
    
    MagicCondition NOT_MONSTROUS_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.Monstrous) == false;
        }
    };
    
    MagicCondition NOT_EXCLUDE_COMBAT_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.ExcludeFromCombat) == false;
        }
    };

    MagicCondition SORCERY_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.canPlaySorcery(source.getController());
        }
    };
    
    MagicCondition BEFORE_YOUR_ATTACK_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getTurnPlayer() == source.getController() &&
                   game.getPhase().getType().ordinal() < MagicPhaseType.DeclareAttackers.ordinal();
        }
    };
    
    MagicCondition DECLARE_ATTACKERS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.DeclareAttackers);
        }
    };

    MagicCondition YOUR_UPKEEP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.Upkeep) &&
                   game.getTurnPlayer() == source.getController();
        }
    };
    
    MagicCondition OPPONENTS_UPKEEP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.Upkeep) &&
                   game.getTurnPlayer() != source.getController();
        }
    };

    MagicCondition YOUR_TURN_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController() == game.getTurnPlayer();
        }
    };
    
    MagicCondition BEEN_ATTACKED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.isEnemy(game.getTurnPlayer()) &&
                   game.getTurnPlayer().getNrOfAttackers() > 0;
        }
    };

    MagicCondition END_OF_COMBAT_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.EndOfCombat);
        }
    };
    
    MagicCondition DURING_COMBAT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return (game.isPhase(MagicPhaseType.BeginOfCombat) ||
                   game.isPhase(MagicPhaseType.DeclareAttackers) ||
                   game.isPhase(MagicPhaseType.DeclareBlockers) ||
                   game.isPhase(MagicPhaseType.EndOfCombat));
        }
    };

    MagicCondition CAN_TAP_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.canTap();
        }
    };

    MagicCondition CAN_UNTAP_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.canUntap();
        }
    };

    MagicCondition TAPPED_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.isTapped();
        }
    };

    MagicCondition UNTAPPED_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.isUntapped();
        }
    };
    
    MagicCondition IS_BLOCKED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isBlocked();
        }
    };
    
    MagicCondition IS_ATTACKING_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isAttacking();
        }
    };
    
    MagicCondition IS_ATTACKING_ALONE_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isAttacking() && source.getController().getNrOfAttackers()==1;
        }
    };

    MagicCondition ABILITY_ONCE_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.getAbilityPlayedThisTurn() < 1;
        }
    };
    
    MagicCondition ABILITY_TWICE_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.getAbilityPlayedThisTurn() < 2;
        }
    };
    
    MagicCondition ABILITY_THRICE_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.getAbilityPlayedThisTurn() < 3;
        }
    };

    MagicCondition NOT_CREATURE_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return !source.isCreature();
        }
    };

    MagicCondition THREE_BLACK_CREATURES_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicTargetFilterFactory.BLACK_CREATURE_YOU_CONTROL)>=3;
        }
    };
    
    MagicCondition METALCRAFT_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicType.Artifact)>=3;
        }
    };

    MagicCondition CAN_REGENERATE_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.canRegenerate();
        }
    };

    MagicCondition THREE_ATTACKERS_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfAttackers() >= 3;
        }
    };

    MagicCondition BASIC_LAND_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicTargetFilterFactory.BASIC_LAND_YOU_CONTROL)>=1;
        }
    };
    
    MagicCondition PLAINS_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Plains);
        }
    };
    
    MagicCondition ISLAND_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Island);
        }
    };
    
    MagicCondition SWAMP_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Swamp);
        }
    };
    
    MagicCondition MOUNTAIN_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Mountain);
        }
    };
    
    MagicCondition FOREST_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().controlsPermanent(MagicSubType.Forest);
        }
    };
    
    MagicCondition TWO_MOUNTAINS_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicSubType.Mountain)>=2;
        }
    };

    MagicCondition LEAST_FIVE_OTHER_MOUNTAINS=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            final MagicOtherPermanentTargetFilter filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.MOUNTAIN_YOU_CONTROL,
                permanent
            );
            return permanent.getController().getNrOfPermanents(filter) >= 5;
        }
    };

    MagicCondition TWO_OR_MORE_WHITE_PERMANENTS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicColor.White)>= 2;
        }
    };
    
    MagicCondition TWO_OR_MORE_BLUE_PERMANENTS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
           return source.getController().getNrOfPermanents(MagicColor.Blue)>=2;
        }
    };
    
    MagicCondition TWO_OR_MORE_BLACK_PERMANENTS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
           return source.getController().getNrOfPermanents(MagicColor.Black)>=2;
        }
    };
    
    MagicCondition TWO_OR_MORE_RED_PERMANENTS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
           return source.getController().getNrOfPermanents(MagicColor.Red)>=2;
        }
    };
    
    MagicCondition TWO_OR_MORE_GREEN_PERMANENTS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
           return source.getController().getNrOfPermanents(MagicColor.Green)>=2;
        }
    };
    
    MagicCondition EIGHT_OR_MORE_LANDS = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicType.Land) >= 8;
        }
    };
    
    MagicCondition THRESHOLD_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getGraveyard().size() >= 7;
        }
    };

    MagicCondition FATEFUL_HOUR = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getLife() <= 5;
        }
    };

    MagicCondition HELLBENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getHandSize() == 0;
        }
    };
    
    MagicCondition OPPONENT_HELLBENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getOpponent().getHandSize() == 0;
        }
    };

    MagicCondition ENCHANTED_IS_UNTAPPED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getEnchantedPermanent().isUntapped();
        }
    };
    
    MagicCondition HAS_EXILED_CARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getExiledCard() != MagicCard.NONE;
        }
    };
    
    MagicCondition HAS_EXILED_CREATURE_CARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            final MagicCard card = permanent.getExiledCard(); 
            return card != MagicCard.NONE && card.hasType(MagicType.Creature);
        }
    };
    
    MagicCondition HAS_EQUIPPED_CREATURE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getEquippedCreature().isCreature();
        }
    };
    
    MagicCondition IS_EQUIPPED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isEquipped();
        }
    };
    
    MagicCondition IS_ENCHANTED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isEnchanted();
        }
    };

    MagicCondition IS_ENCHANTMENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isEnchantment();
        }
    };
    
    MagicCondition IS_SPIRIT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.hasSubType(MagicSubType.Spirit);
        }
    };
    
    MagicCondition IS_WARRIOR = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.hasSubType(MagicSubType.Warrior);
        }
    };
    
    MagicCondition NO_UNTAPPED_LANDS_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicTargetFilterFactory.UNTAPPED_LAND_YOU_CONTROL) == 0;
        }
    };

    MagicCondition IS_MONSTROUS_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.hasState(MagicPermanentState.Monstrous) == true;
        }
    };

    MagicCondition EMPTY_GRAVEYARD_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getGraveyard().size()==0;
        }
    };
    
    MagicCondition LIBRARY_HAS_20_OR_LESS_CARDS_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPlayer player=source.getController();
            return player.getLibrary().size()<=20 | player.getOpponent().getLibrary().size()<=20;
        }
    };
    
    MagicCondition OPP_GRAVEYARD_WITH_10_OR_MORE_CARDS_CONDTITION= new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getOpponent().getGraveyard().size()>=10;
        }
    };
    
    MagicCondition OPP_NOT_CONTROL_WHITE_OR_BLUE_CREATURE_CONDITION=new MagicCondition() {
        public boolean accept(MagicSource source) {
            return !source.getOpponent().controlsPermanent(MagicTargetFilterFactory.WHITE_OR_BLUE_CREATURE);
        }
    };

    MagicCondition MOST_CARDS_IN_HAND_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPlayer player = source.getController();
            return player.getHandSize() > player.getOpponent().getHandSize();
        }
    };
    
    MagicCondition EXACTLY_SEVEN_CARDS_IN_HAND_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPlayer player = source.getController();
            return player.getHandSize() == 7;
        }
    };

    MagicCondition NO_SHELL_COUNTERS_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent) source;
            return permanent.getCounters(MagicCounterType.Shell)==0;
        }
    };

    MagicCondition HAS_MINUSONE_COUNTER_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent) source;
            return permanent.hasCounters(MagicCounterType.MinusOne);
        }
    };
    
    MagicCondition HAS_PLUSONE_COUNTER_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent) source;
            return permanent.hasCounters(MagicCounterType.PlusOne);
        }
    };
    
    MagicCondition HAS_TEN_PLUSONE_COUNTER_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent) source;
            return permanent.getCounters(MagicCounterType.PlusOne) >= 10;
        }
    };
    
    MagicCondition OPPONENT_TEN_OR_LESS_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getOpponent().getLife() <= 10;
        }
    };
    
    MagicCondition OPPONENT_HAS_GREATER_OR_EQUAL_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getOpponent().getLife() >= source.getController().getLife();
        }
    };
    
    MagicCondition YOU_25_OR_MORE_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getController().getLife() >= 25;
        }
    };
    
    MagicCondition YOU_30_OR_MORE_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return source.getController().getLife() >= 30;
        }
    };
    
    MagicCondition YOU_30_OR_MORE_OPPPONENT_10_OR_LESS_LIFE = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return YOU_30_OR_MORE_LIFE.accept(source) &&
                   OPPONENT_TEN_OR_LESS_LIFE.accept(source);
        }
    };
    
    MagicCondition HAS_WARRIOR_IN_GRAVEYARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            final MagicPlayer player = source.getController();
            return game.filterCards(player, MagicTargetFilterFactory.WARRIOR_CARD_FROM_GRAVEYARD).size() > 0;
        }
    };

    MagicCondition HAS_ARTIFACT_IN_GRAVEYARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            final MagicPlayer player = source.getController();
            return game.filterCards(player, MagicTargetFilterFactory.ARTIFACT_CARD_FROM_GRAVEYARD).size() > 0;
        }
    };

    MagicCondition OPP_NOT_CONTROL_CREATURE_CONDITION = new MagicCondition() {
        public boolean accept(MagicSource source) {
            return !source.getOpponent().controlsPermanent(MagicType.Creature);
        }
    };

    MagicCondition NOT_YOUR_TURN_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController() != game.getTurnPlayer();
        }
    };
    
    MagicCondition NOT_CONTROL_NONARTIFACT_NONWHITE_CREATURE_CONDITION = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return !source.getController().controlsPermanent(MagicTargetFilterFactory.NONARTIFACT_NONWHITE_CREATURE);
        }
    };
    
    MagicCondition FACE_DOWN_PERMANENT_CONDITION = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent) source;
            return permanent.isFaceDown();
        }
    };
    
    MagicCondition NO_SPELLS_CAST_LAST_TURN = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getSpellsCastLastTurn() == 0;
        }
    };
    
    MagicCondition TWO_OR_MORE_SPELLS_CAST_BY_PLAYER_LAST_TURN = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return (source.getController().getSpellsCastLastTurn() >= 2) || (source.getOpponent().getSpellsCastLastTurn() >= 2);
        }
    };
    
    MagicCondition YOU_FIVE_OR_MORE_VAMPIRES = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicSubType.Vampire) >= 5;
        }
    };
    
    MagicCondition MOUNTAIN_ON_BATTLEFIELD = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return source.getGame().getNrOfPermanents(MagicSubType.Mountain) > 0;
        }
    };
    
    MagicCondition FIVE_OR_MORE_ISLANDS_ON_BATTLEFIELD = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return source.getGame().getNrOfPermanents(MagicSubType.Island) >= 5;
        }
    };
    
    MagicCondition MORE_CREATURES_THAN_DEFENDING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Creature) >
                   game.getDefendingPlayer().getNrOfPermanents(MagicType.Creature);
        }
    };
    
    MagicCondition MORE_CREATURES_THAN_ATTACKING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Creature) >
                   game.getAttackingPlayer().getNrOfPermanents(MagicType.Creature);
        }
    };

    MagicCondition MORE_LANDS_THAN_DEFENDING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Land) >
                   game.getDefendingPlayer().getNrOfPermanents(MagicType.Land);
        }
    };
    
    MagicCondition MORE_LANDS_THAN_ATTACKING = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return source.getController().getNrOfPermanents(MagicType.Land) >
                   game.getAttackingPlayer().getNrOfPermanents(MagicType.Land);
        }
    };
    
    MagicCondition FOUR_OR_MORE_SNOW_PERMANENTS = new MagicCondition() {
        @Override
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicType.Snow)>=4;
        }
    };
    
    MagicCondition CREATURE_DIED_THIS_TURN = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.getCreatureDiedThisTurn();
        }
    };

    MagicCondition YOU_ATTACKED_WITH_CREATURE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPlayer player = source.getController();
            return player.getCreaturesAttackedThisTurn() >= 1;
        }
    };
    
    MagicCondition CREATURE_IN_A_GRAVEYARD = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            final MagicPlayer player = source.getController();
            return game.filterCards(player, MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD).size() > 0 ||
                    game.filterCards(player,MagicTargetFilterFactory.CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD).size() >0;
        }
    };

    MagicCondition FORMIDABLE = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final List<MagicPermanent> creatures = source.getController().filterPermanents(MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            int totalPower = 0;
            for (final MagicPermanent creature: creatures) {
                totalPower += creature.getPowerValue();
            }
            return totalPower >= 8;
        }
    };
}

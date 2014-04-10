package magic.model.condition;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.phase.MagicPhaseType;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

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

    MagicCondition SORCERY_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.canPlaySorcery(source.getController());
        }
    };

    MagicCondition YOUR_UPKEEP_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.Upkeep) &&
                   game.getTurnPlayer() == source.getController();
        }
    };
    
    MagicCondition DECLARE_ATTACKERS_BEEN_ATTACKED = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.DeclareAttackers) &&
                   game.getTurnPlayer() == source.getController().getOpponent();
        }
    };

    MagicCondition END_OF_COMBAT_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicGame game = source.getGame();
            return game.isPhase(MagicPhaseType.EndOfCombat);
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

    MagicCondition IS_ATTACKING_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.isAttacking();
        }
    };

    MagicCondition ABILITY_ONCE_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent=(MagicPermanent)source;
            return permanent.getAbilityPlayedThisTurn()==0;
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
            return source.getController().getNrOfPermanents(MagicType.Basic)>=1;
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

    MagicCondition TWO_OR_MORE_WHITE_PERMANENTS=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicTargetFilterFactory.WHITE_PERMANENT_YOU_CONTROL)>= 2;
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
}

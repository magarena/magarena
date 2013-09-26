package magic.model.condition;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.phase.MagicPhaseType;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilter;

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

    MagicCondition TWO_MOUNTAINS_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicSubType.Mountain)>=2;
        }
    };

    MagicCondition OPP_FOUR_LANDS_CONDITION=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getOpponent().getNrOfPermanents(MagicType.Land)>=4;
        }
    };

    MagicCondition LEAST_FIVE_OTHER_MOUNTAINS=new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            final MagicOtherPermanentTargetFilter filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_MOUNTAIN_YOU_CONTROL,
                permanent
            );
            return permanent.getController().getNrOfPermanents(filter) >= 5;
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

    MagicCondition POWER_4_OR_GREATER_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getPower() >= 4;
        }
    };

    MagicCondition ENCHANTED_IS_UNTAPPED_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicPermanent permanent = (MagicPermanent)source;
            return permanent.getEnchantedCreature().isUntapped();
        }
    };
    
    MagicCondition HAS_ALL_KALDRA_EQUIPMENT = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            final MagicTargetFilter<MagicPermanent> shield = new MagicTargetFilter.NameTargetFilter("Shield of Kaldra");
            final MagicTargetFilter<MagicPermanent> sword = new MagicTargetFilter.NameTargetFilter("Sword of Kaldra");
            final MagicTargetFilter<MagicPermanent> helm = new MagicTargetFilter.NameTargetFilter("Helm of Kaldra");
            final MagicPlayer player = source.getController();
            return player.controlsPermanent(shield) &&
                   player.controlsPermanent(sword) &&
                   player.controlsPermanent(helm);
        }
    };
}

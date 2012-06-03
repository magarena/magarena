package magic.model.condition;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.phase.MagicPhaseType;

public interface MagicCondition {
    
    MagicCondition NONE = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
            return true;
        }
    };

	MagicCondition CARD_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicCard card=(MagicCard)source;
			final MagicCardDefinition cardDefinition=card.getCardDefinition();
			if (cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasAbility(MagicAbility.Flash)) {
				return true;
			} if (cardDefinition.hasType(MagicType.Land)) {
				return game.canPlayLand(card.getOwner());
			} else {
				return game.canPlaySorcery(card.getOwner());
			}
		}
	};
	
	MagicCondition SORCERY_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return game.canPlaySorcery(source.getController());
		}
	};
	
	MagicCondition YOUR_UPKEEP_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return game.isPhase(MagicPhaseType.Upkeep) &&
					game.getTurnPlayer() == source.getController();
		}
	};
	
	MagicCondition END_OF_COMBAT_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return game.isPhase(MagicPhaseType.EndOfCombat);
		}
	};

	MagicCondition ONE_LIFE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getLife()>0;
		}
	};

	MagicCondition TWO_LIFE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getLife()>1;
		}
	};
	
	MagicCondition HAS_CARD_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getHandSize()>0;
		}
	};
	
	MagicCondition HAS_TWO_CARDS_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getHandSize() > 1;
		}
	};
	
	MagicCondition CAN_TAP_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.canTap();
		}
	};

	MagicCondition CAN_UNTAP_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.canUntap();
		}
	};
	
	MagicCondition TAPPED_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.isTapped();
		}
	};
	
	MagicCondition IS_ATTACKING_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent = (MagicPermanent)source;
			return permanent.isAttacking();
		}
	};
	
	MagicCondition ABILITY_ONCE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getAbilityPlayedThisTurn()==0;
		}
	};
	
    MagicCondition AI_EQUIP_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return !game.isArtificial() || 
                   !permanent.getEquippedCreature().isValid() ||
                   permanent.getAbilityPlayedThisTurn() < 2;
		}
	};
    
    MagicCondition NOT_CREATURE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
            return !source.isCreature();
		}
	};
    
	MagicCondition MINUS_COUNTER_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.MinusOne)>0;
		}
	};
	
	MagicCondition PLUS_COUNTER_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent = (MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.PlusOne) > 0;
		}
	};
	
	MagicCondition CHARGE_COUNTER_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge)>0;
		}
	};

	MagicCondition METALCRAFT_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Artifact,game)>=3;
		}
    };

	MagicCondition TWO_CHARGE_COUNTERS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge)>=2;
		}
	};
	
	MagicCondition THREE_CHARGE_COUNTERS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge)>=3;
		}
	};
	
	MagicCondition FOUR_CHARGE_COUNTERS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge)>=4;
		}
	};
	
	MagicCondition EIGHT_CHARGE_COUNTERS_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent = (MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge) >= 8;
		}
	};
	
	MagicCondition CAN_REGENERATE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.canRegenerate();
		}
	};
		
	MagicCondition CONTROL_BAT_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithSubType(MagicSubType.Bat);
		}
	};

	MagicCondition CONTROL_BEAST_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithSubType(MagicSubType.Beast);
		}
	};
	
	MagicCondition CONTROL_GOBLIN_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithSubType(MagicSubType.Goblin);
		}
	};
	
	MagicCondition CONTROL_ARTIFACT_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithType(MagicType.Artifact);
		}
	};
	
	MagicCondition CONTROL_GOLEM_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithSubType(MagicSubType.Golem);
		}
	};
	
    MagicCondition ONE_CREATURE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Creature,game)>=1;
		}
	};
	
	MagicCondition TWO_CREATURES_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Creature,game)>=2;
		}
	};
	
	MagicCondition TWO_MOUNTAINS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithSubType(MagicSubType.Mountain, game)>=2;
		}
	};

	MagicCondition OPP_FOUR_LANDS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return game.getOpponent(source.getController()).getNrOfPermanentsWithType(MagicType.Land,game)>=4;
		}
	};
	
	MagicCondition THRESHOLD_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getGraveyard().size() >= 7;
		}
	};
	
	MagicCondition POWER_4_OR_GREATER_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent = (MagicPermanent)source;
			return permanent.getPower() >= 4;
		}
	};
	
	MagicCondition ENCHANTED_IS_UNTAPPED_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent = (MagicPermanent)source;
			return permanent.getEnchantedCreature().isUntapped();
		}
	};
	
	MagicCondition GRAVEYARD_CONTAINS_CREATURE = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			for (MagicCard card : source.getController().getGraveyard()) {
				if (card.getCardDefinition().isCreature()) {
					return true;
				}
			}
			return false;
		}
	};
		
	boolean accept(final MagicGame game,final MagicSource source);
}

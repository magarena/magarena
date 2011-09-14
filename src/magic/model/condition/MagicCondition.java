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

public interface MagicCondition {

    MagicCondition[] NONE = new MagicCondition[0];

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
			return permanent.canTap(game);
		}
	};

	MagicCondition CAN_UNTAP_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.canUntap(game);
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
			return source.getController().getNrOfPermanentsWithType(MagicType.Artifact)>=3;
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
	
    MagicCondition ONE_CREATURE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Creature)>=1;
		}
	};
	
	MagicCondition TWO_CREATURES_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Creature)>=2;
		}
	};

	MagicCondition OPP_FOUR_LANDS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return game.getOpponent(source.getController()).getNrOfPermanentsWithType(MagicType.Land)>=4;
		}
	};
		
	boolean accept(final MagicGame game,final MagicSource source);
}

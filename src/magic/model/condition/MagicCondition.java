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

    public static final MagicCondition[] NONE = new MagicCondition[0];

	public static final MagicCondition CARD_CONDITION=new MagicCondition() {
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
	
	public static final MagicCondition SORCERY_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return game.canPlaySorcery(source.getController());
		}
	};

	public static final MagicCondition ONE_LIFE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getLife()>0;
		}
	};

	public static final MagicCondition TWO_LIFE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getLife()>1;
		}
	};
	
	public static final MagicCondition HAS_CARD_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getHandSize()>0;
		}
	};
	
	public static final MagicCondition HAS_TWO_CARDS_CONDITION = new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getHandSize() > 1;
		}
	};
	
	public static final MagicCondition CAN_TAP_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.canTap(game);
		}
	};

	public static final MagicCondition CAN_UNTAP_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.canUntap(game);
		}
	};
	
	public static final MagicCondition TAPPED_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.isTapped();
		}
	};
	
	public static final MagicCondition ABILITY_ONCE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getAbilityPlayedThisTurn()==0;
		}
	};
	
	public static final MagicCondition MINUS_COUNTER_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.MinusOne)>0;
		}
	};
	
	public static final MagicCondition CHARGE_COUNTER_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge)>0;
		}
	};

	public static final MagicCondition METALCRAFT_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Artifact)>=3;
		}
    };

	public static final MagicCondition TWO_CHARGE_COUNTERS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge)>=2;
		}
	};
	
	public static final MagicCondition THREE_CHARGE_COUNTERS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.getCounters(MagicCounterType.Charge)>=3;
		}
	};
	
	public static final MagicCondition CAN_REGENERATE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return permanent.canRegenerate();
		}
	};
		
	public static final MagicCondition CONTROL_BAT_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithSubType(MagicSubType.Bat);
		}
	};

	public static final MagicCondition CONTROL_BEAST_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithSubType(MagicSubType.Beast);
		}
	};
	
	public static final MagicCondition CONTROL_GOBLIN_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().controlsPermanentWithSubType(MagicSubType.Goblin);
		}
	};
	
    public static final MagicCondition ONE_CREATURE_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Creature)>=1;
		}
	};
	
	public static final MagicCondition TWO_CREATURES_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return source.getController().getNrOfPermanentsWithType(MagicType.Creature)>=2;
		}
	};

	public static final MagicCondition OPP_FOUR_LANDS_CONDITION=new MagicCondition() {
		public boolean accept(final MagicGame game,final MagicSource source) {
			return game.getOpponent(source.getController()).getNrOfPermanentsWithType(MagicType.Land)>=4;
		}
	};
		
	public boolean accept(final MagicGame game,final MagicSource source);
}

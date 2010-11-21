package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.stack.MagicCardOnStack;

public interface MagicTargetFilter {
	
	public static final MagicTargetFilter TARGET_SPELL=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.isSpell();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Stack;
		}
	};

	public static final MagicTargetFilter TARGET_CREATURE_SPELL=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			if (target.isSpell()) {
				final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
				final MagicCardDefinition card=cardOnStack.getCardDefinition();
				return card.isCreature();
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Stack;
		}
	};

	public static final MagicTargetFilter TARGET_NONCREATURE_SPELL=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			if (target.isSpell()) {
				final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
				final MagicCardDefinition card=cardOnStack.getCardDefinition();
				return !card.isCreature();
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Stack;
		}
	};
		
	public static final MagicTargetFilter TARGET_INSTANT_OR_SORCERY_SPELL=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			if (target.isSpell()) {
				final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
				final MagicCardDefinition card=cardOnStack.getCardDefinition();
				return card.hasType(MagicType.Instant)||card.hasType(MagicType.Sorcery);
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Stack;
		}
	};
	
	public static final MagicTargetFilter TARGET_PLAYER=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return true;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Player;
		}		
	};

	public static final MagicTargetFilter TARGET_OPPONENT=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target!=player;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Player;
		}
	};
	
	public static final MagicTargetFilter TARGET_SPELL_OR_PERMANENT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.isSpell()||target.isPermanent();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Stack||targetType==MagicTargetType.Permanent;
		}
	};
	
	public static final MagicTargetFilter TARGET_PERMANENT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return true;
		}

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	public static final MagicTargetFilter TARGET_NONLAND_PERMANENT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return !targetPermanent.isLand();
		}

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	public static final MagicTargetFilter TARGET_ARTIFACT_OR_ENCHANTMENT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isArtifact()||targetPermanent.isEnchantment();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			if (target.getController()!=player) {
				final MagicPermanent targetPermanent=(MagicPermanent)target;
				return targetPermanent.isArtifact()||targetPermanent.isEnchantment();
			}
			return false;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
		
	public static final MagicTargetFilter TARGET_CREATURE=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return ((MagicPermanent)target).isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_CREATURE_OR_PLAYER=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.isPlayer()||((MagicPermanent)target).isCreature();
		}	
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent||targetType==MagicTargetType.Player;
		}		
	};
	
	public static final MagicTargetFilter TARGET_CREATURE_OR_ARTIFACT=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()||permanent.isArtifact();
		}		

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};

	public static final MagicTargetFilter TARGET_CREATURE_OR_ENCHANTMENT=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()||permanent.isEnchantment();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	public static final MagicTargetFilter TARGET_PERMANENT_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
			
	public static final MagicTargetFilter TARGET_CREATURE_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player&&((MagicPermanent)target).isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			if (target.getController()==player) {
				final MagicPermanent permanent=(MagicPermanent)target;
				if (permanent.isCreature()) {
					final int colorFlags=permanent.getColorFlags();
					return MagicColor.Red.hasColor(colorFlags)||MagicColor.Green.hasColor(colorFlags);
				}
			}
			return false;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	public static final MagicTargetFilter TARGET_BAT_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player&&((MagicPermanent)target).hasSubType(MagicSubType.Bat);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_BEAST_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player&&((MagicPermanent)target).hasSubType(MagicSubType.Beast);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_GOBLIN_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player&&((MagicPermanent)target).hasSubType(MagicSubType.Goblin);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	public static final MagicTargetFilter TARGET_CREATURE_YOUR_OPPONENT_CONTROLS=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()!=player&&((MagicPermanent)target).isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	public static final MagicTargetFilter TARGET_TAPPED_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.isTapped();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};

	public static final MagicTargetFilter TARGET_UNTAPPED_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!permanent.isTapped();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	public static final MagicTargetFilter TARGET_NONWHITE_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!MagicColor.White.hasColor(permanent.getColorFlags());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_NONBLACK_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!MagicColor.Black.hasColor(permanent.getColorFlags());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
				
	public static final MagicTargetFilter TARGET_CREATURE_WITHOUT_FLYING=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!permanent.hasAbility(game,MagicAbility.Flying);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_CREATURE_WITH_FLYING=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.hasAbility(game,MagicAbility.Flying);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	public static final MagicTargetFilter TARGET_CREATURE_CONVERTED_3_OR_LESS=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.getCardDefinition().getCost().getConvertedCost()<=3;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_ATTACKING_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.isAttacking();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public static final MagicTargetFilter TARGET_ATTACKING_CREATURE_WITH_FLYING=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.isAttacking()&&permanent.hasAbility(game,MagicAbility.Flying);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	public static final MagicTargetFilter TARGET_ATTACKING_OR_BLOCKING_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&(permanent.isAttacking()||permanent.isBlocking());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	public static final MagicTargetFilter TARGET_ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.getController()==player&&permanent.isCreature()&&(permanent.isAttacking()||permanent.isBlocking());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	public static final MagicTargetFilter TARGET_CREATURE_CARD_FROM_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return ((MagicCard)target).getCardDefinition().isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard;
		}						
	};

	public static final MagicTargetFilter TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return ((MagicCard)target).getCardDefinition().isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.OpponentsGraveyard;
		}						
	};

	public static final MagicTargetFilter TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicCardDefinition cardDefinition=((MagicCard)target).getCardDefinition();
			return cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasType(MagicType.Sorcery);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard;
		}						
	};
	
	public static final MagicTargetFilter TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicCardDefinition cardDefinition=((MagicCard)target).getCardDefinition();
			return cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasType(MagicType.Sorcery);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.OpponentsGraveyard;
		}						
	};
	
	public static final MagicTargetFilter TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return ((MagicCard)target).getCardDefinition().isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard||targetType==MagicTargetType.OpponentsGraveyard;
		}
	};

	public static final MagicTargetFilter TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicCardDefinition cardDefinition=((MagicCard)target).getCardDefinition();
			return cardDefinition.isCreature()||cardDefinition.isArtifact();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard||targetType==MagicTargetType.OpponentsGraveyard;
		}
	};
	
	public static final MagicTargetFilter TARGET_GOBLIN_CARD_FROM_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return ((MagicCard)target).getCardDefinition().hasSubType(MagicSubType.Goblin);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard;
		}						
	};
		
	// Permanent reference can not be used because game is copied.
	public static final class MagicOtherPermanentTargetFilter implements MagicTargetFilter {

		private final MagicTargetFilter targetFilter;
		private long id;

		public MagicOtherPermanentTargetFilter(final MagicTargetFilter targetFilter,final MagicPermanent invalidPermanent) {
			
			this.targetFilter=targetFilter;
			this.id=invalidPermanent.getId();
		}

		@Override
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return targetFilter.accept(game,player,target)&&((MagicPermanent)target).getId()!=id;
		}
		
		@Override
		public boolean acceptType(final MagicTargetType targetType) {

			return targetFilter.acceptType(targetType);
		}		
	};
	
	public static final class CardTargetFilter implements MagicTargetFilter {
		
		private final MagicCardDefinition cardDefinition;
		
		public CardTargetFilter(final MagicCardDefinition cardDefinition) {
			
			this.cardDefinition=cardDefinition;
		}

		@Override
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return (((MagicPermanent)target).getCardDefinition()==cardDefinition);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	public static final class NameTargetFilter implements MagicTargetFilter {
		
		private final String name;
		
		public NameTargetFilter(final String name) {

			this.name=name;
		}

		@Override
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return name.equals(target.getName());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target);
	
	public boolean acceptType(final MagicTargetType targetType);
}
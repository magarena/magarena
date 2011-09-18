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
    
    MagicTargetFilter SELF = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return false;
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return false;
		}
	};
	
    MagicTargetFilter ALL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return true;
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return true;
		}
	};
	
	MagicTargetFilter TARGET_SPELL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.isSpell();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Stack;
		}
	};
	
    MagicTargetFilter TARGET_RED_GREEN_SPELL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			if (target.isSpell()) { 
				final MagicCardOnStack cardOnStack = (MagicCardOnStack)target;
				final int colors = cardOnStack.getCardDefinition().getColorFlags();
				return MagicColor.Red.hasColor(colors) || MagicColor.Green.hasColor(colors);
			}
			return false;
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Stack;
		}
	};

	MagicTargetFilter TARGET_CREATURE_SPELL=new MagicTargetFilter() {
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

	MagicTargetFilter TARGET_NONCREATURE_SPELL=new MagicTargetFilter() {

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
		
	MagicTargetFilter TARGET_INSTANT_OR_SORCERY_SPELL=new MagicTargetFilter() {

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
	
	MagicTargetFilter TARGET_PLAYER=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return true;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Player;
		}		
	};

	MagicTargetFilter TARGET_OPPONENT=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target!=player;
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Player;
		}
	};
	
	MagicTargetFilter TARGET_SPELL_OR_PERMANENT=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.isSpell()||target.isPermanent();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Stack||targetType==MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_BLACK_PERMANENT=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return MagicColor.Black.hasColor(((MagicPermanent) target).getColorFlags());
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_PERMANENT = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            return true;
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}
	};
	
    MagicTargetFilter TARGET_BLACK_RED_PERMANENT=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			final int colors = targetPermanent.getColorFlags();
            return MagicColor.Black.hasColor(colors)||MagicColor.Red.hasColor(colors);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}
	};

	MagicTargetFilter TARGET_NONBASIC_LAND=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isLand()&&!targetPermanent.getCardDefinition().isBasic();
		}

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_LAND = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent targetPermanent = (MagicPermanent)target;
			return targetPermanent.isLand();
		}

		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_NONLAND_PERMANENT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return !targetPermanent.isLand();
		}

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_ARTIFACT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isArtifact();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ARTIFACT_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent targetPermanent = (MagicPermanent)target;
			return target.getController()==player && targetPermanent.isArtifact();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ARTIFACT_CREATURE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent targetPermanent = (MagicPermanent)target;
			return target.getController()==player &&
					targetPermanent.isArtifact() &&
					targetPermanent.isCreature();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
    MagicTargetFilter TARGET_ARTIFACT_OR_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isArtifact()||targetPermanent.isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ARTIFACT_OR_ENCHANTMENT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isArtifact()||targetPermanent.isEnchantment();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ARTIFACT_OR_LAND = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {	
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isArtifact() || targetPermanent.isLand();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isLand()||targetPermanent.isArtifact()||targetPermanent.isEnchantment();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS=new MagicTargetFilter() {
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
		
	MagicTargetFilter TARGET_CREATURE=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return ((MagicPermanent)target).isCreature();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_NONCREATURE=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return !((MagicPermanent)target).isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CREATURE_OR_PLAYER=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.isPlayer()||((MagicPermanent)target).isCreature();
		}	
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent||targetType==MagicTargetType.Player;
		}		
	};

	MagicTargetFilter TARGET_CREATURE_OR_LAND=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()||permanent.isLand();
		}		

		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};

	MagicTargetFilter TARGET_CREATURE_OR_ENCHANTMENT=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()||permanent.isEnchantment();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_ENCHANTMENT=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			return targetPermanent.isEnchantment();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};

	MagicTargetFilter TARGET_PERMANENT_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController()==player;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_LAND_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player&&((MagicPermanent)target).isLand();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_FOREST_YOU_CONTROL = new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player && ((MagicPermanent)target).hasSubType(MagicSubType.Forest);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_MOUNTAIN_YOU_CONTROL = new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player && ((MagicPermanent)target).hasSubType(MagicSubType.Mountain);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_PLAINS_YOU_CONTROL = new MagicTargetFilter() {	
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player && ((MagicPermanent)target).hasSubType(MagicSubType.Plains);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CREATURE_YOU_CONTROL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController()==player&&((MagicPermanent)target).isCreature();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			if (target.getController()==player) {
				final MagicPermanent permanent=(MagicPermanent)target;
				return !permanent.hasType(MagicType.Legendary)&&permanent.isCreature();
			}
			return false;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL=new MagicTargetFilter() {

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

	MagicTargetFilter TARGET_GREEN_OR_WHITE_CREATURE = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent permanent = (MagicPermanent)target;
			if (permanent.isCreature()) {
				final int colorFlags = permanent.getColorFlags();
				return MagicColor.Green.hasColor(colorFlags) || MagicColor.White.hasColor(colorFlags);
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_WHITE_OR_BLUE_CREATURE = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent permanent = (MagicPermanent)target;
			if (permanent.isCreature()) {
				final int colorFlags = permanent.getColorFlags();
				return MagicColor.White.hasColor(colorFlags) || MagicColor.Blue.hasColor(colorFlags);
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};

    MagicTargetFilter TARGET_BLACK_CREATURE = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            final MagicPermanent permanent = (MagicPermanent)target;
            return permanent.isCreature() && MagicColor.Black.hasColor(permanent.getColorFlags());
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_BLACK_CREATURE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			if (target.getController() == player) {
				final MagicPermanent permanent = (MagicPermanent)target;
				return permanent.isCreature() && MagicColor.Black.hasColor(permanent.getColorFlags());
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_BLUE_CREATURE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			if (target.getController() == player) {
				final MagicPermanent permanent = (MagicPermanent)target;
				return permanent.isCreature() && MagicColor.Blue.hasColor(permanent.getColorFlags());
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_GREEN_CREATURE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			if (target.getController() == player) {
				final MagicPermanent permanent = (MagicPermanent)target;
				return permanent.isCreature() && MagicColor.Green.hasColor(permanent.getColorFlags());
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_RED_CREATURE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			if (target.getController() == player) {
				final MagicPermanent permanent = (MagicPermanent)target;
				return permanent.isCreature() && MagicColor.Red.hasColor(permanent.getColorFlags());
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_WHITE_CREATURE = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
            final MagicPermanent permanent = (MagicPermanent)target;
            return permanent.isCreature() && MagicColor.White.hasColor(permanent.getColorFlags());
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_WHITE_CREATURE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			if (target.getController() == player) {
				final MagicPermanent permanent = (MagicPermanent)target;
				return permanent.isCreature() && MagicColor.White.hasColor(permanent.getColorFlags());
			}
			return false;
		}

		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
		
	MagicTargetFilter TARGET_BAT_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player&&((MagicPermanent)target).hasSubType(MagicSubType.Bat);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_BEAST_YOU_CONTROL=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()==player&&((MagicPermanent)target).hasSubType(MagicSubType.Beast);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_DRAGON_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player && 
                   ((MagicPermanent)target).isCreature() && 
                   ((MagicPermanent)target).hasSubType(MagicSubType.Dragon);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_GOBLIN_YOU_CONTROL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController()==player && 
                   ((MagicPermanent)target).isCreature() && 
                   ((MagicPermanent)target).hasSubType(MagicSubType.Goblin);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
    MagicTargetFilter TARGET_KNIGHT_YOU_CONTROL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController()==player && 
                   ((MagicPermanent)target).isCreature() && 
                   ((MagicPermanent)target).hasSubType(MagicSubType.Knight);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
    
    MagicTargetFilter TARGET_ILLUSION_YOU_CONTROL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController()==player &&
                   ((MagicPermanent)target).isCreature() && 
                   ((MagicPermanent)target).hasSubType(MagicSubType.Illusion);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_MERFOLK_CREATURE = new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {		
			return ((MagicPermanent)target).hasSubType(MagicSubType.Merfolk);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType==MagicTargetType.Permanent;
		}		
	};
    
    MagicTargetFilter TARGET_MERFOLK_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player &&
					((MagicPermanent)target).isCreature() &&
					((MagicPermanent)target).hasSubType(MagicSubType.Merfolk);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
    
    MagicTargetFilter TARGET_SOLDIER_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player &&
					((MagicPermanent)target).isCreature() &&
					((MagicPermanent)target).hasSubType(MagicSubType.Soldier);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_NON_ZOMBIE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player &&
					((MagicPermanent)target).isCreature() &&
					!((MagicPermanent)target).hasSubType(MagicSubType.Zombie);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
    MagicTargetFilter TARGET_ZOMBIE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player &&
					((MagicPermanent)target).isCreature() &&
					((MagicPermanent)target).hasSubType(MagicSubType.Zombie);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_KOR_YOU_CONTROL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController()==player && 
                   ((MagicPermanent)target).isCreature() && 
                   ((MagicPermanent)target).hasSubType(MagicSubType.Kor);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ELF_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player && 
                   ((MagicPermanent)target).isCreature() && 
                   ((MagicPermanent)target).hasSubType(MagicSubType.Elf);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_FAERIE_YOU_CONTROL = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return target.getController() == player && 
                   ((MagicPermanent)target).isCreature() && 
                   ((MagicPermanent)target).hasSubType(MagicSubType.Faerie);
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CREATURE_YOUR_OPPONENT_CONTROLS=new MagicTargetFilter() {
		
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			
			return target.getController()!=player&&((MagicPermanent)target).isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_TAPPED_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.isTapped();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};

	MagicTargetFilter TARGET_UNTAPPED_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!permanent.isTapped();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_NONWHITE_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!MagicColor.White.hasColor(permanent.getColorFlags());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_NONBLACK_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!MagicColor.Black.hasColor(permanent.getColorFlags());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_NONARTIFACT_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!permanent.isArtifact();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_NONARTIFACT_NONBLACK_CREATURE = new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature() &&
					!permanent.isArtifact() &&
					!MagicColor.Black.hasColor(permanent.getColorFlags());
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CREATURE_WITHOUT_FLYING=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&!permanent.hasAbility(game,MagicAbility.Flying);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CREATURE_WITH_FLYING=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.hasAbility(game,MagicAbility.Flying);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_CREATURE_CONVERTED_3_OR_LESS=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.getCardDefinition().getCost().getConvertedCost()<=3;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CREATURE_POWER_2_OR_LESS = new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature() && permanent.getCardDefinition().getPower() <= 2;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CREATURE_POWER_4_OR_MORE = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent permanent = (MagicPermanent)target;
			return permanent.isCreature() && permanent.getCardDefinition().getPower() >= 4;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ATTACKING_CREATURE=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.isAttacking();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
    MagicTargetFilter TARGET_ATTACKING_CREATURE_YOU_CONTROL=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicPermanent permanent=(MagicPermanent)target;
			return player == permanent.getController() && permanent.isCreature() && permanent.isAttacking();
		}
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_ATTACKING_CREATURE_WITH_FLYING=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.isAttacking()&&permanent.hasAbility(game,MagicAbility.Flying);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_ATTACKING_OR_BLOCKING_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&(permanent.isAttacking()||permanent.isBlocking());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};

	MagicTargetFilter TARGET_ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.getController()==player&&permanent.isCreature()&&(permanent.isAttacking()||permanent.isBlocking());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}
	};
	
	MagicTargetFilter TARGET_BLOCKED_CREATURE=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicPermanent permanent=(MagicPermanent)target;
			return permanent.isCreature()&&permanent.isBlocked();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Permanent;
		}		
	};
	
	MagicTargetFilter TARGET_CARD_FROM_GRAVEYARD=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return true;
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Graveyard;
		}						
	};
	
	MagicTargetFilter TARGET_CREATURE_CARD_FROM_GRAVEYARD=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return ((MagicCard)target).getCardDefinition().isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Graveyard;
		}						
	};
	
	MagicTargetFilter TARGET_PERMANENT_CARD_FROM_GRAVEYARD = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return !((MagicCard)target).getCardDefinition().isSpell();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Graveyard;
		}						
	};
	
    MagicTargetFilter TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD=new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicCardDefinition cardDefinition = ((MagicCard)target).getCardDefinition();
			return cardDefinition.getConvertedCost() <= 3 && !cardDefinition.isSpell();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType==MagicTargetType.Graveyard;
		}						
	};

	MagicTargetFilter TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return ((MagicCard)target).getCardDefinition().isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.OpponentsGraveyard;
		}						
	};

	MagicTargetFilter TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicCardDefinition cardDefinition=((MagicCard)target).getCardDefinition();
			return cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasType(MagicType.Sorcery);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard;
		}						
	};
	
	MagicTargetFilter TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicCardDefinition cardDefinition=((MagicCard)target).getCardDefinition();
			return cardDefinition.hasType(MagicType.Instant)||cardDefinition.hasType(MagicType.Sorcery);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.OpponentsGraveyard;
		}						
	};
	
	MagicTargetFilter TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD = new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return ((MagicCard)target).getCardDefinition().hasType(MagicType.Enchantment);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {	
			return targetType == MagicTargetType.Graveyard;
		}						
	};
	
	MagicTargetFilter TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return ((MagicCard)target).getCardDefinition().isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard||targetType==MagicTargetType.OpponentsGraveyard;
		}
	};

	MagicTargetFilter TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			final MagicCardDefinition cardDefinition=((MagicCard)target).getCardDefinition();
			return cardDefinition.isCreature()||cardDefinition.isArtifact();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard||targetType==MagicTargetType.OpponentsGraveyard;
		}
	};
	
	MagicTargetFilter TARGET_GOBLIN_CARD_FROM_GRAVEYARD=new MagicTargetFilter() {

		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {

			return ((MagicCard)target).getCardDefinition().hasSubType(MagicSubType.Goblin);
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			
			return targetType==MagicTargetType.Graveyard;
		}						
	};
	
	MagicTargetFilter TARGET_CREATURE_CARD_FROM_HAND = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			return ((MagicCard)target).getCardDefinition().isCreature();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Hand;
		}						
	};
	
	MagicTargetFilter TARGET_GREEN_CREATURE_CARD_FROM_HAND = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicCardDefinition cardDefinition = ((MagicCard)target).getCardDefinition();
			return cardDefinition.isCreature() && MagicColor.Green.hasColor(cardDefinition.getColorFlags());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Hand;
		}						
	};
	
	MagicTargetFilter TARGET_MULTICOLOR_CREATURE_CARD_FROM_HAND = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicCardDefinition cardDefinition = ((MagicCard)target).getCardDefinition();
			return cardDefinition.isCreature() && MagicColor.isMulti(cardDefinition.getColorFlags());
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Hand;
		}						
	};
	
	MagicTargetFilter TARGET_BASIC_LAND_CARD_FROM_HAND = new MagicTargetFilter() {
		public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
			final MagicCardDefinition cardDefinition = ((MagicCard)target).getCardDefinition();
			return cardDefinition.isLand()&&cardDefinition.isBasic();
		}
		
		public boolean acceptType(final MagicTargetType targetType) {
			return targetType == MagicTargetType.Hand;
		}						
	};
	
	// Permanent reference can not be used because game is copied.
	public static final class MagicOtherPermanentTargetFilter implements MagicTargetFilter {

		private final MagicTargetFilter targetFilter;
        private final long id;		

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
	
	boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target);
	
	boolean acceptType(final MagicTargetType targetType);
}

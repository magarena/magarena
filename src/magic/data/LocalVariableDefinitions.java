package magic.data;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicColoredType;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class LocalVariableDefinitions {

	private static final MagicLocalVariable CAIRN_WANDERER=new MagicDummyLocalVariable() {
		
		@Override
		public long getAbilityFlags(MagicGame game,MagicPermanent permanent,long flags) {

			long newFlags=0;
			for (final MagicPlayer player : game.getPlayers()) {
				
				for (final MagicCard card : player.getGraveyard()) {

					final MagicCardDefinition cardDefinition=card.getCardDefinition();
					if (cardDefinition.isCreature()) {
						newFlags|=cardDefinition.getAbilityFlags();
					}
				}
			}
			return flags|(newFlags&MagicAbility.CAIRN_WANDERER_FLAGS);
		}
	};
				
	private static final MagicLocalVariable GUUL_DRAZ_SPECTER=new MagicDummyLocalVariable() {

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {

			if (game.getOpponent(permanent.getController()).getHand().isEmpty()) {
				pt.power+=3;
				pt.toughness+=3;
			}
		}		
	};
	
	private static final MagicLocalVariable JUND_HACKBLADE=new MagicDummyLocalVariable() {

		private boolean isValid(final MagicPermanent owner) {
			
			for (final MagicPermanent permanent : owner.getController().getPermanents()) {
				
				if (permanent!=owner&&permanent.getColoredType()==MagicColoredType.MultiColored) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			
			if (isValid(permanent)) {
				pt.power++;
				pt.toughness++;
			}
		}
		
		@Override
		public long getAbilityFlags(MagicGame game,MagicPermanent permanent,long flags) {

			return isValid(permanent)?flags|MagicAbility.Haste.getMask():flags;
		}
	};
	
	private static final MagicLocalVariable KITESAIL_APPRENTICE=new MagicDummyLocalVariable() {

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {

			if (permanent.isEquipped()) {
				pt.power++;
				pt.toughness++;
			}
		}
		
		
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
		
			return permanent.isEquipped()?flags|MagicAbility.Flying.getMask():flags;
		}
	};
	
	private static final MagicLocalVariable RUTHLESS_CULLBLADE=new MagicDummyLocalVariable() {

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {

			if (game.getOpponent(permanent.getController()).getLife()<=10) {
				pt.power+=2;
				pt.toughness++;
			}
		}		
	};

	private static final MagicLocalVariable STUDENT_OF_WARFARE=new MagicDummyLocalVariable() {

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {

			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=7) {
				pt.power=4;
				pt.toughness=4;
			} else if (charges>=2) {
				pt.power=3;
				pt.toughness=3;
			}
		}		
		
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {

			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=7) {
				return flags|MagicAbility.DoubleStrike.getMask();
			} else if (charges>=2) {
				return flags|MagicAbility.FirstStrike.getMask();
			}
			return flags;
		}
	};
	
	public static void addLocalVariables() {

		MagicCardDefinition cardDefinition;
		
		// Cairn Wanderer
		cardDefinition=CardDefinitions.getInstance().getCard("Cairn Wanderer");
		cardDefinition.addLocalVariable(MagicStaticLocalVariable.getInstance());
		cardDefinition.addLocalVariable(CAIRN_WANDERER);
								
		// Guul Draz Specter
		cardDefinition=CardDefinitions.getInstance().getCard("Guul Draz Specter");
		cardDefinition.addLocalVariable(MagicStaticLocalVariable.getInstance());
		cardDefinition.addLocalVariable(GUUL_DRAZ_SPECTER);
		
		// Jund Hackblade
		cardDefinition=CardDefinitions.getInstance().getCard("Jund Hackblade");
		cardDefinition.addLocalVariable(MagicStaticLocalVariable.getInstance());
		cardDefinition.addLocalVariable(JUND_HACKBLADE);		
		
		// Kitesail Apprentice
		cardDefinition=CardDefinitions.getInstance().getCard("Kitesail Apprentice");
		cardDefinition.addLocalVariable(MagicStaticLocalVariable.getInstance());
		cardDefinition.addLocalVariable(KITESAIL_APPRENTICE);

		// Ruthless Cullblade
		cardDefinition=CardDefinitions.getInstance().getCard("Ruthless Cullblade");
		cardDefinition.addLocalVariable(MagicStaticLocalVariable.getInstance());
		cardDefinition.addLocalVariable(RUTHLESS_CULLBLADE);	
		
		// Student of Warfare
		cardDefinition=CardDefinitions.getInstance().getCard("Student of Warfare");
		cardDefinition.addLocalVariable(STUDENT_OF_WARFARE);	
		cardDefinition.addLocalVariable(MagicStaticLocalVariable.getInstance());
	}
}
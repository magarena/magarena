package magic.model.variable;

import magic.data.CardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;

// Implements all static abilities of cards.
public class MagicStaticLocalVariable implements MagicLocalVariable {

	private static final MagicLocalVariable INSTANCE=new MagicStaticLocalVariable();
	
	private static int akromasMemorial;
	private static int angelicShield;
	private static int balefireLiege;
	private static int bloodmarkMentor;
	private static int boartuskLiege;
	private static int captainOfTheWatch;
	private static int creakwoodLiege;
	private static int cumberStone;
	private static int deathbringerLiege;
	private static int eldraziMonument;
	private static int firesOfYavimaya;
	private static int glenElendraLiege;
	private static int gloriousAnthem;
	private static int gloryOfWarfare;
	private static int goblinChieftain;
	private static int godheadOfAwe;
	private static int kinsbaileCavalier;
	private static int kulrathKnight;
	private static int levitation;
	private static int madrushCyclops;
	private static int murkfiendLiege;
	public static int platinumAngel; // You can't lose the game.
	private static int razorjawOni;
	public static int spiritOfTheHearth; // You can't be target of spells or abilities your opponent controls.
	private static int tolsimirWolfblood;
	private static int trueConviction;
	private static int veteranArmorer;
	private static int windbriskRaptor;
	
	private int getOtherCount(final int cardDefinitionIndex,final MagicPermanent permanent,final int count) {
		
		return permanent.getCardDefinition().getIndex()!=cardDefinitionIndex?count:count-1;
	}
	
	private int getOtherCount(final int cardDefinitionIndex,final MagicPermanent permanent,final MagicPlayer controller) {

		final int count=controller.getCount(cardDefinitionIndex);
		return count==0?0:getOtherCount(cardDefinitionIndex,permanent,count);
	}

	@Override
	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {

		final MagicPlayer controller=permanent.getController();

		// Power and toughness change.
		if (getOtherCount(godheadOfAwe,permanent,game.getCount(godheadOfAwe))>0) {
			pt.power=1;
			pt.toughness=1;
		} else if (permanent.getCounters(MagicCounterType.Feather)>0) {
			pt.power=3;
			pt.toughness=1;
		}

		// Power or toughness increase.
		pt.toughness+=controller.getCount(angelicShield);
		pt.toughness+=getOtherCount(veteranArmorer,permanent,controller);
		int count=controller.getCount(gloryOfWarfare);
		if (count>0) {
			if (game.hasTurn(controller)) {
				pt.power+=count*2;
			} else {
				pt.toughness+=count*2;
			}
		}
		pt.power-=game.getOpponent(controller).getCount(cumberStone);
		
		// Power and toughness increase.
		int both=permanent.getCounters(MagicCounterType.PlusOne)-permanent.getCounters(MagicCounterType.MinusOne);

		both+=controller.getCount(eldraziMonument);
		both+=controller.getCount(gloriousAnthem);
		count=controller.getCount(captainOfTheWatch);
		if (count>0&&permanent.hasSubType(MagicSubType.Soldier)) {
			both+=getOtherCount(captainOfTheWatch,permanent,count);
		}
		count=controller.getCount(goblinChieftain);
		if (count>0&&permanent.hasSubType(MagicSubType.Goblin)) {
			both+=getOtherCount(goblinChieftain,permanent,count);
		}
	
		final int colorFlags=permanent.getColorFlags();
		if (MagicColor.Black.hasColor(colorFlags)) {
			both+=getOtherCount(creakwoodLiege,permanent,controller);
			both+=getOtherCount(deathbringerLiege,permanent,controller);
			both+=getOtherCount(glenElendraLiege,permanent,controller);
		}
		if (MagicColor.Blue.hasColor(colorFlags)) {
			both+=getOtherCount(glenElendraLiege,permanent,controller);
			both+=getOtherCount(murkfiendLiege,permanent,controller);
		}
		if (MagicColor.Green.hasColor(colorFlags)) {
			both+=getOtherCount(boartuskLiege,permanent,controller);
			both+=getOtherCount(creakwoodLiege,permanent,controller);
			both+=getOtherCount(murkfiendLiege,permanent,controller);
			both+=getOtherCount(tolsimirWolfblood,permanent,controller);			
		}
		if (MagicColor.Red.hasColor(colorFlags)) {
			both+=getOtherCount(boartuskLiege,permanent,controller);
			both+=getOtherCount(balefireLiege,permanent,controller);
		}
		if (MagicColor.White.hasColor(colorFlags)) {
			both+=getOtherCount(balefireLiege,permanent,controller);
			both+=getOtherCount(deathbringerLiege,permanent,controller);
			both+=getOtherCount(tolsimirWolfblood,permanent,controller);
		}
		
		pt.power+=both;
		pt.toughness+=both;
	}
	
	@Override
	public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,long flags) {
		
		if (permanent.isCreature()) {
			final MagicPlayer controller=permanent.getController();

			if (controller.getCount(akromasMemorial)>0) {
				flags|=MagicAbility.AKROMAS_MEMORIAL_FLAGS;
			}
			if (controller.getCount(eldraziMonument)>0) {
				flags|=MagicAbility.ELDRAZI_MONUMENT_FLAGS;
			}
			if (controller.getCount(trueConviction)>0) {
				flags|=MagicAbility.TRUE_CONVICTION_FLAGS;
			}
			if (controller.getCount(bloodmarkMentor)>0&&MagicColor.Red.hasColor(permanent.getColorFlags())) {
				flags|=MagicAbility.FirstStrike.getMask();
			}
			if (controller.getCount(captainOfTheWatch)>0&&permanent.hasSubType(MagicSubType.Soldier)) {
				flags|=MagicAbility.Vigilance.getMask();
			}
			if (controller.getCount(firesOfYavimaya)>0||
				controller.getCount(madrushCyclops)>0||
				(controller.getCount(goblinChieftain)>0&&permanent.hasSubType(MagicSubType.Goblin))) {
				flags|=MagicAbility.Haste.getMask();
			}
			if (game.getOtherPlayerCount(kulrathKnight,controller)>0&&permanent.hasCounters()) {
				flags|=MagicAbility.CannotAttackOrBlock.getMask();
			}
			if (controller.getCount(levitation)>0||permanent.getCounters(MagicCounterType.Feather)>0) {
				flags|=MagicAbility.Flying.getMask();
			}
			if ((controller.getCount(kinsbaileCavalier)>0&&permanent.hasSubType(MagicSubType.Knight))) {
				flags|=MagicAbility.DoubleStrike.getMask();
			}
			if (game.getCount(razorjawOni)>0&&MagicColor.Black.hasColor(permanent.getColorFlags())) {
				flags|=MagicAbility.CannotBlock.getMask();
			}
			if (controller.getCount(windbriskRaptor)>0&&permanent.isAttacking()) {
				flags|=MagicAbility.LifeLink.getMask();
			}	
		}
		
		return flags;
	}

	@Override
	public int getSubTypeFlags(final MagicPermanent permanent,int flags) {
		
		if (permanent.getCardDefinition().hasAbility(MagicAbility.Changeling)) {
			return flags|MagicSubType.ALL_CREATURES;
		}		
		return flags;
	}

	@Override
	public int getColorFlags(final MagicPermanent permanent,final int flags) {
		
		return flags;
	}
	
	public static final MagicLocalVariable getInstance() {
		
		return INSTANCE;
	}
	
	public static void initializeCardDefinitions() {

		final CardDefinitions definitions=CardDefinitions.getInstance();
		akromasMemorial=definitions.getCard("Akroma's Memorial").getIndex();
		angelicShield=definitions.getCard("Angelic Shield").getIndex();
		balefireLiege=definitions.getCard("Balefire Liege").getIndex();
		bloodmarkMentor=definitions.getCard("Bloodmark Mentor").getIndex();
		boartuskLiege=definitions.getCard("Boartusk Liege").getIndex();
		captainOfTheWatch=definitions.getCard("Captain of the Watch").getIndex();
		creakwoodLiege=definitions.getCard("Creakwood Liege").getIndex();
		cumberStone=definitions.getCard("Cumber Stone").getIndex();
		deathbringerLiege=definitions.getCard("Deathbringer Liege").getIndex();
		eldraziMonument=definitions.getCard("Eldrazi Monument").getIndex();
		firesOfYavimaya=definitions.getCard("Fires of Yavimaya").getIndex();
		glenElendraLiege=definitions.getCard("Glen Elendra Liege").getIndex();
		gloriousAnthem=definitions.getCard("Glorious Anthem").getIndex();
		gloryOfWarfare=definitions.getCard("Glory of Warfare").getIndex();
		goblinChieftain=definitions.getCard("Goblin Chieftain").getIndex();
		godheadOfAwe=definitions.getCard("Godhead of Awe").getIndex();
		kinsbaileCavalier=definitions.getCard("Kinsbaile Cavalier").getIndex();
		kulrathKnight=definitions.getCard("Kulrath Knight").getIndex();
		levitation=definitions.getCard("Levitation").getIndex();
		madrushCyclops=definitions.getCard("Madrush Cyclops").getIndex();
		murkfiendLiege=definitions.getCard("Murkfiend Liege").getIndex();
		platinumAngel=definitions.getCard("Platinum Angel").getIndex();
		razorjawOni=definitions.getCard("Razorjaw Oni").getIndex();
		spiritOfTheHearth=definitions.getCard("Spirit of the Hearth").getIndex();
		tolsimirWolfblood=definitions.getCard("Tolsimir Wolfblood").getIndex();
		trueConviction=definitions.getCard("True Conviction").getIndex();
		veteranArmorer=definitions.getCard("Veteran Armorer").getIndex();
		windbriskRaptor=definitions.getCard("Windbrisk Raptor").getIndex();
	}
}
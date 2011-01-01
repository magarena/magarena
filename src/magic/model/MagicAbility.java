package magic.model;

public enum MagicAbility {
	
	AttacksEachTurnIfAble("attacks each turn if able",-10),
	CannotBlock("can't block",-50),
	CannotAttackOrBlock("can't attack or block",-200),
	CannotBlockWithoutFlying("can't block creatures without flying",-40),
	CannotBeBlockedByFlying("can't be blocked by creatures with flying",20),
	CannotBeBlockedExceptWithFlying("can't be blocked except by creatures with flying",30),
	CannotBeCountered("can't be countered",0),
	CannotBeTheTarget("can't be the target of spells or abilities your opponent controls",80),
	Changeling("changeling",10),
	Deathtouch("deathtouch",60),
	Defender("defender",-100),
	DoubleStrike("double strike",100),
	Exalted("exalted",10),
	Fear("fear",50),
	Flash("flash",0),
	Flying("flying",50),
	FirstStrike("first strike",50),
	Forestwalk("forestwalk",10),
	Indestructible("indestructible",150),
	Islandwalk("Islandwalk",10),
	Haste("haste",0),
	LifeLink("lifelink",40),
	Mountainwalk("mountainwalk",10),
	Persist("persist",60),
	PlainsWalk("plainswalk",10),
	ProtectionFromBlack("protection from black",20),
	ProtectionFromBlue("protection from blue",20),
	ProtectionFromGreen("protection from green",20),
	ProtectionFromRed("protection from red",20),
	ProtectionFromWhite("protection from white",20),
	ProtectionFromMonoColored("protection from monocolored",50),
	ProtectionFromAllColors("protection from all colors",150),
	ProtectionFromCreatures("protection from creatures",100),
	ProtectionFromDemons("protection from Demons",10),
	ProtectionFromDragons("protection from Dragons",10),
	Reach("reach",20),
	Shroud("shroud",60),
	Swampwalk("swampwalk",10),
	Trample("trample",30),
	Unblockable("unblockable",100),
	Vigilance("vigilance",20),
	Wither("wither",30),
	TotemArmor("totem armor",0),
	Intimidate("intimidate",45),
	;

	public static final long PROTECTION_FLAGS=
		ProtectionFromBlack.getMask()|
		ProtectionFromBlue.getMask()|
		ProtectionFromGreen.getMask()|
		ProtectionFromRed.getMask()|
		ProtectionFromWhite.getMask()|
		ProtectionFromMonoColored.getMask()|
		ProtectionFromAllColors.getMask()|
		ProtectionFromCreatures.getMask()|
		ProtectionFromDemons.getMask()|
		ProtectionFromDragons.getMask();
	
	public static final long LANDWALK_FLAGS=
		Forestwalk.getMask()|
		Islandwalk.getMask()|
		Mountainwalk.getMask()|
		PlainsWalk.getMask()|
		Swampwalk.getMask();
			
	public static final long CAIRN_WANDERER_FLAGS=
		Flying.getMask()|
		Fear.getMask()|
		FirstStrike.getMask()|
		DoubleStrike.getMask()|
		Deathtouch.getMask()|
		Haste.getMask()|
		LifeLink.getMask()|
		Reach.getMask()|		
		Trample.getMask()|
		Shroud.getMask()|
		Vigilance.getMask()|
		LANDWALK_FLAGS|
		PROTECTION_FLAGS;
	
	public static final long TITANIC_ULTIMATUM_FLAGS=
		FirstStrike.getMask()|
		LifeLink.getMask()|
		Trample.getMask();
	
	public static final long AKROMAS_MEMORIAL_FLAGS=
		Flying.getMask()|
		FirstStrike.getMask()|
		Vigilance.getMask()|
		Trample.getMask()|
		Haste.getMask()|
		ProtectionFromBlack.getMask()|
		ProtectionFromRed.getMask();
	
	public static final long ELDRAZI_MONUMENT_FLAGS=
		Flying.getMask()|Indestructible.getMask();
	
	public static final long TRUE_CONVICTION_FLAGS=
		DoubleStrike.getMask()|LifeLink.getMask();
			
	public static final long EXCLUDE_MASK=Long.MAX_VALUE-Flash.getMask()-CannotBeCountered.getMask()-TotemArmor.getMask();
	
	private final String name;
	private final int score;
	private final int index;
	private final long mask;
	
	private MagicAbility(final String name,final int score) {
	
		this.name=name;
		this.score=score;
		this.index=AbilityCount.nextIndex();
		mask=1L<<index;
	}
	
	public String getName() {
		
		return name;
	}
	
	@Override
	public String toString() {

		return name;
	}

	public int getScore() {
		
		return score;
	}
	
	public int getIndex() {
		
		return index;
	}
	
	public long getMask() {
		
		return mask;
	}

	public boolean hasAbility(final long flags) {
		
		return (flags&mask)!=0;
	}
	
	public static int getScore(final long flags) {
		
		int score=0;
		for (final MagicAbility ability : values()) {
			
			if (ability.hasAbility(flags)) {
				score+=ability.getScore();
			}
		}
		return score;
	}
	
	public static MagicAbility getAbility(final String name) {
		
		for (final MagicAbility ability : values()) {
			
			if (ability.getName().equalsIgnoreCase(name)) {
				return ability;
			}
		}
		return null;
	}
	
	private static final class AbilityCount {
		
		private static int count=0;
		
		public static int nextIndex() {
			
			return count++;
		}
	}
}
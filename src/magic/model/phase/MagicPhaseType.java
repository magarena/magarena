package magic.model.phase;

public enum MagicPhaseType {

	Untap("Untap",false,0),
	Upkeep("Upkeep",false,1),
	Draw("Draw",false,2),
	FirstMain("First Main",true,3),
	BeginOfCombat("Begin of Combat",false,4),
	DeclareAttackers("Declare Attackers",false,5),
	DeclareBlockers("Declare Blockers",false,6),
	CombatDamage("Combat Damage",false,7),
	EndOfCombat("End of Combat",false,8),
	SecondMain("Second Main",true,9),
	EndOfTurn("End of Turn",false,10),
	Cleanup("Cleanup",false,11),
	;
	
	private final String name;
	private final boolean main;
	private final int index;
	
	private MagicPhaseType(final String name,final boolean main,final int index) {

		this.name=name;
		this.main=main;
		this.index=index;
	}
	
	public String getName() {

		return name;
	}

	public boolean isMain() {
		
		return main;
	}
	
	public int getIndex() {

		return index;
	}
}
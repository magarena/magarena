package magic.model.phase;

public enum MagicPhaseType {

    Mulligan("Mulligan"),
    Untap("Untap"),
    Upkeep("Upkeep"),
    Draw("Draw"),
    FirstMain("First Main"),
    BeginOfCombat("Begin of Combat"),
    DeclareAttackers("Declare Attackers"),
    DeclareBlockers("Declare Blockers"),
    CombatDamage("Combat Damage"),
    EndOfCombat("End of Combat"),
    SecondMain("Second Main"),
    EndOfTurn("End of Turn"),
    Cleanup("Cleanup"),
    ;
    
    private final String name;
    
    private MagicPhaseType(final String name) {
        this.name=name;
    }
    
    public String getName() {
        return name;
    }

    public boolean isMain() {
        return this == FirstMain || this == SecondMain;    
    }
}

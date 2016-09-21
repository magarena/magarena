package magic.model.phase;

public enum MagicPhaseType {

    Mulligan("Mulligan", "MU"),
    Untap("Untap", "UT"),
    Upkeep("Upkeep", "UP"),
    Draw("Draw", "DR"),
    FirstMain("First Main", "M1"),
    BeginOfCombat("Begin of Combat", "BC"),
    DeclareAttackers("Declare Attackers", "DA"),
    DeclareBlockers("Declare Blockers", "DB"),
    CombatDamage("Combat Damage", "CD"),
    EndOfCombat("End of Combat", "EC"),
    SecondMain("Second Main", "M2"),
    EndOfTurn("End of Turn", "ET"),
    Cleanup("Cleanup", "CU");

    private final String name;
    private final String abbreviation;

    private MagicPhaseType(final String name, final String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public boolean isMain() {
        return this == FirstMain ||
               this == SecondMain;
    }

    public boolean isCombat() {
        return this == BeginOfCombat ||
               this == DeclareAttackers ||
               this == DeclareBlockers ||
               this == CombatDamage ||
               this == EndOfCombat;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

}

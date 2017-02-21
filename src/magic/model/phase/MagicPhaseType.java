package magic.model.phase;

public enum MagicPhaseType {

    Mulligan("MU"),
    Untap("UT"),
    Upkeep("UP"),
    Draw("DR"),
    FirstMain("M1"),
    BeginOfCombat("BC"),
    DeclareAttackers("DA"),
    DeclareBlockers("DB"),
    CombatDamage("CD"),
    EndOfCombat("EC"),
    SecondMain("M2"),
    EndOfTurn("ET"),
    Cleanup("CU");

    private final String abbreviation;

    private MagicPhaseType(final String abbreviation) {
        this.abbreviation = abbreviation;
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

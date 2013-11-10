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
        return this == FirstMain ||
               this == SecondMain;
    }

    public boolean isCombat() {
        return this == BeginOfCombat ||
               this == DeclareAttackers ||
               this == DeclareBlockers ||
               this == EndOfCombat;
    }

    public String getAbbreviation() {
        switch (this) {
        case Mulligan:          return "MU";
        case Draw:              return "DR";
        case Upkeep:            return "UP";
        case FirstMain:         return "M1";
        case BeginOfCombat:     return "BC";
        case Cleanup:           return "CU";
        case CombatDamage:      return "CD";
        case DeclareAttackers:  return "DA";
        case DeclareBlockers:   return "DB";
        case EndOfCombat:       return "EC";
        case EndOfTurn:         return "ET";
        case SecondMain:        return "M2";
        case Untap:             return "UT";
        default:                return name;
        }
    }
}

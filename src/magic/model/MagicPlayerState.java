package magic.model;

public enum MagicPlayerState {

    WasDealtDamage(""),
    CantCastSpells("can't cast spells this turn"),
    CantActivateAbilities("can't activate abilities this turn"),
    HasLostLife(""),
    HasGainedLife(""),
    Monarch(""),
    Revolt(""), //has had a controlled permanent leave the battlefield
    CitysBlessing("")
    ;

    private final String description;
    private final int mask;

    // states that persist after cleanup
    public static final int CLEANUP_MASK =
        Monarch.getMask() |
        CitysBlessing.getMask();

    // states that persist during a turn
    public static final int TURN_MASK =
        Monarch.getMask() |
        WasDealtDamage.getMask() |
        HasLostLife.getMask() |
        HasGainedLife.getMask() |
        Revolt.getMask() |
        CitysBlessing.getMask();

    private MagicPlayerState(final String description) {
        this.description=description;
        this.mask=1<<ordinal();
    }

    public String getDescription() {
        return description;
    }

    public int getMask() {
        return mask;
    }

    public boolean hasState(final int flags) {
        return (flags&mask)!=0;
    }
}

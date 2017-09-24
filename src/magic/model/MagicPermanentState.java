package magic.model;

public enum MagicPermanentState {

    Tapped("tapped", "{T}"),
    Summoned("summoned", "{n}"),
    DoesNotUntapDuringNext("doesn't untap during its controller's next untap step", "{s}"),
    DoesNotUntapDuringNext0("doesn't untap during your next untap step", "{s}"),
    DoesNotUntapDuringNext1("doesn't untap during your next untap step", "{s}"),
    Exerted("exerted", ""),
    Explores("explores", ""),
    Regenerated("regenerated", "{r}"),
    CannotBeRegenerated("can't be regenerated", "{~r}"),
    Attacking("attacking", "{c}"),
    Blocking("blocking", "{c}"),
    Blocked("blocked", "{b}"),
    ExcludeManaSource("exclude as mana source", ""),
    ExcludeFromCombat("exclude from combat", ""),
    Destroyed("destroyed", ""),
    CannotAttack("can't attack", ""),
    NoCombatDamage("assigns no combat damage", ""),
    MustPayEchoCost("echo", ""),
    Monstrous("monstrous", ""),
    FaceDown("face down", ""),
    Manifest("manifest", ""),
    Flipped("flipped", ""),
    Transformed("transformed", ""),
    DealtFirstStrike("dealt first strike", ""),
    Exploit("exploit", ""),
    WasDealtDamage("", ""),
    CastFromHand("", ""),
    Renowned("renowned", "");

    // states that persist after cleanup
    public static final int CLEANUP_MASK =
        Tapped.getMask()
            | Summoned.getMask()
            | DoesNotUntapDuringNext.getMask()
            | DoesNotUntapDuringNext0.getMask()
            | DoesNotUntapDuringNext1.getMask()
            | ExcludeManaSource.getMask()
            | ExcludeFromCombat.getMask()
            | MustPayEchoCost.getMask()
            | Monstrous.getMask()
            | FaceDown.getMask()
            | Manifest.getMask()
            | Flipped.getMask()
            | Transformed.getMask()
            | Renowned.getMask();

    private final String description;
    private final String text;
    private final int mask;

    private MagicPermanentState(final String description, final String text) {
        this.description = description;
        this.text = text;
        this.mask = 1 << ordinal();
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public int getMask() {
        return mask;
    }

    public boolean hasState(final int flags) {
        return (flags & mask) != 0;
    }
}

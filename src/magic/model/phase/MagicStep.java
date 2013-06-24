package magic.model.phase;

public enum MagicStep {
    /** Begin of each phase */
    Begin,

    /** Let active player play spells until pass. */
    ActivePlayer,

    /** Let other player play spells until pass. If spells were played, go back
     * to ActivePlayer. */
    OtherPlayer,

    /** If stack is empty, go to StackResolved. Otherwise resolve top spell of
     * stack and go back to ActivePlayer. */
    Resolve,

    /** Go to the next phase. */
    NextPhase,
}

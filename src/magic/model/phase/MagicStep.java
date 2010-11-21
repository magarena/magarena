package magic.model.phase;

public enum MagicStep {

	Begin, /** Begin of each phase */
	ActivePlayer, /** Let active player play spells until pass. */
	OtherPlayer, /** Let other player play spells until pass. If spells were played, go back to ActivePlayer. */
	Resolve, /** If stack is empty, go to StackResolved. Otherwise resolve top spell of stack and go back to ActivePlayer. */
	NextPhase, /** Go to the next phase. */
}
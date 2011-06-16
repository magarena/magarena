package magic.model.trigger;

public enum MagicTriggerType {

	AtUpkeep(true), // player
	AtEndOfTurn(true), // player
	WhenDamageIsDealt(true), // damage
	WhenSpellIsPlayed(true), // card on stack
	WhenComesIntoPlay(true), // null
	WhenOtherComesIntoPlay(true), // permanent
	WhenPutIntoGraveyard(true), // graveyard trigger data
	WhenOtherPutIntoGraveyardFromPlay(true), // permanent
	WhenAttacks(true), // permanent
	WhenBlocks(true), // permanent
	IfDamageWouldBeDealt(false), // damage
	;
	
	private final boolean stack;
	
	private MagicTriggerType(final boolean stack) {
		this.stack=stack;
	}
	
	public boolean usesStack() {
		return stack;
	}
}

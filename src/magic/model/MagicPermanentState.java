package magic.model;

public enum MagicPermanentState {
	
	Tapped("tapped","{T}"),
	Summoned("summoned","{n}"),
	DoesNotUntapDuringNext("doesn't untap during its controller's next untap step","{s}"),
	Regenerated("regenerated","{r}"),
	CannotBeRegenerated("can't be regenerated","{~r}"),
	LosesAllAbilities("loses all abilities",""),
	Attacking("attacking","{c}"),
	Blocking("blocking","{c}"),
	Blocked("blocked","{b}"),
	Animated("animated","{A}"), // until end of turn
	SacrificeAtEndOfTurn("sacrifice at end of turn","{S}"),
	RemoveAtEndOfTurn("remove from game at end of turn","{E}"),
	RemoveAtEndOfYourTurn("remove from game at end of your turn","{E}"),
	ExcludeManaSource("exclude as mana source",""),
	ExcludeFromCombat("exclude from combat",""),
	Kicked("kicked",""),
	Destroyed("destroyed",""),
	ReturnToOwnerAtEndOfTurn("return to owner at end of turn","{R}"),
	ReturnToHandOfOwnerAtEndOfCombat("return to owner's hand at end of combat",""),
	ExileAtEndOfCombat("exile at end of combat",""),
	DestroyAtEndOfCombat("destroy at end of combat","")
	;

	public static final int CLEANUP_MASK=
		Tapped.getMask()|
		Summoned.getMask()|
		DoesNotUntapDuringNext.getMask()|
		SacrificeAtEndOfTurn.getMask()|
		RemoveAtEndOfTurn.getMask()|
		RemoveAtEndOfYourTurn.getMask()|
		ReturnToOwnerAtEndOfTurn.getMask()|
		ExcludeManaSource.getMask()|
		ExcludeFromCombat.getMask()|
		Kicked.getMask();
	
	private final String description;
	private final String text;
	private final int mask;
	
	private MagicPermanentState(final String description,final String text) {
		this.description=description;
		this.text=text;
		this.mask=1<<ordinal();
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
		return (flags&mask)!=0;
	}
}

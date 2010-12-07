package magic.model;

public enum MagicPermanentState {
	
	Tapped("tapped","{T}",0),
	Summoned("summoned",null,1),
	DoesNotUntap("doesn't untap during its controller's next untap step","{s}",2),
	Regenerated("regenerated","{r}",3),
	CannotBeRegenerated("can't be regenerated",null,4),
	Attacking("attacking","{c}",5),
	Blocking("blocking","{c}",6),
	Blocked("blocked","{b}",7),
	Animated("animated","{A}",8), // until end of turn
	SacrificeAtEndOfTurn("sacrifice at end of turn",null,9),
	RemoveAtEndOfTurn("remove from game at end of turn",null,10),
	RemoveAtEndOfYourTurn("remove from game at end of your turn",null,11),
	ExcludeManaSource("exclude as mana source",null,12),
	ExcludeFromCombat("exclude from combat",null,13),
	Kicked("kicked",null,14),
	;

	public static final int CLEANUP_MASK=
		Tapped.getMask()|
		Summoned.getMask()|
		DoesNotUntap.getMask()|
		RemoveAtEndOfYourTurn.getMask()|
		ExcludeManaSource.getMask()|
		ExcludeFromCombat.getMask()|
		Kicked.getMask();
	
	private final String description;
	private final String text;
	private final int index;
	private final int mask;
	
	private MagicPermanentState(final String description,final String text,final int index) {
		
		this.description=description;
		this.text=text;
		this.index=index;
		this.mask=1<<index;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public String getText() {
		
		return text;
	}
	
	public int getIndex() {
		
		return index;
	}
	
	public int getMask() {
		
		return mask;
	}
	
	public boolean hasState(final int flags) {
		
		return (flags&mask)!=0;
	}
}
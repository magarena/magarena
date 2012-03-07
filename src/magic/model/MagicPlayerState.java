package magic.model;

public enum MagicPlayerState {

	PreventAllDamage("prevent all damage that would be dealt to you or creatures you control this turn"),
	PreventAllCombatDamage("prevent all combat damage that would be dealt this turn"),
	Exhausted("creatures and lands don't untap during your next untap step"),
	WasDealtDamage("")
	;

	public static final int CLEANUP_MASK=Exhausted.getMask();
	
	private final String description;
	private final int mask;
	
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

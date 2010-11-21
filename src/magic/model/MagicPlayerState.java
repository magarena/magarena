package magic.model;

public enum MagicPlayerState {

	PreventAllDamage("prevent all damage that would be dealt to you or creatures you control this turn",0),
	Exhausted("creatures and lands don't untap during your next untap step",1),
	;

	public static final int CLEANUP_MASK=Exhausted.getMask();
	
	private final String description;
	private final int index;
	private final int mask;
	
	private MagicPlayerState(final String description,final int index) {
		
		this.description=description;
		this.index=index;
		this.mask=1<<index;
	}
	
	public String getDescription() {
		
		return description;
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
package magic.model;

public enum MagicIdentifierType {

	Permanent(0),
	PermanentTrigger(1),
	ItemOnStack(2);
	
	public static final int NR_OF_IDENTIFIERS=values().length;
	
	private final int index;
	
	private MagicIdentifierType(final int index) {
		
		this.index=index;
	}
	
	public int getIndex() {
		
		return index;
	}	
}
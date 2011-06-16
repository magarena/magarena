package magic.model;

public enum MagicCounterType {
	
	PlusOne("+1/+1","{+}"),
	MinusOne("-1/-1","{-}"),
	Charge("charge","{C}"),
	Feather("feather","{F}"),
	;

	public static final int NR_COUNTERS=MagicCounterType.values().length;
	
	private final String name;
	private final String text;
	
	private MagicCounterType(final String name,final String text) {
		this.name=name;
		this.text=text;
	}
	
	public String getName() {
		return name;
	}
	
	public String getText() {
		return text;
	}
		
	public int getIndex() {
		return ordinal();
	}
}

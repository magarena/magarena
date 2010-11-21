package magic.model;

public enum MagicCounterType {
	
	PlusOne("+1/+1","{+}",0),
	MinusOne("-1/-1","{-}",1),
	Charge("charge","{C}",2),
	Feather("feather","{F}",3),
	;

	public static final int NR_COUNTERS=MagicCounterType.values().length;
	
	private final String name;
	private final String text;
	private final int index;
	
	private MagicCounterType(final String name,final String text,final int index) {
		
		this.name=name;
		this.text=text;
		this.index=index;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getText() {
		
		return text;
	}
		
	public int getIndex() {
		
		return index;
	}
}
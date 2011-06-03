package magic.model;

public enum MagicType {

	Basic("Basic",0),
	Legendary("Legendary",1),
	Land("Land",2),
	Creature("Creature",3),
	Sorcery("Sorcery",4),
	Instant("Instant",5),
	Artifact("Artifact",6),
	Enchantment("Enchantment",7)
	;
	
	private final String name;
	private final int index;
	
	private MagicType(final String name,final int index) {
		
		this.name=name;
		this.index=index;
	}
	
	public String getName() {
		
		return name;
	}
	
	public int getIndex() {
		
		return index;
	}
	
	public int getMask() {
		return 1<<index;
	}
	
	public static MagicType getType(final String name) {
		
		for (final MagicType type : values()) {
			
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
    public boolean hasType(final int flags) {
		return (flags & getMask()) != 0;
	}
}

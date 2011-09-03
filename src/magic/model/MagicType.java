package magic.model;

public enum MagicType {

	Basic("Basic"),
	Legendary("Legendary"),
	Land("Land"),
	Creature("Creature"),
	Sorcery("Sorcery"),
	Instant("Instant"),
	Artifact("Artifact"),
	Enchantment("Enchantment")
	;
	
	private final String name;
    private final int mask;
	
	private MagicType(final String name) {
		this.name=name;
        this.mask=1<<ordinal();
	}
	
	public String getName() {
		return name;
	}
	
	public int getMask() {
		return mask;
	}
	
	public static MagicType getType(final String name) {
		for (final MagicType type : values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
        throw new RuntimeException("No corresponding MagicType for " + name);
	}
	
    public boolean hasType(final int flags) {
		return (flags & getMask()) != 0;
	}
}

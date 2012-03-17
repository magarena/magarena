package magic.model;

public enum MagicType {

    // these are supertypes
	Basic("Basic"),
	Legendary("Legendary"),
    Onging("Onging"),
    Snow("Snow"),
    World("World"),
	
    // these are card types
	Artifact("Artifact"),
	Creature("Creature"),
	Enchantment("Enchantment"),
	Instant("Instant"),
	Land("Land"),
	Plane("Plane"),
	Planeswalker("Planeswalker"),
	Scheme("Scheme"),
	Sorcery("Sorcery"),
    Tribal("Tribal"),
    Vanguard("Vanguard"),
	;
	
	private final String name;
    private final int mask;
	
	private MagicType(final String name) {
		this.name=name;
        this.mask=1<<ordinal();
	}
	
	private String getName() {
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

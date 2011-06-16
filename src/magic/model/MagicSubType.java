package magic.model;

public enum MagicSubType {
    //non creatures
	Forest("Forest"),
	Island("Island"),
	Mountain("Mountain"),
	Plains("Plains"),
	Swamp("Swamp"),
	Equipment("Equipment"),
	Aura("Aura"),

    //creatures
	Bat("Bat"),
	Beast("Beast"),
	Demon("Demon"),
	Dragon("Dragon"),
	Goblin("Goblin"),
	Knight("Knight"),
	Soldier("Soldier"),
	Illusion("Illusion"),
	Elemental("Elemental"),
	Construct("Construct"),
	;

	public static final int ALL_BASIC_LANDS = 
        Forest.getMask()|
        Island.getMask()|
        Mountain.getMask()|
        Plains.getMask()|
        Swamp.getMask();
	
	public static final int ALL_CREATURES=
        Bat.getMask()|
        Beast.getMask()|
        Demon.getMask()|
        Dragon.getMask()|
        Goblin.getMask()|
        Knight.getMask()|
        Soldier.getMask()|
        Illusion.getMask()|
        Elemental.getMask()|
        Construct.getMask();
	
	private final String name;
	private final int mask;
	
	private MagicSubType(final String name) {
		this.name=name;
		this.mask=1<<ordinal();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isCreatureType() {
	    return (ALL_CREATURES & mask) != 0;
	}
	
	public int getIndex() {
	    return ordinal();
	}
	
	public int getMask() {
		return mask;
	}
	
	public boolean hasSubType(final int flags) {
		return (flags & mask) != 0;
	}
	
	public static MagicSubType getSubType(final String name) {
		for (final MagicSubType type : values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}

package magic.model;

public enum MagicSubType {

	Forest("Forest",false,0),
	Island("Island",false,1),
	Mountain("Mountain",false,2),
	Plains("Plains",false,3),
	Swamp("Swamp",false,4),
	Equipment("Equipment",false,5),
	Aura("Aura",false,6),
	Bat("Bat",true,7),
	Beast("Beast",true,8),
	Demon("Demon",true,9),
	Dragon("Dragon",true,10),
	Goblin("Goblin",true,11),
	Knight("Knight",true,12),
	Soldier("Soldier",true,13),
	Illusion("Illusion",true,14),
	Elemental("Elemental",true,15),
	Construct("Construct",true,16),
	;

	public static final int ALL_BASIC_LANDS=Forest.getMask()|Island.getMask()|Mountain.getMask()|Plains.getMask()|Swamp.getMask();
	
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
	private final boolean creature;
	private final int index;
	private final int mask;
	
	private MagicSubType(final String name,final boolean creature,final int index) {
		
		this.name=name;
		this.creature=creature;
		this.index=index;
		this.mask=1<<index;
	}
	
	public String getName() {
		
		return name;
	}
	
	public boolean isCreatureType() {
		
		return creature;
	}
	
	public int getIndex() {
		
		return index;
	}
	
	public int getMask() {
		
		return mask;
	}
	
	public boolean hasSubType(final int flags) {
		
		return (flags&mask)!=0;
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

package magic.model;

import java.util.EnumSet;

public enum MagicSubType {
    //non creatures
	Forest,
	Island,
	Mountain,
	Plains,
	Swamp,
	Equipment,
	Aura,

    //creatures
    Advisor, Ally, Angel, Anteater, Antelope, Ape, Archer, Archon, Artificer,
    Assassin, Assembly_Worker, Atog, Aurochs, Avatar, Badger, Barbarian,
    Basilisk, Bat, Bear, Beast, Beeble, Berserker, Bird, Blinkmoth, Boar,
    Bringer, Brushwagg, Camarid, Camel, Caribou, Carrier, Cat, Centaur,
    Cephalid, Chimera, Citizen, Cleric, Cockatrice, Construct, Coward, Crab,
    Crocodile, Cyclops, Dauthi, Demon, Deserter, Devil, Djinn, Dragon, Drake,
    Dreadnought, Drone, Druid, Dryad, Dwarf, Efreet, Elder, Eldrazi, Elemental,
    Elephant, Elf, Elk, Eye, Faerie, Ferret, Fish, Flagbearer, Fox, Frog,
    Fungus, Gargoyle, Germ, Giant, Gnome, Goat, Goblin, Golem, Gorgon,
    Graveborn, Gremlin, Griffin, Hag, Harpy, Hellion, Hippo, Hippogriff,
    Homarid, Homunculus, Horror, Horse, Hound, Human, Hydra, Hyena, Illusion,
    Imp, Incarnation, Insect, Jellyfish, Juggernaut, Kavu, Kirin, Kithkin,
    Knight, Kobold, Kor, Kraken, Lammasu, Leech, Leviathan, Lhurgoyf, Licid,
    Lizard, Manticore, Masticore, Mercenary, Merfolk, Metathran, Minion,
    Minotaur, Monger, Mongoose, Monk, Moonfolk, Mutant, Myr, Mystic, Nautilus,
    Nephilim, Nightmare, Nightstalker, Ninja, Noggle, Nomad, Octopus, Ogre,
    Ooze, Orb, Orc, Orgg, Ouphe, Ox, Oyster, Pegasus, Pentavite, Pest,
    Phelddagrif, Phoenix, Pincher, Pirate, Plant, Praetor, Prism, Rabbit, Rat,
    Rebel, Reflection, Rhino, Rigger, Rogue, Salamander, Samurai, Sand,
    Saproling, Satyr, Scarecrow, Scorpion, Scout, Serf, Serpent, Shade, Shaman,
    Shapeshifter, Sheep, Siren, Skeleton, Slith, Sliver, Slug, Snake, Soldier,
    Soltari, Spawn, Specter, Spellshaper, Sphinx, Spider, Spike, Spirit,
    Splinter, Sponge, Squid, Squirrel, Starfish, Surrakar, Survivor, Tetravite,
    Thalakos, Thopter, Thrull, Treefolk, Triskelavite, Troll, Turtle, Unicorn,
    Vampire, Vedalken, Viashino, Volver, Wall, Warrior, Weird, Whale, Wizard,
    Wolf, Wolverine, Wombat, Worm, Wraith, Wurm, Yeti, Zombie, Zubera;

	public static final EnumSet<MagicSubType> ALL_BASIC_LANDS = EnumSet.range(Forest, Swamp);
	
    public static final EnumSet<MagicSubType> ALL_CREATURES = EnumSet.range(Advisor, Zubera);
	
	private MagicSubType() {
	}
	
	public boolean isCreatureType() {
	    return ALL_CREATURES.contains(this);
	}
	
	public boolean hasSubType(final EnumSet flags) {
		return flags.contains(this);
	}
	
	public static MagicSubType getSubType(final String name) {
		for (final MagicSubType type : values()) {
			if (type.toString().equalsIgnoreCase(name)) {
				return type;
			}
		}
        System.err.println("ERROR: Unknown subtype " + name);
        System.exit(1);
        return null;
	}
}

package magic.model;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public enum MagicSubType {
    //basic land subtypes
    Plains,
    Island,
    Swamp,
    Mountain,
    Forest,

    //other land subtypes
    Desert,
    Lair,
    Locus,
    Mine,
    Power_Plant,
    Tower,
    Urza_s() {
        @Override
        public String toString() {
            return "Urza's";
        }
    },
    Gate,

    //artifact subtypes
    Clue,
    Contraption,
    Equipment,
    Fortification,
    Treasure,
    Vehicle,

    //enchantment subtypes
    Aura,
    Cartouche,
    Curse,
    Shrine,

    //instant/sorcery (spell) subtypes
    Arcane,
    Trap,

    //planeswalker subtypes
    Ajani, Arlinn, Ashiok, Bolas, Chandra, Dack, Daretti, Domri, Dovin, Elspeth, Freyalise, Garruk, Gideon, Huatli, Jace, Karn, Kaya, Kiora, Koth,
    Liliana, Nahiri, Narset, Nissa, Nixilis, Ral, Saheeli, Samut, Sarkhan, Sorin, Teferi, Tezzeret, Venser, Tamiyo, Tibalt, Ugin, Vraska, Xenagos,

    //special handling for Assembly_Worker
    Assembly_Worker() {
        @Override
        public String toString() {
            return "Assembly-Worker";
        }
    },

    //creature and tribal subtypes
    Advisor, Aetherborn, Ally, Angel, Anteater, Antelope, Ape, Archer, Archon, Artificer,
    Assassin, Atog, Aurochs, Avatar, Badger, Barbarian, Basilisk,
    Bat, Bear, Beast, Beeble, Berserker, Bird, Blinkmoth, Boar, Bringer, Brushwagg,
    Camarid, Camel, Caribou, Carrier, Cat, Centaur, Cephalid, Chimera, Citizen,
    Cleric, Cockatrice, Construct, Coward, Crab, Crocodile, Cyclops, Dauthi, Demon,
    Deserter, Devil, Dinosaur, Djinn, Dragon, Drake, Dreadnought, Drone, Druid, Dryad, Dwarf,
    Efreet, Elder, Eldrazi, Elemental, Elephant, Elf, Elk, Eye, Faerie, Ferret,
    Fish, Flagbearer, Fox, Frog, Fungus, Gargoyle, Germ, Giant, Gnome, Goat,
    Goblin, God, Golem, Gorgon, Graveborn, Gremlin, Griffin, Hag, Harpy, Hellion, Hippo,
    Hippogriff, Homarid, Homunculus, Horror, Horse, Hound, Human, Hydra, Hyena,
    Illusion, Imp, Incarnation, Insect, Jackal, Jellyfish, Juggernaut, Kavu, Kirin,
    Kithkin, Knight, Kobold, Kor, Kraken, Lamia, Lammasu, Leech, Leviathan, Lhurgoyf,
    Licid, Lizard, Manticore, Masticore, Mercenary, Merfolk, Metathran, Minion,
    Minotaur, Mole, Monger, Mongoose, Monk, Monkey, Moonfolk, Mutant, Myr, Mystic, Naga, Nautilus,
    Nephilim, Nightmare, Nightstalker, Ninja, Noggle, Nomad, Nymph, Octopus, Ogre, Ooze,
    Orb, Orc, Orgg, Ouphe, Ox, Oyster, Pegasus, Pentavite, Pest, Phelddagrif,
    Phoenix, Pilot, Pincher, Pirate, Plant, Praetor, Prism, Processor, Rabbit, Rat, Rebel,
    Reflection, Rhino, Rigger, Rogue, Sable, Salamander, Samurai, Sand, Saproling, Satyr,
    Scarecrow, Scion, Scorpion, Scout, Serf, Serpent, Servo, Shade, Shaman, Shapeshifter, Sheep,
    Siren, Skeleton, Slith, Sliver, Slug, Snake, Soldier, Soltari, Spawn, Specter,
    Spellshaper, Sphinx, Spider, Spike, Spirit, Splinter, Sponge, Squid, Squirrel,
    Starfish, Surrakar, Survivor, Tetravite, Thalakos, Thopter, Thrull, Treefolk,
    Trilobite, Triskelavite, Troll, Turtle, Unicorn, Vampire, Vedalken, Viashino, Volver,
    Wall, Warrior, Weird, Werewolf, Whale, Wizard, Wolf, Wolverine, Wombat, Worm,
    Wraith, Wurm, Yeti, Zombie, Zubera,

    //plane subtypes
    Alara, Arkhos, Bolas_s_Meditation_Realm, Dominaria, Equilor, Iquatana, Ir,
    Kaldheim, Kamigawa, Karsus, Kinshala, Lorwyn, Luvion, Mercadia, Mirrodin,
    Moag, Muraganda, Phyrexia, Pyrulea, Rabiah, Rath, Ravnica, Segovia,
    Serra_s_Realm, Shadowmoor, Shandalar, Ulgrotha, Valla, Wildfire, Zendikar

    ;

    public static final Set<MagicSubType> ALL_BASIC_LANDS = EnumSet.range(Plains, Forest);

    public static final Set<MagicSubType> ALL_LANDS = EnumSet.range(Plains, Gate);

    public static final Set<MagicSubType> ALL_ARTIFACTS = EnumSet.range(Contraption, Vehicle);

    public static final Set<MagicSubType> ALL_ENCHANTMENTS = EnumSet.range(Aura, Shrine);

    public static final Set<MagicSubType> ALL_CREATURES = EnumSet.range(Assembly_Worker, Zubera);

    public static final Set<MagicSubType> ALL_PLANESWALKERS = EnumSet.range(Ajani, Xenagos);

    public static final Set<MagicSubType> FILTER_SUBTYPES = EnumSet.range(Plains,Zubera);

    private MagicSubType() {
    }

    public static MagicSubType getSubType(final String name) {
        //final String cname = name.replaceAll("[^A-Za-z0-9]", "_");
        for (final MagicSubType type : values()) {
            if (type.toString().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new RuntimeException("unknown subtype \"" + name + "\"");
    }

    public static EnumSet<MagicSubType> getSubTypes(final String[] subTypeNames) {
        final EnumSet<MagicSubType> givenSubTypeFlags = EnumSet.noneOf(MagicSubType.class);
        for (final String subTypeName : subTypeNames) {
            givenSubTypeFlags.add(getSubType(subTypeName));
        }
        return givenSubTypeFlags;
    }

    public static EnumSet<MagicSubType> prefixSubTypes(final List<String> tokens) {
        final EnumSet<MagicSubType> subTypes = EnumSet.noneOf(MagicSubType.class);
        boolean matched = true;
        for (Iterator<String> iterator = tokens.iterator(); iterator.hasNext() && matched;) {
            final String name = iterator.next();
            matched = false;
            for (final MagicSubType type : values()) {
                if (type.toString().equalsIgnoreCase(name)) {
                    matched = true;
                    subTypes.add(type);
                    iterator.remove();
                    break;
                }
            }
        }
        return subTypes;
    }

    @SuppressWarnings("incomplete-switch")
    public MagicAbility getLandwalkAbility() {
        switch (this) {
            case Plains: return MagicAbility.Plainswalk;
            case Island: return MagicAbility.Islandwalk;
            case Swamp: return MagicAbility.Swampwalk;
            case Mountain: return MagicAbility.Mountainwalk;
            case Forest: return MagicAbility.Forestwalk;
            default: throw new RuntimeException("No landwalk ability for MagicSubType " + this);
        }
    }
}

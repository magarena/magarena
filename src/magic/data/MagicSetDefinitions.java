package magic.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import magic.MagicMain;
import magic.model.MagicCardDefinition;
import magic.model.MagicSetDefinition;
import magic.data.MagicDate;

public class MagicSetDefinitions {

    public static enum MagicSets {

       _2ED ("Unlimited Edition", new MagicDate(1998,12,1)),
       _3ED ("Revised Edition", new MagicDate(1994,3)),
       _4ED ("Fourth Edition", new MagicDate(1995,3)),
       _5DN ("Fifth Dawn", new MagicDate(2004,5,22)),
       _5ED ("Fifth Edition", new MagicDate(1997,3,27)),
       _6ED ("Classic (Sixth Edition)", new MagicDate(1999,4,28)),
       _7ED ("Seventh Edition", new MagicDate(2001,4,11)),
       _8ED ("Core Set - Eighth Edition", new MagicDate(2003,7,28)),
       _9ED ("Core Set - Ninth Edition", new MagicDate(2005,7,29)),
       _10E ("Core Set - Tenth Edition", new MagicDate(2007,7,13)),
        ALA ("Shards of Alara", new MagicDate(2008,10,3)),
        ALL ("Alliances", new MagicDate(1996,7,10)),
        APC ("Apocalypse", new MagicDate(2001,6,4)),
        ARB ("Alara Reborn", new MagicDate(2009,4,30)),
        ARN ("Arabian Nights", new MagicDate(1993,12)),
        ATQ ("Antiquities", new MagicDate(1994,3)),
        AVR ("Avacyn Restored", new MagicDate(2012,5,4)),
        BNG ("Born of the Gods", new MagicDate(2014,2,7)),
        BOK ("Betrayers of Kamigawa", new MagicDate(2005,2,4)),
        C13 ("Commander (2013 Edition)", new MagicDate(2013,11,1)),
        CFX ("Conflux", new MagicDate(2009,2,6)),
        CHK ("Champions of Kamigawa", new MagicDate(2004,10,1)),
        CHR ("Chronicles", new MagicDate(1995,7)),
        CMD ("Commander", new MagicDate(2011,6,17)),
        CNS ("Conspiracy", new MagicDate(2014,6,6)),
        CSP ("Coldsnap", new MagicDate(2006,7,21)),
        DGM ("Dragon's Maze", new MagicDate(2013,5,3)),
        DIS ("Dissension", new MagicDate(2006,5,6)),
        DKA ("Dark Ascension", new MagicDate(2012,2,3)),
        DRK ("The Dark", new MagicDate(1994,8)),
        DST ("Darksteel", new MagicDate(2004,2,6)),
        EVE ("Eventide", new MagicDate(2008,7,25)),
        EXO ("Exodus", new MagicDate(1998,6,15)),
        FEM ("Fallen Empires", new MagicDate(1994,11)),
        FUT ("Future Sight", new MagicDate(2007,5,4)),
        GPT ("Guildpact", new MagicDate(2006,2,3)),
        GTC ("Gatecrash", new MagicDate(2013,2,1)),
        HML ("Homelands", new MagicDate(1995,10)),
        ICE ("Ice Age", new MagicDate(1995,6)),
        INV ("Invasion", new MagicDate(2000,10,2)),
        ISD ("Innistrad", new MagicDate(2011,11,30)),
        JOU ("Journey into Nyx", new MagicDate(2014,5,2)),
        JUD ("Judgment", new MagicDate(2002,5,27)),
        LEA ("Limited Edition Alpha", new MagicDate(1993,8,5)),
        LEB ("Limited Edition Beta", new MagicDate(1993,10)),
        LEG ("Legends", new MagicDate(1994,6)),
        LGN ("Legions", new MagicDate(2003,2,3)),
        LRW ("Lorwyn", new MagicDate(2007,10,12)),
        M10 ("Magic 2010", new MagicDate(2009,7,17)),
        M11 ("Magic 2011", new MagicDate(2010,7,16)),
        M12 ("Magic 2012", new MagicDate(2011,7,9)),
        M13 ("Magic 2013", new MagicDate(2012,7,7)),
        M14 ("Magic 2014", new MagicDate(2013,7,19)),
        M15 ("Magic 2015", new MagicDate(2014,7,18)),
        MBS ("Mirrodin Besieged", new MagicDate(2011,2,4)),
        MIR ("Mirage", new MagicDate(1996,10,7)),
        MMQ ("Mercadian Masques", new MagicDate(1999,10,4)),
        MOR ("Morningtide", new MagicDate(2008,2,1)),
        MRD ("Mirrodin", new MagicDate(2003,10,3)),
        NEM ("Nemesis", new MagicDate(2000,2,14)),
        NPH ("New Phyrexia", new MagicDate(2011,5,13)),
        ODY ("Odyssey", new MagicDate(2001,10,1)),
        ONS ("Onslaught", new MagicDate(2002,10,7)),
        PC2 ("Planechase (2012 Edition)", new MagicDate(2012,6,1)),
        PCY ("Prophecy", new MagicDate(2000,6,5)),
        PLC ("Planar Chaos", new MagicDate(2007,2,2)),
        PLS ("Planeshift", new MagicDate(2001,2,5)),
        PO2 ("Portal Second Age", new MagicDate(1998,6)),
        POR ("Portal", new MagicDate(1997,6)),
        PTK ("Portal Three Kingdoms", new MagicDate(1999,5)),
        RAV ("Ravnica: City of Guilds", new MagicDate(2005,10,7)),
        ROE ("Rise of the Eldrazi", new MagicDate(2010,4,23)),
        RTR ("Return to Ravnica", new MagicDate(2012,10,5)),
        S00 ("Starter 2000", new MagicDate(2000,7)),
        S99 ("Starter 1999", new MagicDate(1999,7)),
        SCG ("Scourge", new MagicDate(2003,5,26)),
        SHM ("Shadowmoor", new MagicDate(2008,5,2)),
        SOK ("Saviors of Kamigawa", new MagicDate(2005,6,3)),
        SOM ("Scars of Mirrodin", new MagicDate(2010,10,1)),
        STH ("Stronghold", new MagicDate(1998,3,2)),
        THS ("Theros", new MagicDate(2013,9,27)),
        TMP ("Tempest", new MagicDate(1997,10,13)),
        TOR ("Torment", new MagicDate(2002,2,4)),
        TSB ("Time Spiral", new MagicDate(2006,10,6)),
        TSP ("Time Spiral", new MagicDate(2006,10,6)),
        UDS ("Urza's Destiny", new MagicDate(1999,6,7)),
        ULG ("Urza's Legacy", new MagicDate(1999,2,15)),
        USG ("Urza's Saga", new MagicDate(1998,10,12)),
        VIS ("Visions", new MagicDate(1997,2,3)),
        WTH ("Weatherlight", new MagicDate(1997,6,9)),
        WWK ("Worldwake", new MagicDate(2010,2,5)),
        ZEN ("Zendikar", new MagicDate(2009,10,2));

        private final String setName;
        private final MagicDate releaseDate;

        private MagicSets(final String name, final MagicDate release) {
            this.setName = name;
            this.releaseDate = release;
        }

        public String getSetName() {
            return setName;
        }
        
        public MagicDate getReleaseDate() {
            return releaseDate;
        }

    }

    private static final HashMap<MagicSets, MagicSetDefinition> loadedSets = new HashMap<MagicSets, MagicSetDefinition>();

    private static MagicSetDefinition loadMagicSet(final MagicSets magicSet) {
        String content = null;
        final String filename = "/magic/data/sets/" + magicSet.toString().replace("_", "") + ".txt";
        try {
            content = FileIO.toStr(MagicMain.rootFrame.getClass().getResourceAsStream(filename));
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + filename);
            return null;
        }

        final MagicSetDefinition magicSetDef = new MagicSetDefinition(magicSet.toString());

        try (final Scanner sc = new Scanner(content)) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine();
                magicSetDef.add(line.trim());
            }
        }

        return magicSetDef;

    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<String>();
        for (MagicSets magicSet : MagicSets.values()) {
            values.add(magicSet.toString().replace("_", "") + " " + magicSet.getSetName());
        }
        return values.toArray(new String[values.size()]);
    }

    public static boolean isCardInSet(MagicCardDefinition card, MagicSets magicSet) {
        if (!loadedSets.containsKey(magicSet)) {
            loadedSets.put(magicSet, loadMagicSet(magicSet));
        }
        return loadedSets.get(magicSet).contains(card.getName());
    }

    public static void clearLoadedSets() {
        loadedSets.clear();
    }

}

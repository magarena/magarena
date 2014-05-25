package magic.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import magic.MagicMain;
import magic.model.MagicCardDefinition;
import magic.model.MagicSetDefinition;

public class MagicSetDefinitions {

    public static enum MagicSets {

       _2ED ("Unlimited Edition"),
       _3ED ("Revised Edition"),
       _4ED ("Fourth Edition"),
       _5DN ("Fifth Dawn"),
       _5ED ("Fifth Edition"),
       _6ED ("Classic (Sixth Edition)"),
       _7ED ("Seventh Edition"),
       _8ED ("Core Set - Eighth Edition"),
       _9ED ("Core Set - Ninth Edition"),
       _10E ("Core Set - Tenth Edition"),
        ALA ("Shards of Alara"),
        ALL ("Alliances"),
        APC ("Apocalypse"),
        ARB ("Alara Reborn"),
        ARN ("Arabian Nights"),
        ATQ ("Antiquities"),
        AVR ("Avacyn Restored"),
        BNG ("Born of the Gods"),
        BOK ("Betrayers of Kamigawa"),
        C13 ("Commander (2013 Edition)"),
        CFX ("Conflux"),
        CHK ("Champions of Kamigawa"),
        CHR ("Chronicles"),
        CMD ("Commander"),
        CNS ("Conspiracy"),
        CSP ("Coldsnap"),
        DGM ("Dragon's Maze"),
        DIS ("Dissension"),
        DKA ("Dark Ascension"),
        DRK ("The Dark"),
        DST ("Darksteel"),
        EVE ("Eventide"),
        EXO ("Exodus"),
        FEM ("Fallen Empires"),
        FUT ("Future Sight"),
        GPT ("Guildpact"),
        GTC ("Gatecrash"),
        HML ("Homelands"),
        ICE ("Ice Age"),
        INV ("Invasion"),
        ISD ("Innistrad"),
        JOU ("Journey into Nyx"),
        JUD ("Judgment"),
        LEA ("Limited Edition Alpha"),
        LEB ("Limited Edition Beta"),
        LEG ("Legends"),
        LGN ("Legions"),
        LRW ("Lorwyn"),
        M10 ("Magic 2010"),
        M11 ("Magic 2011"),
        M12 ("Magic 2012"),
        M13 ("Magic 2013"),
        M14 ("Magic 2014"),
        MBS ("Mirrodin Besieged"),
        MIR ("Mirage"),
        MMQ ("Mercadian Masques"),
        MOR ("Morningtide"),
        MRD ("Mirrodin"),
        NEM ("Nemesis"),
        NPH ("New Phyrexia"),
        ODY ("Odyssey"),
        ONS ("Onslaught"),
        PC2 ("Planechase (2012 Edition)"),
        PCY ("Prophecy"),
        PLC ("Planar Chaos"),
        PLS ("Planeshift"),
        PO2 ("Portal Second Age"),
        POR ("Portal"),
        PTK ("Portal Three Kingdoms"),
        RAV ("Ravnica: City of Guilds"),
        ROE ("Rise of the Eldrazi"),
        RTR ("Return to Ravnica"),
        S00 ("Starter 2000"),
        S99 ("Starter 1999"),
        SCG ("Scourge"),
        SHM ("Shadowmoor"),
        SOK ("Saviors of Kamigawa"),
        SOM ("Scars of Mirrodin"),
        STH ("Stronghold"),
        THS ("Theros"),
        TMP ("Tempest"),
        TOR ("Torment"),
        TSB ("Time Spiral"),
        TSP ("Time Spiral"),
        UDS ("Urza's Destiny"),
        ULG ("Urza's Legacy"),
        USG ("Urza's Saga"),
        VIS ("Visions"),
        WTH ("Weatherlight"),
        WWK ("Worldwake"),
        ZEN ("Zendikar");

        private final String setName;

        private MagicSets(final String name) {
            this.setName = name;
        }

        public String getSetName() {
            return setName;
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

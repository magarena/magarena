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

       _10E ("10th Edition"),
       _2ED ("Unlimited Edition"),
       _3ED ("Revised Edition"),
       _4ED (""),
       _5DN (""),
       _5ED (""),
       _6ED (""),
       _7ED (""),
       _8ED (""),
       _9ED (""),
        ALA (""),
        ALL (""),
        APC (""),
        ARB (""),
        ARN ("Arabian Nights"),
        ATQ (""),
        AVR (""),
        BNG (""),
        BOK (""),
        C13 (""),
        CFX (""),
        CHK (""),
        CHR (""),
        CMD (""),
        CSP (""),
        DGM (""),
        DIS (""),
        DKA (""),
        DRK (""),
        DST (""),
        EVE (""),
        EXO (""),
        FEM (""),
        FUT (""),
        GPT (""),
        GTC (""),
        HML (""),
        ICE (""),
        INV (""),
        ISD (""),
        JOU (""),
        JUD (""),
        LEA (""),
        LEB (""),
        LEG ("Legends"),
        LGN (""),
        LRW (""),
        M10 (""),
        M11 (""),
        M12 (""),
        M13 (""),
        M14 (""),
        MBS (""),
        MIR (""),
        MMQ (""),
        MOR (""),
        MRD (""),
        NEM (""),
        NPH (""),
        ODY (""),
        ONS (""),
        PC2 (""),
        PCY (""),
        PLC (""),
        PLS (""),
        PO2 (""),
        POR (""),
        PTK (""),
        RAV (""),
        ROE (""),
        RTR (""),
        S00 (""),
        S99 (""),
        SCG (""),
        SHM (""),
        SOK (""),
        SOM (""),
        STH (""),
        THS (""),
        TMP (""),
        TOR (""),
        TSB (""),
        TSP (""),
        UDS (""),
        ULG (""),
        USG (""),
        VIS (""),
        WTH (""),
        WWK (""),
        ZEN ("");

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

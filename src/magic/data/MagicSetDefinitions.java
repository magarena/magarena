package magic.data;

import magic.model.MagicCardDefinition;
import magic.model.MagicSetDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import magic.utility.MagicFileSystem;

public class MagicSetDefinitions {

    private static final HashMap<MagicSets, MagicSetDefinition> loadedSets = new HashMap<>();

    private static MagicSetDefinition loadMagicSet(final MagicSets magicSet) {

        final MagicSetDefinition magicSetDef = new MagicSetDefinition(magicSet.toString());

        try (final Scanner sc = new Scanner(getSetFileContent(magicSet))) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine();
                magicSetDef.add(line.trim());
            }
        }

        return magicSetDef;

    }

    private static String getSetFileContent(final MagicSets magicSet) {
        final String filename = "/magic/data/sets/" + magicSet.toString().replace("_", "") + ".txt";
        try (final InputStream inputStream = MagicFileSystem.getJarResourceStream(filename)) {
            return inputStream != null ? FileIO.toStr(inputStream) : "";
        } catch (final IOException ex) {
            System.err.println(filename + " : " + ex.getMessage());
            return "";
        }
    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
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

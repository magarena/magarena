package magic.data;

import magic.model.MagicCardDefinition;
import magic.model.MagicSetDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import magic.utility.MagicResources;

public class MagicSetDefinitions {

    private static final HashMap<MagicSets, MagicSetDefinition> loadedSets = new HashMap<>();

    private static MagicSetDefinition loadMagicSet(final MagicSets magicSet) {

        final MagicSetDefinition magicSetDef = new MagicSetDefinition(magicSet.toString());

        try (final Scanner sc = new Scanner(MagicResources.getFileContent(magicSet))) {
            while (sc.hasNextLine()) {
                magicSetDef.add(sc.nextLine());
            }
        }

        return magicSetDef;

    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (MagicSets magicSet : MagicSets.values()) {
            values.add(magicSet.toString().replace("_", "") + " " + magicSet.getSetName());
        }
        return values.toArray(new String[0]);
    }

    public static boolean isCardInSet(final MagicCardDefinition card, final MagicSets magicSet) {
        if (!loadedSets.containsKey(magicSet)) {
            loadedSets.put(magicSet, loadMagicSet(magicSet));
        }
        return loadedSets.get(magicSet).containsCard(card);
    }

    public static void clearLoadedSets() {
        loadedSets.clear();
    }

}

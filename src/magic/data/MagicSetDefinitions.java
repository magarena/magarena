package magic.data;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import magic.model.MagicCardDefinition;
import magic.model.MagicSetDefinition;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
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

    public static boolean isCardInSet(final MagicCardDefinition card, final MagicSets magicSet) {
        if (!loadedSets.containsKey(magicSet)) {
            loadedSets.put(magicSet, loadMagicSet(magicSet));
        }
        return loadedSets.get(magicSet).containsCard(card);
    }

    public static void clearLoadedSets() {
        loadedSets.clear();
    }

    /**
     * Creates a csv file containing a list of all sets and the
     * total number of playable/unplayable/potential cards in each.
     */
    public static void createSetStatistics() throws IOException {

        final List<MagicCardDefinition> cards = CardDefinitions.getAllCards();

        final Path savePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("CardStatistics.csv");
        try (final PrintWriter writer = new PrintWriter(savePath.toFile())) {
            writer.println("Set,Cards,Playable,Unimplemented,Potential,No Status");
            for (MagicSets set : MagicSets.values()) {
                int totalPlayable = 0;
                int totalUnplayable = 0;
                int totalPotential = 0;
                int totalStatus = 0;
                for (MagicCardDefinition card : cards) {
                    if (isCardInSet(card, set)) {
                        if (!card.hasStatus() && CardDefinitions.isPotential(card)) {
                            totalStatus++;
                        }
                        if (card.isPlayable()) {
                            if (card.isInvalid()) {
                                if (CardDefinitions.isPotential(card)) {
                                    totalPotential++;
                                } else {
                                    totalUnplayable++;
                                }
                            } else {
                                totalPlayable++;
                            }
                        }
                    }
                }
                writer.printf("%s %s,%d,%d,%d,%d,%d\n",
                    set.name().replaceAll("_", ""),
                    set.getSetName(),
                    totalPlayable + totalUnplayable + totalPotential,
                    totalPlayable,
                    totalUnplayable + totalPotential,
                    totalPotential,
                    totalStatus
                );
            }
        }
        Desktop.getDesktop().open(MagicFileSystem.getDataPath(DataPath.LOGS).toFile());
    }

}

package magic.data;

import magic.generator.RandomDeckGenerator;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import magic.MagicUtility;

/**
 * Load card definitions from deckgenerators.txt
 */
public class DeckGenerators {

    private static final DeckGenerators INSTANCE = new DeckGenerators();

    private static final String FILENAME = "deckgenerators.txt";

    private final Map<String, RandomDeckGenerator> generatorsMap;

    private DeckGenerators() {
        generatorsMap = new TreeMap<String, RandomDeckGenerator>();
    }

    public Set<String> getGeneratorNames() {
        return generatorsMap.keySet();
    }

    private void addDeckGenerator(final String name) {
        // find class
        final String cname = name.replaceAll("[^A-Za-z0-9]", "_");
        try {
            generatorsMap.put(
                    name,
                    Class.forName("magic.generator." + cname + "_DeckGenerator")
                    .asSubclass(RandomDeckGenerator.class)
                    .newInstance()
                    );
            if (MagicUtility.showStartupStats()) {
                System.err.println("added deck generator " + name);
            }

        } catch (final ClassNotFoundException ex) {
            System.err.println("WARNING. Unable to find deck generator class for " + name);
        } catch (final ClassCastException ex) {
            throw new RuntimeException(ex);
        } catch (final InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public RandomDeckGenerator getDeckGenerator(final String name) {
        return generatorsMap.get(name);
    }

    private void loadDeckGenerators(final String filename) {
        final InputStream stream = this.getClass().getResourceAsStream(filename);
        String content = null;
        try { // load file
            content = FileIO.toStr(stream);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load deck generators from " + filename);
            return;
        }

        // add entries
        try (final Scanner sc = new Scanner(content)) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                if (line.length() == 0) {
                    // blank line
                } else {
                    addDeckGenerator(line);
                }
            }
        }

    }

    public void loadDeckGenerators() {
        loadDeckGenerators(FILENAME);
        if (MagicUtility.showStartupStats()) {
            System.err.println(getNrGenerators()+ " deck generators loaded");
        }
    }

    public int getNrGenerators() {
        return generatorsMap.size();
    }

    public static DeckGenerators getInstance() {
        return INSTANCE;
    }

    /**
     * Generates a cube-limited random deck for the specified player.
     * <p>
     * This also includes the various random theme decks (like "Fairy Horde", "Token Madness", etc).
     */
    private static void setRandomColorDeck(final MagicPlayerDefinition player) {
        final MagicCubeDefinition cubeDefinition = CubeDefinitions.getCubeDefinition(DuelConfig.getInstance().getCube());
        final RandomDeckGenerator generator = new RandomDeckGenerator(cubeDefinition);
        player.generateDeck(generator);
    }

    /**
     * Assigns a random deck to the specified player.
     * <p>
     * This can be generated from scratch or an existing deck file.
     */
    public static void setRandomDeck(final MagicPlayerDefinition player) {
        final boolean isUnspecifiedGenerator = (player.getDeckGenerator() == null) && (player.getDeckProfile().getNrOfColors() == 0);
        final boolean loadRandomDeckFile = player.getDeckProfile().isPreConstructed() || isUnspecifiedGenerator;
        if (loadRandomDeckFile) {
            DeckUtils.loadRandomDeckFile(player);
        } else {
            setRandomColorDeck(player);
        }
    }
}

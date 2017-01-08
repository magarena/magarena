package magic.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import magic.generator.RandomDeckGenerator;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.utility.DeckUtils;
import magic.utility.FileIO;
import magic.utility.MagicSystem;

/**
 * Load card definitions from deckgenerators.txt
 */
public class DeckGenerators {

    private static final DeckGenerators INSTANCE = new DeckGenerators();

    private static final String FILENAME = "deckgenerators.txt";

    private final Map<String, Class<? extends RandomDeckGenerator>> generatorsClass;
    private final Map<String, RandomDeckGenerator> generatorsMap;

    private DeckGenerators() {
        generatorsClass = new TreeMap<String, Class<? extends RandomDeckGenerator>>();
        generatorsMap = new TreeMap<String, RandomDeckGenerator>();
    }

    public Set<String> getGeneratorNames() {
        return generatorsClass.keySet();
    }

    private void addDeckGenerator(final String name) {
        // find class
        final String cname = name.replaceAll("[^A-Za-z0-9]", "_");
        try {
            generatorsClass.put(
                name,
                Class.forName("magic.generator." + cname + "_DeckGenerator")
                .asSubclass(RandomDeckGenerator.class)
            );
            if (MagicSystem.showStartupStats()) {
                System.err.println("added deck generator " + name);
            }

        } catch (final ClassNotFoundException ex) {
            System.err.println("WARNING. Unable to find deck generator class for " + name);
        } catch (final ClassCastException ex) {
            throw new RuntimeException(ex);
        }
    }

    public RandomDeckGenerator getDeckGenerator(final String name) {
        if (generatorsClass.containsKey(name) && generatorsMap.containsKey(name) == false) {
            try {
                generatorsMap.put(name, generatorsClass.get(name).newInstance());
            } catch (final InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (final IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
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
        if (MagicSystem.showStartupStats()) {
            System.err.println(getNrGenerators()+ " deck generators loaded");
        }
    }

    public int getNrGenerators() {
        return generatorsClass.size();
    }

    public static DeckGenerators getInstance() {
        return INSTANCE;
    }

    /**
     * Generates a cube-limited random deck for the specified player.
     * <p>
     * This also includes the various random theme decks (like "Fairy Horde", "Token Madness", etc).
     */
    private static void setRandomColorDeck(final DuelPlayerConfig player) {
        final MagicDeckProfile deckProfile = player.getDeckProfile();
        final MagicDeck deck = player.getDeck();
        final MagicFormat cubeDefinition = DuelConfig.getInstance().getCube();
        final RandomDeckGenerator defaultGenerator = new RandomDeckGenerator(cubeDefinition);
        final RandomDeckGenerator customGenerator = DeckGenerators.getDeckGenerator(deckProfile);
        if (customGenerator == null) {
            defaultGenerator.generateDeck(MagicDeck.DEFAULT_SIZE, deckProfile, deck);
        } else {
            customGenerator.setCubeDefinition(cubeDefinition);
            customGenerator.generateDeck(MagicDeck.DEFAULT_SIZE, deckProfile, deck);
        }
        DeckGenerator.addBasicLandsToDeck(deck, deckProfile, MagicDeck.DEFAULT_SIZE);
        deck.setFilename("Random_" + deckProfile.getColorText().toUpperCase() + "_deck");
        deck.setDescription("Built using random color deck generator.");
    }

    /**
     * Assigns a random deck to the specified player.
     * <p>
     * This can be generated from scratch or an existing deck file.
     */
    public static void setRandomDeck(final DuelPlayerConfig player) {
        final boolean isUnspecifiedGenerator = (getDeckGenerator(player.getDeckProfile()) == null) && (player.getDeckProfile().getNrOfColors() == 0);
        final boolean loadRandomDeckFile = player.getDeckProfile().isPreConstructed() || isUnspecifiedGenerator;
        if (loadRandomDeckFile) {
            DeckUtils.loadRandomDeckFile(player);
        } else {
            setRandomColorDeck(player);
        }
    }

    public static RandomDeckGenerator getDeckGenerator(final MagicDeckProfile deckProfile) {
        if (deckProfile.getDeckGeneratorName() != null) {
            return DeckGenerators.getInstance().getDeckGenerator(deckProfile.getDeckGeneratorName());
        } else {
            return null;
        }
    }

}

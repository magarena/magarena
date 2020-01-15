package magic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.CardDefinitions;
import magic.data.DuelConfig;
import magic.exception.handler.ConsoleExceptionHandler;
import magic.headless.HeadlessGameController;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;
import magic.model.player.AiProfile;
import magic.test.TestGameBuilder;
import magic.utility.MagicSystem;
import magic.utility.ProgressReporter;

/**
 * Special kind of headless game, where every implemented
 * card and token is used for a very short game in a predefined scenario.
 */
public class CardTest {
    private static void parseCommandLine(CommandLineArgs cmdline) {
        MagicAI.setMaxThreads(cmdline.getMaxThreads());
        MagicSystem.setIsDevMode(cmdline.isDevMode());
    }

    public static void main(final CommandLineArgs cmdline) {
        Thread.setDefaultUncaughtExceptionHandler(new ConsoleExceptionHandler());

        parseCommandLine(cmdline);

        MagicSystem.initialize(new ProgressReporter());

        System.out.println("=================== Card test ====================");

        Collection<MagicCardDefinition> allC = CardDefinitions.getAllPlayableCardDefs();

        double totalTime = 0;
        int totalCards = 0;

        Set<String> skip = new HashSet<>();
        String skipList = cmdline.getSkipList();
        if (skipList != null) {
            try (Stream<String> lines = Files.lines(Paths.get(skipList))) {
                skip = lines.collect(Collectors.toSet());
                System.out.println("Skipping " + skip.size() + " cards from file: " + skipList);
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to load skiplist from file " + skipList, e);
            }
        }

        List<String> cards = new ArrayList<>();
        List<MagicCardDefinition> tokens = new ArrayList<>();

        for (MagicCardDefinition def : allC) {
            String name = def.getName();
            if (name.isEmpty()) {
                // Manifest, morph, or alike ...
                System.out.println("Def without name: " + def.getDistinctName());
                continue;
            }
            if (skip.contains(name)) continue;
            if (def.isToken()) tokens.add(def);
            else cards.add(name);
        }

        System.out.println("Cards: " + cards.size());
        System.out.println("Tokens: " + tokens.size());

        int t = 1;
        for (MagicCardDefinition token : tokens) {
            System.out.println("Testing token #" + t + ": " + token.getName());
            t++;
            totalCards++;
            MagicGame game = new TokenScenario(token).getGame();
            final double duration = runGame(game);
            totalTime += duration;

            System.out.printf("Token time: %.2fs : %s\n", duration, token);
        }


        int n = 1;
        for (String name : cards) {
            System.out.println("Testing card #" + n + ": " + name);
            n++;
            totalCards++;
            MagicGame game = new CardScenario(name).getGame();
            final double duration = runGame(game);
            totalTime += duration;

            System.out.printf("Time: %.2fs : %s\n", duration, name);
        }
        System.out.println("Tested " + totalCards + " cards/tokens.");
        System.out.println("Total time: " + totalTime);
        System.out.println("Time per card: " + totalTime / totalCards);
    }

    private static double runGame(MagicGame game) {
        game.setArtificial(true);

        //5 seconds max for a quick match
        final HeadlessGameController controller = new HeadlessGameController(
            game, 5 * 1000
        );

        final long start_time = System.currentTimeMillis();
        controller.runGame();
        return (double) (System.currentTimeMillis() - start_time) / 1000;
    }

    /**
     * Base scenario, where player starts with two tested cares in hand.
     */
    private static class CardScenario extends BaseScenario {
        private String testCard;

        public CardScenario(String testCard) {
            this.testCard = testCard;
        }

        @Override
        public void prepareScenario(MagicPlayer player, MagicPlayer opponent) {
            addToHand(player, testCard, 2);

        }
    }

    /**
     * Base scenario, where player starts one tested token in play.
     */
    private static class TokenScenario extends BaseScenario {
        private MagicCardDefinition testToken;

        public TokenScenario(MagicCardDefinition testToken) {
            this.testToken = testToken;
        }

        @Override
        public void prepareScenario(MagicPlayer player, MagicPlayer opponent) {
            createPermanent(player, testToken, false, 1);
        }
    }

    /**
     * Base scenario - player has most likely enough mana to cast anything,
     * opponent has at least one permanent of every type, so there would likely be some targets
     * for variety of possible spells and effects.
     * <p>
     * Libraries have only two cards (basic lands) in them, so the game is likely to end quickly.
     */
    private static abstract class BaseScenario extends TestGameBuilder {
        public abstract void prepareScenario(MagicPlayer player, MagicPlayer opponent);

        public static MagicDuel createAiDuel(final MagicAIImpl aAiType, final int aAiLevel) {
            // Set number of games.
            final DuelConfig config = new DuelConfig();
            config.setNrOfGames(1);
            config.setStartLife(20);

            AiProfile ai = AiProfile.create(aAiType, aAiLevel);

            // Create players
            config.setPlayerProfile(0, ai);
            config.setPlayerProfile(1, ai);

            final MagicDuel duel = new MagicDuel(config);

            final MagicDeckProfile profile = new MagicDeckProfile("bgruw");
            final DuelPlayerConfig player1 = new DuelPlayerConfig(ai, profile);
            final DuelPlayerConfig player2 = new DuelPlayerConfig(ai, profile);

            duel.setPlayers(new DuelPlayerConfig[]{player1, player2});
            duel.setStartPlayer(0);
            return duel;
        }

        public static MagicDuel createAiDuel() {
            return createAiDuel(MagicAIImpl.MMABFast, 2);
        }

        @Override
        public MagicGame getGame() {
            final MagicDuel duel = createAiDuel();
            final MagicGame game = duel.nextGame();
            game.setPhase(MagicMainPhase.getFirstInstance());
            final MagicPlayer player = game.getPlayer(0);
            final MagicPlayer opponent = game.getPlayer(1);

            MagicPlayer P = player;

            P.setLife(10);
            addToLibrary(P, "Plains", 2);
            // 5 mana of every color
            createPermanent(P, "Plains", false, 5);
            createPermanent(P, "Forest", false, 5);
            createPermanent(P, "Island", false, 5);
            createPermanent(P, "Swamp", false, 5);
            createPermanent(P, "Mountain", false, 5);
            // Own artifact creature
            createPermanent(P, "Ornithopter", false, 1);

            P = opponent;

            P.setLife(10);
            addToLibrary(P, "Plains", 2);
            createPermanent(P, "Plains", false, 1);
            createPermanent(P, "Forest", false, 1);
            createPermanent(P, "Island", false, 1);
            createPermanent(P, "Swamp", false, 1);
            createPermanent(P, "Mountain", false, 1);
            // Creature
            createPermanent(P, "Wall of Ice", false, 1);
            // Enchantment
            createPermanent(P, "Orcish Oriflamme", false, 1);
            // Artifact Creature
            createPermanent(P, "Omega Myr", false, 1);
            // Planeswalker
            createPermanent(P, "Ajani Goldmane", false, 1);
            // Sorcery
            addToHand(P, "Angelic Edict", 1);
            // Instant
            addToHand(P, "Giant Growth", 1);
            prepareScenario(player, opponent);

            return game;
        }
    }
}

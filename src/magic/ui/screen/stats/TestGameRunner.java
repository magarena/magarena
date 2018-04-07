package magic.ui.screen.stats;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import magic.ai.MagicAIImpl;
import magic.data.DeckGenerators;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.settings.IntegerSetting;
import magic.data.stats.MagicStats;
import magic.exception.InvalidDeckException;
import magic.headless.HeadlessGameController;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.player.AiProfile;
import magic.utility.DeckUtils;

class TestGameRunner extends SwingWorker<Void, Integer> {

    private static final Logger LOGGER = Logger.getLogger(TestGameRunner.class.getName());

    private static MagicAIImpl[] ai = {MagicAIImpl.MMABFast, MagicAIImpl.MMABFast};
    private static int[] str = {1, 1};
    private static String profile = "@";
    private static String[] deck = {"", ""};

    private final StatsContentPanel listener;
    private final int totalGames;

    TestGameRunner(StatsContentPanel listener, int totalGames) {
        this.listener = listener;
        this.totalGames = totalGames;
    }

    private void playGame() {
        final MagicDuel duel = setupDuel();
        final MagicGame game = duel.nextGame();
        game.setArtificial(true);
        //maximum duration of a game is 1 minute
        final HeadlessGameController controller = new HeadlessGameController(game, GeneralConfig.get(IntegerSetting.TEST_MATCH_LIMIT) * 1000);
        controller.runGame();
        MagicStats.saveGameData(game);
    }

    @Override
    protected Void doInBackground() {
        System.out.println("=== running test games : " + totalGames + " ===");
        for (int i = 0; i < totalGames; i++) {
            try {
                playGame();
                publish(i + 1);
            } catch (InvalidDeckException ex) {
                // ignore game if exception due to bad deck.
                LOGGER.log(Level.WARNING, null, ex);
                i -= 1;
            }
        }
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            listener.onTestGameRunnerFinished();
        }
    }

    @Override
    protected void process(List<Integer> chunks) {
        listener.notifyTestGameRunnerProcess(chunks.get(chunks.size() - 1));
    }

    private static MagicDuel setupDuel() {

// Set the random seed
//        if (seed != 0) {
//            MagicRandom.setRNGState(seed);
//            seed = MagicRandom.nextRNGInt() + 1;
//        }

        // Set number of games.
        final DuelConfig config=new DuelConfig();
        config.setNrOfGames(1);
        config.setStartLife(10);

        // Set difficulty.
        final MagicDuel testDuel=new MagicDuel(config);
        testDuel.initialize();

        // Create players
        final DuelPlayerConfig[] players = new DuelPlayerConfig[2];
        for (int i = 0; i < 2; i++) {
            players[i] = new DuelPlayerConfig(
                AiProfile.create(ai[i], str[i]),
                MagicDeckProfile.getDeckProfile(profile)
            );
        }
        testDuel.setPlayers(players);

        // Set the deck.
        for (int i = 0; i < 2; i++) {
            if (deck[i].length() > 0) {
                DeckUtils.loadAndSetPlayerDeck(deck[i], players[i]);
            } else {
                DeckGenerators.setRandomDeck(players[i]);
            }
        }

        return testDuel;
    }

}

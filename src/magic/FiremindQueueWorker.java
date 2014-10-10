package magic;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.CardDefinitions;
import magic.data.DeckUtils;
import magic.data.DuelConfig;
import magic.firemind.Duel;
import magic.firemind.FiremindClient;
import magic.model.FiremindGameReport;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicRandom;
import magic.ui.GameController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class FiremindQueueWorker {

    private static int games;
    private static int str1 = 2;
    private static int str2 = 2;
    private static int life = 20;
    private static int seed;
    private static String deck1 = "";
    private static String deck2 = "";
    private static MagicAIImpl ai1 = MagicAIImpl.MCTS;
    private static MagicAIImpl ai2 = MagicAIImpl.MCTS;
    private static Duel currentDuel;
    private static int gameCount = 0;

    private static MagicDuel setupDuel() {
        // Set the random seed
        if (seed != 0) {
            MagicRandom.setRNGState(seed);
            seed = MagicRandom.nextRNGInt(Integer.MAX_VALUE) + 1;
        }

        // Set number of games.
        final DuelConfig config = new DuelConfig();
        config.setNrOfGames(games);
        config.setStartLife(life);

        // Set difficulty.
        final MagicDuel testDuel = new MagicDuel(config);
        testDuel.initialize();
        testDuel.setDifficulty(0, str1);
        testDuel.setDifficulty(1, str2);

        // Set the AI
        testDuel.setAIs(new MagicAI[] { ai1.getAI(), ai2.getAI() });
        testDuel.getPlayer(0).setArtificial(true);
        testDuel.getPlayer(1).setArtificial(true);

        // Set the deck.
        if (deck1.length() > 0) {
            DeckUtils.loadDeck(deck1, testDuel.getPlayer(0));
        }
        if (deck2.length() > 0) {
            DeckUtils.loadDeck(deck2, testDuel.getPlayer(1));
        }

        return testDuel;
    }

    public static void main(final String[] args) {

        FiremindClient.setHostByEnvironment();
        while (true) {
            Duel duel = FiremindClient.popDeckJob();
            if (duel != null) {
                try {
                	CardDefinitions.loadCardDefinitions();
                    final FiremindGameReport reporter = new FiremindGameReport(
                            duel.id);
                    Thread.setDefaultUncaughtExceptionHandler(reporter);
                    System.out.println(duel.games_to_play + " Games to run");
                    File theDir = new File("duels/" + duel.id);
                    theDir.mkdir();

                    deck1 = saveDeckFile("duels/" + duel.id + "/" + "deck1",
                            duel.deck1_text);
                    deck2 = saveDeckFile("duels/" + duel.id + "/" + "deck2",
                            duel.deck2_text);
                    currentDuel = duel;
                    games = duel.games_to_play;

                    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                    runDuel();
                    FiremindClient.postSuccess(duel.id);
                    if (gameCount > 25) {
                        System.out
                                .println("Exceeded max number of games. Shutting down.");
                        return;
                    }

                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    FiremindClient.postFailure(duel.id, sw.toString());
                    e.printStackTrace();
                }
                FiremindClient.resetChangedScripts();
                
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("Woken");
                }
            }
        }

    }

    private static String saveDeckFile(String name, String content) {
        try {
            File deckFile = new File(name + ".dec");
            deckFile.getParentFile().mkdirs();
            deckFile.createNewFile();
            FileWriter fw = new FileWriter(deckFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            return deckFile.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void runDuel() {
        int played = 0;
        int wins = 0;
        MagicGameLog.initialize();
        final MagicDuel testDuel = setupDuel();

        Date baseDate = new Date();
        baseDate.setTime(0);
        long started = System.currentTimeMillis();
        while (testDuel.getGamesPlayed() < testDuel.getGamesTotal()) {
            final MagicGame game = testDuel.nextGame(false);
            game.setArtificial(true);
            final GameController controller = new GameController(game);

            // maximum duration of a game is 60 minutes
            controller.setMaxTestGameDuration(3600000);

            controller.runGame();
            if (testDuel.getGamesPlayed() > played) {
                gameCount++;
                played = testDuel.getGamesPlayed();
                long diff = System.currentTimeMillis() - started;
                String[] vers = MagicMain.VERSION.split("\\.");
                String log = MagicGameLog.getLogFileName();
                FiremindClient.postGame(currentDuel.id, played, new Date(
                        baseDate.getTime() + diff),
                        testDuel.getGamesWon() > wins, Integer
                                .parseInt(vers[0]), Integer.parseInt(vers[1]),
                        log);

                wins = testDuel.getGamesWon();
                started = System.currentTimeMillis();
                MagicGameLog.initialize();
            }
        }
        System.out.println("Duel finished " + played + " of "
                + testDuel.getGamesTotal() + " run");

    }

}

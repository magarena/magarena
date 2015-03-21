package magic.firemind;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.CardDefinitions;
import magic.data.DeckUtils;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.exception.InvalidDeckException;
import magic.headless.HeadlessGameController;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicRandom;

public class FiremindDuelRunner {
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

    public static void main(String[] args) {
        FiremindClient.setHostByEnvironment();
        Duel duel = FiremindClient.popDeckJob();
        if (duel == null) {
            System.exit(1);
        }
        try {
            final FiremindGameReport reporter = new FiremindGameReport(duel.id);
            Thread.setDefaultUncaughtExceptionHandler(reporter);
            System.out.println(duel.games_to_play + " Games to run");
            File theDir = new File("duels/" + duel.id);
            theDir.mkdir();

            deck1 = saveDeckFile("duels/" + duel.id + "/" + "deck1",
                    duel.deck1_text);
            deck2 = saveDeckFile("duels/" + duel.id + "/" + "deck2",
                    duel.deck2_text);
            loadCardsInDeck(duel.deck1_text);
            loadCardsInDeck(duel.deck2_text);

            currentDuel = duel;
            games = duel.games_to_play;
            seed = duel.seed;
            life = duel.life;
            str1 = duel.strAi1;
            str2 = duel.strAi2;
            try {
                ai1 = MagicAIImpl.valueOf(duel.ai1);
            } catch (final IllegalArgumentException ex) {
                System.err.println("Error: " + duel.ai1 + " is not valid AI");
            }
            try {
                ai2 = MagicAIImpl.valueOf(duel.ai2);
            } catch (final IllegalArgumentException ex) {
                System.err.println("Error: " + duel.ai2 + " is not valid AI");
            }
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            runDuel();
            FiremindClient.postSuccess(duel.id);

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            FiremindClient.postFailure(duel.id, sw.toString());
            e.printStackTrace();
            System.exit(2);
        }
        FiremindClient.resetChangedScripts();
        System.exit(0);
    }

    private static MagicDuel setupDuel() throws InvalidDeckException {
        // Set the random seed
        if (seed != 0) {
            MagicRandom.setRNGState(seed);
            seed = MagicRandom.nextRNGInt() + 1;
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
        final MagicDeckProfile profile = new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1 = new MagicPlayerDefinition(
                "Player1", true, profile);
        final MagicPlayerDefinition player2 = new MagicPlayerDefinition(
                "Player2", true, profile);
        testDuel.setPlayers(new MagicPlayerDefinition[] { player1, player2 });

        // Set the AI
        testDuel.setAIs(new MagicAI[] { ai1.getAI(), ai2.getAI() });
        testDuel.getPlayer(0).setArtificial(true);
        testDuel.getPlayer(1).setArtificial(true);

        // Set the deck.
        if (deck1.length() > 0) {
            DeckUtils.loadAndSetPlayerDeck(deck1, testDuel.getPlayer(0));
        }
        if (deck2.length() > 0) {
            DeckUtils.loadAndSetPlayerDeck(deck2, testDuel.getPlayer(1));
        }

        return testDuel;
    }

    public static void loadCardsInDeck(String decklist) {
        Pattern r = Pattern.compile("^(\\d+) (.*)$");
        for (String line : decklist.split("\\r\\n")) {
            Matcher m = r.matcher(line);
            if (m.find()) {
                CardDefinitions.loadCardDefinition(m.group(2));
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

    private static void runDuel() throws InvalidDeckException {
        int played = 0;
        int wins = 0;
        MagicGameLog.initialize();
        final MagicDuel testDuel = setupDuel();

        Date baseDate = new Date();
        baseDate.setTime(0);
        long started = System.currentTimeMillis();
        while (testDuel.getGamesPlayed() < testDuel.getGamesTotal()) {
            final MagicGame game = testDuel.nextGame();
            game.setArtificial(true);

            // maximum duration of a game is 60 minutes
            final HeadlessGameController controller = new HeadlessGameController(
                    game, 3600000);

            controller.runGame();
            if (testDuel.getGamesPlayed() > played) {
                played = testDuel.getGamesPlayed();
                long diff = System.currentTimeMillis() - started;
                String[] vers = GeneralConfig.VERSION.split("\\.");
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

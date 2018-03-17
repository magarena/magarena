package magic.firemind;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import magic.ai.MagicAIImpl;
import magic.data.CardDefinitions;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.settings.IntegerSetting;
import magic.headless.HeadlessGameController;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicRandom;
import magic.model.player.AiProfile;
import magic.utility.DeckUtils;
import magic.utility.MagicSystem;
import static java.nio.charset.StandardCharsets.UTF_8;

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

            deck1 = saveDeckFile("firemind-duel-" + duel.id + "deck1",
                    duel.deck1_text);
            deck2 = saveDeckFile("firemind-duel-" + duel.id + "deck2",
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

    private static MagicDuel setupDuel() {
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

        final MagicDeckProfile profile = new MagicDeckProfile("bgruw");

        final DuelPlayerConfig player1 = new DuelPlayerConfig(
            AiProfile.create("Player1", ai1, str1),
            profile
        );

        final DuelPlayerConfig player2 = new DuelPlayerConfig(
            AiProfile.create("Player2", ai2, str2),
            profile
        );

        testDuel.setPlayers(new DuelPlayerConfig[] { player1, player2 });

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
            File deckFile = DeckUtils.getDecksFolder().resolve(name + ".dec").toFile();
            deckFile.createNewFile();
            Writer fw = Files.newBufferedWriter(deckFile.getAbsoluteFile().toPath(), UTF_8);
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
            final MagicGame game = testDuel.nextGame();
            game.setArtificial(true);

            // maximum duration of a game is 60 minutes
            final HeadlessGameController controller = new HeadlessGameController(
                    game, GeneralConfig.get(IntegerSetting.FIREMIND_MATCH_LIMIT) * 1000);

            controller.runGame();
            if (testDuel.getGamesPlayed() > played) {
                played = testDuel.getGamesPlayed();
                long diff = System.currentTimeMillis() - started;
                String[] vers = MagicSystem.VERSION.split("\\.");
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

package magic.firemind;

import magic.utility.FileIO;
import magic.model.action.MagicAction;
import magic.model.stack.MagicItemOnStack;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.utility.MagicSystem;

public class FiremindGameReport implements Thread.UncaughtExceptionHandler {
    private Integer currentDuelId;

    public FiremindGameReport(Integer duel_id) {
        super();
        currentDuelId = duel_id;
    }

    @Override
    public void uncaughtException(final Thread th, final Throwable ex) {
        buildReport(MagicGame.getInstance(), th, ex);
        ex.printStackTrace();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        FiremindClient.postFailure(currentDuelId, sw.toString());
    }

    private static void buildCard(final MagicGame game, final String place, final MagicCard card, final StringBuilder report) {
        report.append("   - ").append(place).append(" : ")
                .append(card.getName()).append("\n");
    }

    private static void buildPermanent(final MagicGame game, final MagicPermanent permanent, final StringBuilder report) {
        report.append("   - Permanent : ").append(permanent.getName());
        if (permanent.isCreature()) {
            final MagicPowerToughness pt = permanent.getPowerToughness();
            report.append("  Power : ").append(pt.power());
            report.append("  Toughness : ").append(pt.toughness());
            report.append("  Damage : ").append(permanent.getDamage());
        }
        if (permanent.hasState(MagicPermanentState.Tapped)) {
            report.append("  Tapped");
        }
        if (permanent.hasState(MagicPermanentState.Summoned)) {
            report.append("  Summoned");
        }
        report.append("\n");
    }

    private static void buildPlayer(final MagicGame game, final MagicPlayer player, final StringBuilder report) {
        report.append(player.getIndex()).append("] ");
        report.append("Player : ").append(player.getName());
        report.append("  Life : ").append(player.getLife());
        report.append("  Delayed : ").append(player.getBuilderCost());
        report.append("\n");

        for (final MagicCard card : player.getHand()) {
            buildCard(game, "Hand", card, report);
        }

        for (final MagicCard card : player.getGraveyard()) {
            buildCard(game, "Graveyard", card, report);
        }

        for (final MagicPermanent permanent : player.getPermanents()) {
            buildPermanent(game, permanent, report);
        }
    }

    private static void buildStack(final MagicGame game, final StringBuilder report) {
        report.append("Stack : ").append(game.getStack().size()).append('\n');
        for (final MagicItemOnStack itemOnStack : game.getStack()) {
            report.append("   - Name : ");
            report.append(itemOnStack.getName());
            report.append("  Player : ");
            report.append(itemOnStack.getController().getName());
            report.append('\n');
        }
    }

    private static void buildScore(final MagicGame game, final StringBuilder report) {
        int totalScore = 0;
        int count = 0;
        for (final MagicAction action : game.getActions()) {
            final int score = action.getScore(game.getScorePlayer());
            totalScore += score;
            final String text = action.toString();
            if (!text.isEmpty()) {
                report.append(++count).append(". ").append(text).append(" = ")
                        .append(score).append("\n");
            }
        }
        report.append("Score = ").append(totalScore).append("\n");
    }

    private static String buildReport(final MagicGame game) {
        final StringBuilder report = new StringBuilder();
        report.append("Turn : ").append(game.getTurn());
        report.append("  Phase : ").append(game.getPhase().getType());
        report.append("  Step : ").append(game.getStep());
        report.append("  Player : ").append(game.getTurnPlayer());
        report.append("  Score : ").append(game.getScore());
        report.append("\n");

        for (final MagicPlayer player : game.getPlayers()) {
            buildPlayer(game, player, report);
        }

        buildStack(game, report);
        buildScore(game, report);
        return report.toString();
    }

    public void buildReport(final MagicGame game, final Thread th, final Throwable ex) {
        final StringBuilder sb = new StringBuilder();
        sb.append("CRASH REPORT FOR MAGARENA THREAD ").append(th);
        sb.append('\n');
        sb.append("CREATED ON ").append((new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()));
        sb.append('\n');
        sb.append("MAGARENA VERSION " + MagicSystem.VERSION);
        sb.append(", JRE ").append(System.getProperty("java.version"));
        sb.append(", OS ").append(System.getProperty("os.name"));
        sb.append("_").append(System.getProperty("os.version"));
        sb.append(" ").append(System.getProperty("os.arch"));
        sb.append("\n\n");
        try {
            // buildReport might throw an exception
            if (game != null) {
                sb.append(buildReport(game));
                sb.append('\n');
            }
        } catch (final Throwable ex2) {
            sb.append("Exception from MagicGameReport.buildReport: ").append(ex2.getMessage());
            sb.append('\n');
            final StringWriter result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            ex2.printStackTrace(printWriter);
            sb.append(result.toString());
            sb.append('\n');
        }
        sb.append("Exception from controller.runGame: ").append(ex.getMessage());
        sb.append('\n');
        final StringWriter result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        sb.append(result.toString());
        sb.append('\n');

        FiremindClient.postFailure(currentDuelId, sb.toString());

        // print a copy to stderr
        System.err.println(sb.toString());

        // save a copy to a crash log file
        final Path clog = MagicFileSystem.getDataPath(DataPath.LOGS).resolve(
                "crash.log");
        try {
            FileIO.toFile(clog.toFile(), sb.toString(), true);
        } catch (final IOException ex3) {
            System.err.println("Unable to save crash log");
        }
    }
}

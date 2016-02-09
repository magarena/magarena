package magic.exception.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicAction;
import magic.model.stack.MagicItemOnStack;
import magic.utility.MagicSystem;
import magic.exception.GameException;


public class ExceptionReport {

    private final StringBuilder sb = new StringBuilder();

    public ExceptionReport(final Thread th, final Throwable ex) {

        sb.append("CRASH REPORT FOR MAGARENA THREAD ").append(th);
        sb.append('\n');
        sb.append("CREATED ON ").append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        sb.append('\n');
        sb.append("MAGARENA VERSION ").append(MagicSystem.VERSION);
        sb.append(", JRE ").append(System.getProperty("java.version"));
        sb.append(", OS ").append(System.getProperty("os.name"));
        sb.append("_").append(System.getProperty("os.version"));
        sb.append(" ").append(System.getProperty("os.arch"));
        sb.append("\n================================\n");
        sb.append(MagicSystem.getHeapUtilizationStats());
        sb.append("\n================================\n");
        sb.append(MagicSystem.getRuntimeParameters());
        sb.append("\n\n");

        final MagicGame game = (ex instanceof GameException) ?
            ((GameException)ex).getGame() :
            MagicGame.getInstance();

        try {
            //buildReport might throw an exception
            if (game != null) {
                sb.append(getGameDetails(game));
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

    }

    @Override
    public String toString() {
        return sb.toString();
    }

    private static String getGameDetails(final MagicGame game) {

        final StringBuilder report = new StringBuilder();

        report.append("Turn : ").append(game.getTurn());
        report.append("  Phase : ").append(game.getPhase().getType());
        report.append("  Step : ").append(game.getStep());
        report.append("  Player : ").append(game.getTurnPlayer());
        report.append("  AI : ").append(game.getPlayer(1).getAiProfile().getAiType());
        report.append("  Score : ").append(game.getScore());
        report.append("\n");

        for (final MagicPlayer player : game.getPlayers()) {
            buildPlayer(player, report);
        }

        buildStack(game, report);
        buildScore(game, report);

        return report.toString();
    }

    private static void buildPlayer(final MagicPlayer player, final StringBuilder report) {
        report.append(player.getIndex()).append("] ");
        report.append("Player : ").append(player.getName());
        report.append("  Life : ").append(player.getLife());
        report.append("  Delayed : ").append(player.getBuilderCost());
        report.append("\n");

        for (final MagicCard card : player.getHand()) {
            buildCard("Hand", card, report);
        }

        for (final MagicCard card : player.getGraveyard()) {
            buildCard("Graveyard", card, report);
        }

        for (final MagicCard card : player.getLibrary().getCardsFromTop(7)) {
            buildCard("Library", card, report);
        }

        for (final MagicPermanent permanent : player.getPermanents()) {
            buildPermanent(permanent, report);
        }
    }

    private static void buildStack(final MagicGame game,final StringBuilder report) {
        report.append("Stack : ").append(game.getStack().size()).append('\n');
        for (final MagicItemOnStack itemOnStack : game.getStack()) {
            report.append("   - Name : ");
            report.append(itemOnStack.getName());
            report.append("  Player : ");
            report.append(itemOnStack.getController().getName());
            report.append('\n');
        }
    }

    private static void buildScore(final MagicGame game,final StringBuilder report) {
        int totalScore=0;
        int count=0;
        for (final MagicAction action : game.getActions()) {
            final int score=action.getScore(game.getScorePlayer());
            totalScore+=score;
            final String text=action.toString();
            if (!text.isEmpty()) {
                report.append(++count).append(". ").append(text).append(" = ").append(score).append("\n");
            }
        }
        report.append("Score = ").append(totalScore).append("\n");
    }

    private static void buildCard(final String place, final MagicCard card, final StringBuilder report) {
        report.append("   - ").append(place).append(" : ").append(card.getName()).append("\n");
    }

    private static void buildPermanent(final MagicPermanent permanent, final StringBuilder report) {
        report.append("   - Permanent : ").append(permanent.getName());
        if (permanent.isCreature()) {
            final MagicPowerToughness pt=permanent.getPowerToughness();
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

}

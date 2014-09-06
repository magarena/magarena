package magic.model;

import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.MagicMain;
import magic.data.FileIO;
import magic.model.action.MagicAction;
import magic.model.stack.MagicItemOnStack;
import magic.utility.GraphicsUtilities;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class MagicGameReport implements Thread.UncaughtExceptionHandler {

    // Safeguard that ensures that if for some reason uncaughtException()
    // is called multiple times (eg. from a timer running on a separate thread)
    // only the first case is actually handled. This prevents multiple
    // error notification dialogs being created.
    private boolean isRunning = false;
    private boolean notifyUser = true;

    @Override
    public void uncaughtException(final Thread th, final Throwable ex) {
        reportException(th, ex);
    }

    public void disableNotification() {
        notifyUser = false;
    }

    public void reportException(final Thread th, final Throwable ex) {
        if (!isRunning) {
            isRunning = true;
            MagicGameReport.buildReport(MagicGame.getInstance(), th, ex);
            if (MagicMain.rootFrame != null) {
                doScreenShot(MagicMain.rootFrame.getContentPane());
            }
            if (notifyUser) {
                doNotifyUser();
            }
            System.exit(1);
        }
    }

    /**
     * Displays a message to user in the event an unexpected exception occurs.
     * User can open logs folder and/or Issue tracker directly from this dialog.
     */
    private static void doNotifyUser() {
        try {

            // By specifying a frame the JOptionPane will be shown in the taskbar.
            // Otherwise if the dialog is hidden it is easy to forget it is still open.
            final JFrame frame = new JFrame("Fatal Error");
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

            String prompt = "An unexpected error has occurred and Magarena will need to close.";
            if (Desktop.isDesktopSupported()) {
                prompt +=
                    "\n\nPlease consider submitting a crash report so that the development team can investigate.\n" +
                    "Would you like to open the crash logs directory in file explorer?";
                final int action = JOptionPane.showConfirmDialog(frame, prompt, "Fatal Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null);
                if (action == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(MagicFileSystem.getDataPath(DataPath.LOGS).toFile());
                }
            } else {
                JOptionPane.showMessageDialog(frame, prompt, "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            // do nothing - crash report has already been generated and app is about to exit anyway.
        }
    }

    private static void buildCard(final MagicGame game,final String place,final MagicCard card,final StringBuilder report) {
        report.append("   - ").append(place).append(" : ").append(card.getName()).append("\n");
    }

    private static void buildPermanent(final MagicGame game,final MagicPermanent permanent,final StringBuilder report) {
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

    private static void buildPlayer(final MagicGame game,final MagicPlayer player,final StringBuilder report) {
        report.append(player.getIndex()).append("] ");
        report.append("Player : ").append(player.getName());
        report.append("  Life : ").append(player.getLife());
        report.append("  Delayed : ").append(player.getBuilderCost());
        report.append("\n");

        for (final MagicCard card: player.getHand()) {
            buildCard(game,"Hand",card,report);
        }

        for (final MagicCard card: player.getGraveyard()) {
            buildCard(game,"Graveyard",card,report);
        }

        for (final MagicPermanent permanent : player.getPermanents()) {
            buildPermanent(game,permanent,report);
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

    private static String buildReport(final MagicGame game) {
        final StringBuilder report=new StringBuilder();
        report.append("Turn : ").append(game.getTurn());
        report.append("  Phase : ").append(game.getPhase().getType());
        report.append("  Step : ").append(game.getStep());
        report.append("  Player : ").append(game.getTurnPlayer());
        report.append("  Score : ").append(game.getScore());
        report.append("\n");

        for (final MagicPlayer player : game.getPlayers()) {
            buildPlayer(game,player,report);
        }

        buildStack(game,report);
        buildScore(game,report);
        return report.toString();
    }

    public static void buildReport(final MagicGame game, final Thread th, final Throwable ex) {
        final StringBuilder sb = new StringBuilder();
        sb.append("CRASH REPORT FOR MAGARENA THREAD ").append(th);
        sb.append('\n');
        sb.append("CREATED ON ").append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        sb.append('\n');
        sb.append("MAGARENA VERSION ").append(MagicMain.VERSION);
        sb.append(", JRE ").append(System.getProperty("java.version"));
        sb.append(", OS ").append(System.getProperty("os.name"));
        sb.append("_").append(System.getProperty("os.version"));
        sb.append(" ").append(System.getProperty("os.arch"));
        sb.append("\n================================\n");
        sb.append(MagicMain.getHeapUtilizationStats());
        sb.append("\n================================\n");
        sb.append(MagicMain.getRuntimeParameters());
        sb.append("\n\n");
        try {
            //buildReport might throw an exception
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

        //print a copy to stderr
        System.err.println(sb.toString());

        //save a copy to a crash log file
        final Path clog = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("crash.log");
        try {
            FileIO.toFile(clog.toFile(), sb.toString(), true);
        } catch (final IOException ex3) {
            System.err.println("Unable to save crash log");
        }
    }

    private static void doScreenShot(final Component container) {
        if (container != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Path filePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("crash.png");
                        GraphicsUtilities.doScreenshotToFile(container, filePath);
                     } catch (Exception e) {
                        System.err.println("ScreenShot failed : " + e.toString());
                    }
                }
            });
        }
    }

}

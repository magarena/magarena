package magic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Paint;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

final public class MagicUtility {
    private MagicUtility() {}

    public static final boolean IS_WINDOWS_OS = System.getProperty("os.name").toLowerCase().startsWith("windows");

    private static final Paint debugBorderPaint = new GradientPaint(0, 0, Color.red, 100, 100, Color.white, true);

    public static void setBusyMouseCursor(final boolean b) {
        MagicMain.rootFrame.setCursor(
                b ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) :
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static void setDebugBorder(final JComponent component) {
        component.setBorder(BorderFactory.createDashedBorder(debugBorderPaint));
    }

    public static boolean isTestGame() {
        return (System.getProperty("testGame") != null);
    }

    public static boolean isDevMode() {
        return Boolean.getBoolean("devMode") || isDebugMode();
    }

    public static boolean isDebugMode() {
        return Boolean.getBoolean("debug");
    }

    /**
     * add "-DselfMode=true" VM argument for AI vs AI mode.
     */
    public static boolean isAiVersusAi() {
        return Boolean.getBoolean("selfMode");
    }

    /**
     * add "-DshowStats=true" to output startup statistics to console.
     */
    public static boolean showStartupStats() {
        return Boolean.getBoolean("showStats");
    }

}

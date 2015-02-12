package magic.utility;

final public class MagicUtility {
    private MagicUtility() {}

    public static final boolean IS_WINDOWS_OS = System.getProperty("os.name").toLowerCase().startsWith("windows");

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
     * add "-DparseMissing=true" VM argument for parsing scripts_missing folder.
     */
    public static boolean isParseMissing() {
        return Boolean.getBoolean("parseMissing");
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

package magic.ui.screen.card.explorer;

public enum ExplorerScreenLayout {

    DEFAULT,
    NO_SIDEBAR;

    private static ExplorerScreenLayout activeLayout = DEFAULT;

    private ExplorerScreenLayout next() {
        return values()[(this.ordinal()+1) % values().length];
    }

    public static ExplorerScreenLayout getLayout() {
        return activeLayout;
    }

    public static void setNextLayout() {
        activeLayout = activeLayout.next();
    }
}

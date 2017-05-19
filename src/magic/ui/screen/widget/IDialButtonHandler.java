package magic.ui.screen.widget;

public interface IDialButtonHandler {
    int getDialPositionsCount();
    int getDialPosition();
    boolean doLeftClickAction(int dialPosition);
    boolean doRightClickAction(int dialPosition);
    void onMouseEntered(int dialPosition);
}

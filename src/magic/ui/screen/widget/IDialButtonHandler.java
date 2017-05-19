package magic.ui.screen.widget;

public interface IDialButtonHandler {
    int getDialPositionsCount();
    int getDialPosition();
    boolean doLeftClickAction(int nextPosition);
    boolean doRightClickAction(int previousPosition);
    void onMouseEntered(int position);
}

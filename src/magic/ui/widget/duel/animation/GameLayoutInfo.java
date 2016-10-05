package magic.ui.widget.duel.animation;

import java.awt.Dimension;
import java.awt.Rectangle;

public class GameLayoutInfo {

    private final Rectangle permanentsZone[] = new Rectangle[2];
    private final Rectangle libraryButton[] = new Rectangle[2];
    private final Rectangle handButton[] = new Rectangle[2];
    private Rectangle cardInHand = new Rectangle();
    private Rectangle stackViewer = new Rectangle();
    private final Dimension gamePanelSize;
    private Rectangle turnPanel = new Rectangle();

    public GameLayoutInfo(Dimension containerSize) {
        this.gamePanelSize = containerSize;
    }

    public void setLibraryButtonLayout(int playerIndex, Rectangle rect) {
        libraryButton[playerIndex] = rect;
    }

    public void setHandButtonLayout(int playerIndex, Rectangle rect) {
        handButton[playerIndex] = rect;
    }

    public void setCardInHandLayout(final Rectangle aRectangle) {
        cardInHand = new Rectangle(aRectangle);
    }

    public void setStackLayout(Rectangle aRectangle) {
        stackViewer = new Rectangle(aRectangle);
    }

    public void setPermanentsZoneLayout(int playerIndex, Rectangle aRectangle) {
        permanentsZone[playerIndex] = aRectangle;
    }

    public void setTurnPanelLayout(Rectangle aRectangle) {
        this.turnPanel = aRectangle;
    }

    Rectangle getHandButtonLayout(int playerIndex) {
        return handButton[playerIndex];
    }

    Rectangle getLibraryButtonLayout(int playerIndex) {
        return libraryButton[playerIndex];
    }

    Rectangle getStackLayout() {
        return stackViewer;
    }

    Rectangle getPermanentsZoneLayout(int playerIndex) {
        return permanentsZone[playerIndex];
    }

    Dimension getGamePanelSize() {
        return gamePanelSize;
    }

    Rectangle getTurnPanelLayout() {
        return turnPanel;
    }

}

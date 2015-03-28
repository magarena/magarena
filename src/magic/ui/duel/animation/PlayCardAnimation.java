package magic.ui.duel.animation;

import java.awt.Dimension;
import java.awt.Point;

import java.awt.Rectangle;
import magic.model.IUIGameController;
import magic.model.MagicCardDefinition;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerZone;
import magic.ui.duel.DuelPanel;

public class PlayCardAnimation {

    private Dimension startSize = new Dimension();
    private Dimension endSize = new Dimension();
    private Point startPoint = null;
    private Point endPoint = null;
    private final MagicPlayer player;
    private final DuelPanel gamePanel;
    private final MagicCardDefinition card;
    private final IUIGameController controller;

    public PlayCardAnimation(final MagicPlayer player, final MagicCardDefinition card, final DuelPanel gamePanel) {
        this.player = player;
        this.card = card;
        this.gamePanel = gamePanel;
        this.controller = gamePanel.getController();
    }

    public Dimension getStartSize() {
        return startSize;
    }

    public Dimension getEndSize() {
        return endSize;
    }

    public MagicPlayer getPlayer() {
        return player;
    }

    public Point getStartPoint() {
        if (startPoint == null) {
            final Rectangle rect = controller.getPlayerZoneButtonRectangle(player, MagicPlayerZone.HAND, gamePanel);
            startPoint = rect.getLocation();
            startSize = rect.getSize();
        }
        return startPoint;
    }

    public Point getEndPoint() {
        if (endPoint == null) {
            final Rectangle rect = controller.getStackViewerRectangle(gamePanel);
            endPoint = rect.getLocation();
        }
        return endPoint;
    }

    public MagicCardDefinition getCard() {
        return card;
    }

    public void setStartSize(Dimension cardSize) {
        startSize = cardSize;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void setEndSize(Dimension size) {
        this.endSize = size;
    }

}

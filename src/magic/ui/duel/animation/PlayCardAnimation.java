package magic.ui.duel.animation;

import java.awt.Dimension;
import java.awt.Point;

import magic.model.MagicCardDefinition;
import magic.model.MagicPlayer;
import magic.ui.GamePanel;

public class PlayCardAnimation {

    private Dimension startSize = new Dimension(30, 40); // approx size of hand icon.
    private final Dimension endSize = new Dimension(startSize.width * 2, startSize.height * 2);
    private Point startPoint = null;
    private Point endPoint = null;
    private final MagicPlayer player;
    private final GamePanel gamePanel;
    private final MagicCardDefinition card;

    public PlayCardAnimation(final MagicPlayer player, final MagicCardDefinition card, final GamePanel gamePanel) {
        this.player = player;
        this.card = card;
        this.gamePanel = gamePanel;
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
            final int y = player.getIndex() == 1 ? 40 : gamePanel.getHeight() - startSize.height;
            startPoint = new Point(70 + 28, y); // approx position of hand icon.
        }
        return startPoint;
    }

    public Point getEndPoint() {
        if (endPoint == null) {
            final int y = player.getIndex() == 1 ? 40 : gamePanel.getHeight() - startSize.height;
            endPoint = new Point((gamePanel.getWidth() / 2) - endSize.width, y);
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

}

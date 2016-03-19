package magic.ui.canvas.cards;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import magic.model.MagicCard;
import magic.ui.MagicImages;

final class CardCanvas {

    private Dimension cardSize;
    private Point position;
    private final Rectangle boundary = new Rectangle();
    private final MagicCard card;

    public CardCanvas(final MagicCard card) {
        this.card = card;
        this.cardSize = new Dimension();
        setPosition(new Point(0, 0));
    }

    MagicCard getMagicCard() {
        return card;
    }

    public void setPosition(final Point p0) {
        position = p0;
        boundary.setBounds(position.x, position.y, cardSize.width, cardSize.height);
    }
    public Point getPosition() {
        return position;
    }

    public Dimension getSize() {
        return this.cardSize;
    }

    @Override
    public int hashCode() {
        final int hashcode1 = getFrontImage().hashCode();
        final int hashcode2 = getBackImage() == null ? 0 : getBackImage().hashCode();
        return 73 * hashcode1 ^ 79 * hashcode2;
    }

    public BufferedImage getFrontImage() {
        return MagicImages.getCardImageUseCache(card.getCardDefinition());
    }

    public BufferedImage getBackImage() {
        return null;
    }

    Rectangle getBounds() {
        return new Rectangle(position, cardSize);
    }

    void setSize(int cardWidth, int cardHeight) {
        cardSize = new Dimension(cardWidth, cardHeight);
    }

}

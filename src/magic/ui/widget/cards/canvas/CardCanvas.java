package magic.ui.widget.cards.canvas;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;

final class CardCanvas {

    private Dimension cardSize;
    private Point position;
    private final Rectangle boundary = new Rectangle();
    private final IRenderableCard card;
    private BufferedImage greyscaleImage;

    public CardCanvas(final IRenderableCard card) {
        this.card = card;
        this.cardSize = new Dimension();
        setPosition(new Point(0, 0));
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
        final int hashcode1 = MagicImages.getCardImage(card).hashCode();
        final int hashcode2 = getBackImage() == null ? 0 : getBackImage().hashCode();
        return 73 * hashcode1 ^ 79 * hashcode2;
    }

    private BufferedImage getGreyscaleImage(BufferedImage image) {
        if (greyscaleImage == null) {
            greyscaleImage = (BufferedImage) ImageHelper.getGreyScaleImage(image);
        }
        return greyscaleImage;
    }

    public BufferedImage getFrontImage() {
        BufferedImage colorImage = MagicImages.getCardImage(card);
        return card.isInvalid() ? getGreyscaleImage(colorImage): colorImage;
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

    MagicCardDefinition getCardDefinition() {
        return card.getCardDefinition();
    }

}

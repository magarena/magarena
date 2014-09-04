package magic.ui.canvas.cards;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

final class CardCanvas {

    private final ICardCanvas cardObject;
    private final Dimension cardSize;
    private Point position;
    private Rectangle boundary = new Rectangle();

    public CardCanvas(final ICardCanvas cardObject) {
        this.cardObject = cardObject;
        this.cardSize = new Dimension();
        setPosition(new Point(0, 0));
    }

    public ICardCanvas getCard() {
        return this.cardObject;
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
        final int hashcode1 = getCard().getFrontImage().hashCode();
        final int hashcode2 = getCard().getBackImage() == null ? 0 : getCard().getBackImage().hashCode();
        return 73 * hashcode1 ^ 79 * hashcode2;
    }

}

package magic.ui.widget.duel.animation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public class ArrowBuilder {
    private ArrowBuilder() {}

    static void drawArrow(Graphics g, Rectangle startRect, Rectangle endRect) {

        final Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);

        final int startX = startRect.x + (startRect.width / 2);
        final int startY = startRect.y + (startRect.height / 2);
        final int endX = endRect.x;
        final int endY = endRect.y + (endRect.height / 2);

        float ex = endX - startX;
        float ey = endY - startY;
        if (ex == 0 && ey == 0) return;
        float length = (float)Math.sqrt(ex * ex + ey * ey);
        float bendPercent = (float)Math.asin(ey / length);
        if (endX > startX) bendPercent = -bendPercent;

        Area arrow = getArrow(length, -bendPercent);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(startX, startY);
        g2d.rotate(Math.atan2(ey, ex));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(composite);
        g2d.setColor(Color.RED);
        g2d.fill(arrow);
        g2d.setColor(Color.BLACK);
        g2d.draw(arrow);
        g2d.dispose();

    }

    private static Area getArrow (float length, float bendPercent) {

        final int bodyWidth = 10;
        final float headSize = 17;

        float p1x = 0, p1y = 0;
        float p2x = length, p2y = 0;
        float cx = length / 2, cy = length / 8f * bendPercent;

        float adjSize, ex, ey, abs_e;
        adjSize = (float)(bodyWidth / 2 / Math.sqrt(2));
        ex = p2x - cx;
        ey = p2y - cy;
        abs_e = (float)Math.sqrt(ex * ex + ey * ey);
        ex /= abs_e;
        ey /= abs_e;
        GeneralPath bodyPath = new GeneralPath();
        bodyPath.moveTo(p2x + (ey - ex) * adjSize, p2y - (ex + ey) * adjSize);
        bodyPath.quadTo(cx, cy, p1x, p1y - bodyWidth / 2);
        bodyPath.lineTo(p1x, p1y + bodyWidth / 2);
        bodyPath.quadTo(cx, cy, p2x - (ey + ex) * adjSize, p2y + (ex - ey) * adjSize);
        bodyPath.closePath();

        adjSize = (float)(headSize / Math.sqrt(2));
        ex = p2x - cx;
        ey = p2y - cy;
        abs_e = (float)Math.sqrt(ex * ex + ey * ey);
        ex /= abs_e;
        ey /= abs_e;
        GeneralPath headPath = new GeneralPath();
        headPath.moveTo(p2x - (ey + ex) * adjSize, p2y + (ex - ey) * adjSize);
        headPath.lineTo(p2x, p2y);
        headPath.lineTo(p2x + (ey - ex) * adjSize, p2y - (ex + ey) * adjSize);
        headPath.closePath();

        Area area = new Area(headPath);
        area.add(new Area(bodyPath));
        return area;
    }

}

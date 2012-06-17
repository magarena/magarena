package magic.ui.widget;

import com.sun.java.swing.plaf.nimbus.AbstractRegionPainter;
import java.awt.Graphics2D;
import javax.swing.JComponent;

// for overriding painters that can't be configured to paint nothing
public class BlankPainter extends AbstractRegionPainter {
    public BlankPainter() {
        super();
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        
    }

    @Override
    protected final PaintContext getPaintContext() {
        return null;
    }
}

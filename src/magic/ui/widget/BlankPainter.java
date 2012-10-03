package magic.ui.widget;

import com.sun.java.swing.plaf.nimbus.AbstractRegionPainter;

import javax.swing.JComponent;
import java.awt.Graphics2D;

// for overriding painters that can't be configured to paint nothing
public class BlankPainter extends AbstractRegionPainter {

    @Override
    protected void doPaint(final Graphics2D g, final JComponent c, final int width, final int height, final Object[] extendedCacheKeys) {
        
    }

    @Override
    protected final PaintContext getPaintContext() {
        return null;
    }
}

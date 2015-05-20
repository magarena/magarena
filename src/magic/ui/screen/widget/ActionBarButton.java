package magic.ui.screen.widget;

import java.awt.Color;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.Arrays;
import magic.ui.MagicStyle;

@SuppressWarnings("serial")
public class ActionBarButton extends MenuButton {

    // CTR
    public ActionBarButton(ImageIcon icon, String actionName, String tooltip, AbstractAction action, boolean showSeparator) {
        super("", action, tooltip, showSeparator);
        setButtonIcons(icon);
        setToolTipText("<html><b>" + actionName + "</b><br>" + tooltip + "</html>");
    }
    public ActionBarButton(ImageIcon icon, String actionName, String tooltip, AbstractAction action) {
        this(icon, actionName, tooltip, action, true);
    }
    // CTR - text only action.
    public ActionBarButton(final String caption, final String tooltip, final AbstractAction action, final boolean showSeparator) {
        super(caption, action, tooltip, showSeparator);
        if (tooltip != null) {
            setToolTipText("<html><b>" + caption + "</b><br>" + tooltip + "</html>");
        }
    }
    public ActionBarButton(final String caption, final String tooltip, final AbstractAction action) {
        this(caption, tooltip, action, true);
    }
    public ActionBarButton(final String caption, final AbstractAction action) {
        this(caption, null, action);
    }
    protected ActionBarButton() {}

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return new Point(0, -45);
    }

    private void setButtonIcons(final ImageIcon defaultIcon) {
        // default
        setIcon(defaultIcon);
        // rollover
        final ImageProducer ip = defaultIcon.getImage().getSource();
        float[] filter = getImageFilterValues(MagicStyle.HIGHLIGHT_COLOR);
        setRolloverIcon(new ImageIcon(
                Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(ip, new WhiteColorSwapImageFilter(filter))))
        );
        // pressed
        filter = getImageFilterValues(MagicStyle.HIGHLIGHT_COLOR.darker());
        setPressedIcon(new ImageIcon(
                Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(ip, new WhiteColorSwapImageFilter(filter))))
        );
    }

    private float[] getImageFilterValues(final Color color) {
        return new float[]{
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f};
    }

    class WhiteColorSwapImageFilter extends RGBImageFilter {
        private final float[] filter;

        public WhiteColorSwapImageFilter(float[] arrays) {
            super();
            filter = Arrays.copyOf(arrays, arrays.length);
            canFilterIndexColorModel = true;
        }

        @Override
        public int filterRGB(int x, int y, int argb) {
            int r = (int) (((argb >> 16) & 0xff) * filter[0]);
            int g = (int) (((argb >> 8) & 0xff) * filter[1]);
            int b = (int) (((argb) & 0xff) * filter[2]);
            return (argb & 0xff000000) | (r << 16) | (g << 8) | (b);
        }
    }

}

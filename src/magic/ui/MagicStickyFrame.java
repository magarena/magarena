package magic.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import magic.data.GeneralConfig;
import magic.data.settings.BooleanSetting;

@SuppressWarnings("serial")
class MagicStickyFrame extends JFrame {

    private static final Dimension MIN_SIZE = new Dimension(GeneralConfig.DEFAULT_FRAME_WIDTH, GeneralConfig.DEFAULT_FRAME_HEIGHT);

    protected final GeneralConfig config = GeneralConfig.getInstance();

    private boolean ignoreWindowDeactivate;
    private int normalFrameState;
    private Rectangle normalFrameRect;

    MagicStickyFrame() {
        addWindowListeners();
        restoreSavedFrameState();
    }

    private void addWindowListeners() {
        addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (!isUndecorated()) {
                    normalFrameState = e.getNewState();
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(final WindowEvent ev) {
                if (isFullScreen() && ev.getOppositeWindow() == null && !ignoreWindowDeactivate) {
                    try {
                        setState(Frame.ICONIFIED);
                    } catch (Exception ex) {
                        // see issue #130: Crashes when there is a change in focus? On Mac.
                        System.err.println("setState(Frame.ICONIFIED) failed\n" + ex);
                    }
                }
                ignoreWindowDeactivate = false;
            }
        });
    }

    private boolean isFullScreen() {
        return getExtendedState() == JFrame.MAXIMIZED_BOTH && this.isUndecorated();
    }

    private void saveNormalFrameSizePos() {
        normalFrameState = getExtendedState();
        setExtendedState(Frame.NORMAL);
        normalFrameRect = new Rectangle(getLocation(), getSize());
    }

    private void setFrameFullScreenState() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void setFrameNormalState() {
        setExtendedState(normalFrameState);
        setSize(normalFrameRect.width, normalFrameRect.height);
        setLocation(normalFrameRect.x, normalFrameRect.y);
        setUndecorated(false);
    }

    private void setFullScreenMode(final boolean isFullScreen) {
        if (isFullScreen) {
            saveNormalFrameSizePos();
            this.dispose();
            setFrameFullScreenState();
        } else {
            this.dispose();
            setFrameNormalState();
        }
        setVisible(true);
        ignoreWindowDeactivate = true;
    }

    public void toggleFullScreenMode() {
        setFullScreenMode(!isFullScreen());
    }

    private void setSizableFrameState() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = config.getSizableFrameBounds();
        Point p1 = new Point(rect.x, rect.y);
        Point p2 = new Point(p1.x + rect.width, p1.y + rect.height);
        if (p1.x >= 0 && p1.y >= 0 && p2.x <= screen.width && p2.y <= screen.height) {
            setSize(rect.width, rect.height);
            setLocation(rect.x, rect.y);
        } else {
            // saved frame overlaps screen bounds so position
            // in center of screen at minimum size.
            setSize(MIN_SIZE);
            setLocationRelativeTo(null);
        }
    }

    private void restoreSavedFrameState() {

        setMinimumSize(MIN_SIZE);
        setSizableFrameState();

        if (config.get(BooleanSetting.MAXIMIZE_FRAME) == true) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        normalFrameState = getExtendedState();

        if (config.isFullScreen()) {
            setFullScreenMode(true);
        }
    }

    private boolean isMaximized() {
        return normalFrameState == Frame.MAXIMIZED_BOTH;
    }

    protected void saveSizeAndPosition() {

        config.set(BooleanSetting.MAXIMIZE_FRAME, isMaximized());
        config.setFullScreen(isFullScreen());

        if (getExtendedState() == Frame.NORMAL) {
            config.setSizableFrameBounds(getLocation(), getSize());

        } else if (getExtendedState() == Frame.MAXIMIZED_BOTH && !isFullScreen()) {
            setExtendedState(Frame.NORMAL);
            config.setSizableFrameBounds(getLocation(), getSize());

        } else if (isFullScreen()) {
            config.setSizableFrameBounds(normalFrameRect);
        }

        config.save();
    }

}

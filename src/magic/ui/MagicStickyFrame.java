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
import magic.data.settings.IntegerSetting;

@SuppressWarnings("serial")
public class MagicStickyFrame extends JFrame {

    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = 600;

    private static final Dimension MIN_SIZE =
        new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);

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

    private Rectangle getSizableFrameSetting() {
        return new Rectangle(
            GeneralConfig.get(IntegerSetting.FRAME_LEFT),
            GeneralConfig.get(IntegerSetting.FRAME_TOP),
            GeneralConfig.get(IntegerSetting.FRAME_WIDTH),
            GeneralConfig.get(IntegerSetting.FRAME_HEIGHT)
        );
    }

    private void setSizableFrameSetting(Point aPoint, Dimension aSize) {
        GeneralConfig.set(IntegerSetting.FRAME_LEFT, aPoint.x);
        GeneralConfig.set(IntegerSetting.FRAME_TOP, aPoint.y);
        GeneralConfig.set(IntegerSetting.FRAME_WIDTH, aSize.width);
        GeneralConfig.set(IntegerSetting.FRAME_HEIGHT, aSize.height);
    }

    private void setSizableFrameSetting(Rectangle rect) {
        setSizableFrameSetting(rect.getLocation(), rect.getSize());
    }

    private void setSizableFrameState() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = getSizableFrameSetting();
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

        if (GeneralConfig.get(BooleanSetting.MAXIMIZE_FRAME)) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        normalFrameState = getExtendedState();

        if (GeneralConfig.get(BooleanSetting.FULL_SCREEN)) {
            setFullScreenMode(true);
        }
    }

    private boolean isMaximized() {
        return normalFrameState == Frame.MAXIMIZED_BOTH;
    }

    protected void saveSizeAndPosition() {

        GeneralConfig.set(BooleanSetting.MAXIMIZE_FRAME, isMaximized());
        GeneralConfig.set(BooleanSetting.FULL_SCREEN, isFullScreen());

        if (getExtendedState() == Frame.NORMAL) {
            setSizableFrameSetting(getLocation(), getSize());

        } else if (getExtendedState() == Frame.MAXIMIZED_BOTH && !isFullScreen()) {
            setExtendedState(Frame.NORMAL);
            setSizableFrameSetting(getLocation(), getSize());

        } else if (isFullScreen()) {
            setSizableFrameSetting(normalFrameRect);
        }

        GeneralConfig.saveToFile();
    }

}

package magic.ui.duel.sidebar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.widget.MenuedTitleBar;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogStackViewer extends TexturedPanel {

    // translatable strings
    private static final String _S1 = "View log file";
    private static final String _S2 = "Log Off";
    private static final String _S3 = "Log On";
    private static final String _S4 = "Log";
    private static final String _S5 = "Stack";

    private final LogViewer logViewer;
    private final StackViewer stackViewer;
    private final MenuedTitleBar logTitleBar;
    private final MenuedTitleBar stackTitleBar;
    private final LogPopupMenu logMenu;

    LogStackViewer(LogViewer aLogBookViewer, StackViewer aStackViewer) {
        
        this.logViewer = aLogBookViewer;
        this.stackViewer = aStackViewer;

        logMenu = new LogPopupMenu(isLogVisible());

        logTitleBar = new MenuedTitleBar(UiString.get(_S4), logMenu);
        stackTitleBar = new MenuedTitleBar(UiString.get(_S5), null);

        setOpaque(false);
        setBorders();

        setLayout(new MigLayout("insets 0, gap 0, flowy", "[fill, grow]", "[][shrinkprio 200][][]"));
        setLogVisible(isLogVisible());
    }

    private void doUpdateLayout() {
        removeAll();
        add(logTitleBar, "hidemode 1");
        add(logViewer, (isLogVisible() ? "h 80:100%" : "h 0:100%") + ", hidemode 0");
        add(stackTitleBar);
        add(stackViewer);
        revalidate();
    }

    Rectangle getStackViewerRectangle(Component canvas) {
        final Point pointOnCanvas = SwingUtilities.convertPoint(this, stackTitleBar.getLocation(), canvas);
        return new Rectangle(pointOnCanvas.x, pointOnCanvas.y, stackTitleBar.getWidth(), stackTitleBar.getHeight());
    }

    private void setBorders() {
        setBorder(null);
        logTitleBar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        logViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        stackTitleBar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        stackViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
    }

    private void setLogVisible(boolean isVisible) {
        logTitleBar.setPopupMenu(isVisible ? logMenu : null);
        stackTitleBar.setPopupMenu(isVisible ? null : logMenu);
        logTitleBar.setVisible(isVisible);
        logViewer.setVisible(isVisible);
        logMenu.setIsLogVisible(isVisible);
        doUpdateLayout();
    }

    private boolean isLogVisible() {
        return logViewer.isVisible();
    }

    private void switchLogVisibility() {
        setLogVisible(!isLogVisible());
    }

    private class LogPopupMenu extends JPopupMenu {

        private final JMenuItem visibilityMenu = new JMenuItem();

        public LogPopupMenu(boolean isLogVisible) {
            add(getVisibilityMenuItem(isLogVisible));
            addSeparator();
            add(getLogFileMenuItem());
        }

        private JMenuItem getLogFileMenuItem() {
            final JMenuItem menu = new JMenuItem(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ScreenController.showGameLogScreen();
                }
            });
            menu.setText(UiString.get(UiString.get(_S1)));
            return menu;
        }
        
        private JMenuItem getVisibilityMenuItem(boolean isLogVisible) {            
            visibilityMenu.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchLogVisibility();
                }
            });
            visibilityMenu.setText(getVisibilityMenuText(isLogVisible));
            return visibilityMenu;
        }

        private String getVisibilityMenuText(boolean isVisible) {
            return isVisible ? UiString.get(_S2) : UiString.get(_S3);
        }

        private void setIsLogVisible(boolean isVisible) {
            visibilityMenu.setText(getVisibilityMenuText(isVisible));
        }
    }

}

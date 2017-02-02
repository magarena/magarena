package magic.ui.widget.duel.viewer;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.ui.FontsAndBorders;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class GameStatusPanel extends TexturedPanel implements ChangeListener {

    private final MigLayout migLayout = new MigLayout("flowy, insets 0, gap 0");
    private final UserActionPanel userActionPanel;
    private final SwingGameController controller;
    private boolean isNewTurnNotification = false;
    private final NewTurnPanel newTurnPanel;
    private final TurnStatusPanel turnStatusPanel;


    public GameStatusPanel(final SwingGameController controller) {

        this.controller = controller;

        // create UI components
        userActionPanel = new UserActionPanel(controller);
        newTurnPanel = new NewTurnPanel();
        turnStatusPanel = new TurnStatusPanel(controller);

        setLookAndFeel();
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        setLayout(migLayout);
        add(isNewTurnNotification ? newTurnPanel : turnStatusPanel, "w 100%, h 100%");
        add(userActionPanel, "w 100%, h 100%");
        revalidate();
        repaint();
    }

    private void setLookAndFeel() {
        userActionPanel.setOpaque(false);
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        setBorder(FontsAndBorders.BLACK_BORDER);
    }

    public UserActionPanel getUserActionPanel() {
        return userActionPanel;
    }

    public void update() {
        turnStatusPanel.refresh(controller.getViewerInfo());
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        update();
    }

    public void showNewTurnNotification(GameViewerInfo gameInfo) {
        assert SwingUtilities.isEventDispatchThread();
        isNewTurnNotification = true;
        newTurnPanel.refreshData(gameInfo);
        refreshLayout();
    }

    public void hideNewTurnNotification() {
        assert SwingUtilities.isEventDispatchThread();
        isNewTurnNotification = false;
        refreshLayout();
    }

    public Rectangle getTurnPanelLayout(Component container) {
        final Point pt = SwingUtilities.convertPoint(this, turnStatusPanel.getLocation(), container);
        return new Rectangle(pt.x, pt.y, turnStatusPanel.getWidth(), turnStatusPanel.getHeight());
    }

}

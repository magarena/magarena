package magic.ui.widget.duel.sidebar;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import magic.ui.FontsAndBorders;
import magic.ui.IChoiceViewer;
import magic.ui.duel.viewerinfo.StackItemViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StackViewer extends TexturedPanel
    implements IChoiceViewer {

    private final SwingGameController controller;
    private final Collection<StackButton> buttons;
    private JScrollPane stackScrollPane;
    private ScrollablePanel stackScrollablePanel;

    public StackViewer(final SwingGameController controller) {

        this.controller=controller;

        controller.registerChoiceViewer(this);
        buttons=new ArrayList<>();

        refreshLayout();
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
    }

    private void refreshLayout() {

        stackScrollablePanel = new ScrollablePanel();
        stackScrollablePanel.setLayout(new MigLayout("insets 0, gap 0, flowy"));
        stackScrollablePanel.setOpaque(false);

        stackScrollPane = new JScrollPane(stackScrollablePanel);
        stackScrollPane.setMinimumSize(new Dimension(0, 0));
        stackScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        stackScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        stackScrollPane.setBorder(null);
        stackScrollPane.setOpaque(false);
        stackScrollPane.getViewport().setOpaque(false);

        removeAll();
        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        add(stackScrollPane, "w 100%");
    }

    public void update() {

        final int maxWidth = getWidth() - 30;

        stackScrollablePanel.removeAll();
        buttons.clear();

        // Display stack items
        final List<StackItemViewerInfo> stack = controller.getGameViewerInfo().getStack();
        final int stackSize = stack.size();
        for (int i = 0; i < stackSize; i++) {
            final StackItemViewerInfo stackInfo = stack.get(i);
            StackButton btn = new StackButton(controller, stackInfo, maxWidth, stackSize - i);
            buttons.add(btn);
            stackScrollablePanel.add(btn, "w 100%");
        }
        stackScrollablePanel.revalidate();
        controller.setStackCount(buttons.size());

        // set preferred size for layout manager.
        int preferredHeight =
                stackScrollablePanel.getPreferredSize().height;
        setPreferredSize(new Dimension(getWidth(), preferredHeight));

        showValidChoices(controller.getValidChoices());
    }

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        for (final StackButton button : buttons) {
            button.showValidChoices(validChoices);
        }
    }

    /**
     * By using a Scrollable panel in the ScrollPane the content will adjust
     * correctly based on whether the vertical scrollbar is visible or not.
     */
    @SuppressWarnings("serial")
    private final class ScrollablePanel extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return getFont().getSize();
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return getFont().getSize();
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        // we don't want to track the height, because we want to scroll vertically.
        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

    }

}

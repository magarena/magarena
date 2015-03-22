package magic.ui.duel.viewer;

import magic.ui.SwingGameController;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;
import magic.ui.widget.TextLabel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class StackViewer extends JPanel implements ChoiceViewer {

    private static final long serialVersionUID = 1L;
    private static final String TITLE_CAPTION = "Stack";

    private final SwingGameController controller;
    private final boolean isImageMode;
    private final Collection<StackButton> buttons;
    private List<IStackViewerListener> _listeners = new ArrayList<>();
    private JScrollPane stackScrollPane;
    private ScrollablePanel stackScrollablePanel;
    private TitleBar stackTitleBar;

    public StackViewer(final SwingGameController controller, final boolean isImageMode0) {

        this.controller=controller;
        this.isImageMode=isImageMode0;

        controller.registerChoiceViewer(this);
        buttons=new ArrayList<StackButton>();

        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        //
        // Title bar
        stackTitleBar = new TitleBar(TITLE_CAPTION);
        add(stackTitleBar, "w 100%");
        stackTitleBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        //
        // Stack scroll pane
        stackScrollablePanel = new ScrollablePanel();
        stackScrollablePanel.setLayout(new MigLayout("insets 0, gap 0, flowy"));
        stackScrollPane = new JScrollPane(stackScrollablePanel);
        add(stackScrollPane, "w 100%");
        stackScrollPane.setMinimumSize(new Dimension(0, 0));
        stackScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        stackScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        stackScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    }

    public static String getTitle() {
        return TITLE_CAPTION;
    }

    public void update() {

        final int maxWidth=getWidth()-40;

        stackScrollablePanel.removeAll();
        buttons.clear();

        // Display stack items
        final List<StackViewerInfo> stack = controller.getViewerInfo().getStack();
        for (final StackViewerInfo stackInfo : stack) {
            StackButton btn = new StackButton(stackInfo, maxWidth);
            buttons.add(btn);
            stackScrollablePanel.add(btn, "w 100%");
        }
        stackTitleBar.setText(TITLE_CAPTION + (stack.size() > 0 ? ": " + stack.size() : ""));

        // set preferred size for layout manager.
        int preferredHeight =
                stackTitleBar.getPreferredSize().height +
                stackScrollablePanel.getPreferredSize().height;
        if (!isEmpty()) {
            preferredHeight += 2;
        }
        setPreferredSize(new Dimension(getWidth(), preferredHeight));

        notifyStackViewerUpdated();

        showValidChoices(controller.getValidChoices());

    }

    public boolean isEmpty() {
        return buttons.isEmpty();
    }

    @Override
    public void showValidChoices(final Set<?> validChoices) {

        for (final StackButton button : buttons) {

            button.showValidChoices(validChoices);
        }
    }

    private final class StackButton extends PanelButton implements ChoiceViewer {

        private static final long serialVersionUID = 1L;

        private final StackViewerInfo stackInfo;

        public StackButton(final StackViewerInfo stackInfo,final int maxWidth) {

            this.stackInfo=stackInfo;

            final JPanel panel=new JPanel();
            panel.setOpaque(false);
            panel.setBorder(FontsAndBorders.getPlayerBorder(stackInfo.visible));
            panel.setLayout(new BorderLayout(0,0));
            setComponent(panel);

            final JLabel sourceLabel=new JLabel(stackInfo.name);
            sourceLabel.setIcon(stackInfo.icon);
            sourceLabel.setForeground(ThemeFactory.getInstance().getCurrentTheme().getNameColor());
            panel.add(sourceLabel,BorderLayout.NORTH);

            final TextLabel textLabel=new TextLabel(stackInfo.description,maxWidth,false);
            panel.add(textLabel,BorderLayout.CENTER);
        }

        @Override
        public void mouseClicked() {
            controller.processClick(stackInfo.itemOnStack);
        }

        @Override
        public void mouseEntered() {
            if (isImageMode) {
                final Rectangle rect=new Rectangle(
                        StackViewer.this.getLocationOnScreen().x,
                        getLocationOnScreen().y,
                        StackViewer.this.getWidth(),
                        getHeight());
                controller.viewInfoRight(stackInfo.cardDefinition,0,rect);
            }
        }

        @Override
        public void mouseExited() {
            if (isImageMode) {
                controller.hideInfo();
            }
        }

        @Override
        public void showValidChoices(final Set<?> validChoices) {
            setValid(validChoices.contains(stackInfo.itemOnStack));
        }

        @Override
        public Color getValidColor() {
            return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
        }
    }

    public synchronized void addListener(IStackViewerListener obj) {
        _listeners.add(obj);
    }

    public synchronized void removeListener(IStackViewerListener obj) {
        _listeners.remove(obj);
    }

    private synchronized void notifyStackViewerUpdated() {
        for (final IStackViewerListener listener : _listeners) {
            listener.stackViewerUpdated();
        }
    }

    /**
     * By using a Scrollable panel in the ScrollPane the content will adjust
     * correctly based on whether the vertical scrollbar is visible or not.
     */
    @SuppressWarnings("serial")
    private final class ScrollablePanel extends JPanel implements Scrollable {

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return getFont().getSize();
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return getFont().getSize();
        }

        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        // we don't want to track the height, because we want to scroll vertically.
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

    }

}

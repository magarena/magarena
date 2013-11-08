package magic.ui.viewer;

import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;
import magic.ui.widget.TextLabel;
import magic.ui.widget.TitleBar;
import magic.ui.widget.ViewerScrollPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

    private final ViewerInfo viewerInfo;
    private final GameController controller;
    private final ViewerScrollPane viewerPane;
    private final boolean image;
    private final Collection<StackButton> buttons;
    private Rectangle setRectangle = new Rectangle();
    private List<IStackViewerListener> _listeners = new ArrayList<>();

    final TitleBar stackTitleBar;

    public StackViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean image) {

        boolean useMigLayout = false;

        this.viewerInfo=viewerInfo;
        this.controller=controller;
        this.image=image;
        setOpaque(false);

        controller.registerChoiceViewer(this);

        setLayout(useMigLayout ? new MigLayout("debug, insets 0, gap 0, flowy") : new BorderLayout(0, 0));

        final Theme theme = ThemeFactory.getInstance().getCurrentTheme();
        stackTitleBar = new TitleBar("Stack");
        stackTitleBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        stackTitleBar.setIcon(theme.getIcon(Theme.ICON_SMALL_STACK));
        add(stackTitleBar, useMigLayout ? "w 100%, h 20px!" : BorderLayout.NORTH);

        viewerPane=new ViewerScrollPane();
        viewerPane.setBorder(BorderFactory.createEmptyBorder());
        add(viewerPane, useMigLayout ? "w 100%, h 0" : BorderLayout.CENTER);

        // Set unchanging minimum sizes.
        setMinimumSize(new Dimension(0, stackTitleBar.getMinimumSize().height));
        viewerPane.setMinimumSize(new Dimension(0, 0));

        buttons=new ArrayList<StackButton>();

    }

    @Override
    public void setBounds(final Rectangle r) {

        this.setRectangle=new Rectangle(r);
        super.setBounds(r);
    }

    public static String getTitle() {
        return "Stack";
    }

    private JPanel getNewStackButtonPanel(final StackViewerInfo stackInfo, final int maxWidth) {
        final JPanel panel=new JPanel(new BorderLayout());
        panel.setBorder(FontsAndBorders.SMALL_EMPTY_BORDER);
        final StackButton button=new StackButton(stackInfo, maxWidth);
        buttons.add(button);
        panel.add(button,BorderLayout.CENTER);
        return panel;
    }

    public void update() {

        final int maxWidth=getWidth()-40;

        buttons.clear();

        final JPanel contentPanel=viewerPane.getContent();
        for (final StackViewerInfo stackInfo : viewerInfo.getStack()) {
            contentPanel.add(getNewStackButtonPanel(stackInfo, maxWidth));
        }

        if (image) {
            final int contentHeight = viewerPane.getContent().getPreferredSize().height + 20;
            if (contentHeight < setRectangle.height) {
                setBounds(getX(), setRectangle.y + setRectangle.height -contentHeight, getWidth(), contentHeight);
            } else {
                setBounds(setRectangle);
            }
        }

        showValidChoices(controller.getValidChoices());
        viewerPane.switchContent();
        repaint();

        notifyStackViewerUpdated();

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
            panel.setLayout(new BorderLayout(0,2));
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
            if (image) {
                final Rectangle rect=new Rectangle(
                        StackViewer.this.getLocationOnScreen().x,
                        getLocationOnScreen().y,
                        StackViewer.this.getWidth(),
                        getHeight());
                controller.viewInfoRight(stackInfo.cardDefinition,0,rect);
            } else {
                controller.viewCard(stackInfo.cardDefinition,0);
            }
        }

        @Override
        public void mouseExited() {
            if (image) {
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
}

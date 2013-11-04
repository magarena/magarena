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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class StackViewer extends JPanel implements ChoiceViewer {

    private static final long serialVersionUID = 1L;

    private final ViewerInfo viewerInfo;
    private final GameController controller;
    private final ViewerScrollPane viewerPane;
    private final boolean image;
    private final Collection<StackButton> buttons;
    private Rectangle setRectangle = new Rectangle();

    private JComponent layoutContainer = null;
    final TitleBar stackTitleBar;

    public StackViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean image) {

        boolean useMig = false;

        this.viewerInfo=viewerInfo;
        this.controller=controller;
        this.image=image;
        setOpaque(false);

        controller.registerChoiceViewer(this);

        setLayout(useMig ? new MigLayout("debug, insets 0, gap 0, flowy") : new BorderLayout(0, 0));

        final Theme theme = ThemeFactory.getInstance().getCurrentTheme();
        stackTitleBar = new TitleBar("Stack");
        stackTitleBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        stackTitleBar.setIcon(theme.getIcon(Theme.ICON_SMALL_STACK));
        add(stackTitleBar, useMig ? "w 100%, h 20px!" : BorderLayout.NORTH);

        viewerPane=new ViewerScrollPane();
        viewerPane.setBorder(BorderFactory.createEmptyBorder());
        add(viewerPane, useMig ? "w 100%, h 0" : BorderLayout.CENTER);

        // Set unchanging minimum sizes.
        setMinimumSize(new Dimension(0, stackTitleBar.getMinimumSize().height));
        viewerPane.setMinimumSize(new Dimension(0, 0));

        buttons=new ArrayList<StackButton>();

        update();

    }

    @Override
    public void setBounds(final Rectangle r) {

        this.setRectangle=new Rectangle(r);
        super.setBounds(r);
    }

    public static String getTitle() {
        return "Stack";
    }

    public void update() {
        System.out.print("StackViewer.update()");

        final int maxWidth=getWidth()-40;

        buttons.clear();

        final JPanel contentPanel=viewerPane.getContent();
        for (final StackViewerInfo stackInfo : viewerInfo.getStack()) {

            final JPanel panel=new JPanel(new BorderLayout());
            panel.setBorder(FontsAndBorders.SMALL_EMPTY_BORDER);
            final StackButton button=new StackButton(stackInfo,maxWidth);
            buttons.add(button);
            panel.add(button,BorderLayout.CENTER);
            contentPanel.add(panel);
        }

        if (image) {
            final int contentHeight = viewerPane.getContent().getPreferredSize().height + 20;
            if (contentHeight < setRectangle.height) {
                setBounds(getX(), setRectangle.y + setRectangle.height -contentHeight, getWidth(), contentHeight);
            } else {
                setBounds(setRectangle);
            }
        }

        int stackHeight = getBounds().height;
        System.out.print(" : contentHeight = " + stackHeight); // contentHeight);

        showValidChoices(controller.getValidChoices());
        viewerPane.switchContent();
        repaint();

        System.out.println();

        if (getParent() != null && layoutContainer == null) {
            layoutContainer = (JComponent)getParent().getParent();
            System.out.println(layoutContainer.getClass().getName());
        }
        if (layoutContainer != null) {
            if (layoutContainer.getClass().getName() == "javax.swing.JSplitPane") {
                setSplitterPosition((JSplitPane)layoutContainer, stackHeight);
            } else if (layoutContainer.getClass().getName() == "magic.ui.widget.TexturedPanel") {
                System.out.println("Validating JPanel");
                Dimension d = new Dimension(getBounds().width, getBounds().height + 1);
                setPreferredSize(d);
                setMinimumSize(d);
                setMaximumSize(d);
                viewerPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                layoutContainer.validate();
            }
        }



//        //updateSplitterComponent(getBounds().height);
//        if (getParent() != null && splitter == null) {
//            Component c = getParent().getParent();
//            if (c.getClass().getName() == "javax.swing.JSplitPane") {
//                splitter = (JSplitPane)getParent().getParent();
//            }
//        }
//
//        if (splitter != null) {
//            setSplitterPosition(stackHeight);
//        } else {
//
//        }

    }

    private void setSplitterPosition(JSplitPane splitter, int stackHeight) {
        if (splitter != null) {
            System.out.println("splitter.getDividerLocation() = " + splitter.getDividerLocation());
            splitter.setDividerLocation(splitter.getHeight() - stackHeight - stackTitleBar.getHeight());
            //splitter.validate();
        }
    }

//    private void updateSplitterComponent(int stackHeight) {
//        if (stackHeight > 0 && splitter == null) {
//            Component p = getParent();
//            while (p != null) {
//                if (p.getClass().getName() == "javax.swing.JSplitPane") {
//                    //splitter = (JSplitPane)p;
//                    break;
//                }
//            }
//        }
//        if (splitter != null) {
//
////            } else {
////                throw new NullPointerException("Could not locate JSplitPane component.");
////            }
//        }
//    }

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
}

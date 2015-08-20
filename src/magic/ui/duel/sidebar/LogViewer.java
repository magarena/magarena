package magic.ui.duel.sidebar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import magic.data.GeneralConfig;
import magic.model.MagicMessage;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.SwingGameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MenuedTitleBar;
import magic.ui.widget.MessagePanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogViewer extends JPanel {

    // translatable strings
    private static final String _S1 = "Log";
    private static final String _S2 = "Log Off";
    private static final String _S3 = "Log On";
    private static final String _S4 = "View log file";

    private static final CompoundBorder SEPARATOR_BORDER=BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY),
        FontsAndBorders.EMPTY_BORDER
    );

    private final SwingGameController controller;
    private final JPanel messagePanels;
    private final JScrollPane scrollPane;
    private final TitleBar tb;
    private final JMenuItem visibilityMenu = new JMenuItem();

    LogViewer(final SwingGameController aController) {
        controller = aController;

        tb = new MenuedTitleBar(UiString.get(_S1), getLogPopupMenu());
        tb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        messagePanels = new JPanel();
        messagePanels.setOpaque(false);
        messagePanels.setLayout(new MigLayout("insets 0, gap 0, flowy"));

        scrollPane = new LogScrollPane(messagePanels);

        setLogVisibility(GeneralConfig.getInstance().isLogMessagesVisible());

        doUpdateLayout();

    }

    private void setLogVisibility(final boolean isVisible) {
        visibilityMenu.setText(isVisible ? UiString.get(_S2) : UiString.get(_S3));
        tb.setText(isVisible ? UiString.get(_S1) :UiString.get(_S2));
        scrollPane.setVisible(isVisible);
        setOpaque(isVisible);
        GeneralConfig.getInstance().setLogMessagesVisible(isVisible);
    }

    private JPopupMenu getLogPopupMenu() {
        visibilityMenu.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final boolean isVisible = !scrollPane.isVisible();
                setLogVisibility(isVisible);
                if (isVisible) {
                    update();
                    revalidate();
                }
            }
        });
        // View log file
        final JMenuItem menu2 = new JMenuItem(new AbstractAction(UiString.get(_S4)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenController.showGameLogScreen();
            }
        });
        final JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(visibilityMenu);
        popupMenu.addSeparator();
        popupMenu.add(menu2);
        return popupMenu;
    }

    private void doUpdateLayout() {
        setLayout(new BorderLayout());
        add(tb, BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
    }
   
    void update() {
        final List<MagicMessage> msgs = controller.getViewerInfo().getLog();
        final int n = msgs.size();
        int i = 0;
        for (int c = 0; c < messagePanels.getComponentCount();) {
            final MessagePanel mp = (MessagePanel)messagePanels.getComponent(c);
            final MagicMessage msg = i < n ? msgs.get(i) : null;
            if (mp.getMessage() == msg) {
                i++;
                c++;
            } else {
                messagePanels.remove(c);
            }
        }
        for (;i < n; i++) {
            messagePanels.add(getNewMessagePanel(msgs.get(i)), "w 100%");
        }
    }

    private MessagePanel getNewMessagePanel(final MagicMessage message) {
        final MessagePanel panel = new MessagePanel(message, getWidth());
        panel.setOpaque(false);
        panel.setBorder(SEPARATOR_BORDER);
        return panel;
    }

    private class LogScrollPane extends JScrollPane {

        private static final int INCREMENT = 108;

        public LogScrollPane(Component aView) {

            super(aView);

            final JScrollBar vscroll = getVerticalScrollBar();
            vscroll.setUnitIncrement(INCREMENT);
            vscroll.setBlockIncrement(INCREMENT);
            vscroll.addAdjustmentListener(new AdjustmentListener() {
                @Override
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());
                }
            });

            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

            setBorder(BorderFactory.createEmptyBorder());
            getViewport().setOpaque(false);

        }

    }
}

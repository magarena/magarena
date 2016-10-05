package magic.ui.widget.duel.sidebar;

import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import magic.data.GeneralConfig;
import magic.model.MagicMessage;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.message.MessageStyle;
import magic.ui.widget.message.TextComponent;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogViewer extends TexturedPanel {

    private final SwingGameController controller;
    private final JPanel messagePanels;
    private final JScrollPane scrollPane;

    LogViewer(final SwingGameController aController) {

        this.controller = aController;

        messagePanels = new JPanel();
        messagePanels.setOpaque(false);
        messagePanels.setLayout(new MigLayout("insets 0, gap 0, flowy"));

        scrollPane = new LogScrollPane(messagePanels);

        setLayout(new MigLayout("insets 0"));
        add(scrollPane, "w 100%, h 100%");

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

    }

    void update() {
        boolean isRemoved = false;
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
                isRemoved = true;
            }
        }
        final boolean isAdded = i < n;
        for (;i < n; i++) {
            messagePanels.add(getNewMessagePanel(msgs.get(i)), "w 100%");
        }
        if (isAdded || isRemoved) {
            messagePanels.revalidate();
            scrollPane.repaint();
        }
    }

    private MessagePanel getNewMessagePanel(final MagicMessage message) {
        return new MessagePanel(message, this, controller);
    }

    void setMessageStyle(MessageStyle aStyle) {
        TextComponent.messageStyle = aStyle;
        messagePanels.removeAll();
        update();
        GeneralConfig.getInstance().setLogMessageStyle(aStyle);
        GeneralConfig.getInstance().save();
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

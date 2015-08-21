package magic.ui.duel.sidebar;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import magic.model.MagicMessage;
import magic.ui.SwingGameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogViewer extends TexturedPanel {

    private static final CompoundBorder SEPARATOR_BORDER=BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY),
        FontsAndBorders.EMPTY_BORDER
    );

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

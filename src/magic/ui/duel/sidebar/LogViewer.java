package magic.ui.duel.sidebar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import magic.data.GeneralConfig;
import magic.model.MagicMessage;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.SwingGameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MessagePanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogViewer extends JPanel {

    // translatable strings
    private static final String _S1 = "Log";
    private static final String _S2 = "OFF";
    private static final String _S3 = "[ LMB: On/Off   RMB: Full Log File ]";

    private static final CompoundBorder SEPARATOR_BORDER=BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY),
        FontsAndBorders.EMPTY_BORDER
    );

    private static final int INCREMENT=108;

    private final SwingGameController controller;
    private final JPanel messagePanels;
    private final JScrollPane scrollPane;
    private final TitleBar tb;

    private final MouseAdapter mouseDispatcher = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) { dispatchEvent(e); }
        @Override
        public void mouseEntered(MouseEvent e) { dispatchEvent(e); }
        @Override
        public void mouseExited(MouseEvent e) { dispatchEvent(e); }
    };

    LogViewer(final SwingGameController aController) {
        controller = aController;

        tb = new TitleBar(UiString.get(_S1));
        tb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        tb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tb.addMouseListener(mouseDispatcher);

        messagePanels = new JPanel();
        messagePanels.setOpaque(false);
        messagePanels.setLayout(new MigLayout("insets 0, gap 0, flowy"));

        scrollPane=new JScrollPane();
        scrollPane.setVisible(GeneralConfig.getInstance().isLogMessagesVisible());
        scrollPane.getViewport().setView(messagePanels);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        final JScrollBar vscroll = scrollPane.getVerticalScrollBar();
        vscroll.setUnitIncrement(INCREMENT);
        vscroll.setBlockIncrement(INCREMENT);
        vscroll.addAdjustmentListener(new AdjustmentListener() {  
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {  
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
            }
        });
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        scrollPane.addMouseListener(mouseDispatcher);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        ScreenController.showGameLogScreen();
                        updateTitlebarCaption(false);
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        scrollPane.setVisible(!scrollPane.isVisible());
                        updateTitlebarCaption(true);
                    }
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                updateTitlebarCaption(false);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                updateTitlebarCaption(true);
            }
        });

        updateTitlebarCaption(false);
        
        doUpdateLayout();

    }

    private void doUpdateLayout() {
        setLayout(new BorderLayout());
        add(tb, BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
    }
   
    private void updateTitlebarCaption(final boolean showHelpHints) {
        final boolean isHidden = !scrollPane.isVisible();
        tb.setText(String.format("%s %s%s",
                UiString.get(_S1),
                isHidden ? " " + UiString.get(_S2) : "",
                showHelpHints ? "   " + UiString.get(_S3) : "")
        );
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
}

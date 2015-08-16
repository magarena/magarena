package magic.ui.duel.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import magic.data.GeneralConfig;
import magic.model.MagicLogBook;
import magic.model.MagicMessage;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MessagePanel;
import magic.ui.widget.TitleBar;

@SuppressWarnings("serial")
public class LogBookViewer extends JPanel {

    // translatable strings
    private static final String _S1 = "Log";
    private static final String _S2 = "OFF";
    private static final String _S3 = "[ LMB: On/Off   RMB: Full Log File ]";

    private static final CompoundBorder SEPARATOR_BORDER=BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY),
        FontsAndBorders.EMPTY_BORDER
    );

    private static final int INCREMENT=108;

    private final MagicLogBook logBook;
    private final JPanel messagePanels;
    private final JScrollPane scrollPane;
    private boolean isScrollbarVisible;
    private final TitleBar tb;
    private int messagesHeight = 0;

    private final MouseAdapter mouseDispatcher = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) { dispatchEvent(e); }
        @Override
        public void mouseEntered(MouseEvent e) { dispatchEvent(e); }
        @Override
        public void mouseExited(MouseEvent e) { dispatchEvent(e); }
    };

    public LogBookViewer(final MagicLogBook logBook) {

        this.logBook=logBook;

        setLayout(new BorderLayout());

        tb = new TitleBar(UiString.get(_S1));
        tb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        tb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tb.addMouseListener(mouseDispatcher);
        add(tb, BorderLayout.NORTH);

        final JPanel centerPanel=new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        add(centerPanel,BorderLayout.CENTER);

        messagePanels=new JPanel();
        messagePanels.setOpaque(false);
        messagePanels.setLayout(new BoxLayout(messagePanels,BoxLayout.Y_AXIS));
        centerPanel.add(messagePanels,BorderLayout.NORTH);

        scrollPane=new JScrollPane();
        scrollPane.setVisible(GeneralConfig.getInstance().isLogMessagesVisible());
        scrollPane.getViewport().setView(centerPanel);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollbarPolicy();
        scrollPane.getVerticalScrollBar().setUnitIncrement(INCREMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(INCREMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        scrollPane.addMouseListener(mouseDispatcher);
        add(scrollPane,BorderLayout.CENTER);

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

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent arg0) {
                forceVerticalScrollbarToMax();
            }
        });

        updateTitlebarCaption(false);

    }

    private void updateTitlebarCaption(final boolean showHelpHints) {
        final boolean isHidden = !scrollPane.isVisible();
        tb.setText(String.format("%s %s%s",
                UiString.get(_S1),
                isHidden ? " " + UiString.get(_S2) : "",
                showHelpHints ? "   " + UiString.get(_S3) : "")
        );

    }

    public void update() {
        messagePanels.removeAll();
        messagesHeight = 0;
        synchronized (logBook) {
            final ListIterator<MagicMessage> itr = getUndoIterator();
            while (itr.hasNext()) {
                addMessagePanel(getNewMessagePanel(itr.next()));
            }
        }
        forceVerticalScrollbarToMax();
    }

    public void addMagicMessage(final MagicMessage magicMessage) {
        addMessagePanel(getNewMessagePanel(magicMessage));
        forceVerticalScrollbarToMax();
    }

    /**
     * returns iterator pointing to first visible most recent message.
     * <p>
     * only need to create MessagePanels for last X messages that will be visible in the log viewer.
     */
    private ListIterator<MagicMessage> getUndoIterator() {
        final ListIterator<MagicMessage> listIterator = logBook.listIterator(logBook.size());
        int totalHeight = 0;
        while (listIterator.hasPrevious() && totalHeight <= scrollPane.getViewport().getHeight()) {
            final MessagePanel aPanel = getNewMessagePanel(listIterator.previous());
            totalHeight += aPanel.getPreferredSize().height;
        }
        return listIterator;
    }

    /**
     * adds new MessagePanel displaying the latest log message to end of log viewer
     * and removes first panel if it is pushed out of view by the new message.
     */
    private void addMessagePanel(final MessagePanel aPanel) {
        messagePanels.add(aPanel);
        if (messagesHeight > scrollPane.getViewport().getHeight()) {
            messagesHeight -= messagePanels.getComponent(0).getHeight();
            messagePanels.remove(0);
        }
        messagesHeight += aPanel.getPreferredSize().height;
    }

    private MessagePanel getNewMessagePanel(final MagicMessage message) {
        final Insets s = SEPARATOR_BORDER.getInsideBorder().getBorderInsets(null);
        final int maxWidth = getWidth() - s.left - s.right;
        final MessagePanel panel = new MessagePanel(message, maxWidth);
        panel.setOpaque(false);
        panel.setBorder(SEPARATOR_BORDER);
        return panel;
    }

    private void setVerticalScrollbarPolicy() {
        scrollPane.setVerticalScrollBarPolicy(getVerticalScrollbarPolicy());
    }

    private int getVerticalScrollbarPolicy() {
        return isScrollbarVisible ?
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS :
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER;
    }

    public void forceVerticalScrollbarToMax() {
        scrollPane.validate();
        final JScrollBar scrollbar = scrollPane.getVerticalScrollBar();
        scrollbar.setValue(scrollbar.getMaximum());
    }

}

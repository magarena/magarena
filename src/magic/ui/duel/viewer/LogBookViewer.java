package magic.ui.duel.viewer;

import magic.data.GeneralConfig;
import magic.model.MagicLogBook;
import magic.model.MagicMessage;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MessagePanel;
import magic.ui.widget.TitleBar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ListIterator;
import magic.ui.ScreenController;

public class LogBookViewer extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final CompoundBorder SEPARATOR_BORDER=BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY),
        FontsAndBorders.EMPTY_BORDER
    );

    private static final int INCREMENT=108;

    private final MagicLogBook logBook;
    private final JPanel messagePanel;
    private final JScrollPane scrollPane;
    private boolean isScrollbarVisible;
    private boolean isNewMessageAddedToTop;
    private final TitleBar tb;

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

        tb = new TitleBar("Log");
        tb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        tb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tb.addMouseListener(mouseDispatcher);
        add(tb, BorderLayout.NORTH);

        final JPanel centerPanel=new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        add(centerPanel,BorderLayout.CENTER);

        messagePanel=new JPanel();
        messagePanel.setOpaque(false);
        messagePanel.setLayout(new BoxLayout(messagePanel,BoxLayout.Y_AXIS));
        centerPanel.add(messagePanel,BorderLayout.NORTH);

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
                if (!isNewMessageAddedToTop) {
                    forceVerticalScrollbarToMax();
                }
            }
        });

        updateTitlebarCaption(false);

    }

    private void updateTitlebarCaption(final boolean showHelpHints) {
        final boolean isHidden = !scrollPane.isVisible();
        tb.setText("Log " + (isHidden ? " OFF" : "") + (showHelpHints ? "   [ LMB: On/Off   RMB: Full Log File ]" : ""));
    }

    public void update() {
        messagePanel.removeAll();
        synchronized (logBook) {
            if (!isNewMessageAddedToTop) {
                // use the default order of messages in logBook list.
                for (final MagicMessage msg : logBook) {
                    messagePanel.add(getNewMessagePanel(msg));
                }
                forceVerticalScrollbarToMax();
            } else {
                // display messages in reverse order.
                final ListIterator<MagicMessage> listIterator = logBook.listIterator(logBook.size());
                while(listIterator.hasPrevious()){
                    messagePanel.add(getNewMessagePanel(listIterator.previous()));
                }
                validate();
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        }
    }

    public void addMagicMessage(final MagicMessage magicMessage) {
        if (isNewMessageAddedToTop) {
            messagePanel.add(getNewMessagePanel(magicMessage), 0);
            scrollPane.getVerticalScrollBar().setValue(0);
        } else {
            messagePanel.add(getNewMessagePanel(magicMessage));
            forceVerticalScrollbarToMax();
        }
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

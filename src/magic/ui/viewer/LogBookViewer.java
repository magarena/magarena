package magic.ui.viewer;

import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.model.MagicLogBook;
import magic.model.MagicMessage;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MessagePanel;
import magic.ui.widget.TitleBar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.event.MouseInputAdapter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ListIterator;

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
    private final GeneralConfig config = GeneralConfig.getInstance();
    private boolean isScrollbarVisible = false; //config.isLogScrollbarVisible();
    private boolean isNewMessageAddedToTop = false; // config.isLogMessageAddedToTop();

    public LogBookViewer(final MagicLogBook logBook) {

        this.logBook=logBook;

        setLayout(new BorderLayout());

        TitleBar tb = new TitleBar("Log");
        tb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        //tb.add(getOptionsButton(), BorderLayout.EAST);
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
        scrollPane.getViewport().setView(centerPanel);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollbarPolicy();
        scrollPane.getVerticalScrollBar().setUnitIncrement(INCREMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(INCREMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane,BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                if (!isNewMessageAddedToTop) {
                    forceVerticalScrollbarToMax();
                }
            }
        });

    }


    public MagicLogBook getLogBook() {
        return logBook;
    }

    public void update() {
        messagePanel.removeAll();
        synchronized (logBook) {
            if (!isNewMessageAddedToTop) {
                // use the default order of messages in logBook list.
                for (MagicMessage msg : logBook) {
                    messagePanel.add(getNewMessagePanel(msg));
                }
                forceVerticalScrollbarToMax();
            } else {
                // display messages in reverse order.
                ListIterator<MagicMessage> listIterator = logBook.listIterator(logBook.size());
                while(listIterator.hasPrevious()){
                    messagePanel.add(getNewMessagePanel(listIterator.previous()));
                }
                validate();
                scrollPane.getVerticalScrollBar().setValue(0);
            }

        }
    }

    public void addMagicMessage(MagicMessage magicMessage) {
        if (isNewMessageAddedToTop) {
            messagePanel.add(getNewMessagePanel(magicMessage), 0);
            scrollPane.getVerticalScrollBar().setValue(0);
        } else {
            messagePanel.add(getNewMessagePanel(magicMessage));
            forceVerticalScrollbarToMax();
        }
    }

    private MessagePanel getNewMessagePanel(MagicMessage message) {
        Insets s = SEPARATOR_BORDER.getInsideBorder().getBorderInsets(null);
        final int maxWidth = getWidth() - (s.left + s.right);
        final MessagePanel panel = new MessagePanel(message, maxWidth);
        panel.setOpaque(false);
        panel.setBorder(SEPARATOR_BORDER);
        return panel;
    }

    private JLabel getOptionsButton() {
        JLabel lbl = new JLabel(IconImages.PROGRESS);
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doPopup(e);
            }
            private void doPopup(MouseEvent e){
                JPopupMenu menu = getOptionsPopupMenu();
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        return lbl;
    }

    private JPopupMenu getOptionsPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(getMenuItem_ScrollbarVisibility());
        menu.add(getMenuItem_TopInsert());
        return menu;
    }

    private JMenuItem getMenuItem_TopInsert() {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem("Add new message to top", isNewMessageAddedToTop);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isNewMessageAddedToTop = !isNewMessageAddedToTop;
                update();
                config.setLogMessageAddedToTop(isNewMessageAddedToTop);
                config.save();
            }
        });
        return item;
    }

    private JCheckBoxMenuItem getMenuItem_ScrollbarVisibility() {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem("Show scroll-bar", isScrollbarVisible);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isScrollbarVisible = !isScrollbarVisible;
                setVerticalScrollbarPolicy();
                config.setLogScrollbarVisible(isScrollbarVisible);
                config.save();
            }
        });
        return item;
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
        JScrollBar scrollbar = scrollPane.getVerticalScrollBar();
        scrollbar.setValue(scrollbar.getMaximum());
    }

}

package magic.ui.viewer;

import magic.model.MagicLogBook;
import magic.model.MagicMessage;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MessagePanel;
import magic.ui.widget.TitleBar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;

public class LogBookViewer extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Border SEPARATOR_BORDER=BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK),
        FontsAndBorders.EMPTY_BORDER
    );

    private static final int INCREMENT=108;

    private final MagicLogBook logBook;
    private final JPanel messagePanel;
    private final JScrollPane scrollPane;

    public LogBookViewer(final MagicLogBook logBook) {

        this.logBook=logBook;

        setLayout(new BorderLayout());

        TitleBar tb = new TitleBar("Log");
        tb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
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
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(INCREMENT);
        scrollPane.getVerticalScrollBar().setBlockIncrement(INCREMENT);
        add(scrollPane,BorderLayout.CENTER);
    }

    public MagicLogBook getLogBook() {

        return logBook;
    }

    public void addMagicMessage(MagicMessage magicMessage) {
        final int maxWidth = getWidth() - 175;
        final MessagePanel panel = new MessagePanel(magicMessage, maxWidth);
        panel.setOpaque(false);
        panel.setBorder(SEPARATOR_BORDER);
        messagePanel.add(panel, 0);
        scrollPane.getVerticalScrollBar().setValue(0);
    }

}
